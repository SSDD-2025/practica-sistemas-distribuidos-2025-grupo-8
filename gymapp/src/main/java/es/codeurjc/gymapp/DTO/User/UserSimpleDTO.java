package es.codeurjc.gymapp.DTO.User;

import java.sql.Blob;
import java.util.List;

import es.codeurjc.gymapp.model.*;

public record UserSimpleDTO(String name, Blob imageFile, Boolean isAdmin) {

}
