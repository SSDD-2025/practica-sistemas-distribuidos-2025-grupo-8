package es.codeurjc.gymapp.DTO.Exercise;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import es.codeurjc.gymapp.model.Exercise;

public interface ExerciseMapper {

    ExerciseSimpleDTO toSimpleDTO(Exercise exercise);
    List<ExerciseSimpleDTO> toSimpleDTOs(Collection<Exercise> exercises);
    Set<ExerciseSimpleDTO> toSimpleDTOsSet(Collection<Exercise> exercises);
    
    ExerciseDTO toDTO(Exercise exercise);
    List<ExerciseDTO> toDTOs(Collection<Exercise> exercises);

    Exercise toDomain(ExerciseSimpleDTO exerciseSimpleDTO);
    List<Exercise> toDomains(Collection<ExerciseSimpleDTO> exercisesDTO);

    Exercise toDomain(ExerciseDTO exerciseSimpleDTO);
    List<Exercise> toDomainsDTO(Collection<ExerciseDTO> exercisesDTO);
}
