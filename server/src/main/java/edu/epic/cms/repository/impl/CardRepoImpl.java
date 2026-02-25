package edu.epic.cms.repository.impl;

import edu.epic.cms.model.Card;
import edu.epic.cms.api.UpdateCardDTO;
import edu.epic.cms.repository.CardRepo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CardRepoImpl implements CardRepo {

    private final JdbcTemplate jdbcTemplate;

    public CardRepoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Card> getAllCards(String cardStatus) {
        StringBuilder sql = new StringBuilder("SELECT c.CardNumber, c.ExpireDate, c.CardStatus, s.Description as CardStatusDescription, " +
                "c.CreditLimit, c.CashLimit, c.AvailableCreditLimit, c.AvailableCashLimit, c.LastUpdateTime, c.LastUpdatedUser " +
                "FROM Card c LEFT JOIN Status s ON c.CardStatus = s.StatusCode WHERE 1=1");
        
        List<Object> params = new java.util.ArrayList<>();
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

    @Override
    public boolean createCard(Card card) {
        String sql = "INSERT INTO Card (CardNumber, ExpireDate, CreditLimit, CashLimit, " +
                "AvailableCreditLimit, AvailableCashLimit, LastUpdatedUser) VALUES (?, ?, ?, ?, ?, ?, ?)";

        int result = jdbcTemplate.update(sql,
                card.getCardNumber(),
                card.getExpireDate(),
                card.getCreditLimit(),
                card.getCashLimit(),
                card.getAvailableCreditLimit(),
                card.getAvailableCashLimit(),
                card.getLastUpdatedUser());
        
        return result > 0;
    }


    @Override
    public boolean existsByCardNumber(String cardNumber) {
        String sql = "SELECT COUNT(*) FROM Card WHERE CardNumber = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cardNumber);
        return count != null && count > 0;
    }

    @Override
    public boolean updateCard(String cardNumber, UpdateCardDTO updateCard) {
        String sql = "UPDATE Card SET ExpireDate = ?, CreditLimit = ?, CashLimit = ?, " +
                "AvailableCreditLimit = ?, AvailableCashLimit = ?, LastUpdatedUser = ? WHERE CardNumber = ?";

        int result = jdbcTemplate.update(sql,
                updateCard.getExpireDate(),
                updateCard.getCreditLimit(),
                updateCard.getCashLimit(),
                updateCard.getAvailableCreditLimit(),
                updateCard.getAvailableCashLimit(),
                updateCard.getLastUpdatedUser(),
                cardNumber);

        return result > 0;
    }

    @Override
    public Card getCardByNumber(String cardNumber) {
        String sql = "SELECT c.CardNumber, c.ExpireDate, c.CardStatus, s.Description as CardStatusDescription, " +
                "c.CreditLimit, c.CashLimit, c.AvailableCreditLimit, c.AvailableCashLimit, c.LastUpdateTime, c.LastUpdatedUser " +
                "FROM Card c LEFT JOIN Status s ON c.CardStatus = s.StatusCode WHERE c.CardNumber = ?";

        List<Card> cards = jdbcTemplate.query(sql, (rs, rowNum) -> {
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
        }, cardNumber);


        return cards.stream().findFirst().orElse(null);
    }

    @Override
    public boolean deleteCard(String cardNumber) {
        String sql = "DELETE FROM Card WHERE CardNumber = ?";
        int result = jdbcTemplate.update(sql, cardNumber);
        return result > 0;
    }
}
