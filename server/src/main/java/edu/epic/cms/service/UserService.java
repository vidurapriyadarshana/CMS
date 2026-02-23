package edu.epic.cms.service;

import edu.epic.cms.api.UserResponse;
import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
}
