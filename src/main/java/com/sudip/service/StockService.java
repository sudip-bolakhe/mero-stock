package com.sudip.service;

import com.sudip.mode.Stock;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

public interface StockService {

  Stock add(Stock stock);
  Page<Stock> listAll(Pageable pageable);

  Stock getById(String id);
  void updateStatus(String id, String status);
}
