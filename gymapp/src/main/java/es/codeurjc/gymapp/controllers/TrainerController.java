package es.codeurjc.gymapp.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.codeurjc.gymapp.model.Trainer;
import es.codeurjc.gymapp.model.User;
import es.codeurjc.gymapp.model.UserSession;
import es.codeurjc.gymapp.services.TrainerServices;
import es.codeurjc.gymapp.services.UserServices;

@Controller
public class TrainerController {
    
	@Autowired
	private UserSession userSession;

    @Autowired
	private UserServices userServices;

    @Autowired
	private TrainerServices trainerServices;

    @PostMapping("/trainer")
	public String trainers(Model model) {
        Iterable<Trainer> iterable = trainerServices.findAll();
        if(!iterable.iterator().hasNext()) return "trainers/noTrainer";
        model.addAttribute("trainers", iterable);
        if(userSession.isLoggedIn()) {
            User user = userServices.findByName(userSession.getName()).get();
            if(user.getTrainer() != null) model.addAttribute("personalTrainer", user.getTrainer().getName());
            else model.addAttribute("personalTrainer", "Ninguno, selecciona uno");
        }
        else{
            model.addAttribute("personalTrainer", "Inicia sesión para ver tu entrenador personal");
        }
		return "trainers/trainersShow";
	}

    @GetMapping("/trainer/{id}")
    public String getTrainer(Model model, @PathVariable Long id) {
        Optional<Trainer> trainer = trainerServices.findById(id);
        model.addAttribute("trainer", trainer.get().getName());
        model.addAttribute("logged", userSession.isLoggedIn());
        return "trainers/trainerDetails"; 
    }

    @PostMapping("/trainer/{id}/addOrReplace")
    public String addOrReplace(Model model, @PathVariable Long id) { //select personal trainer 
        Optional<Trainer> trainer = trainerServices.findById(id);
        User user = userServices.findByName(userSession.getName()).get();
        user.setTrainer(trainer.get());
        userServices.save(user);
        trainer.get().addUser(user);
        trainerServices.save(trainer.get()); 
        model.addAttribute("message", "Entrenador personal cambiado correctamente");     
        return "trainers/trainerMessage"; 
    }

	@PostMapping("/trainer/add")
	public String addTrainer(Model model) {		
		return "trainers/trainerAdd"; 
	}

    @PostMapping("/trainer/{id}/delete")
	public String deleteTrainer(Model model, @PathVariable Long id) {		
		Optional<Trainer> trainer = trainerServices.findById(id);
        for (User user : trainer.get().getUsers()) {
            user.setTrainer(null);
            userServices.save(user);
        }
        trainerServices.deleteById(id); 
        model.addAttribute("message", "Entrenador eliminado correctamente");  
        return "trainers/trainerMessage";
    }

	@PostMapping("/trainer/add/form")
	public String addTrainerForm(Model model, @RequestParam String name) {		
		trainerServices.save(new Trainer(name));
        model.addAttribute("message", "Entrenador añadido correctamente");  
		return "trainers/trainerMessage"; 
	}
}
