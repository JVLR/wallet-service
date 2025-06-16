
package com.wallet.service;

import com.wallet.exception.DataException;
import com.wallet.model.HistoricBalance;
import com.wallet.model.OperationType;
import com.wallet.model.dto.RetrieveHistoricBalanceRequest;
import com.wallet.model.dto.RetrieveHistoricBalanceResponse;
import com.wallet.repository.WalletsRepositoryJpa;
import com.wallet.repository.entity.HistoricBalanceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RetrieveHistoricBalanceServiceTest {

    private WalletsRepositoryJpa walletsRepository;
    private RetrieveHistoricBalanceService service;

    @BeforeEach
    void setUp() {
        walletsRepository = mock(WalletsRepositoryJpa.class);
        service = new RetrieveHistoricBalanceService(walletsRepository);
    }

    @Test
    void shouldReturnHistoricBalanceList() {
        String userId = "user-1";
        LocalDateTime start = LocalDate.now().minusDays(5).atStartOfDay();
        LocalDateTime end = LocalDate.now().atStartOfDay();
        RetrieveHistoricBalanceRequest request = mock(RetrieveHistoricBalanceRequest.class);
        when(request.userId()).thenReturn(userId);
        when(request.startDate()).thenReturn(start);
        when(request.endDate()).thenReturn(end);

        HistoricBalanceEntity entity = mock(HistoricBalanceEntity.class);
        HistoricBalance model = new HistoricBalance(
                "id-1",
                start,
                BigDecimal.TEN,
                OperationType.DEPOSIT.name(),
                userId
        );
        when(entity.toModel()).thenReturn(model);

        when(walletsRepository.findHistoricBalanceByUserAndDateIdAndDate(userId, start, end))
                .thenReturn(List.of(entity));

        List<RetrieveHistoricBalanceResponse> result = service.doExecute(request);

        assertEquals(1, result.size());
        assertEquals(BigDecimal.TEN, result.get(0).amount());
        assertEquals(OperationType.DEPOSIT.name(), result.get(0).operation());
        assertEquals(start.toLocalDate(), result.get(0).date().toLocalDate());
    }

    @Test
    void shouldThrowExceptionWhenNoHistoricFound() {
        String userId = "user-2";
        LocalDate start = LocalDate.now().minusDays(10);
        LocalDate end = LocalDate.now();
        RetrieveHistoricBalanceRequest request = mock(RetrieveHistoricBalanceRequest.class);
        when(request.userId()).thenReturn(userId);
        when(request.startDate()).thenReturn(start.atStartOfDay());
        when(request.endDate()).thenReturn(end.atStartOfDay());

        when(walletsRepository.findHistoricBalanceByUserAndDateIdAndDate(userId, start.atStartOfDay(), end.atStartOfDay()))
                .thenReturn(List.of());

        DataException ex = assertThrows(DataException.class, () -> service.doExecute(request));
        assertTrue(ex.getMessage().contains("Historical Balance not found for user: " + userId));
    }
}