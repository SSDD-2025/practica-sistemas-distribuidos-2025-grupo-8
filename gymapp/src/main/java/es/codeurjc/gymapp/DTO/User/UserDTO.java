package es.codeurjc.gymapp.DTO.User;

import java.sql.Blob;
import java.util.List;

import es.codeurjc.gymapp.model.*;

public record UserDTO(Long id, String name, Blob imageFile, Trainer trainer
                    , List<Routine> routines, Boolean isAdmin, List<Comment> comments) {

}
