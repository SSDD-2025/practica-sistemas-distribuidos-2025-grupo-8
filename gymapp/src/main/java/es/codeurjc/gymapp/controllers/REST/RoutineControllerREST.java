package es.codeurjc.gymapp.controllers.REST;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.gymapp.DTO.Routine.RoutineDTO;
import es.codeurjc.gymapp.DTO.Routine.RoutineSimpleDTO;
import es.codeurjc.gymapp.model.Exercise;
import es.codeurjc.gymapp.model.Routine;
import es.codeurjc.gymapp.model.UserSession;
import es.codeurjc.gymapp.services.ExerciseServices;
import es.codeurjc.gymapp.services.MaterialServices;
import es.codeurjc.gymapp.services.RoutineServices;
import es.codeurjc.gymapp.services.UserServices;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequestMapping("/routine")
public class RoutineControllerREST {
    
    @Autowired
    private UserSession userSession;
    @Autowired 
    private RoutineServices routineServices;
    @Autowired
    private UserServices userServices;
    @Autowired
    private ExerciseServices exerciseServices;
    @Autowired
    private MaterialServices materialServices;

    @GetMapping("/")
    public ResponseEntity<List<RoutineSimpleDTO>> getRoutines() {
        List<RoutineSimpleDTO> routines;
        if(userSession.isLoggedIn()){
            routines = routineServices.findByUser(userServices.findByName(userSession.getName()).get());
            return ResponseEntity.ok(routines);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RoutineDTO> getRoutine(@PathVariable long id) {
        RoutineDTO routine;
        if(userSession.isLoggedIn()){
            routine = routineServices.findByIdNotSimple(id).get();
            return ResponseEntity.ok(routine);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<RoutineDTO> createRoutine(@RequestBody RoutineDTO routineDTO) {
        routineServices.save(routineDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(routineDTO.id()).toUri();
        
        return ResponseEntity.created(location).body(routineDTO);
    }

    @PutMapping("/{id}")
    public RoutineDTO modifyRoutine(@PathVariable long id,@RequestBody RoutineDTO routineDTO){
        if(routineServices.findById(id).isPresent()){
            RoutineDTO newRoutineDTO = new RoutineDTO(id, routineDTO.name(), routineDTO.description(), routineDTO.day(), routineDTO.exercises(), routineDTO.userMember());
            routineServices.save(newRoutineDTO);
            return newRoutineDTO;
        }
        throw new NoSuchElementException(); 
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RoutineDTO> deleteRoutine(@PathVariable long id){
        RoutineDTO routineDTO;
        if(routineServices.findByIdNotSimple(id).isPresent()){
            routineDTO = routineServices.findByIdNotSimple(id).get();
            routineServices.deleteById(id);
            return ResponseEntity.ok(routineDTO);
        }
        return ResponseEntity.notFound().build();
    }

}
