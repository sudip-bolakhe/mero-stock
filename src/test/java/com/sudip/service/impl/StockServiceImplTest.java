package com.sudip.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.sudip.cloud.MyCloud;
import com.sudip.mode.Stock;
import com.sudip.repository.StockRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
class StockServiceImplTest {

  @Mock
  private StockRepository stockRepository;

  @Mock
  private MyCloud myCloud;

  @InjectMocks
  @Spy
  private StockServiceImpl stockService;


  @Test
  void testListAllShouldReturnPaginatedStock() {
    // given
    Pageable pageable = Pageable.from(1);
    Stock stock = mock(Stock.class);
    Page<Stock> stockPage = Page.of(Collections.singletonList(stock), Pageable.from(1), 1);
    given(stockRepository.findAll(pageable)).willReturn(stockPage);
    // When
    Page<Stock> result = stockService.listAll(pageable);
    // Then
    assertEquals(stockPage, result);
    verify(stockRepository).findAll(pageable);
  }

  @Test
  void testUpdateStatusShouldUpdateStatus() {
    // given
    Stock stock = mock(Stock.class);
    given(stockRepository.findById("1")).willReturn(Optional.of(stock));
    // When
    stockService.updateStatus("1", "NEW_PURCHASE");
    // Then
    verify(stock).setStatus("NEW_PURCHASE");
    verify(stockRepository).update(stock);
  }

  @Test
  void testUpdateStatusShouldUpdateStatus1() {
    // given
    Stock stock = mock(Stock.class);
    Stock updatedStock = mock(Stock.class);
    Stock fromCognito = mock(Stock.class);
   doReturn(stock).when(stockService).getById("1");
   given(stockRepository.update(stock)).willReturn(updatedStock);
   given(myCloud.updateInCongnito(any(Stock.class),  eq("NEW_PURCHASE"))).willReturn(fromCognito);
    // When
    stockService.updateStatus("1", "NEW_PURCHASE");
    // Then
    verify(stock).setStatus("NEW_PURCHASE");
    verify(fromCognito).setStatus("Updated in aws");
    verify(stockRepository).update(stock);
  }

  @Test
  void testGetByIdShouldReturnStock() {
    // given
    Stock stock = mock(Stock.class);
    given(stock.getName()).willReturn("mlbsl");
    given(stockRepository.findById("1")).willReturn(Optional.of(stock));
    // When
    Stock result = stockService.getById("1");
    // Then
    assertEquals(stock, result);
    assertEquals("mlbsl", result.getName());
  }

  @Test
  void testGetByIdWithErrorFromRepoShouldReturnNull() {
    // given
    given(stockRepository.findById("1")).willThrow(new RuntimeException(" From Db"));
    // When
    Stock result = stockService.getById("1");
    // Then
    assertNull(result);
  }
  @Test
  void testGetByIdWithInvalidIdShouldThrowError() {
    // given
    given(stockRepository.findById("1")).willReturn(Optional.empty());
    // when //then
    Exception exception = assertThrows(
        RuntimeException.class,
        () -> stockService.getById("1")
    );
    assertEquals("Stock not found by Id : 1", exception.getMessage());
  }

}
