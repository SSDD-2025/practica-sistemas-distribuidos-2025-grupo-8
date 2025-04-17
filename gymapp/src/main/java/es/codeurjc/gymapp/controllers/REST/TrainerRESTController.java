package es.codeurjc.gymapp.controllers.REST;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.codeurjc.gymapp.DTO.Trainer.TrainerDTO;
import es.codeurjc.gymapp.DTO.Trainer.TrainerSimpleDTO;
import es.codeurjc.gymapp.services.TrainerServices;

@RestController
@RequestMapping("/api/trainers")
public class TrainerRESTController {

    @Autowired
    private TrainerServices trainerServices;

    //GetMapping parts
    
    @GetMapping("/")
    public ResponseEntity<List<TrainerSimpleDTO>> getTrainers() {
        if(trainerServices.findAllSimple().isEmpty())
            return ResponseEntity.notFound().build();
        List<TrainerSimpleDTO> trainers = trainerServices.findAllSimple();
        return ResponseEntity.ok(trainers);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TrainerDTO> getTrainer(@PathVariable Long id){
        if(trainerServices.findDTOById(id).isEmpty())
            return ResponseEntity.notFound().build();
        TrainerDTO trainer = trainerServices.findDTOById(id).get();
        return ResponseEntity.ok(trainer);
    }

    //PostMapping parts

    @PostMapping("/")
    public ResponseEntity<TrainerDTO> postTrainer(@RequestBody TrainerDTO trainerDTO) {
        trainerServices.save(trainerDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(trainerDTO.id()).toUri();

        return ResponseEntity.created(location).body(trainerDTO);
    }

    //PutMapping parts

    @PutMapping("/{id}")
    public ResponseEntity<TrainerDTO> putTrainer(@PathVariable Long id, @RequestBody TrainerDTO trainerDTO) {
        if(trainerServices.findDTOById(id).isEmpty())
            throw new NoSuchElementException();
        TrainerDTO newTrainer = new TrainerDTO(id, trainerDTO.name(), trainerDTO.description(), 
            trainerDTO.imageFile(), trainerDTO.users(), trainerDTO.comments());
        trainerServices.save(newTrainer);
        return ResponseEntity.ok(newTrainer);
    }

    //DeleteMapping parts
    
    @DeleteMapping("/{id}")
    public ResponseEntity<TrainerDTO> deleteTrainer(@PathVariable Long id){
        if(trainerServices.findDTOById(id).isEmpty())
            return ResponseEntity.notFound().build();
        
        TrainerDTO trainerDTO = trainerServices.findDTOById(id).get();
        trainerServices.deleteById(id);
        return ResponseEntity.ok(trainerDTO);
    }
}
