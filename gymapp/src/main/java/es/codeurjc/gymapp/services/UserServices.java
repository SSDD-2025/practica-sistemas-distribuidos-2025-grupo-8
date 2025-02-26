package es.codeurjc.gymapp.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;

import es.codeurjc.gymapp.model.Routine;
import es.codeurjc.gymapp.model.User;
import es.codeurjc.gymapp.repositories.RoutineRepository;
import es.codeurjc.gymapp.repositories.UserRepository;

@Service
public class UserServices {

    @Autowired
	private UserRepository userRepository;

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void save(User user, MultipartFile imageFile) throws IOException{
        if(!imageFile.isEmpty()) {
            user.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(),
            imageFile.getSize()));
        }
        userRepository.save(user);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    public void addRoutine(User user, Routine routine) {
        user.addRoutine(routine);
        userRepository.save(user);
    }
    
    public void deleteRoutine(User user, Routine routine){
        user.deleteRoutine(routine);
        userRepository.save(user);
    }
}
