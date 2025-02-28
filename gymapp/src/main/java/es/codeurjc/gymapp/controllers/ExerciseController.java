package es.codeurjc.gymapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import es.codeurjc.gymapp.services.ExerciseServices;

@Controller
public class ExerciseController {
    @Autowired
    private ExerciseServices exerciseService;

    @GetMapping("/exercise/add")
    public String addExercise(Model model) {
        return "exercises/exercise_add";
    }

    @PostMapping("/exercise/save")
    public String saveExercise(Model model, @RequestParam String nameExercise, @RequestParam String description){
        exerciseService.save(nameExercise, description);
        return "exercises/exercise_save";
    }
}
