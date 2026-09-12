package org.ing.surveyhub.security;

import org.ing.surveyhub.repository.AdminUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

/**
 * Brute-force koruması: tek admin hesabı olduğu için kullanıcı adı zaten
 * tahmin edilebilir — art arda MAX_FAILED_ATTEMPTS başarısız denemeden sonra
 * hesap LOCK_DURATION süreyle kilitlenir (bkz. AdminUser.registerFailedAttempt).
 * Kilit süresi dolunca bir sonraki denemede sayaç otomatik sıfırlanır.
 */
@Component
public class LoginAttemptListener {

    private static final Logger log = LoggerFactory.getLogger(LoginAttemptListener.class);
    private static final Logger auditLog = LoggerFactory.getLogger("AUDIT.login");
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(15);

    private final AdminUserRepository adminUserRepository;

    public LoginAttemptListener(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @EventListener
    @Transactional
    public void onSuccess(AuthenticationSuccessEvent event) {
        adminUserRepository.findByUsername(event.getAuthentication().getName())
                .ifPresent(user -> {
                    if (user.getFailedAttempts() > 0 || user.getLockedUntil() != null) {
                        user.resetFailedAttempts();
                        adminUserRepository.save(user);
                    }
                });
    }

    @EventListener
    @Transactional
    public void onFailure(AuthenticationFailureBadCredentialsEvent event) {
        String username = event.getAuthentication().getName();
        String ip = remoteAddress(event.getAuthentication());
        auditLog.warn("Başarısız admin girişi: kullanıcı='{}', ip={}", username, ip);

        adminUserRepository.findByUsername(username).ifPresent(user -> {
            Instant now = Instant.now();
            if (user.isLocked(now)) {
                return;
            }
            user.registerFailedAttempt(MAX_FAILED_ATTEMPTS, LOCK_DURATION, now);
            adminUserRepository.save(user);
            if (user.isLocked(now)) {
                log.warn("Admin hesabı '{}' {} başarısız denemeden sonra {} dakikalığına kilitlendi (ip={}).",
                        username, MAX_FAILED_ATTEMPTS, LOCK_DURATION.toMinutes(), ip);
            }
        });
    }

    private static String remoteAddress(Authentication authentication) {
        if (authentication.getDetails() instanceof WebAuthenticationDetails webDetails) {
            return webDetails.getRemoteAddress();
        }
        return "unknown";
    }
}
