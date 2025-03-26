package es.codeurjc.gymapp.DTO.User;

import java.sql.Blob;
import java.util.List;

import es.codeurjc.gymapp.model.*;

public record UserSimpleDTO(Long id, String name, Blob imageFile
                    , List<Routine> routines, Boolean isAdmin, 
                    List<Comment> comments) {

}
