package edu.epic.cms.service.impl;

import edu.epic.cms.model.CardRequest;
import edu.epic.cms.repository.CardRepo;
import edu.epic.cms.repository.CardRequestRepo;
import edu.epic.cms.util.CardEncryptionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CardRequestServiceImplTests {

    private CardRequestServiceImpl cardRequestService;

    @Mock
    private CardRequestRepo cardRequestRepo;

    @Mock
    private CardRepo cardRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cardRequestService = new CardRequestServiceImpl(cardRequestRepo, cardRepo);
    }

    @Test
    void getAllCardRequests_ShouldReturnMaskedCardNumbers() {
        CardRequest request1 = new CardRequest();
        request1.setCardNumber("encrypted1");
        
        CardRequest request2 = new CardRequest();
        request2.setCardNumber("encrypted2");

        when(cardRequestRepo.getAllCardRequests(null, null)).thenReturn(Arrays.asList(request1, request2));

        try (MockedStatic<CardEncryptionUtil> mockedEncryption = mockStatic(CardEncryptionUtil.class)) {
            mockedEncryption.when(() -> CardEncryptionUtil.decrypt("encrypted1")).thenReturn("1234567890123456");
            mockedEncryption.when(() -> CardEncryptionUtil.maskCardNumber("1234567890123456")).thenReturn("123456******3456");
            
            mockedEncryption.when(() -> CardEncryptionUtil.decrypt("encrypted2")).thenReturn("6543210987654321");
            mockedEncryption.when(() -> CardEncryptionUtil.maskCardNumber("6543210987654321")).thenReturn("654321******4321");

            List<CardRequest> result = cardRequestService.getAllCardRequests(null, null);

            assertEquals(2, result.size());
            assertEquals("123456******3456", result.get(0).getCardNumber());
            assertEquals("encrypted1", result.get(0).getEncryptedCardNumber());
            assertEquals("654321******4321", result.get(1).getCardNumber());
            assertEquals("encrypted2", result.get(1).getEncryptedCardNumber());
        }
    }

    @Test
    void getCardRequestsByCardNumber_ShouldReturnMaskedCardNumbers() {
        CardRequest request1 = new CardRequest();
        request1.setCardNumber("encrypted1");

        when(cardRequestRepo.getCardRequestsByCardNumber("encrypted1")).thenReturn(Arrays.asList(request1));

        try (MockedStatic<CardEncryptionUtil> mockedEncryption = mockStatic(CardEncryptionUtil.class)) {
            mockedEncryption.when(() -> CardEncryptionUtil.decrypt("encrypted1")).thenReturn("1234567890123456");
            mockedEncryption.when(() -> CardEncryptionUtil.maskCardNumber("1234567890123456")).thenReturn("123456******3456");

            List<CardRequest> result = cardRequestService.getCardRequestsByCardNumber("encrypted1");

            assertEquals(1, result.size());
            assertEquals("123456******3456", result.get(0).getCardNumber());
            assertEquals("encrypted1", result.get(0).getEncryptedCardNumber());
        }
    }
}
