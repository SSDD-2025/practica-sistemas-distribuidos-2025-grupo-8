package es.codeurjc.gymapp.DTO.User;

import org.mapstruct.Mapper;

import es.codeurjc.gymapp.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    UserSimpleDTO toSimpleDTO(User user);

    User toDomain(UserDTO userDTO);
    User toDomain(UserSimpleDTO userSimpleDTO);
}
