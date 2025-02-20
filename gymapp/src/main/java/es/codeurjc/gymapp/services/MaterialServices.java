package es.codeurjc.gymapp.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import es.codeurjc.gymapp.model.Material;
import es.codeurjc.gymapp.repositories.MaterialRepository;

@Service

public class MaterialServices {
    
    @Autowired
    private MaterialRepository materialRepository;

    public MaterialServices() {
        //materialRepository.save(new Material());
        //materialRepository.save(new Material());
    }
}
