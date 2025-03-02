package es.codeurjc.gymapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.gymapp.model.Comment;
import es.codeurjc.gymapp.model.Trainer;
import es.codeurjc.gymapp.model.User;
import es.codeurjc.gymapp.repositories.CommentsRepository;

@Service
public class CommentService {

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private UserServices userServices;

    @Autowired 
    private TrainerServices trainerServices;

    public Optional<Comment> findById(long id){
        return commentsRepository.findById(id);
    }

    public void save(Trainer trainerToComment, Comment comment, User user){
        trainerToComment.getComments().add(comment);
        comment.setAuthor(user);
        comment.setTrainer(trainerToComment);
        user.getComments().add(comment);
        commentsRepository.save(comment);
        userServices.save(user);
        trainerServices.save(trainerToComment);
    }

    public boolean delete(Long commentId, Trainer trainer) {
        Optional<Comment> opComment = this.findById(commentId); 
        Comment comment;
        if (opComment.isPresent()) {
            comment = opComment.get();
            User user = comment.getAuthor();
            trainer.getComments().remove(comment);
            trainerServices.save(trainer);
            user.getComments().remove(comment);
            userServices.save(user);
            commentsRepository.flush();
            commentsRepository.delete(comment);
            return true;
        }
        return false;
    }

    public void deleteAllComments(User user) {
        List<Comment> commentsCopy = new ArrayList<>(user.getComments());
        for (Comment comment : commentsCopy){
            this.delete(comment.getId(), comment.getTrainer());
        }
    }
}
