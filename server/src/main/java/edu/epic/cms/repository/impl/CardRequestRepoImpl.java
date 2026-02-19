package edu.epic.cms.repository.impl;

import edu.epic.cms.model.CardRequest;
import edu.epic.cms.repository.CardRequestRepo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CardRequestRepoImpl implements CardRequestRepo {

    private final JdbcTemplate jdbcTemplate;

    public CardRequestRepoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean createCardRequest(CardRequest cardRequest) {
        String sql = "INSERT INTO CardRequest (RequestReasonCode, Remark, CardNumber, Status) VALUES (?, ?, ?, ?)";
        int result = jdbcTemplate.update(sql,
                cardRequest.getRequestReasonCode(),
                cardRequest.getRemark(),
                cardRequest.getCardNumber(),
                cardRequest.getStatus());
        return result > 0;
    }

    @Override
    public boolean hasPendingRequest(String cardNumber) {
        String sql = "SELECT COUNT(*) FROM CardRequest WHERE CardNumber = ? AND CompletionStatus = 'PENDING'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cardNumber);
        return count != null && count > 0;
    }

    @Override
    public boolean updateStatusByCardNumber(String cardNumber, String status) {
        String sql = "UPDATE CardRequest SET Status = ? WHERE CardNumber = ? AND CompletionStatus = 'PENDING'";
        int result = jdbcTemplate.update(sql, status, cardNumber);
        return result > 0;
    }
}

