package es.codeurjc.gymapp.controllers.REST;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.gymapp.DTO.Routine.RoutineDTO;
import es.codeurjc.gymapp.DTO.Routine.RoutineSimpleDTO;
import es.codeurjc.gymapp.model.UserSession;
import es.codeurjc.gymapp.services.DTOServices;
import es.codeurjc.gymapp.services.RoutineServices;
import es.codeurjc.gymapp.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequestMapping("/api/routines")
public class RoutineRESTController {
    
    @Autowired
    private UserSession userSession;
    @Autowired 
    private RoutineServices routineServices;
    @Autowired
    private UserServices userServices;

    @Operation(summary = "Get all routines")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = RoutineDTO.class)))
    })
    @GetMapping("/")
    public ResponseEntity<List<RoutineSimpleDTO>> getRoutines() {
        List<RoutineSimpleDTO> routines;
        if(userSession.isLoggedIn()){
            routines = routineServices.findByUser(userServices.findByName(userSession.getName()).get());
            return ResponseEntity.ok(routines);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get routine by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = RoutineDTO.class))),
        @ApiResponse(responseCode = "404",
                     description = "Not Found",
                     content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RoutineDTO> getRoutine(@PathVariable long id) {
        RoutineDTO routine;
        if(routineServices.findByIdNotSimple(id).isPresent()){
            if(routineServices.findByIdNotSimple(id).get().userMember().name().equals(userSession.getName())){
                routine = routineServices.findByIdNotSimple(id).get();
                return ResponseEntity.ok(routine);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Create a new routine")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",
                     description = "Created",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = RoutineDTO.class)))
    })
    @PostMapping("/")
    public ResponseEntity<RoutineDTO> createRoutine(@RequestBody RoutineDTO routineDTO) {
        RoutineDTO newRoutine = routineServices.save(routineDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(newRoutine.id()).toUri();
        
        return ResponseEntity.created(location).body(newRoutine);
    }

    @Operation(summary = "Modify routine by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = RoutineDTO.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<RoutineDTO> modifyRoutine(@PathVariable long id,@RequestBody RoutineDTO routineDTO){
        if(routineServices.findByIdNotSimple(id).isPresent()){
            RoutineDTO oldRoutine = routineServices.findByIdNotSimple(id).get();
            RoutineDTO newRoutineDTO = DTOServices.mergeRecords(oldRoutine, routineDTO);

            routineServices.save(newRoutineDTO);
            return ResponseEntity.ok(newRoutineDTO);

        }
        return ResponseEntity.notFound().build(); 
    }

    @Operation(summary = "Delete exercise by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = RoutineDTO.class))),
        @ApiResponse(responseCode = "404",
                     description = "Not Found",
                     content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<RoutineDTO> deleteRoutine(@PathVariable long id){
        RoutineDTO routineDTO;
        if(routineServices.findByIdNotSimple(id).isPresent()){
            if(routineServices.findByIdNotSimple(id).get().userMember().name().equals(userSession.getName())){
                routineDTO = routineServices.findByIdNotSimple(id).get();
                routineServices.deleteById(id);
                return ResponseEntity.ok(routineDTO);
            }
        }
        return ResponseEntity.notFound().build();
    }

}
