package es.codeurjc.gymapp;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import es.codeurjc.gymapp.model.User;
import es.codeurjc.gymapp.repositories.UserRepository;

@Component
public class DatabaseUsersLoader {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void initDatabase() {
        if(userRepository.count()==0){
            userRepository.save(new User("user", passwordEncoder.encode("pass"), "USER"));
            userRepository.save(new User("admin", passwordEncoder.encode("adminpass"), "USER", "ADMIN"));
        }
    }
}

