package es.codeurjc.gymapp.controllers.REST;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.codeurjc.gymapp.DTO.Trainer.TrainerDTO;
import es.codeurjc.gymapp.DTO.User.*;
import es.codeurjc.gymapp.services.DTOServices;
import es.codeurjc.gymapp.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Optional;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = UserSimpleDTO.class)))
    })
    @GetMapping("/")
    public ResponseEntity<List<UserSimpleDTO>> getUsers() {
        if(userServices.findAll().isEmpty())
            return ResponseEntity.notFound().build();
        List<UserSimpleDTO> users = userServices.findAllSimple();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get user by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = TrainerDTO.class))),
        @ApiResponse(responseCode = "404",
                     description = "Not Found",
                     content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
        Optional<UserDTO> userOp = userServices.findById(id);
        if(!userOp.isPresent())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userOp.get());
    }

    @Operation(summary = "Get image by its user's ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = Resource.class))),
        @ApiResponse(responseCode = "404",
                     description = "Not Found",
                     content = @Content)
    })
    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getImage(@PathVariable Long id) throws SQLException{
        Resource userImage = userServices.getUserImage(id);
        return ResponseEntity
                            .ok()
                            .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                            .body(userImage);
    }

    //PostMapping parts
    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",
                     description = "Created",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = UserDTO.class)))
    })
    @PostMapping("/")
    public ResponseEntity<UserDTO> postUser(@RequestBody UserDTO userDTO) {
        userServices.save(userDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(userDTO.id()).toUri();

        return ResponseEntity.created(location).body(userDTO);
    }

    @Operation(summary = "Add user image by user's ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404",
                     description = "Not Found",
                     content = @Content)
    })
    @PostMapping("/{id}/image")
    public ResponseEntity<UserDTO> postImage(@PathVariable Long id, @RequestBody MultipartFile imageFile) throws IOException {
        if(userServices.findById(id).isEmpty())
            return ResponseEntity.notFound().build();
        UserDTO userDTO = userServices.findById(id).get();
        userServices.save(userDTO, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    //PutMapping parts
    @Operation(summary = "Modify user by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = UserDTO.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> putUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        if(userServices.findById(id).isEmpty())
            return ResponseEntity.notFound().build();
        UserDTO oldUser = userServices.findById(id).get();
        UserDTO newUser = DTOServices.mergeRecords(oldUser, userDTO);
        userServices.save(newUser);
        return ResponseEntity.ok(newUser);
    }

    @Operation(summary = "Modify user image by userr's ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404",
                     description = "Not Found",
                     content = @Content)
    })
    @PutMapping("/{id}/image")
    public ResponseEntity<UserDTO> putImage(@PathVariable Long id, @RequestBody MultipartFile imageFile) throws IOException {
        if(userServices.findById(id).isEmpty())
            return ResponseEntity.notFound().build();
        UserDTO userDTO = userServices.findById(id).get();
        userServices.save(userDTO, imageFile);
        return ResponseEntity.ok(userDTO);
    }

    @Operation(summary = "Delete user by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404",
                     description = "Not Found",
                     content = @Content)
    })
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
