package com.example.backend.service;

import com.example.backend.dto.UserDTO;
import com.example.backend.dto.UserRegistrationDTO;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID: " + id));
        return convertToDTO(user);
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable: " + username));
        return convertToDTO(user);
    }

    public UserDTO createUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByUsername(registrationDTO.username())) {
            throw new RuntimeException("Ce nom d'utilisateur existe d\u00e9j\u00e0");
        }

        if (userRepository.existsByEmail(registrationDTO.email())) {
            throw new RuntimeException("Cet email est d\u00e9j\u00e0 utilis\u00e9");
        }

        User user = new User();
        user.setUsername(registrationDTO.username());
        user.setPassword(passwordEncoder.encode(registrationDTO.password()));
        user.setEmail(registrationDTO.email());
        user.setFirstName(registrationDTO.firstName());
        user.setLastName(registrationDTO.lastName());
        user.setPhoneNumber(registrationDTO.phoneNumber());
        user.setRole(User.Role.USER);
        user.setActive(true);

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID: " + id));

        user.setEmail(userDTO.email());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        user.setPhoneNumber(userDTO.phoneNumber());

        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur introuvable avec l'ID: " + id);
        }
        userRepository.deleteById(id);
    }

    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID: " + id));
        user.setActive(false);
        userRepository.save(user);
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getRole().name(),
                user.getActive(),
                user.getCreatedAt()
        );
    }
}
