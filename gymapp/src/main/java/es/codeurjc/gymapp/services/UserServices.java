package es.codeurjc.gymapp.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import es.codeurjc.gymapp.model.User;
import es.codeurjc.gymapp.repositories.UserRepository;

@Service
public class UserServices {

    @Autowired
	private UserRepository userRepository;

    public UserServices() {
        //userRepository.save(new User());
        //userRepository.save(new User());
    }
}
