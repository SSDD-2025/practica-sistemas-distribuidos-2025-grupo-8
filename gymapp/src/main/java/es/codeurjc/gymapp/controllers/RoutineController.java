package es.codeurjc.gymapp.controllers;

import java.util.ArrayList;
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
        return "routines";
    }

    @PostMapping("/routine/create")
    public String createRoutine(Model model) {
        List<Exercise> exercises = exerciseServices.findAll();
        model.addAttribute("exercises", exercises);
        return "routineCreate";
    } 

    @PostMapping("/routine/save")
    public String saveRoutine(Model model, @RequestParam String name, @RequestParam String description, 
    @RequestParam String day, @RequestParam Set<Exercise> exercise, HttpSession session) {
        Routine routine;
        if(!userSession.isLoggedIn()){
            routine = new Routine(name, description, day, exercise);
            session.setAttribute("routines",routine);
            return "routineSaved";
        }else{
            User user = userServices.findByName(userSession.getName()).get();
            routine = new Routine(name, description, day, exercise,user);
            userServices.addRoutine(user,routine);
            routineServices.save(routine);
            return "routineSaved";
        }
    }
    @PostMapping("/routine/view")
    public String viewRoutine(Model model,HttpSession session){
        List<Routine> routines;
        if(userSession.isLoggedIn()){
            routines = routineServices.findByName(userSession.getName());
            model.addAttribute("routines", routines);
            return "routineView";
        }
        /* 
        routines.add((Routine) session.getAttribute("routines"));
        model.addAttribute("routines", routines);*/
        return "routineView";
    }

    @GetMapping("/routine/view/{id}")
    public String routineViewer(Model model,@PathVariable Long id){
        Optional<Routine> routine = routineServices.findById(id);
        if(routine.isPresent()){
            model.addAttribute("routine", routine.get());
            return "routineViewer";
        } 
        return "error";
    }

    @PostMapping("/routine/delete/{id}")
    public String deleteRoutine(Model model, @RequestParam Long id) {
        if(userSession.isLoggedIn()){
            User user = userServices.findByName(userSession.getName()).get();
            userServices.deleteRoutine(user,routineServices.findById(id).get());
            routineServices.deleteById(id);
            return "routineDelete";
        }
        //TODO: should now erase in case no user is logged in
        routineServices.deleteById(id);
        return "routineDelete";
    }

    @PostMapping("/routine/modify/{id}")
    public String modifyRoutine(Model model, @RequestParam Long id) {
        Routine routine = routineServices.findById(id).get();
        if(userSession.isLoggedIn()){
            model.addAttribute("routine", routine);
            model.addAttribute("exercises", exerciseServices.findAll());
            model.addAttribute("selectedExercises", routine.getExercises());
            return "routineModify";
        }
        //TODO: should now modify in case no user is logged in
        return "routineModify";
    }

    @PostMapping("/routine/modified")
    public String saveModifiedRoutine(@RequestParam Long id, @RequestParam String name, @RequestParam String description, @RequestParam String day, @RequestParam List<Long> exerciseIds) {
        Optional<Routine> optionalRoutine = routineServices.findById(id);
        if (optionalRoutine.isPresent()) {
            Routine routine = optionalRoutine.get();
            routine.setName(name);
            routine.setDescription(description);
            routine.setDay(day);
            List<Exercise> exercises = exerciseServices.findAllById(exerciseIds);
            routine.setExercises(exerciseServices.listToSet(exercises));
            routineServices.save(routine);
            return "redirect:/routine/view/" + id;
        }
        return "error";
    }
    
}
