package edu.epic.cms.service.impl;

import edu.epic.cms.api.UserResponseDTO;
import edu.epic.cms.model.User;
import edu.epic.cms.repository.UserRepo;
import edu.epic.cms.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepo.getAllUsers();
        List<UserResponseDTO> responses = new ArrayList<>();
        for (User user : users) {
            UserResponseDTO response = new UserResponseDTO();
            response.setUserName(user.getUserName());
            response.setStatus(user.getStatus());
            response.setName(user.getName());
            responses.add(response);
        }
        return responses;
    }
}
