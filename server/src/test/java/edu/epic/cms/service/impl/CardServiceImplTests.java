package edu.epic.cms.service.impl;

import edu.epic.cms.exception.CardCreationException;
import edu.epic.cms.exception.CardNotFoundException;
import edu.epic.cms.exception.OutstandingBalanceException;
import edu.epic.cms.model.Card;
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
