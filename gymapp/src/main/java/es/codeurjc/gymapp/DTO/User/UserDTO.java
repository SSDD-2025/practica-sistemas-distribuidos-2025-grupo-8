package es.codeurjc.gymapp.DTO.User;
import java.util.List;

import es.codeurjc.gymapp.DTO.Comment.CommentSimpleDTO;
import es.codeurjc.gymapp.DTO.Routine.RoutineSimpleDTO;
import es.codeurjc.gymapp.DTO.Trainer.TrainerSimpleDTO;

public record UserDTO(Long id, String name, String encodedPassword,
                        TrainerSimpleDTO trainer, List<RoutineSimpleDTO> routines,
                        List<CommentSimpleDTO> comments, String... roles) {

}
