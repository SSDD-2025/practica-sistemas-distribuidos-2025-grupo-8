package es.codeurjc.gymapp.DTO.Routine;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.codeurjc.gymapp.model.Routine;

@Mapper(componentModel = "spring")
public interface RoutineMapper {
   RoutineDTO toDTO(Routine routine);
   
   @Mapping(target = "exercises", ignore = true)
   Routine toDomain(RoutineSimpleDTO routineDTO);

   List<RoutineSimpleDTO> toDTOs(Collection<Routine> routines);
}
