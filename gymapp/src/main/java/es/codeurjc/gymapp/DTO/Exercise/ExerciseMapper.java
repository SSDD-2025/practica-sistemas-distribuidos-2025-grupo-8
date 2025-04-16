package es.codeurjc.gymapp.DTO.Exercise;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;

import es.codeurjc.gymapp.model.Exercise;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {

    ExerciseSimpleDTO toSimpleDTO(Exercise exercise);
    List<ExerciseSimpleDTO> toSimpleDTOs(Collection<Exercise> exercises);
    Set<ExerciseSimpleDTO> toSimpleDTOsSet(Collection<Exercise> exercises);
    
    ExerciseDTO toDTO(Exercise exercise);
    List<ExerciseDTO> toDTOs(Collection<Exercise> exercises);

    Exercise toDomain(ExerciseSimpleDTO exerciseSimpleDTO);
    List<Exercise> toDomainsSimple(Collection<ExerciseSimpleDTO> exercisesDTO);

    Exercise toDomain(ExerciseDTO exerciseSimpleDTO);
    List<Exercise> toDomains(Collection<ExerciseDTO> exercisesDTO);
}
