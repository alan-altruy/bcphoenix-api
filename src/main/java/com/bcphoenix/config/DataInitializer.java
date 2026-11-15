
package com.bcphoenix.config;

import com.bcphoenix.model.User;
import com.bcphoenix.model.Role;
import com.bcphoenix.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class DataInitializer {

    private static final Logger logger =
            LoggerFactory.getLogger(DataInitializer.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    ApplicationRunner initAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            Environment env) {

        return new AdminInitializer(
                userRepository, passwordEncoder, env, entityManager
        );
    }

    private static class AdminInitializer implements ApplicationRunner {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final Environment env;
        private final EntityManager entityManager;

        AdminInitializer(
                UserRepository userRepository,
                PasswordEncoder passwordEncoder,
                Environment env,
                EntityManager entityManager) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
            this.env = env;
            this.entityManager = entityManager;
        }

        @Override
        @Transactional
        public void run(org.springframework.boot.ApplicationArguments args) {

            String adminEmail = env.getProperty("ADMIN_EMAIL");
            String adminPassword = env.getProperty("ADMIN_PASSWORD");

            if (adminEmail == null || adminEmail.isBlank()
                    || adminPassword == null || adminPassword.isBlank()) {
                throw new IllegalStateException(
                        "ADMIN_EMAIL et ADMIN_PASSWORD doivent être renseignés."
                );
            }

            User admin = userRepository
                    .findByEmailIgnoreCase(adminEmail)
                    .orElse(null);

            if (admin != null) {

                // Évite de recalculer le hash si le mot de passe est déjà correct
                if (!passwordEncoder.matches(
                        adminPassword, admin.getPasswordHash())) {

                    admin.setPasswordHash(
                            passwordEncoder.encode(adminPassword)
                    );

                    userRepository.save(admin);

                    logger.info("Mot de passe administrateur mis à jour : {}", adminEmail);
                } else {
                    logger.info("Mot de passe administrateur déjà à jour.");
                }

            } else {

                Role adminRole = entityManager.find(Role.class, 2);

                if (adminRole == null) {
                    throw new IllegalStateException(
                            "Le rôle ADMIN (ID 2) est introuvable."
                    );
                }

                admin = new User();
                admin.setFirstName("Admin");
                admin.setLastName("System");
                admin.setEmail(adminEmail);
                admin.setPasswordHash(
                        passwordEncoder.encode(adminPassword)
                );
                admin.setRole(adminRole);

                userRepository.save(admin);

                logger.info("Premier administrateur créé : {}", adminEmail);
            }
        }
    }
}
