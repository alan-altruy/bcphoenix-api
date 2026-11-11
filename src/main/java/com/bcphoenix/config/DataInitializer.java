package com.bcphoenix.config;

import com.bcphoenix.model.User;
import com.bcphoenix.model.Role;
import com.bcphoenix.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class DataInitializer {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    @Transactional
    CommandLineRunner initAdmin(UserRepository userRepository, 
                                PasswordEncoder passwordEncoder, 
                                Environment env) {
        return args -> {
            // Récupère l'email/username et le mot de passe depuis le .env / environnement
            String adminEmail = env.getProperty("ADMIN_EMAIL"); 
            String adminPassword = env.getProperty("ADMIN_PASSWORD");

            // Si les variables sont présentes et qu'aucun admin n'existe encore avec cet email
            if (adminEmail != null && !adminEmail.isBlank() && userRepository.findByEmailIgnoreCase(adminEmail).isEmpty()) {
                
                // Récupération sécurisée du rôle ADMIN (ID 2 créé par le schema.sql)
                Role adminRole = entityManager.find(Role.class, 2);
                if (adminRole == null) {
                    throw new IllegalStateException("Le rôle ADMIN (ID 2) est introuvable en base de données.");
                }

                User admin = new User();
                admin.setFirstName("Admin");
                admin.setLastName("System");
                admin.setEmail(adminEmail);
                admin.setPasswordHash(passwordEncoder.encode(adminPassword));
                admin.setRole(adminRole); // Ajustez selon le nom de votre setter (ex: setRole)

                userRepository.save(admin);
                Logger logger = LoggerFactory.getLogger(DataInitializer.class);
                logger.info("Premier administrateur créé avec succès : {}", adminEmail);
            }
        };
    }
}
