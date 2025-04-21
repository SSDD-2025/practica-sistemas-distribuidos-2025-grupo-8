package es.codeurjc.gymapp.controllers.WEB;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.gymapp.DTO.Trainer.TrainerDTO;
import es.codeurjc.gymapp.DTO.User.UserDTO;
import es.codeurjc.gymapp.model.UserSession;
import es.codeurjc.gymapp.services.TrainerServices;
import es.codeurjc.gymapp.services.UserServices;

@Controller
public class TrainerController implements CommandLineRunner {
    
	@Autowired
	private UserSession userSession;

    @Autowired
	private UserServices userServices;

    @Autowired
	private TrainerServices trainerServices;
    
    @Override
    public void run(String... args) throws Exception {
        if(trainerServices.count() == 0){
            // Trainer 1
            TrainerDTO arnold = new TrainerDTO(null, "Arnold Schwarzenegger","Entrenador de culturismo", 
                 null, null);

            // Trainer 2
            TrainerDTO theRock = new TrainerDTO(null,"Dwayne Johnson", "Entrenador de lucha libre",
                null, null);

            // Save trainers
            trainerServices.save(arnold, loadImageAsBlob("static/images/arnold.png"));
            trainerServices.save(theRock, loadImageAsBlob("static/images/theRock.png"));
        }

    }


    private Blob loadImageAsBlob(String imagePath) {
        try {
            // ClassResource to get image
            ClassPathResource resource = new ClassPathResource(imagePath);
            InputStream inputStream = resource.getInputStream();
            
            byte[] imageBytes = inputStream.readAllBytes();
            return BlobProxy.generateProxy(new ByteArrayInputStream(imageBytes), imageBytes.length);
        } catch (IOException e) {
            //Null if image not found
            return null;
        }
    }


    @PostMapping("/trainer")
	public String trainers(Model model) {
        Iterable<TrainerDTO> iterable = trainerServices.findAll();
        if(!iterable.iterator().hasNext()) 
            return "trainers/noTrainer";
        model.addAttribute("trainers", iterable);
        if(userSession.isLoggedIn()) {
            UserDTO user = userServices.findByName(userSession.getName()).get();
            if(user.trainer() != null) 
                model.addAttribute("personalTrainer", user.trainer().name());
            else 
                model.addAttribute("personalTrainer", "Ninguno, selecciona uno");
        }else{
            model.addAttribute("personalTrainer", "Inicia sesión para ver tu entrenador personal");
        }
		return "trainers/trainersShow";
	}

    @RequestMapping("/trainer/{id}")
    public String getTrainer(Model model, @PathVariable Long id) {
        Optional<TrainerDTO> trainer = trainerServices.findDTOById(id);
        model.addAttribute("trainer", trainer.get());
        model.addAttribute("logged", userSession.isLoggedIn());
        return "trainers/trainerDetails"; 
    }


    @PostMapping("/trainer/{id}/addOrReplace")
    public String addOrReplace(Model model, @PathVariable Long id) { //select personal trainer 
        Optional<TrainerDTO> trainer = trainerServices.findDTOById(id);
        UserDTO user = userServices.findByName(userSession.getName()).get();
        trainerServices.addOrReplace(user,trainer.get()); 
        model.addAttribute("message", "Entrenador personal cambiado correctamente");     
        return "trainers/trainerMessage"; 
    }

	@PostMapping("/trainer/add")
	public String addTrainer(Model model) {		
		return "trainers/trainerAdd"; 
	}

    @PostMapping("/trainer/{id}/delete")
	public String deleteTrainer(Model model, @PathVariable Long id) {		
		Optional<TrainerDTO> trainer = trainerServices.findDTOById(id);
        trainerServices.deleteTrainer(trainer.get(),id);
        model.addAttribute("message", "Entrenador eliminado correctamente");  
        return "trainers/trainerMessage";
    }

    @RequestMapping("/trainer/{id}/comments")
    public String showComments(Model model, @PathVariable Long id) {
        return getTrainer2(model, id);
    }


    
    @PostMapping("/trainer/{id}/comments/add")
    public String addComment(Model model, @PathVariable Long id){
        Optional<TrainerDTO> opTrainer = trainerServices.findDTOById(id);
        if (opTrainer.isEmpty()){
            model.addAttribute("message", "No se ha encontrado el entrenador");
            return "error";
        }
        model.addAttribute("id", id);
        model.addAttribute("trainerName", opTrainer.get().name());
        return "trainers/trainerComments_add";
    }
    
    @PostMapping("/trainer/{id}/comments/save")
    public String saveComment(Model model, @PathVariable long id, @RequestParam String message){
        Optional<TrainerDTO> opTrainer = trainerServices.findDTOById(id);
        if (opTrainer.isEmpty()){
            model.addAttribute("message", "Error al guardar el comentario");
            return "error";
        }
        
        UserDTO user = userServices.findByName(userSession.getName()).get();
        trainerServices.addCommentToTrainer(opTrainer.get(), user, message);
        opTrainer = trainerServices.findDTOById(id);
        model.addAttribute("logged", userSession.isLoggedIn());
        model.addAttribute("trainerId", id);
        model.addAttribute("comments", opTrainer.get().comments());
        return "trainers/trainerComments";
    }

    @PostMapping("/trainer/{trainerId}/comments/{commentId}/delete")
    public String deleteComment(Model model, @PathVariable long trainerId, @PathVariable long commentId){
        if(trainerServices.deleteCommentFromTrainer(trainerId, commentId)) {
            return getTrainer2(model, trainerId);
        }
        model.addAttribute("message", "No se ha podido eliminar el comentario");
        return "error";
    }
    
	@PostMapping("/trainer/add/form")
	public String addTrainerForm(Model model, @RequestParam String name, @RequestParam String description, @RequestParam MultipartFile image) throws IOException {		
        if (name.isEmpty() || image.isEmpty()){
            model.addAttribute("message", "El nombre y la imagen del entrenador es obligatorio.");
            return "error";
        }
        if (description.isEmpty()){
            description = "No hay descripción acerca del entrenador.";
        }
        
        TrainerDTO trainer = new TrainerDTO(null,name, description,null, null);
        trainerServices.save(trainer, image);
        model.addAttribute("message", "Entrenador añadido correctamente");  
        return "trainers/trainerMessage"; 
        
	}
    
    @PostMapping("/trainer/image/{id}")
	public String changeImage(Model model, @RequestParam MultipartFile image, @PathVariable Long id) throws IOException {
        Optional<TrainerDTO> trainer = trainerServices.findDTOById(id);
        trainerServices.setImageFile(trainer.get(), image);
		return "index"; 
	}
    
    @GetMapping("/trainer/image/{id}")
	public ResponseEntity<Object> downloadImage(@PathVariable Long id) throws SQLException {
        Optional<TrainerDTO> trainer = trainerServices.findDTOById(id);
        if(!trainer.isPresent()) return ResponseEntity.notFound().build();
        
        Resource image = trainerServices.getTrainerImage(id);
        if(image == null) return ResponseEntity.notFound().build();
        
        return ResponseEntity
        .ok()
        .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
        .body(image);
		
	}
    private String getTrainer2(Model model, Long id) {
        Optional<TrainerDTO> opTrainer = trainerServices.findDTOById(id);
        if (opTrainer.isEmpty()){
            model.addAttribute("message", "No se ha encontrado el entrenador");
            return "error";
        }
        TrainerDTO trainer = opTrainer.get();
        model.addAttribute("logged", userSession.isLoggedIn());
        model.addAttribute("trainerId", id);
        model.addAttribute("comments", trainer.comments());
        return "trainers/trainerComments";
    }
}
