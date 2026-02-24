package edu.epic.cms.service;

import edu.epic.cms.api.UserResponseDTO;
import java.util.List;

public interface UserService {
    List<UserResponseDTO> getAllUsers();
}
