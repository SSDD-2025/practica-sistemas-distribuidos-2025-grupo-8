package es.codeurjc.gymapp.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;

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

    public TrainerServices() {
        // trainerRepository.save(new Trainer());
        // trainerRepository.save(new Trainer());
    }

    public Optional<Trainer> findById(Long id) {
        return trainerRepository.findById(id);
    }

    public void save(Trainer trainer) {
        trainerRepository.save(trainer);
    }

    public void deleteById(Long id) {
        trainerRepository.deleteById(id);
    }

    public Iterable<Trainer> findAll() {
        return trainerRepository.findAll();
    }

    public void save(Trainer trainer, MultipartFile imageFile) throws IOException{
        if(!imageFile.isEmpty()) {
            trainer.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(),
            imageFile.getSize()));
        }
        trainerRepository.save(trainer);
    }

    public void addCommentToTrainer(Trainer trainer, User user, String message){
        Comment comment = new Comment(message);
        commentServices.save(trainer, comment, user);
    }

    public boolean deleteCommentFromTrainer(long trainerId, long commentId) {
        Optional<Trainer> opTrainer = this.findById(trainerId);
        Trainer trainer;
        boolean commentDeleted = false;
        if (opTrainer.isPresent()){
            trainer = opTrainer.get();
            commentDeleted = commentServices.delete(commentId, trainer);
            trainerRepository.save(trainer);
        }
        return commentDeleted;
    }
}
