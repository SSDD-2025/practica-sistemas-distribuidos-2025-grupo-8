package es.codeurjc.DTO.Material;

import java.util.Set;

import es.codeurjc.DTO.Exercise.ExerciseSimpleDTO;

public record MaterialDTO(Long id, String name, Set<ExerciseSimpleDTO> exercises) {
	
}
