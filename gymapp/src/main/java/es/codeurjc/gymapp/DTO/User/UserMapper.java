package es.codeurjc.gymapp.DTO.User;

import java.util.*;

import org.mapstruct.Mapper;

import es.codeurjc.gymapp.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    UserSimpleDTO toSimpleDTO(User user);
    List<UserSimpleDTO> toSimpleDTOs(Collection<User> users);
    List<UserDTO> toDTOs(Collection<User> users);

    User toDomain(UserDTO userDTO);
    User toDomain(UserSimpleDTO userSimpleDTO);
    List<User> toDomains(Collection<UserDTO> usersDTOs);
    List<User> toDomainsFromSimple(Collection<UserSimpleDTO> usersDTOs);
}
