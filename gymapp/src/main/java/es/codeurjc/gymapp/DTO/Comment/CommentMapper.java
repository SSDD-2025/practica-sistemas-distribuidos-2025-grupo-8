package es.codeurjc.gymapp.DTO.Comment;

import java.util.*;

import es.codeurjc.gymapp.model.Comment;

public interface CommentMapper {
    CommentDTO toDTO(Comment comment);
    List<CommentDTO> toDTOs(Collection<Comment> comments);

    CommentSimpleDTO toSimpleDTO(Comment comment);
    List<CommentSimpleDTO> toSimpleDTOs(Collection<Comment> comments);

    Comment toDomain(CommentDTO commentDTO);
    List<Comment> toDomains(Collection<CommentDTO> commentDTOs);
    
    Comment toDomain(CommentSimpleDTO commentSimpleDTO);
    List<Comment> toDomainsSimple(Collection<CommentSimpleDTO> commentSimpleDTOs);
}
