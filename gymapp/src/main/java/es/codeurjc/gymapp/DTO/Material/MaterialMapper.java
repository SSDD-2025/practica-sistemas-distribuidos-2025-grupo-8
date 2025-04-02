package es.codeurjc.gymapp.DTO.Material;

import es.codeurjc.gymapp.model.Material;

import java.util.*;

import org.mapstruct.Mapper;

@Mapper
public interface MaterialMapper {
    Material toDomain(MaterialDTO materialDTO);
    Material toDomain(MaterialSimpleDTO materialSimpleDTO);
    
    List<Material> toDomains(Collection<MaterialDTO> materialsDTO);
    List<Material> toDomainsSimple(Collection<MaterialSimpleDTO> materialsSimpleDTO);

    MaterialDTO toDTO(Material material);
    List<MaterialDTO> toDTOs(Collection<Material> materials);
    MaterialSimpleDTO toSimpleDTO(Material material);
    List<MaterialSimpleDTO> toSimpleDTOs(Collection<Material> materials);

}
