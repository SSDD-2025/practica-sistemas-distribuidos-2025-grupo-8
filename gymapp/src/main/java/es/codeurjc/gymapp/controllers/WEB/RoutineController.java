package es.codeurjc.gymapp.controllers.WEB;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.codeurjc.gymapp.DTO.Exercise.ExerciseDTO;
import es.codeurjc.gymapp.DTO.Exercise.ExerciseSimpleDTO;
import es.codeurjc.gymapp.DTO.Material.MaterialSimpleDTO;
import es.codeurjc.gymapp.DTO.Routine.RoutineDTO;
import es.codeurjc.gymapp.DTO.Routine.RoutineSimpleDTO;
import es.codeurjc.gymapp.DTO.User.UserDTO;
import es.codeurjc.gymapp.DTO.User.UserSimpleDTO;
import es.codeurjc.gymapp.model.UserSession;
import es.codeurjc.gymapp.services.ExerciseServices;
import es.codeurjc.gymapp.services.MaterialServices;
import es.codeurjc.gymapp.services.RoutineServices;
import es.codeurjc.gymapp.services.UserServices;
import jakarta.servlet.http.HttpSession;


@Controller
public class RoutineController implements CommandLineRunner{

    @Autowired
    private UserSession userSession;
    @Autowired 
    private RoutineServices routineServices;
    @Autowired
    private UserServices userServices;
    @Autowired
    private ExerciseServices exerciseServices;
    @Autowired
    private MaterialServices materialServices;


    @Override
    public void run(String... args) throws Exception {
        if(materialServices.count() == 0){
            materialServices.save(new MaterialSimpleDTO(null, "Barra"));
            materialServices.save(new MaterialSimpleDTO(null, "Banco"));
            materialServices.save(new MaterialSimpleDTO(null, "Barra de dominadas"));
        }
        if(exerciseServices.count() == 0){
            exerciseServices.save(new ExerciseDTO(null,"Curl de biceps con mancuernas", "De pie o sentado",null,null));
            exerciseServices.save(new ExerciseDTO(null,"Press francés con mancuernas", "Ideal para el tríceps",null,null));
            exerciseServices.save(new ExerciseDTO(null,"Press de banca", "Pecho",materialServices.findSimpleByName("Banco").get(),null));
            exerciseServices.save(new ExerciseDTO(null,"Sentadillas", "Piernas",materialServices.findSimpleByName("Barra").get(),null));
            exerciseServices.save(new ExerciseDTO(null,"Dominadas", "Espalda",materialServices.findSimpleByName("Barra de dominadas").get(),null));
        }
    }   
    
    @PostMapping("/routine")
    public String routines(Model model) {
        return "routines/routines";
    }

    @PostMapping("/routine/create")
    public String createRoutine(Model model) {
        List<ExerciseDTO> exercises = exerciseServices.findByMaterialIsNotNull();
        model.addAttribute("isLogged", userSession.isLoggedIn());
        model.addAttribute("exercises", exercises);
        return "routines/routineCreate";
    }

    @PostMapping("/routine/save")
    public String saveRoutine(Model model, @RequestParam String name, @RequestParam String description, 
    @RequestParam String day, @RequestParam(required=false) Set<Long> exerciseIds) {
        RoutineDTO routineDTO;
        if(name.isEmpty()){
            model.addAttribute("message", "La rutina debe tener nombre");
            return "error";
        }
        if(day.isEmpty()){
            model.addAttribute("message", "La rutina debe contener algun día");
            return "error";
        }
        if(exerciseIds == null){
            model.addAttribute("message", "Al menos un ejercicio debe ser seleccionado");
            return "error";
        }
        Set<ExerciseDTO> exercises = exerciseServices.buildExerciseDTOs(exerciseIds);
        Set<ExerciseSimpleDTO> exercisesSimple = exerciseServices.buildExerciseSimpleDTOs(exerciseIds);

        UserDTO userDTO = userServices.findByName(userSession.getName()).get();
        UserSimpleDTO userSimpleDTO = userServices.findByNameSimple(userSession.getName()).get();

        routineDTO = new RoutineDTO(null ,name, description, day, exercisesSimple, userSimpleDTO);

        RoutineDTO routineDTOsaved = routineServices.save(routineDTO);

        userServices.addRoutine(userDTO,routineDTOsaved);
        routineServices.saveExercises(exercises, routineDTOsaved);
        return "routines/routineSaved";
    }
    
    @PostMapping("/routine/view")
    public String viewRoutine(Model model,HttpSession session){
        List<RoutineSimpleDTO> routines;
        if(userSession.isLoggedIn()){
            routines = routineServices.findByUser(userServices.findByName(userSession.getName()).get());
            model.addAttribute("routines", routines);
            model.addAttribute("isLogged", userSession.isLoggedIn());
            return "routines/routineView";
        }
        model.addAttribute("isLogged", userSession.isLoggedIn());
        return "routines/routineView";
    }

    @GetMapping("/routine/view/{id}")
    public String routineViewer(Model model,@PathVariable Long id){
        Optional<RoutineDTO> routine = routineServices.findByIdNotSimple(id);
        if(routine.isPresent()){
            if(routine.get().userMember().name().equals(userSession.getName())){
                model.addAttribute("routine", routine.get());

                model.addAttribute("exercises", routine.get().exercises());

                model.addAttribute("isLogged", userSession.isLoggedIn());
                return "routines/routineViewer";
            }
        } 
        model.addAttribute("message", "No se ha encontrado la rutina");
        return "error";
    }

    @PostMapping("/routine/delete/{id}")
    public String deleteRoutine(Model model, @PathVariable Long id) {
        Optional<RoutineDTO> optionalRoutine = routineServices.findByIdNotSimple(id);
        if (optionalRoutine.isPresent()) {
            if(userSession.isLoggedIn() && optionalRoutine.get().userMember().name().equals(userSession.getName())){
                RoutineDTO routine = routineServices.findByIdNotSimple(id).get();
                UserDTO user = userServices.findByName(userSession.getName()).get();
                userServices.deleteRoutine(user, routine);
                routineServices.deleteUser(routine);
                routineServices.removeExercises(routine);
                routineServices.deleteById(id);
                return "routines/routineDelete";
            }else {
                model.addAttribute("message", "No tienes permiso para eliminar esta rutina");
                return "error"; 
            }
        }else {
            model.addAttribute("message", "No se ha encontrado la rutina");
            return "error"; 
        }
    }

    @PostMapping("/routine/modify/{id}")
    public String modifyRoutine(Model model, @PathVariable Long id) {
        Optional<RoutineDTO>  routine = routineServices.findByIdNotSimple(id);
        if(userSession.isLoggedIn() && routine.get().userMember().name().equals(userSession.getName())){
            model.addAttribute("routine", routine.get());
            model.addAttribute("allExercises", exerciseServices.findByMaterialIsNotNull());
            return "routines/routineModify";
        }else {
            model.addAttribute("message", "No tienes permiso para modificar esta rutina");
            return "error";
        }      
    }

    @PostMapping("/routine/modified")
    public String saveModifiedRoutine(Model model, @RequestParam Long id, @RequestParam String name, 
    @RequestParam String description, @RequestParam String day, @RequestParam(required=false) List<Long> exerciseIds) {
        if(name.equals(null)){
            model.addAttribute("message", "El nombre de la rutina no puede estar vacio");
            return "error";
        }
        if(day.equals(null)){
            model.addAttribute("message", "El día o los días de la rutina no puede/n estar vacio/s");
            return "error";
        }
        if(exerciseIds == null){
            model.addAttribute("message", "La rutina debe tener al menos un ejercicio");
            return "error";
        }
        Optional<RoutineDTO> optionalRoutine = routineServices.findByIdNotSimple(id);
        if (optionalRoutine.isEmpty()){
            model.addAttribute("message", "No se ha encontrado la rutina");
            return "error";
        } 

        //Update of Not DataStructs
        RoutineDTO routineDTO = new RoutineDTO(id, name, description, day, 
                optionalRoutine.get().exercises(), optionalRoutine.get().userMember());
        //Update the routine
        routineServices.modifyRoutine(routineDTO,exerciseIds);        
        return "redirect:/routine/view/" + id;
    }
     
}
