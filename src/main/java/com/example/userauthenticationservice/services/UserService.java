package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findUserById(id);
        if(optionalUser.isEmpty()){
            return null;
        }

        return optionalUser.get();
    }
}
