package edu.epic.cms.search.repository.impl;

import edu.epic.cms.model.Card;
import edu.epic.cms.search.repository.CardSearchRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CardSearchRepositoryImpl implements CardSearchRepository {

    private final JdbcTemplate jdbcTemplate;

    public CardSearchRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Card> searchCards(String encryptedCardNumber, String cardStatus) {
        StringBuilder sql = new StringBuilder("SELECT c.CardNumber, c.ExpireDate, c.CardStatus, s.Description as CardStatusDescription, " +
                "c.CreditLimit, c.CashLimit, c.AvailableCreditLimit, c.AvailableCashLimit, c.LastUpdateTime, c.LastUpdatedUser " +
                "FROM Card c LEFT JOIN Status s ON c.CardStatus = s.StatusCode WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (encryptedCardNumber != null && !encryptedCardNumber.isEmpty()) {
            sql.append(" AND c.CardNumber = ?");
            params.add(encryptedCardNumber);
        }

        if (cardStatus != null && !cardStatus.isEmpty()) {
            sql.append(" AND c.CardStatus = ?");
            params.add(cardStatus);
        }

        return jdbcTemplate.query(sql.toString(), (rs, rowNum) -> {
            Card card = new Card();
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
            return card;
        }, params.toArray());
    }
}
