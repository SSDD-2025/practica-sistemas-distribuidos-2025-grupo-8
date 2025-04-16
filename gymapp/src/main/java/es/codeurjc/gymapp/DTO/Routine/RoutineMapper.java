package es.codeurjc.gymapp.DTO.Routine;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;

import es.codeurjc.gymapp.model.Routine;

@Mapper(componentModel = "spring")
public interface RoutineMapper {
   RoutineDTO toDTO(Routine routine);
   RoutineSimpleDTO toSimpleDTO(Routine routine);
   
   Routine toDomain(RoutineSimpleDTO routineDTO);
   Routine toDomain(RoutineDTO routineDTO);

   List<RoutineSimpleDTO> toSimpleDTOs(Collection<Routine> routines);
}
