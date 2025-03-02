package es.codeurjc.gymapp.services;

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

    public Optional<Comment> findById(long id){
        return commentsRepository.findById(id);
    }

    public void save(Trainer trainerToComment, Comment comment, User user){
        trainerToComment.getComments().add(comment);
        comment.setAuthor(user);
        commentsRepository.save(comment);
    }

    public boolean delete(Long commentId, Trainer trainer) {
        Optional<Comment> opComment = this.findById(commentId); 
        Comment comment;
        if (opComment.isPresent()) {
            comment = opComment.get();
            trainer.getComments().remove(comment);
            commentsRepository.delete(comment);
            return true;
        }
        return false;
    }
}
