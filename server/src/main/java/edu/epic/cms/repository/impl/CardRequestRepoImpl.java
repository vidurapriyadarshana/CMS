package edu.epic.cms.repository.impl;

import edu.epic.cms.model.CardRequest;
import edu.epic.cms.api.CardRequestReportDTO;
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
    public List<CardRequest> getAllCardRequests() {
        String sql = "SELECT RequestId, RequestReasonCode, Remark, CardNumber, CreatedTime, ApprovedUser, RequestStatus, RequestedUser FROM CardRequest";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<CardRequestReportDTO> getAllCardRequestsWithUserNames() {
        String sql = "SELECT cr.RequestId, cr.RequestReasonCode, cr.Remark, cr.CardNumber, cr.CreatedTime, " +
                "cr.ApprovedUser, cr.RequestStatus, cr.RequestedUser, u1.Name as RequestedUserName, " +
                "u2.Name as ApprovedUserName, ct.Description as RequestReasonDescription " +
                "FROM CardRequest cr " +
                "LEFT JOIN User u1 ON cr.RequestedUser = u1.UserName " +
                "LEFT JOIN User u2 ON cr.ApprovedUser = u2.UserName " +
                "LEFT JOIN CardRequestType ct ON cr.RequestReasonCode = ct.Code";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CardRequestReportDTO request = new CardRequestReportDTO();
            request.setRequestId(rs.getInt("RequestId"));
            request.setRequestReasonCode(rs.getString("RequestReasonCode"));
            request.setRequestReasonDescription(rs.getString("RequestReasonDescription"));
            request.setRemark(rs.getString("Remark"));
            request.setCardNumber(rs.getString("CardNumber"));
            if (rs.getTimestamp("CreatedTime") != null) {
                request.setCreatedTime(rs.getTimestamp("CreatedTime").toLocalDateTime());
            }
            request.setApprovedUser(rs.getString("ApprovedUser"));
            request.setApprovedUserName(rs.getString("ApprovedUserName"));
            request.setRequestStatus(rs.getString("RequestStatus"));
            request.setRequestedUser(rs.getString("RequestedUser"));
            request.setRequestedUserName(rs.getString("RequestedUserName"));
            return request;
        });
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
