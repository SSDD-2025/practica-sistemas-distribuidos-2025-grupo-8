package es.codeurjc.gymapp.controllers.REST;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.codeurjc.gymapp.DTO.User.*;
import es.codeurjc.gymapp.services.UserServices;

import java.net.URI;
import java.util.NoSuchElementException;
import java.util.Optional;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/users")
public class UserRESTController {
    @Autowired
    private UserServices userServices;
    //GetMapping parts
    
    @GetMapping("/")
    //TODO: Change name and implementation
    //Posible implementation Log in
    // For admin to see a dashboard maybe
    public ResponseEntity<List<UserSimpleDTO>> getUsers() {
        if(userServices.findAll().isEmpty())
            return ResponseEntity.notFound().build();
        List<UserSimpleDTO> users = userServices.findAllSimple();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
        Optional<UserDTO> userOp = userServices.findById(id);
        if(!userOp.isPresent())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userOp.get());
    }

    //PostMapping parts

    @PostMapping("/")
    public ResponseEntity<UserDTO> postUser(@RequestBody UserDTO userDTO) {
        userServices.save(userDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(userDTO.id()).toUri();

        return ResponseEntity.created(location).body(userDTO);
    }

    //PutMapping parts

    @PutMapping("/{id}")
    public UserDTO putUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        if(userServices.findById(id).isEmpty())
            throw new NoSuchElementException();
        UserDTO newUser = new UserDTO(id, userDTO.name(), userDTO.encodedPassword(), 
                    userDTO.imageFile(), userDTO.trainer(), userDTO.routines(), 
                    userDTO.isAdmin(), userDTO.comments());
        userServices.save(newUser);
        return newUser;
    }

    //DeleteMapping parts
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long id){
        if(userServices.findById(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        UserDTO userDTO = userServices.findById(id).get();
        userServices.deleteById(id);
        return ResponseEntity.ok(userDTO);
    }
}
