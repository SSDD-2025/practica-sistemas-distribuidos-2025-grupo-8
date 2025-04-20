package es.codeurjc.gymapp.controllers.REST;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.codeurjc.gymapp.DTO.Exercise.ExerciseDTO;
import es.codeurjc.gymapp.services.ExerciseServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseRESTController {
    @Autowired
    private ExerciseServices exerciseServices;

    @Operation(summary = "Get all exercises")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = ExerciseDTO.class)))
    })
    @GetMapping("/")
    public ResponseEntity<Page<ExerciseDTO>> getExercises(Pageable pageable) {
        return ResponseEntity.ok(exerciseServices.findAll(pageable));
    }

    @Operation(summary = "Get exercise by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = ExerciseDTO.class))),
        @ApiResponse(responseCode = "404",
                     description = "Not Found",
                     content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDTO> getExercise(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(exerciseServices.findById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new exercise")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",
                     description = "Created",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = ExerciseDTO.class)))
    })
    @PostMapping("/")
    public ResponseEntity<ExerciseDTO> createExercise(@RequestBody ExerciseDTO exerciseDTO) {
        exerciseDTO = exerciseServices.save(exerciseDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(exerciseDTO.id())
        .toUri();
        return ResponseEntity.created(location).body(exerciseDTO);
    }

    @Operation(summary = "Delete exercise by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = ExerciseDTO.class))),
        @ApiResponse(responseCode = "404",
                     description = "Not Found",
                     content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ExerciseDTO> deleteExercise(@PathVariable Long id){
        try {
            return ResponseEntity.ok(exerciseServices.deleteById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
}
