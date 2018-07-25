/**
 * Copyright 2018, Banco Colpatria Multibanca Colpatria S.A. Digital Factory
 * Colpatria http://www.colpatria.com
 *
 * All rights reserved
 * Date: 25/07/2018
 */
package com.belatrix.test.moneyxchangeapi.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import com.belatrix.test.moneyxchangeapi.dto.RateDto;
import com.belatrix.test.moneyxchangeapi.service.RatesService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RatesControllerTest {

  @Mock
  private RatesService ratesService;

  private RatesController controller;

  @Before
  public void setup() {
    controller = new RatesController(ratesService);
  }

  @Test
  public void shouldRespondRequestedRates() {
    RateDto expectedRate = RateDto.builder()
        .date(LocalDate.now())
        .base("USD")
        .rates(Map.of("EUR", 0.8))
        .build();
    when(ratesService.getRateFor("USD", List.of("EUR"))).thenReturn(expectedRate);

    RateDto rate = controller.getRates("USD", List.of("EUR"));

    assertThat(rate, is(equalTo(expectedRate)));
  }

}
