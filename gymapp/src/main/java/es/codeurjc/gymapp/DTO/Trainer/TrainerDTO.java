package es.codeurjc.gymapp.DTO.Trainer;

import java.sql.Blob;
import java.util.*;

import es.codeurjc.gymapp.DTO.Comment.CommentSimpleDTO;
import es.codeurjc.gymapp.DTO.User.UserSimpleDTO;

public record TrainerDTO(Long id, String name, String description
                        , Blob imageFile, Set<UserSimpleDTO> users, 
                        List<CommentSimpleDTO> comments) {}
