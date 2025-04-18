package es.codeurjc.gymapp.DTO.User;


public record UserSimpleDTO(Long id, String name, 
                             String... roles) {

}
