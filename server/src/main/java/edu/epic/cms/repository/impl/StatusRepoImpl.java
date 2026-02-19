package edu.epic.cms.repository.impl;

import edu.epic.cms.model.Status;
import edu.epic.cms.repository.StatusRepo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StatusRepoImpl implements StatusRepo {

    private final JdbcTemplate jdbcTemplate;

    public StatusRepoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Status> getAllStatus() {
        String sql = "SELECT StatusCode, Description FROM Status";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Status status = new Status();
            status.setStatusCode(rs.getString("StatusCode"));
            status.setDescription(rs.getString("Description"));
            return status;
        });
    }
}
