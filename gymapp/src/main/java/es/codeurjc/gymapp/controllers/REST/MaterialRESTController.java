package es.codeurjc.gymapp.controllers.REST;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.codeurjc.gymapp.DTO.Exercise.ExerciseDTO;
import es.codeurjc.gymapp.DTO.Material.MaterialDTO;
import es.codeurjc.gymapp.services.MaterialServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/machineries")
public class MaterialRESTController {

    @Autowired
    private MaterialServices materialServices;

    @Operation(summary = "Get all materials")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = MaterialDTO.class)))
    })
    @GetMapping("/")
    public ResponseEntity<Page<MaterialDTO>> getMaterials(Pageable pageable) {
        return ResponseEntity.ok(materialServices.findAll(pageable));
    }

    @Operation(summary = "Get material by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = MaterialDTO.class))),
        @ApiResponse(responseCode = "404",
                     description = "Not Found",
                     content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<MaterialDTO> getMaterial(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(materialServices.findById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new material")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",
                     description = "Created",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = MaterialDTO.class)))
    })
    @PostMapping("/")
    public ResponseEntity<MaterialDTO> createMaterial(@RequestBody MaterialDTO materialDTO) {
        materialDTO = materialServices.save(materialDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(materialDTO.id())
        .toUri();
        return ResponseEntity.created(location).body(materialDTO);
    }

    @Operation(summary = "Delete material by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "OK",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = MaterialDTO.class))),
        @ApiResponse(responseCode = "404",
                     description = "Not Found",
                     content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<MaterialDTO> deleteMaterial(@PathVariable Long id){
        try {
            return ResponseEntity.ok(materialServices.deleteById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
}
