package es.codeurjc.gymapp.DTO.Comment;

import es.codeurjc.gymapp.DTO.User.UserSimpleDTO;

public record CommentSimpleDTO(Long id, String message, UserSimpleDTO author) {

}
