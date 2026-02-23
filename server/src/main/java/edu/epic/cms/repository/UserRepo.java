package edu.epic.cms.repository;

import edu.epic.cms.model.User;
import java.util.List;

public interface UserRepo {
    List<User> getAllUsers();
}
