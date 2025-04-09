package es.codeurjc.gymapp.controllers.WEB;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.gymapp.DTO.User.UserDTO;
import es.codeurjc.gymapp.model.UserSession;
import es.codeurjc.gymapp.services.CommentService;
import es.codeurjc.gymapp.services.ExerciseServices;
import es.codeurjc.gymapp.services.RoutineServices;
import es.codeurjc.gymapp.services.UserServices;


@Controller
public class UserController implements CommandLineRunner {
    
	@Autowired
	private UserSession userSession;

    @Autowired
	private UserServices userServices;

	@Autowired
	private RoutineServices	routineServices;

	@Autowired
	private ExerciseServices exerciseServices;

	@Autowired
	private CommentService commentServices;

    @RequestMapping("/")
	public String init(Model model) {
		return "index";
	}

	@Override
    public void run(String... args) throws Exception {
		if(userServices.count() == 0){
			UserDTO user = new UserDTO(0L,"admin", "admin", null,
				null, null, true, null);
			userServices.save(user);
		}
    }  
	
    @PostMapping("/facilities")
	public String facilities(Model model) {

		return "facilities";
	}

	@PostMapping("/account")
	public String account(Model model) {
		if(!userSession.isLoggedIn()){
			return "account/account";
		}
		model.addAttribute("name", userSession.getName()); //maybe add more info about the user
		return "account/logged";
	}

	@PostMapping("/account/login")
	public String sessionInit(Model model, @RequestParam String name, @RequestParam String password) {
		if (name.isEmpty() || password.isEmpty()) {
			model.addAttribute("message", "Nombre de usuario o contraseña no pueden estar vacíos");
			return "error";
		}
		// name is supposed to be unique as a username
		Optional<UserDTO> user = userServices.findByName(name); 
		if(!user.isPresent()) {
			model.addAttribute("message", "Usuario no encontrado");
			return "error";
		}
		if(!user.get().password().equals(password)) {
			model.addAttribute("message", "Contraseña incorrecta");
			return "error";
		}
		userSession.setName(name);
		model.addAttribute("message", "Sesión iniciada con éxito");
		return "account/accountMessage";
	}

	@GetMapping("/register/create")
	public String register(Model model) {
		return "account/register";
	}

	@PostMapping("/account/register")
	public String register(Model model, @RequestParam String name, @RequestParam String password, @RequestParam MultipartFile image) throws IOException {
		if (name.isEmpty() || password.isEmpty()) {
			model.addAttribute("message", "Nombre de usuario o contraseña no pueden estar vacíos");
			return "error";
		}
		Optional<UserDTO> user = userServices.findByName(name);
		if(user.isPresent()){
			model.addAttribute("message", "El usuario ya existe");
			return "error"; //user was already registered
		}
		userSession.setName(name);
		UserDTO newUser = new UserDTO(0L,name, password,null,null,null,false,null);
    	userServices.save(newUser, image);
		model.addAttribute("message", "Usuario registrado con éxito");
		return "account/accountMessage";
	}

	@PostMapping("/account/logout")
	public String sessionExit(Model model) {
		userSession.logout();
		model.addAttribute("message", "Sesión cerrada");
		return "account/accountMessage";
	}

	@PostMapping("/account/deleteAccount")
	public String sessionDelete(Model model) {
		Optional<UserDTO> optionalUser = userServices.findByName(userSession.getName());
		if (optionalUser.isEmpty()) {
			model.addAttribute("message", "Usuario no encontrado");
			return "error";
		}
		UserDTO user = optionalUser.get();
		// Delete routines from exercises
		exerciseServices.deleteRoutinesFromExercise(user);
		// Delete all routines
		routineServices.deleteAllRoutines(user);
		// Delete all comments
		commentServices.deleteAllComments(user);
		// Delete user
		userServices.deleteById(user.id());
		userSession.logout();
		model.addAttribute("message", "Cuenta eliminada con éxito");
		return "account/accountMessage";
	}

	
	@PostMapping("/account/image")
	public String changeImage(Model model, @RequestParam MultipartFile image) throws IOException {
		Optional<UserDTO> user = userServices.findByName(userSession.getName());
		//Needn't to check because to change image you have to be logged in
		userServices.setImageFile(user.get(), image);
		model.addAttribute("message", "Imagen cambiada con éxito");
		return "account/accountMessage"; 
	}

	@GetMapping("/user/image")
	public ResponseEntity<Object> downloadImage() throws SQLException {;		
		Optional<UserDTO> user = userServices.findByName(userSession.getName());
		if (user.isPresent() && user.get().imageFile() != null) {			
			Blob image = user.get().imageFile();
			Resource file = new InputStreamResource(image.getBinaryStream());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
					.contentLength(image.length()).body(file);
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping("/admin/users")
	public String showUsers(Model model){
		List<UserDTO> users = userServices.findAll();
		model.addAttribute("users", users);
		return "account/show_users";
	}

}
