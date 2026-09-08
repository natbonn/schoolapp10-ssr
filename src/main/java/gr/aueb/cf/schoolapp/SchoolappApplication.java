package gr.aueb.cf.schoolapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication                 // Μετατρέπει τα annotations σε Spring Beans
@EnableJpaAuditing                     // Timestamp των πεδίων createdAt και updatedAt
public class SchoolappApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolappApplication.class, args);
    }

}
