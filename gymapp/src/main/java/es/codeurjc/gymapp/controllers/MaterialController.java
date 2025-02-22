package es.codeurjc.gymapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import es.codeurjc.gymapp.services.MaterialServices;

@Controller
public class MaterialController {

    @Autowired
    private MaterialServices materialServices;

    @PostMapping("/machinery")
	public String machinery(Model model) {
        model.addAttribute("machineries", materialServices.findAll());
		return "machinery";
	}

    @PostMapping("/machinery/{id}")
    public String showMachinery(Model model, @PathVariable long id) {
        model.addAttribute("machinery", materialServices.findById(id));
        model.addAttribute("id", id);
        return "machinery_show";
    }

}
