package es.codeurjc.gymapp.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.codeurjc.gymapp.DTO.User.UserDTO;
import es.codeurjc.gymapp.DTO.User.UserMapper;
import es.codeurjc.gymapp.DTO.User.UserSimpleDTO;
import es.codeurjc.gymapp.DTO.Routine.RoutineDTO;
import es.codeurjc.gymapp.DTO.Routine.RoutineMapper;
import es.codeurjc.gymapp.DTO.Routine.RoutineSimpleDTO;
import es.codeurjc.gymapp.model.Routine;
import es.codeurjc.gymapp.model.User;
import es.codeurjc.gymapp.repositories.UserRepository;

@Service
public class UserServices {

    @Autowired
	private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private RoutineMapper routineMapper;

    public Optional<UserDTO> findById(Long id) {
        return Optional.of(userMapper.toDTO(userRepository.findById(id).get()));
    }

    Optional<User> findEntityById(Long id) {
        return userRepository.findById(id);
    }

    public List<UserDTO> findAll(){
        return userMapper.toDTOs(userRepository.findAll());
    }
    
    public Page<UserDTO> findAllPage(Pageable pageable){
        return userRepository.findAll(pageable).map(userMapper::toDTO);
    }

    public List<UserSimpleDTO> findAllSimple(){
        return userMapper.toSimpleDTOs(userRepository.findAll());
    }

    public long count(){
        return userRepository.count();
    }

    public void save(UserDTO userDTO, MultipartFile imageFile) throws IOException{
        User user = userMapper.toDomain(userDTO);
        if(!imageFile.isEmpty()) {
            user.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(),
            imageFile.getSize()));
        }
        userRepository.save(user);
    }

    void save(User user, MultipartFile imageFile) throws IOException{
        if(!imageFile.isEmpty()) {
            user.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(),
            imageFile.getSize()));
        }
        userRepository.save(user);
    }

    public void save(UserDTO userDTO) {
        User user = userMapper.toDomain(userDTO);
        userRepository.save(user);
    }
    
    void save(User user){
        User existingUser = findEntityById(user.getId()).orElseThrow();
        user.setImageFile(existingUser.getImageFile());
        userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<UserDTO> findByName(String name) {
        Optional<User> user = userRepository.findByName(name);
        if(user.isPresent()) return Optional.of(userMapper.toDTO(userRepository.findByName(name).get()));
        else return Optional.empty();
    }

    public Optional<UserSimpleDTO> findByNameSimple(String name) {
        return Optional.of(userMapper.toSimpleDTO(userRepository.findByName(name).get()));
    }

    public void addRoutine(UserDTO userDTO, RoutineDTO routineDTO) {
        User user = userMapper.toDomain(userDTO);
        Routine routine = routineMapper.toDomain(routineDTO);
        user.addRoutine(routine);

        save(user);
    }
    
    public void deleteRoutine(UserDTO userDTO, RoutineSimpleDTO routineDTO){
        User user = userMapper.toDomain(userDTO);
        Routine routine = routineMapper.toDomain(routineDTO);
        user.getRoutines().remove(routine);
        save(user);
    }

    public void deleteRoutine(UserDTO userDTO, RoutineDTO routineDTO){
        deleteRoutine(userDTO, routineMapper.toSimpleDTO(routineMapper.toDomain(routineDTO)));
    }

    public UserDTO toDTO(User user){
        return userMapper.toDTO(user);
    }

    public void setImageFile(UserDTO userDTO, MultipartFile image) throws IOException {
        User user = userMapper.toDomain(userDTO);
        if (image.isEmpty()) {
            user.setImageFile(null);
        } else {
            user.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.getSize()));
        }
		save(user, image);
    }

    public Resource getUserImage(Long id) throws SQLException {
        User user = userRepository.findById(id).orElseThrow();

		if (user.getImageFile() != null) {
			return new InputStreamResource(user.getImageFile().getBinaryStream());
		} else {
			return null;
		}
    }
}
