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
        materialServices.save(new Material("Barra"));
        materialServices.save(new Material("Banco"));
        materialServices.save(new Material("Barra de dominadas"));
        exerciseServices.save(new Exercise("Press de banca", "Pecho",materialServices.findByName("Banco").get()));
        exerciseServices.save(new Exercise("Sentadillas", "Piernas",materialServices.findByName("Barra").get()));
        exerciseServices.save(new Exercise("Dominadas", "Espalda",materialServices.findByName("Barra de dominadas").get()));
    }   
    
    @PostMapping("/routine")
    public String routines(Model model) {
        return "routines/routines";
    }

    @PostMapping("/routine/create")
    public String createRoutine(Model model) {
        List<Exercise> exercises = exerciseServices.findAll();
        model.addAttribute("isLogged", userSession.isLoggedIn());
        model.addAttribute("exercises", exercises);
        return "routines/routineCreate";
    }

    @PostMapping("/routine/save")
    public String saveRoutine(Model model, @RequestParam String name, @RequestParam String description, 
    @RequestParam String day, @RequestParam Set<Exercise> exercise, HttpSession session) {
        Routine routine;
        if(name.isEmpty()){
            model.addAttribute("message", "La rutina debe tener nombre");
            return "error";
        }
        if(day.isEmpty()){
            model.addAttribute("message", "La rutina debe contener algun día");
            return "error";
        }
        if(exercise.isEmpty()){
            model.addAttribute("message", "Al menos un ejercicio debe ser seleccionado");
            return "error";
        }
        User user = userServices.findByName(userSession.getName()).get();
        routine = new Routine(name, description, day, exercise, user);
        userServices.addRoutine(user,routine);
        routineServices.save(routine);
        for(Exercise ex : exercise){
            exerciseServices.addRoutine(routine, ex);
            exerciseServices.save(ex);
        }
        return "routines/routineSaved";
    }
    
    @PostMapping("/routine/view")
    public String viewRoutine(Model model,HttpSession session){
        List<Routine> routines;
        if(userSession.isLoggedIn()){
            routines = routineServices.findByUser(userServices.findByName(userSession.getName()).get());
            model.addAttribute("routines", routines);
            model.addAttribute("isLogged", userSession.isLoggedIn());
            return "routines/routineView";
        }
        model.addAttribute("isLogged", userSession.isLoggedIn());
        /* 
        routines.add((Routine) session.getAttribute("routines"));
        model.addAttribute("routines", routines);*/
        return "routines/routineView";
    }

    @GetMapping("/routine/view/{id}")
    public String routineViewer(Model model,@PathVariable Long id){
        Optional<Routine> routine = routineServices.findById(id);
        if(routine.isPresent()){
            model.addAttribute("routine", routine.get());
            model.addAttribute("isLogged", userSession.isLoggedIn());
            return "routines/routineViewer";
        } 
        return "error";
    }

    @PostMapping("/routine/delete/{id}")
    public String deleteRoutine(Model model, @PathVariable Long id) {
        Optional<Routine> optionalRoutine = routineServices.findById(id);
        if (optionalRoutine.isPresent()) {
            Routine routine = optionalRoutine.get();
            if (userSession.isLoggedIn()) {
                User user = userServices.findByName(userSession.getName()).get();
                userServices.deleteRoutine(user, routine);
                
                for (Exercise exercise : routine.getExercises()) {
                    exerciseServices.removeRoutine(routine, exercise);
                    exerciseServices.save(exercise);
                }
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
            model.addAttribute("allExercises", exerciseServices.findAll());
            return "routines/routineModify";
        }
        //TODO: should now modify in case no user is logged in
        return "routines/routineModify";
    }

    @PostMapping("/routine/modified")
    public String saveModifiedRoutine(Model model, @RequestParam Long id, @RequestParam String name, 
    @RequestParam String description, @RequestParam String day, @RequestParam List<Long> exerciseIds) {
        if(name.equals(null)){
            model.addAttribute("message", "El nombre de la rutina no puede estar vacio");
            return "error";
        }
        if(day.equals(null)){
            model.addAttribute("message", "El día o los días de la rutina no puede/n estar vacio/s");
            return "error";
        }
        if(exerciseIds.isEmpty()){
            model.addAttribute("message", "La rutina debe tener al menos un ejercicio");
            return "error";
        }
        Optional<Routine> optionalRoutine = routineServices.findById(id);
        if (!optionalRoutine.isPresent()) return "error";

        Routine routine = optionalRoutine.get();
        //Update of Not DataStructs
        routine.setName(name);
        routine.setDescription(description);
        routine.setDay(day);
        //TODO: Correct update of exercises in the routine idk what does it
        routine.getExercises().clear();
        routine.getExercises().addAll(exerciseServices.findAllById(exerciseIds));
        routineServices.save(routine);

        for(Exercise ex : routine.getExercises()){
            exerciseServices.addRoutine(routine, ex);
            exerciseServices.save(ex);
        }
        
        return "redirect:/routine/view/" + id;
        
        
    }
     
}
