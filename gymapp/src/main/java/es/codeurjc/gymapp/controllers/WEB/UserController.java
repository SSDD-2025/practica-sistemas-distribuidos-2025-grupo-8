package es.codeurjc.gymapp.controllers.WEB;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.gymapp.DTO.User.UserDTO;
import es.codeurjc.gymapp.model.UserSession;
import es.codeurjc.gymapp.security.SecurityConfiguration;
import es.codeurjc.gymapp.services.CommentService;
import es.codeurjc.gymapp.services.ExerciseServices;
import es.codeurjc.gymapp.services.RoutineServices;
import es.codeurjc.gymapp.services.UserServices;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UserController {
    
	@Autowired
	private SecurityConfiguration securityConfiguration;

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
	
    @PostMapping("/facilities")
	public String facilities(Model model) {

		return "facilities";
	}

	@RequestMapping("/account")
	public String account(Model model) {
		if(!userSession.isLoggedIn()){
			return "account/account";
		}
		model.addAttribute("name", userSession.getName()); 
		return "account/logged";
	}

	@GetMapping("/account/loginError")
	public String getMethodName(Model model) {
		model.addAttribute("message", "Usuario o contraseña incorrectos");
		return "account/accountMessage";
	}
	
	@RequestMapping("/account/login")
	public String login(Model model) {
		return "redirect:/account"; 
	}
	

	@GetMapping("/register/create")
	public String register(Model model) {
		return "account/register";
	}

	@PostMapping("/account/register")
	public String register(Model model, @RequestParam String name, @RequestParam String password, @RequestParam MultipartFile image,HttpServletRequest request) throws IOException {
		if (name.isEmpty() || password.isEmpty()) {
			model.addAttribute("message", "Nombre de usuario o contraseña no pueden estar vacíos");
			return "error";
		}
		Optional<UserDTO> user = userServices.findByName(name);
		if(user.isPresent()){
			model.addAttribute("message", "El usuario ya existe");
			return "error"; //user was already registered
		}
		String encodedPassword = securityConfiguration.passwordEncoder().encode(password);

		UserDTO newUser;
		if(image.isEmpty()){		
			newUser = new UserDTO(null, name, encodedPassword, null, null, null, "USER");
		}else{	
			newUser = new UserDTO(null, name, encodedPassword, null, null,  null, "USER");
		}
    	userServices.save(newUser, image);
		userSession.setName(name, encodedPassword, request);
		model.addAttribute("message", "Usuario registrado con éxito");
		return "account/accountMessage";
	}

	@PostMapping("/account/logout")
	public String sessionExit(Model model) {
		return "account/logout";
	}

	@PostMapping("/account/deleteAccount")
	public String sessionDelete(Model model, HttpServletRequest request) {
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
		userSession.logout(request);
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
		if (user.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		UserDTO userDTO = user.get();
		Resource image = userServices.getUserImage(userDTO.id());
		if ( image == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity
							.ok()
							.header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
							.body(image);
	}

	@PostMapping("/admin/users")
	public String showUsers(Model model){
		List<UserDTO> users = userServices.findAll();
		model.addAttribute("users", users);
		return "account/show_users";
	}

}
