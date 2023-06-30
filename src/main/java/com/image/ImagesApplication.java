package com.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.image.Model.User;
import com.image.Service.UserService;


@SpringBootApplication
public class ImagesApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(ImagesApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private UserService userService;

    @Autowired
    public ImagesApplication(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Creating admin account at first time the application running to login with
        // all permissions by email:admin@gmail.com and password:admin..
        User admin = userService.findByEmail("admin@gmail.com");
        if (admin == null) {
            userService.createAdmin();
        }
    }
}
