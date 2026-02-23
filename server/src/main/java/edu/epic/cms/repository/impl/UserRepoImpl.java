package edu.epic.cms.repository.impl;

import edu.epic.cms.model.User;
import edu.epic.cms.repository.UserRepo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepoImpl implements UserRepo {

    private final JdbcTemplate jdbcTemplate;

    public UserRepoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT UserName, Status, Name FROM User";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setUserName(rs.getString("UserName"));
            user.setStatus(rs.getString("Status"));
            user.setName(rs.getString("Name"));
            return user;
        });
    }
}
