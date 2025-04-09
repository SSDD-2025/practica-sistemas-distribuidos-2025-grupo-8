package es.codeurjc.gymapp.controllers.REST;

import java.net.URI;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/api/exercises")
public class ExerciseRESTController {
    @Autowired
    private ExerciseServices exerciseServices;

    @GetMapping("/")
    public ResponseEntity<Collection<ExerciseDTO>> getMaterials() {
        return ResponseEntity.ok(exerciseServices.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDTO> getMaterial(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(exerciseServices.findById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<ExerciseDTO> createExercise(@RequestBody ExerciseDTO exerciseDTO) {
        exerciseDTO = exerciseServices.save(exerciseDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(exerciseDTO.id())
        .toUri();
        return ResponseEntity.created(location).body(exerciseDTO);
    }

    @DeleteMapping("/id")
    public ResponseEntity<ExerciseDTO> deleteExercise(@PathVariable Long id){
        try {
            return ResponseEntity.ok(exerciseServices.deleteById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
}
