package edu.epic.cms.repository.impl;

import edu.epic.cms.model.CardRequest;
import edu.epic.cms.repository.CardRequestRepo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CardRequestRepoImpl implements CardRequestRepo {

    private final JdbcTemplate jdbcTemplate;

    public CardRequestRepoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<CardRequest> rowMapper = (rs, rowNum) -> {
        CardRequest request = new CardRequest();
        request.setRequestId(rs.getInt("RequestId"));
        request.setRequestReasonCode(rs.getString("RequestReasonCode"));
        request.setRemark(rs.getString("Remark"));
        request.setCardNumber(rs.getString("CardNumber"));
        if (rs.getTimestamp("CreatedTime") != null) {
            request.setCreatedTime(rs.getTimestamp("CreatedTime").toLocalDateTime());
        }
        request.setApprovedUser(rs.getString("ApprovedUser"));
        request.setRequestStatus(rs.getString("RequestStatus"));
        request.setRequestedUser(rs.getString("RequestedUser"));
        return request;
    };

    @Override
    public List<CardRequest> getAllCardRequests(String requestReasonCode, String requestStatus) {
        StringBuilder sql = new StringBuilder("SELECT RequestId, RequestReasonCode, Remark, CardNumber, CreatedTime, ApprovedUser, RequestStatus, RequestedUser FROM CardRequest WHERE 1=1");
        List<Object> params = new java.util.ArrayList<>();

        if (requestReasonCode != null && !requestReasonCode.isEmpty()) {
            sql.append(" AND RequestReasonCode = ?");
            params.add(requestReasonCode);
        }

        if (requestStatus != null && !requestStatus.isEmpty()) {
            sql.append(" AND RequestStatus = ?");
            params.add(requestStatus);
        }

        return jdbcTemplate.query(sql.toString(), rowMapper, params.toArray());
    }

    @Override
    public List<CardRequest> getCardRequestsByCardNumber(String cardNumber) {

        String sql = "SELECT RequestId, RequestReasonCode, Remark, CardNumber, CreatedTime, ApprovedUser, RequestStatus, RequestedUser FROM CardRequest WHERE CardNumber = ?";
        return jdbcTemplate.query(sql, rowMapper, cardNumber);
    }

    @Override
    public boolean createCardRequest(CardRequest cardRequest) {
        String sql = "INSERT INTO CardRequest (RequestReasonCode, Remark, CardNumber, RequestedUser) VALUES (?, ?, ?, ?)";
        int result = jdbcTemplate.update(sql,
                cardRequest.getRequestReasonCode(),
                cardRequest.getRemark(),
                cardRequest.getCardNumber(),
                cardRequest.getRequestedUser());
        return result > 0;
    }

    @Override
    public boolean hasPendingRequest(String cardNumber) {
        String sql = "SELECT COUNT(*) FROM CardRequest WHERE CardNumber = ? AND RequestStatus = 'PENDING'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cardNumber);
        return count != null && count > 0;
    }

    @Override
    public boolean updateStatusByCardNumber(String cardNumber, String status, String approvedUser, String requestStatus) {
        String sql = "UPDATE Card c " +
                     "JOIN CardRequest cr ON c.CardNumber = cr.CardNumber " +
                     "SET c.CardStatus = ?, c.LastUpdatedUser = ?, cr.ApprovedUser = ?, cr.RequestStatus = ? " +
                     "WHERE c.CardNumber = ? AND cr.RequestStatus = 'PENDING'";
        int result = jdbcTemplate.update(sql, status, approvedUser, approvedUser, requestStatus, cardNumber);
        return result > 0;
    }

    @Override
    public boolean markRequestAsFailed(String cardNumber, String approvedUser) {
        String sql = "UPDATE CardRequest SET RequestStatus = 'FAILED', ApprovedUser = ? WHERE CardNumber = ? AND RequestStatus = 'PENDING'";
        int result = jdbcTemplate.update(sql, approvedUser, cardNumber);
        return result > 0;
    }

    @Override
    public boolean markRequestAsDeactivated(String cardNumber) {
        String sql = "UPDATE CardRequest SET RequestStatus = 'COMPLETE' WHERE CardNumber = ? AND RequestStatus = 'PENDING'";
        int result = jdbcTemplate.update(sql, cardNumber);
        return result > 0;
    }

    @Override
    public boolean isCardDeactivated(String cardNumber) {
        String sql = "SELECT COUNT(*) FROM CardRequest WHERE CardNumber = ? AND RequestStatus = 'COMPLETE' AND RequestReasonCode = 'CDCL'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cardNumber);
        return count != null && count > 0;
    }
}
