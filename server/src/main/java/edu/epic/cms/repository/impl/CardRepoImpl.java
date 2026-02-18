package edu.epic.cms.repository.impl;

import edu.epic.cms.model.Card;
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
    public List<Card> getAllCards() {
        String sql = "SELECT CardNumber, ExpireDate, CardStatus, CreditLimit, CashLimit, " +
                "AvailableCreditLimit, AvailableCashLimit, LastUpdateTime FROM Card";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Card card = new Card();
            card.setCardNumber(rs.getString("CardNumber"));
            card.setExpireDate(rs.getString("ExpireDate"));
            card.setCardStatus(rs.getString("CardStatus"));
            card.setCreditLimit(rs.getInt("CreditLimit"));
            card.setCashLimit(rs.getInt("CashLimit"));
            card.setAvailableCreditLimit(rs.getInt("AvailableCreditLimit"));
            card.setAvailableCashLimit(rs.getInt("AvailableCashLimit"));
            if (rs.getTimestamp("LastUpdateTime") != null) {
                card.setLastUpdateTime(rs.getTimestamp("LastUpdateTime").toLocalDateTime());
            }
            return card;
        });
    }
}
