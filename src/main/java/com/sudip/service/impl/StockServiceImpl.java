package com.sudip.service.impl;

import com.sudip.cloud.MyCloud;
import com.sudip.mode.Stock;
import com.sudip.repository.StockRepository;
import com.sudip.service.StockService;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;
import java.text.MessageFormat;
import java.util.Optional;

@Singleton
public class StockServiceImpl implements StockService {

  private final StockRepository stockRepository;
  private final MyCloud myCloud;

  public StockServiceImpl(StockRepository stockRepository, MyCloud myCloud) {
    this.stockRepository = stockRepository;
    this.myCloud = myCloud;
  }

  @Override
  public Stock add(Stock stock) {
    return stockRepository.save(stock);
  }

  @Override
  public Page<Stock> listAll(Pageable pageable) {
    return stockRepository.findAll(pageable);
  }

  @Override
  public Stock getById(String id) {
    try{
      return stockRepository.findById(id)
          .orElseThrow( () -> new RuntimeException(
              MessageFormat.format("Stock not found by Id : {0}", id)
          ));
    } catch (Exception e) {
      return null;
    }

  }

  @Override
  public void updateStatus(String id, String status) {
    Optional.ofNullable(getById(id))
        .ifPresent(stock -> {
          stock.setStatus(status);
          Stock updated = stockRepository.update(stock);
          Stock stock1 = new Stock();
          Stock stock2 = myCloud.updateInCongnito(stock1, status);
          stock2.setStatus("Updated in aws");
        });
  }
}
