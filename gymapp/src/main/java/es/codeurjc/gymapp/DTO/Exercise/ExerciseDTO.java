package es.codeurjc.gymapp.DTO.Exercise;

import es.codeurjc.gymapp.DTO.Routine.RoutineSimpleDTO;
import es.codeurjc.gymapp.DTO.Material.MaterialSimpleDTO;

public record ExerciseDTO(Long id, String name, String description
                        , MaterialSimpleDTO material, RoutineSimpleDTO routine) {}
