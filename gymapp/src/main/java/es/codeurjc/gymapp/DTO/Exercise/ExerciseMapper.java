package es.codeurjc.gymapp.DTO.Exercise;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import es.codeurjc.gymapp.model.Exercise;

public interface ExerciseMapper {

    ExerciseSimpleDTO toSimpleDTO(Exercise exercise);
    
    Exercise toDomain(ExerciseSimpleDTO exerciseSimpleDTO);
    Set<Exercise> toDomains(Collection<ExerciseSimpleDTO> exercisesDTO);
    List<ExerciseSimpleDTO> toSimpleDTOs(Collection<Exercise> exercises);
    Set<ExerciseSimpleDTO> toSimpleDTOsSet(Collection<Exercise> exercises);
}
