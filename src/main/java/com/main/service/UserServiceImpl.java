package com.main.service;

import com.main.dto.UserDTO;
import com.main.model.userTypes.User;
import com.main.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
   public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void save(UserDTO userDTO) {
        userRepository.save(new User(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFullname()));
    }
}
