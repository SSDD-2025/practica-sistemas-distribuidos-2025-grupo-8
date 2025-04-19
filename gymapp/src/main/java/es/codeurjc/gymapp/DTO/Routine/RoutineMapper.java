package es.codeurjc.gymapp.DTO.Routine;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.codeurjc.gymapp.DTO.User.UserMapper;
import es.codeurjc.gymapp.model.Routine;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface RoutineMapper {
   @Mapping(source = "user", target = "userMember")
   RoutineDTO toDTO(Routine routine);
   RoutineSimpleDTO toSimpleDTO(Routine routine);
   
   Routine toDomain(RoutineSimpleDTO routineDTO);
   @Mapping(source = "userMember", target = "user")
   Routine toDomain(RoutineDTO routineDTO);

   List<RoutineSimpleDTO> toSimpleDTOs(Collection<Routine> routines);
}
