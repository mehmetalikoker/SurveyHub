package org.ing.surveyhub.security;

import org.ing.surveyhub.repository.AdminUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Açılışta tek bir ROLE_ADMIN hesabını seed eder (yoksa). Kullanıcı adı/parola
 * surveyhub.admin.username / surveyhub.admin.password (SURVEYHUB_ADMIN_USERNAME /
 * SURVEYHUB_ADMIN_PASSWORD ortam değişkenleri) üzerinden gelir — asla sabit kodlanmaz.
 */
@Component
public class AdminUserSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminUserSeeder.class);
    private static final String DEFAULT_PASSWORD_PLACEHOLDER = "changeme";

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final String username;
    private final String password;

    public AdminUserSeeder(AdminUserRepository adminUserRepository,
                            PasswordEncoder passwordEncoder,
                            @Value("${surveyhub.admin.username}") String username,
                            @Value("${surveyhub.admin.password}") String password) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (adminUserRepository.findByUsername(username).isEmpty()) {
            adminUserRepository.save(new AdminUser(username, passwordEncoder.encode(password)));
            log.info("Admin kullanıcı seed edildi: '{}'", username);
        }

        if (DEFAULT_PASSWORD_PLACEHOLDER.equals(password)) {
            log.warn("SURVEYHUB_ADMIN_PASSWORD ayarlanmamış — varsayılan placeholder parola aktif! "
                    + "Prod'a çıkmadan önce mutlaka bir ortam değişkeni ile değiştirin.");
        }
    }
}
