package es.codeurjc.gymapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;

import es.codeurjc.gymapp.model.Material;
import es.codeurjc.gymapp.services.ExerciseServices;
import es.codeurjc.gymapp.services.MaterialServices;

@Controller
public class MaterialController {

    @Autowired
    private MaterialServices materialServices;
    @Autowired
    private ExerciseServices exerciseServices;

    @PostMapping("/machinery")
	public String machinery(Model model) {
        model.addAttribute("machineries", materialServices.findAll());
		return "machinery";
	}

    @GetMapping("/machinery/{id}")
    public String showMachinery(Model model, @PathVariable long id) {
        Material material =  materialServices.findById(id).get();
        model.addAttribute("machinery", material);
        model.addAttribute("exercises", exerciseServices.findByMaterial(material));
        return "machinery_show";
    }

}
