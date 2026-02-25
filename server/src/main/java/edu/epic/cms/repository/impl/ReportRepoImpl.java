package edu.epic.cms.repository.impl;

import edu.epic.cms.dto.CardReportDTO;
import edu.epic.cms.dto.CardRequestReportDTO;
import edu.epic.cms.repository.ReportRepo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReportRepoImpl implements ReportRepo {

    private final JdbcTemplate jdbcTemplate;

    public ReportRepoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<CardReportDTO> getAllCardsWithUserNames(String cardStatus) {
        StringBuilder sql = new StringBuilder("SELECT c.CardNumber, c.ExpireDate, c.CardStatus, c.CreditLimit, c.CashLimit, " +
                "c.AvailableCreditLimit, c.AvailableCashLimit, c.LastUpdateTime, c.LastUpdatedUser, " +
                "u.Name as LastUpdatedUserName, s.Description as CardStatusDescription " +
                "FROM Card c " +
                "LEFT JOIN User u ON c.LastUpdatedUser = u.UserName " +
                "LEFT JOIN Status s ON c.CardStatus = s.StatusCode WHERE 1=1");

        List<Object> params = new java.util.ArrayList<>();
        if (cardStatus != null && !cardStatus.isEmpty()) {
            sql.append(" AND c.CardStatus = ?");
            params.add(cardStatus);
        }

        return jdbcTemplate.query(sql.toString(), (rs, rowNum) -> {
            CardReportDTO card = new CardReportDTO();
            card.setCardNumber(rs.getString("CardNumber"));
            card.setExpireDate(rs.getString("ExpireDate"));
            card.setCardStatus(rs.getString("CardStatus"));
            card.setCardStatusDescription(rs.getString("CardStatusDescription"));
            card.setCreditLimit(rs.getInt("CreditLimit"));
            card.setCashLimit(rs.getInt("CashLimit"));
            card.setAvailableCreditLimit(rs.getInt("AvailableCreditLimit"));
            card.setAvailableCashLimit(rs.getInt("AvailableCashLimit"));
            if (rs.getTimestamp("LastUpdateTime") != null) {
                card.setLastUpdateTime(rs.getTimestamp("LastUpdateTime").toLocalDateTime());
            }
            card.setLastUpdatedUser(rs.getString("LastUpdatedUser"));
            card.setLastUpdatedUserName(rs.getString("LastUpdatedUserName"));
            return card;
        }, params.toArray());
    }

    @Override
    public List<CardRequestReportDTO> getAllCardRequestsWithUserNames(String requestReasonCode, String requestStatus) {
        StringBuilder sql = new StringBuilder("SELECT cr.RequestId, cr.RequestReasonCode, cr.Remark, cr.CardNumber, cr.CreatedTime, " +
                "cr.ApprovedUser, cr.RequestStatus, cr.RequestedUser, u1.Name as RequestedUserName, " +
                "u2.Name as ApprovedUserName, ct.Description as RequestReasonDescription " +
                "FROM CardRequest cr " +
                "LEFT JOIN User u1 ON cr.RequestedUser = u1.UserName " +
                "LEFT JOIN User u2 ON cr.ApprovedUser = u2.UserName " +
                "LEFT JOIN CardRequestType ct ON cr.RequestReasonCode = ct.Code WHERE 1=1");

        List<Object> params = new java.util.ArrayList<>();
        if (requestReasonCode != null && !requestReasonCode.isEmpty()) {
            sql.append(" AND cr.RequestReasonCode = ?");
            params.add(requestReasonCode);
        }
        if (requestStatus != null && !requestStatus.isEmpty()) {
            sql.append(" AND cr.RequestStatus = ?");
            params.add(requestStatus);
        }

        return jdbcTemplate.query(sql.toString(), (rs, rowNum) -> {
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
        }, params.toArray());
    }
}
