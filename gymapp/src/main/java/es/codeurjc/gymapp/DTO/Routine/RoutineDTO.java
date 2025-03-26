package es.codeurjc.gymapp.DTO.Routine;

import java.util.Set;

import es.codeurjc.gymapp.DTO.Exercise.ExerciseSimpleDTO;
import es.codeurjc.gymapp.model.Exercise;
import es.codeurjc.gymapp.model.User;

public record RoutineDTO(Long id, String name, String description, String day, 
                        Set<ExerciseSimpleDTO> exercises,User userMember) {}
