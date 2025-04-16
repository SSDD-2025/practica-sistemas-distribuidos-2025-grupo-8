package es.codeurjc.gymapp.DTO.Material;

import java.util.Set;

import es.codeurjc.gymapp.DTO.Exercise.ExerciseSimpleDTO;

public record MaterialDTO(Long id, String name, Set<ExerciseSimpleDTO> exercises) {
	
}
