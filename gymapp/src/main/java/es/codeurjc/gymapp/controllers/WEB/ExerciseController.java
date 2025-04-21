package es.codeurjc.gymapp.controllers.WEB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.codeurjc.gymapp.services.ExerciseServices;

@Controller
public class ExerciseController {
    @Autowired
    private ExerciseServices exerciseService;

    @RequestMapping("/exercise/add")
    public String addExercise(Model model) {
        return "exercises/exercise_add";
    }

    @PostMapping("/exercise/save")
    public String saveExercise(Model model, @RequestParam String nameExercise, @RequestParam String description){
        exerciseService.save(nameExercise, description);
        return "exercises/exercise_save";
    }

    @PostMapping("/exercise/exercise_selectToDelete")
    public String showExecises(Model model) {
        model.addAttribute("exercises", exerciseService.findAll());
        return "exercises/exercise_selectToDelete";
    }

    @PostMapping("/exercise/delete")
    public String deleteExercise(Model model, @RequestParam Long id) {
        try {
            exerciseService.deleteById(id);
            return "exercises/exercise_deleted";
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", "No se ha podido borrar el ejercicio");
            return "error";
        }
    }
}
