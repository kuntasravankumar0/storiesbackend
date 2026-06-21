package com.inkandreams.config;

import com.inkandreams.entity.User;
import com.inkandreams.entity.WriterProfile;
import com.inkandreams.repository.UserRepository;
import com.inkandreams.repository.WriterProfileRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final WriterProfileRepository writerProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) {
        // Fix column sizes for URL fields
        try {
            entityManager.createNativeQuery("ALTER TABLE stories MODIFY COLUMN cover_image_url TEXT").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE stories MODIFY COLUMN author_note TEXT").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE stories MODIFY COLUMN inline_image_urls LONGTEXT").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE stories MODIFY COLUMN content LONGTEXT").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE stories MODIFY COLUMN genre VARCHAR(50)").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE stories MODIFY COLUMN language VARCHAR(50)").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE audio_stories MODIFY COLUMN genre VARCHAR(50)").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE audio_stories MODIFY COLUMN language VARCHAR(50)").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE audio_stories MODIFY COLUMN cover_image_url TEXT").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE audio_stories MODIFY COLUMN audio_url TEXT").executeUpdate();
            // Fix existing stories that have NULL approved - set them all to approved
            entityManager.createNativeQuery("UPDATE stories SET approved = 1 WHERE approved IS NULL OR approved = 0").executeUpdate();
            System.out.println("Column sizes updated successfully!");
        } catch (Exception e) {
            System.out.println("Column alter skipped: " + e.getMessage());
        }

        // Create default admin if not exists
        if (!userRepository.existsByEmail("gynaneshwarsriramoju@gmail.com")) {
            User admin = User.builder()
                    .username("gynaneshwar")
                    .email("gynaneshwarsriramoju@gmail.com")
                    .password(passwordEncoder.encode("mahesh@22062001"))
                    .mobileNumber("6281833404")
                    .role(User.Role.ADMIN)
                    .blocked(false)
                    .build();
            userRepository.save(admin);
            System.out.println("Default admin created successfully!");
        }

        // Create or update writer profile
        WriterProfile profile = writerProfileRepository.findAll().stream().findFirst()
                .orElse(new WriterProfile());
        if (profile.getBiography() == null || profile.getBiography().length() < 10) {
            profile.setBiography("Hi, I am Gynaneshwar. I write stories that inspire, entertain, and connect with human emotions. My words are my way of touching lives and spreading love across the world.");
            profile.setWritingJourney("I started writing at the age of 16, pouring my thoughts into a small diary. Over the years, my passion grew into a mission to tell stories that matter. From love to mystery, motivation to fantasy, every genre is a new adventure for me.");
            profile.setAchievements("Published 50+ stories across multiple platforms. Featured writer in several online literary magazines. Building Ink and Dreams to share my passion with the world.");
            profile.setNumberOfStoriesWritten(50);
            profile.setPhotoUrl("https://dev-maheshstores.pantheonsite.io/wp-content/uploads/2026/06/WhatsApp-Image-2026-06-20-at-7.21.53-PM.jpeg");
            writerProfileRepository.save(profile);
            System.out.println("Writer profile initialized!");
        }
    }
}
