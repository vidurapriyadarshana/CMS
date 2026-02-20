package edu.epic.cms.service.impl;

import edu.epic.cms.exception.CardCreationException;
import edu.epic.cms.exception.CardNotFoundException;
import edu.epic.cms.exception.OutstandingBalanceException;
import edu.epic.cms.model.Card;
import edu.epic.cms.api.UpdateCard;
import edu.epic.cms.repository.CardRepo;
import edu.epic.cms.repository.CardRequestRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceImplTests {

    private CardServiceImpl cardService;

    @Mock
    private CardRepo cardRepo;

    @Mock
    private CardRequestRepo cardRequestRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cardService = new CardServiceImpl(cardRepo, cardRequestRepo);
    }

    @Test
    void createCard_ShouldThrowException_WhenLimitsAreInvalid() {
        Card card = new Card();
        card.setCardNumber("1234567890123456");
        
        // Negative credit limit
        card.setCreditLimit(-100);
        assertThrows(CardCreationException.class, () -> cardService.createCard(card));

        // Negative cash limit
        card.setCreditLimit(1000);
        card.setCashLimit(-50);
        assertThrows(CardCreationException.class, () -> cardService.createCard(card));

        // Cash limit > Credit limit
        card.setCreditLimit(1000);
        card.setCashLimit(1100);
        CardCreationException exception = assertThrows(CardCreationException.class, () -> cardService.createCard(card));
        assertEquals("Cash limit cannot exceed credit limit", exception.getMessage());
    }

    @Test
    void createCard_ShouldSetAvailableLimits_WhenValid() {
        Card card = new Card();
        card.setCardNumber("1234567890123456");
        card.setCreditLimit(1000);
        card.setCashLimit(500);

        when(cardRepo.existsByCardNumber(anyString())).thenReturn(false);
        when(cardRepo.createCard(any(Card.class))).thenReturn(true);

        cardService.createCard(card);

        assertEquals(1000, card.getAvailableCreditLimit());
        assertEquals(500, card.getAvailableCashLimit());
    }

    @Test
    void updateCard_ShouldThrowException_WhenLimitsAreInvalid() {
        String cardNumber = "encrypted123";
        UpdateCard updateCard = new UpdateCard();
        updateCard.setExpireDate("12/25");
        updateCard.setCreditLimit(1000);
        updateCard.setCashLimit(500);
        updateCard.setAvailableCreditLimit(1000);
        updateCard.setAvailableCashLimit(500);

        when(cardRepo.existsByCardNumber(cardNumber)).thenReturn(true);

        // Cash limit > Credit limit
        updateCard.setCashLimit(1100);
        assertThrows(CardCreationException.class, () -> cardService.updateCard(cardNumber, updateCard));
        updateCard.setCashLimit(500);

        // Available credit limit > Credit limit
        updateCard.setAvailableCreditLimit(1100);
        assertThrows(CardCreationException.class, () -> cardService.updateCard(cardNumber, updateCard));
        updateCard.setAvailableCreditLimit(1000);

        // Available cash limit > Cash limit
        updateCard.setAvailableCashLimit(600);
        assertThrows(CardCreationException.class, () -> cardService.updateCard(cardNumber, updateCard));
        updateCard.setAvailableCashLimit(500);

        // Available cash limit > Available credit limit
        updateCard.setAvailableCreditLimit(400);
        assertThrows(CardCreationException.class, () -> cardService.updateCard(cardNumber, updateCard));
    }

    @Test
    void deleteCard_ShouldThrowNotFound_WhenCardDoesNotExist() {
        String cardNumber = "encrypted123";
        when(cardRepo.existsByCardNumber(cardNumber)).thenReturn(false);

        assertThrows(CardNotFoundException.class, () -> cardService.deleteCard(cardNumber));
        verify(cardRepo, never()).deleteCard(anyString());
    }

    @Test
    void deleteCard_ShouldThrowDeactivated_WhenCardIsDeactivated() {
        String cardNumber = "encrypted123";
        when(cardRepo.existsByCardNumber(cardNumber)).thenReturn(true);
        when(cardRequestRepo.isCardDeactivated(cardNumber)).thenReturn(true);

        CardCreationException exception = assertThrows(CardCreationException.class, () -> cardService.deleteCard(cardNumber));
        assertEquals("Card is deactivated", exception.getMessage());
        verify(cardRepo, never()).deleteCard(anyString());
    }

    @Test
    void deleteCard_ShouldThrowNoPendingRequest_WhenNoPendingRequest() {
        String cardNumber = "encrypted123";
        when(cardRepo.existsByCardNumber(cardNumber)).thenReturn(true);
        when(cardRequestRepo.isCardDeactivated(cardNumber)).thenReturn(false);
        when(cardRequestRepo.hasPendingRequest(cardNumber)).thenReturn(false);

        CardCreationException exception = assertThrows(CardCreationException.class, () -> cardService.deleteCard(cardNumber));
        assertEquals("No pending request found for this card number", exception.getMessage());
        verify(cardRepo, never()).deleteCard(anyString());
    }

    @Test
    void deleteCard_ShouldThrowOutstandingBalance_WhenBalanceExists() {
        String cardNumber = "encrypted123";
        Card card = new Card();
        card.setCreditLimit(1000);
        card.setAvailableCreditLimit(500);

        when(cardRepo.existsByCardNumber(cardNumber)).thenReturn(true);
        when(cardRequestRepo.isCardDeactivated(cardNumber)).thenReturn(false);
        when(cardRequestRepo.hasPendingRequest(cardNumber)).thenReturn(true);
        when(cardRepo.getCardByNumber(cardNumber)).thenReturn(card);

        OutstandingBalanceException exception = assertThrows(OutstandingBalanceException.class, () -> cardService.deleteCard(cardNumber));
        assertEquals("Cannot deactivate card: outstanding balance exists", exception.getMessage());
        verify(cardRequestRepo).markRequestAsFailed(cardNumber);
        verify(cardRepo, never()).deleteCard(anyString());
    }

    @Test
    void deleteCard_ShouldDelete_WhenValid() {
        String cardNumber = "encrypted123";
        Card card = new Card();
        card.setCreditLimit(1000);
        card.setAvailableCreditLimit(1000);

        when(cardRepo.existsByCardNumber(cardNumber)).thenReturn(true);
        when(cardRequestRepo.isCardDeactivated(cardNumber)).thenReturn(false);
        when(cardRequestRepo.hasPendingRequest(cardNumber)).thenReturn(true);
        when(cardRepo.getCardByNumber(cardNumber)).thenReturn(card);
        when(cardRepo.deleteCard(cardNumber)).thenReturn(true);

        assertTrue(cardService.deleteCard(cardNumber));
        verify(cardRepo).deleteCard(cardNumber);
        verify(cardRequestRepo).markRequestAsDeactivated(cardNumber);
    }
}
