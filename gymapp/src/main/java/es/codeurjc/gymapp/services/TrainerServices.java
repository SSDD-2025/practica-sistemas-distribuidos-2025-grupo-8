package es.codeurjc.gymapp.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.codeurjc.gymapp.DTO.Trainer.TrainerDTO;
import es.codeurjc.gymapp.DTO.Trainer.TrainerMapper;
import es.codeurjc.gymapp.DTO.Trainer.TrainerSimpleDTO;
import es.codeurjc.gymapp.DTO.User.UserDTO;
import es.codeurjc.gymapp.DTO.User.UserMapper;
import es.codeurjc.gymapp.model.Comment;
import es.codeurjc.gymapp.model.Trainer;
import es.codeurjc.gymapp.model.User;
import es.codeurjc.gymapp.repositories.TrainerRepository;

@Service
public class TrainerServices {

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private CommentService commentServices;

    @Autowired
    private UserServices userServices;

    @Autowired
    private UserMapper mapperUser;

    @Autowired
    private TrainerMapper mapperTrainer;

    public TrainerServices() {
        // trainerRepository.save(new Trainer());
        // trainerRepository.save(new Trainer());
    }

    public long count(){
        return trainerRepository.count();
    }

    Optional<Trainer> findById(Long id) {
        return trainerRepository.findById(id);
    }

    public Optional<TrainerDTO> findDTOById(Long id) {
        return Optional.of(mapperTrainer.toDTO(trainerRepository.findById(id).get()));
    }

    void save(Trainer trainer) {
        trainerRepository.save(trainer);
    }

    public void save(TrainerDTO trainer) {
        trainerRepository.save(mapperTrainer.toDomain(trainer));
    }

    public void deleteById(Long id) {
        trainerRepository.deleteById(id);
    }

    public List<TrainerDTO> findAll() {
        List<Trainer> trainers = trainerRepository.findAll();
        return mapperTrainer.toDTOs(trainers);
    }

    public Page<TrainerDTO> findAllPage(Pageable pageable){
        return trainerRepository.findAll(pageable).map(trainer -> mapperTrainer.toDTO(trainer));
    }

    public List<TrainerSimpleDTO> findAllSimple() {
        List<Trainer> trainers = trainerRepository.findAll();
        return mapperTrainer.toSimpleDTOs(trainers);
    }

    void save(Trainer trainer, Blob imageFile) throws IOException {
        if (imageFile != null) {
            trainer.setImageFile(imageFile);
        }
        trainerRepository.save(trainer);

    }

    void save(Trainer trainer, MultipartFile imageFile) throws IOException{
        save(trainer, BlobProxy.generateProxy(
                        imageFile.getInputStream(),
                        imageFile.getSize()));
    }

    public void save(TrainerDTO trainer, MultipartFile imageFile) throws IOException{
        save(mapperTrainer.toDomain(trainer), imageFile);
    }

    public void save(TrainerDTO trainer, Blob imageFile) throws IOException{
        save(mapperTrainer.toDomain(trainer), imageFile);
    }

    public void addCommentToTrainer(TrainerDTO trainerDTO, UserDTO user, String message){
        Trainer trainer = mapperTrainer.toDomain(trainerDTO);
        Comment comment = new Comment(message);
        commentServices.save(trainer, comment, user);
    }

    public boolean deleteCommentFromTrainer(long trainerId, long commentId) {
        Optional<Trainer> opTrainer = this.findById(trainerId);
        if (opTrainer.isPresent()){
            Trainer trainer = opTrainer.get();
            return commentServices.delete(commentId, trainer);
        }
        return false;
    }

    public void addOrReplace(UserDTO userDTO, TrainerDTO trainerDTO){
        addOrReplace(mapperUser.toDomain(userDTO), mapperTrainer.toDomain(trainerDTO));
    }

    void addOrReplace(User userFromDTO, Trainer trainerFromDTO) {
        Trainer existingTrainer = trainerRepository.findById(trainerFromDTO.getId()).orElseThrow();
        trainerFromDTO.setImageFile(existingTrainer.getImageFile()); 
    
        User existingUser = userServices.findEntityById(userFromDTO.getId()).orElseThrow();
        userFromDTO.setImageFile(existingUser.getImageFile());
    
        userFromDTO.setTrainer(trainerFromDTO);
        userServices.save(userFromDTO);
    
        trainerFromDTO.addUser(userFromDTO);
        trainerRepository.save(trainerFromDTO);
    }
    

    public void deleteTrainer(TrainerDTO trainer, Long id){
        for (User user : mapperUser.toDomainsSimple(trainer.users())) {
            user.setTrainer(null);
            userServices.save(user);
        }
        deleteById(id); 
    }

    public void setImageFile(TrainerDTO trainerDTO, MultipartFile image) throws IOException {
        Trainer trainer = mapperTrainer.toDomain(trainerDTO);
        if (image.isEmpty()) {
            trainer.setImageFile(null);
        } else {
            trainer.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.getSize()));
        }
		save(trainer, image);
    }

    public Resource getTrainerImage(Long id) throws SQLException {
        Trainer trainer = trainerRepository.findById(id).orElseThrow();

		if (trainer.getImageFile() != null) {
			return new InputStreamResource(trainer.getImageFile().getBinaryStream());
		} else {
			return null;
		}
    }
}
