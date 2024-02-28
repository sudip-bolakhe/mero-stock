package com.sudip.controller;

import com.sudip.service.StockService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

@Controller("/stocks")
public class StockController {

  @Inject
  private StockService service;

  @Get("/")
  public String listStocks(){
    return "List of the stocks";
  }
}
