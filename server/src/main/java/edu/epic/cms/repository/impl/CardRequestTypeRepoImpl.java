package edu.epic.cms.repository.impl;

import edu.epic.cms.model.CardRequestType;
import edu.epic.cms.repository.CardRequestTypeRepo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CardRequestTypeRepoImpl implements CardRequestTypeRepo {

    private final JdbcTemplate jdbcTemplate;

    public CardRequestTypeRepoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<CardRequestType> getAllCardRequestTypes() {
        String sql = "SELECT Code, Description FROM CardRequestType";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CardRequestType type = new CardRequestType();
            type.setCode(rs.getString("Code"));
            type.setDescription(rs.getString("Description"));
            return type;
        });
    }
}
