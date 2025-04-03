package es.codeurjc.gymapp.controllers.REST;

import java.net.URI;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.codeurjc.gymapp.DTO.Material.MaterialDTO;
import es.codeurjc.gymapp.services.MaterialServices;

@RestController
@RequestMapping("/api/machineries")
public class MaterialRESTController {

    @Autowired
    private MaterialServices materialServices;

    @GetMapping("/")
    public Collection<MaterialDTO> getMaterials() {
        return materialServices.findAll();
    }

    @GetMapping("/{id}")
    public MaterialDTO getMaterial(@PathVariable Long id) {
        return materialServices.findById(id);
    }

    @PostMapping("/")
    public ResponseEntity<MaterialDTO> createMaterial(@RequestBody MaterialDTO materialDTO) {
        materialDTO = materialServices.save(materialDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(materialDTO.id())
        .toUri();
        return ResponseEntity.created(location).body(materialDTO);
    }

    @DeleteMapping("/id")
    public MaterialDTO deleteMaterial(@PathVariable Long id){
        return materialServices.deleteById(id);
    }
    
}
