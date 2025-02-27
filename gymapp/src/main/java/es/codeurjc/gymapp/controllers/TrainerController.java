package es.codeurjc.gymapp.controllers;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
        model.addAttribute("trainer", trainer.get());
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
	public String addTrainerForm(Model model, @RequestParam String name, @RequestParam String description, @RequestParam MultipartFile image) throws IOException {		
		Trainer trainer = new Trainer(name, description);
        trainerServices.save(trainer, image);
        model.addAttribute("message", "Entrenador añadido correctamente");  
		return "trainers/trainerMessage"; 
	}

    @PostMapping("/trainer/image/{id}")
	public String changeImage(Model model, @RequestParam MultipartFile image, @PathVariable Long id) throws IOException {
		Optional<Trainer> trainer = trainerServices.findById(id);
		if (image.isEmpty()) {
            trainer.get().setImageFile(null);
        } else {
            trainer.get().setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.getSize()));
        }
		trainerServices.save(trainer.get(), image);
		return "index"; 
	}

    @GetMapping("/trainer/image/{id}")
	public ResponseEntity<Object> downloadImage(@PathVariable Long id) throws SQLException {
        Optional<Trainer> trainer = trainerServices.findById(id);
        if(!trainer.isPresent()) return ResponseEntity.notFound().build();

        Blob image = trainer.get().getImageFile();
        if(image == null) return ResponseEntity.notFound().build();

        Resource file = new InputStreamResource(image.getBinaryStream());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .contentLength(image.length()).body(file);
		
	}
}
