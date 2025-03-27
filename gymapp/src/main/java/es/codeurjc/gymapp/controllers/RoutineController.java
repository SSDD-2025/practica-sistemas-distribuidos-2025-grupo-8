package es.codeurjc.gymapp.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.codeurjc.gymapp.DTO.Exercise.ExerciseSimpleDTO;
import es.codeurjc.gymapp.DTO.Routine.RoutineDTO;
import es.codeurjc.gymapp.DTO.Routine.RoutineSimpleDTO;
import es.codeurjc.gymapp.DTO.User.UserDTO;
import es.codeurjc.gymapp.model.Exercise;
import es.codeurjc.gymapp.model.Material;
import es.codeurjc.gymapp.model.Routine;
import es.codeurjc.gymapp.model.User;
import es.codeurjc.gymapp.model.UserSession;
import es.codeurjc.gymapp.services.ExerciseServices;
import es.codeurjc.gymapp.services.MaterialServices;
import es.codeurjc.gymapp.services.RoutineServices;
import es.codeurjc.gymapp.services.UserServices;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class RoutineController implements CommandLineRunner{

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


    @Override
    public void run(String... args) throws Exception {
        if(materialServices.count() == 0){
            materialServices.save(new Material("Barra"));
            materialServices.save(new Material("Banco"));
            materialServices.save(new Material("Barra de dominadas"));
        }
        if(exerciseServices.count() == 0){
            exerciseServices.save(new Exercise("Curl de biceps con mancuernas", "De pie o sentado"));
            exerciseServices.save(new Exercise("Press francés con mancuernas", "Ideal para el tríceps"));
            exerciseServices.save(new Exercise("Press de banca", "Pecho",materialServices.findByName("Banco").get()));
            exerciseServices.save(new Exercise("Sentadillas", "Piernas",materialServices.findByName("Barra").get()));
            exerciseServices.save(new Exercise("Dominadas", "Espalda",materialServices.findByName("Barra de dominadas").get()));
        }
    }   
    
    @PostMapping("/routine")
    public String routines(Model model) {
        return "routines/routines";
    }

    @PostMapping("/routine/create")
    public String createRoutine(Model model) {
        List<Exercise> exercises = exerciseServices.findByMaterialIsNotNull();
        model.addAttribute("isLogged", userSession.isLoggedIn());
        model.addAttribute("exercises", exercises);
        return "routines/routineCreate";
    }

    @PostMapping("/routine/save")
    public String saveRoutine(Model model, @RequestParam String name, @RequestParam String description, 
    @RequestParam String day, @RequestParam(required=false) Set<ExerciseSimpleDTO> exercise) {
        RoutineDTO routineDTO;
        RoutineSimpleDTO routineSimpleDTO;
        if(name.isEmpty()){
            model.addAttribute("message", "La rutina debe tener nombre");
            return "error";
        }
        if(day.isEmpty()){
            model.addAttribute("message", "La rutina debe contener algun día");
            return "error";
        }
        if(exercise == null){
            model.addAttribute("message", "Al menos un ejercicio debe ser seleccionado");
            return "error";
        }
        UserDTO user = userServices.findByName(userSession.getName()).get();
        routineDTO = new RoutineDTO((long)0,name, description, day, exercise, user);
        userServices.addRoutine(user,routine);
        routineServices.save(routineDTO);
        routineServices.saveExercises(exercise, routineSimpleDTO);
        return "routines/routineSaved";
    }
    
    @PostMapping("/routine/view")
    public String viewRoutine(Model model,HttpSession session){
        List<RoutineSimpleDTO> routines;
        if(userSession.isLoggedIn()){
            routines = routineServices.findByUser(userServices.findByName(userSession.getName()).get());
            model.addAttribute("routines", routines);
            model.addAttribute("isLogged", userSession.isLoggedIn());
            return "routines/routineView";
        }
        model.addAttribute("isLogged", userSession.isLoggedIn());
        return "routines/routineView";
    }

    @GetMapping("/routine/view/{id}")
    public String routineViewer(Model model,@PathVariable Long id){
        Optional<RoutineSimpleDTO> routine = routineServices.findById(id);
        if(routine.isPresent()){
            model.addAttribute("routine", routine.get());
            model.addAttribute("isLogged", userSession.isLoggedIn());
            return "routines/routineViewer";
        } 
        model.addAttribute("message", "No se ha encontrado la rutina");
        return "error";
    }

    @PostMapping("/routine/delete/{id}")
    public String deleteRoutine(Model model, @PathVariable Long id) {
        Optional<RoutineSimpleDTO> optionalRoutine = routineServices.findById(id);
        if (optionalRoutine.isPresent()) {
            RoutineSimpleDTO routine = optionalRoutine.get();
            if (userSession.isLoggedIn()) {
                UserDTO user = userServices.findByName(userSession.getName()).get();
                userServices.deleteRoutine(user, routine);
                routineServices.deleteUser(routine,user);
                routineServices.removeExercises(routine);
                routineServices.deleteById(id);
                return "routines/routineDelete";
            }
        }
        return "error"; 
    }

    @PostMapping("/routine/modify/{id}")
    public String modifyRoutine(Model model, @PathVariable Long id) {
        Routine routine = routineServices.findById(id).get();
        if(userSession.isLoggedIn()){
            model.addAttribute("routine", routine);
            model.addAttribute("allExercises", exerciseServices.findByMaterialIsNotNull());
            return "routines/routineModify";
        }
        model.addAttribute("message", "No hay usuario registrado");
        return "error";
    }

    @PostMapping("/routine/modified")
    public String saveModifiedRoutine(Model model, @RequestParam Long id, @RequestParam String name, 
    @RequestParam String description, @RequestParam String day, @RequestParam(required=false) List<Long> exerciseIds) {
        if(name.equals(null)){
            model.addAttribute("message", "El nombre de la rutina no puede estar vacio");
            return "error";
        }
        if(day.equals(null)){
            model.addAttribute("message", "El día o los días de la rutina no puede/n estar vacio/s");
            return "error";
        }
        if(exerciseIds == null){
            model.addAttribute("message", "La rutina debe tener al menos un ejercicio");
            return "error";
        }
        Optional<Routine> optionalRoutine = routineServices.findById(id);
        if (!optionalRoutine.isPresent()){
            model.addAttribute("message", "No se ha encontrado la rutina");
            return "error";
        } 

        Routine routine = optionalRoutine.get();
        //Update of Not DataStructs
        routine.setName(name);
        routine.setDescription(description);
        routine.setDay(day);
        //Update the routine
        routineServices.modifyRoutine(routine,exerciseIds);        
        return "redirect:/routine/view/" + id;
    }
     
}
