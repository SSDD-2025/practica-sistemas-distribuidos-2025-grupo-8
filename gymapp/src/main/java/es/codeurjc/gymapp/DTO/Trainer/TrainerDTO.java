package es.codeurjc.gymapp.DTO.Trainer;

import java.sql.Blob;
import java.util.*;

import es.codeurjc.gymapp.model.Comment;
import es.codeurjc.gymapp.model.User;

public record TrainerDTO(Long id, String name, String description
                        , Blob imageFile, Set<User> users, List<Comment> comments) {}
