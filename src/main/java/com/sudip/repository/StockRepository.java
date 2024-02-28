package com.sudip.repository;

import com.sudip.mode.Stock;
import io.micronaut.data.repository.PageableRepository;
import jakarta.inject.Singleton;

@Singleton
public interface StockRepository extends PageableRepository<Stock, String> {

}
