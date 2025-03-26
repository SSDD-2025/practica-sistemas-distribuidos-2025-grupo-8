package es.codeurjc.gymapp.DTO.Trainer;

import java.util.*;

import org.mapstruct.Mapper;

import es.codeurjc.gymapp.model.Trainer;

@Mapper(componentModel = "spring")
public interface TrainerMapper {
    TrainerDTO toDTO(Trainer trainer);
    List<TrainerDTO> toDTOs(Collection<Trainer> trainers);
    Trainer toDomain(TrainerDTO trainerDTO);
}
