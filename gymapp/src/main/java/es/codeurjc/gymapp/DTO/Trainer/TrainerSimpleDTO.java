package es.codeurjc.gymapp.DTO.Trainer;

import java.sql.Blob;

public record TrainerSimpleDTO(String name, String description, Blob imageFile) {

}
