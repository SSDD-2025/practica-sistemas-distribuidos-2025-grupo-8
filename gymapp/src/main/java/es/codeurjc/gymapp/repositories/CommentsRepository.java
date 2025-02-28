package es.codeurjc.gymapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.gymapp.model.Comment;

public interface CommentsRepository extends JpaRepository<Comment, Long> {

}
