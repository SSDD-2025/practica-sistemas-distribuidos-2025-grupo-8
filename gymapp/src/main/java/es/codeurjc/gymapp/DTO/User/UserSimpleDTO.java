package es.codeurjc.gymapp.DTO.User;

import java.sql.Blob;

public record UserSimpleDTO(Long id, String name, 
                            Blob imageFile, String... roles) {

}
