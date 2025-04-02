package es.codeurjc.gymapp.DTO.Comment;

import es.codeurjc.gymapp.DTO.Trainer.TrainerSimpleDTO;
import es.codeurjc.gymapp.DTO.User.UserSimpleDTO;

public record CommentDTO(Long id, String message, UserSimpleDTO author,
                        TrainerSimpleDTO trainer) {

}
