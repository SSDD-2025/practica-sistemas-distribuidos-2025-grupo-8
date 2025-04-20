package es.codeurjc.gymapp.controllers.REST;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.codeurjc.gymapp.DTO.Trainer.TrainerDTO;
import es.codeurjc.gymapp.DTO.Trainer.TrainerSimpleDTO;
import es.codeurjc.gymapp.services.DTOServices;
import es.codeurjc.gymapp.services.TrainerServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/trainers")
public class TrainerRESTController {

    @Autowired
    private TrainerServices trainerServices;

    //GetMapping parts
    @Operation(summary = "Get all trainers")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = TrainerDTO.class)))
    })
    @GetMapping("/")
    public ResponseEntity<List<TrainerSimpleDTO>> getTrainers() {
        if(trainerServices.findAllSimple().isEmpty())
            return ResponseEntity.notFound().build();
        List<TrainerSimpleDTO> trainers = trainerServices.findAllSimple();
        return ResponseEntity.ok(trainers);
    }

    @Operation(summary = "Get trainer by its ID")
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
    public ResponseEntity<TrainerDTO> getTrainer(@PathVariable Long id){
        if(trainerServices.findDTOById(id).isEmpty())
            return ResponseEntity.notFound().build();
        TrainerDTO trainer = trainerServices.findDTOById(id).get();
        return ResponseEntity.ok(trainer);
    }

    @Operation(summary = "Get image by its trainer's ID")
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
    public ResponseEntity<Resource> getImage(@PathVariable Long id) throws SQLException {
        Resource trainerImage = trainerServices.getTrainerImage(id);
        return ResponseEntity
                .ok()
                .header("Content-Type", "image/jpeg")
                .body(trainerImage);
    }
        

    //PostMapping parts
    @Operation(summary = "Create a new trainer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",
                     description = "Created",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = TrainerDTO.class)))
    })
    @PostMapping("/")
    public ResponseEntity<TrainerDTO> postTrainer(@RequestBody TrainerDTO trainerDTO) {
        trainerServices.save(trainerDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(trainerDTO.id()).toUri();

        return ResponseEntity.created(location).body(trainerDTO);
    }

    @Operation(summary = "Add trainer image by trainer's ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = TrainerDTO.class))),
        @ApiResponse(responseCode = "404",
                     description = "Not Found",
                     content = @Content)
    })
    @PostMapping("/{id}/image")
    public ResponseEntity<TrainerDTO> postImage(@PathVariable Long id, @RequestBody MultipartFile imageFile) throws IOException {
        if(trainerServices.findDTOById(id).isEmpty())
            return ResponseEntity.notFound().build();
        TrainerDTO trainerDTO = trainerServices.findDTOById(id).get();
        trainerServices.save(trainerDTO, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(trainerDTO);
    }

    //PutMapping parts
    @Operation(summary = "Modify trainer by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = TrainerDTO.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<TrainerDTO> putTrainer(@PathVariable Long id, @RequestBody TrainerDTO trainerDTO) {
        if(trainerServices.findDTOById(id).isEmpty())
            throw new NoSuchElementException();
        TrainerDTO oldTrainer = trainerServices.findDTOById(id).get();
        TrainerDTO newTrainer = DTOServices.mergeRecords(oldTrainer, trainerDTO);
        trainerServices.save(newTrainer);
        return ResponseEntity.ok(newTrainer);
    }

    //PutMapping parts
    @Operation(summary = "Modify trainer image by trainer's ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = TrainerDTO.class))),
        @ApiResponse(responseCode = "404",
                     description = "Not Found",
                     content = @Content)
    })
    @PutMapping("/{id}/image")
    public ResponseEntity<TrainerDTO> putImage(@PathVariable Long id, @RequestBody MultipartFile imageFile) throws IOException {
        if(trainerServices.findDTOById(id).isEmpty())
            return ResponseEntity.notFound().build();
        TrainerDTO trainerDTO = trainerServices.findDTOById(id).get();
        trainerServices.save(trainerDTO, imageFile);
        return ResponseEntity.ok(trainerDTO);
    }

    //DeleteMapping part
    @Operation(summary = "Delete trainer by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = TrainerDTO.class))),
        @ApiResponse(responseCode = "404",
                     description = "Not Found",
                     content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<TrainerDTO> deleteTrainer(@PathVariable Long id){
        if(trainerServices.findDTOById(id).isEmpty())
            return ResponseEntity.notFound().build();
        
        TrainerDTO trainerDTO = trainerServices.findDTOById(id).get();
        trainerServices.deleteById(id);
        return ResponseEntity.ok(trainerDTO);
    }
}
