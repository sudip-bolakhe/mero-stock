package com.sudip.cloud;

import com.sudip.mode.Stock;
import jakarta.inject.Singleton;

@Singleton
public class MyCloudImpl implements MyCloud{

  @Override
  public Stock updateInCongnito(Stock s, String str) {
    Stock stock = new Stock();
    stock.setCode("001");
    return stock;
  }
}
