package es.codeurjc.gymapp.controllers.WEB;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;

import es.codeurjc.gymapp.DTO.Material.MaterialDTO;
import es.codeurjc.gymapp.services.ExerciseServices;
import es.codeurjc.gymapp.services.MaterialServices;

@Controller
public class MaterialController {

    @Autowired
    private MaterialServices materialServices;
    @Autowired
    private ExerciseServices exerciseServices;
    
    @RequestMapping("/machinery")
	public String machinery(Model model) {
		return "machinery/machinery";
	}

    @GetMapping("/machinery/{id}")
    public String showMachinery(Model model, @PathVariable long id) {
        try {
            MaterialDTO material = materialServices.findById(id);
            model.addAttribute("machinery", material);
            model.addAttribute("exercises", material.exercises());
            return "machinery/machinery_show";
        } catch(IllegalArgumentException e) {
            model.addAttribute("message", "No se ha encontrado la maquinaria con id: " + id);
            return "error";
        }
    }

    @PostMapping("/machinery/save")
    public String saveMachinery(Model model, @RequestParam("name") String name, @RequestParam(value = "exercise", required = false) List<Long> exercises) {
        if (exercises != null){
            materialServices.createAndSave(name, exercises);
            return "machinery/machinery_save";
        }
        model.addAttribute("message", "Se debe seleccionar al menos un ejercicio");
        return "error";
        
    }

    @RequestMapping("/machinery/add")
    public String addMachinery(Model model) {
        model.addAttribute("exercises", exerciseServices.findExercisesNotAssigned());
        return "machinery/machinery_add";
    }

    @RequestMapping("/machinery/delete/{id}")
    public String deleteMachinery(@PathVariable long id) {
        materialServices.safeDelete(id);
        return "machinery/machinery_delete";
    }
}
