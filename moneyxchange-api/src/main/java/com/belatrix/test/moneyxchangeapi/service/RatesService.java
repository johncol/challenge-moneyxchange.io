package com.belatrix.test.moneyxchangeapi.service;

import com.belatrix.test.moneyxchangeapi.dto.RateDto;
import java.util.List;

public interface RatesService {

  RateDto getRateFor(String base, List<String> symbols);

}
