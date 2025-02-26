package es.codeurjc.gymapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import es.codeurjc.gymapp.services.ExerciseServices;

@Controller
public class ExerciseController {
    @Autowired
    private ExerciseServices exerciseService;

    @GetMapping("/exercise/{id}")
    public String showExercise(Model model, @PathVariable long id) {
        model.addAttribute("exercise", exerciseService.findById(id));
        return "exercises/exercise_show";
    }
}
