/**
 * Copyright 2018, Banco Colpatria Multibanca Colpatria S.A. Digital Factory
 * Colpatria http://www.colpatria.com
 *
 * All rights reserved
 * Date: 25/07/2018
 */
package com.belatrix.test.moneyxchangeapi.service.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import com.belatrix.test.moneyxchangeapi.domain.Rate;
import com.belatrix.test.moneyxchangeapi.dto.RateDto;
import com.belatrix.test.moneyxchangeapi.exception.ValutaNotSupportedException;
import com.belatrix.test.moneyxchangeapi.repository.RateRepository;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StoredRatesServiceTest {

  @Mock
  public RateRepository repository;

  private StoredRatesService storedRatesService;

  @Before
  public void setup() {
    storedRatesService = new StoredRatesService(repository);
  }

  @Test(expected = ValutaNotSupportedException.class)
  public void shouldThrowValutaNotSupportedExceptionWhenBaseValutaIsNotFound() {
    when(repository.findOneByName("EUR")).thenReturn(Optional.empty());

    storedRatesService.getRateFor("EUR", List.of("USD"));
  }

  @Test(expected = ValutaNotSupportedException.class)
  public void shouldThrowValutaNotSupportedExceptionWhenNoneOfTheSymbolsAreFound() {
    when(repository.findOneByName("EUR")).thenReturn(Optional.of(Rate.of("EUR", 0.83)));
    when(repository.findOneByName("USD")).thenReturn(Optional.empty());

    storedRatesService.getRateFor("EUR", List.of("USD"));
  }

  @Test
  public void shouldReturnExchangeRatesForRequestedBase() {
    when(repository.findOneByName("EUR")).thenReturn(Optional.of(Rate.of("EUR", 0.83)));
    when(repository.findOneByName("USD")).thenReturn(Optional.of(Rate.of("USD", 1.00)));

    RateDto rates = storedRatesService.getRateFor("EUR", List.of("USD"));

    assertThat(rates.getBase(), is(equalTo("EUR")));
  }

  @Test
  public void shouldReturnExchangeRatesForTheValutasThatAreFound() {
    when(repository.findOneByName("EUR")).thenReturn(Optional.of(Rate.of("EUR", 0.83)));
    when(repository.findOneByName("USD")).thenReturn(Optional.of(Rate.of("USD", 1.00)));
    when(repository.findOneByName("COP")).thenReturn(Optional.empty());

    RateDto rates = storedRatesService.getRateFor("EUR", List.of("USD", "COP"));

    Double copRateValue = rates.getRates().get("COP");
    assertThat(copRateValue, is(nullValue()));

    Double usdRateValue = rates.getRates().get("USD");
    assertThat(usdRateValue, is(not(nullValue())));
  }

  @Test
  public void shouldCalculateEachValutaRateAsARelationBetweenTheSymbolUsdRateAndTheBaseUsdRate() {
    when(repository.findOneByName("EUR")).thenReturn(Optional.of(Rate.of("EUR", 0.83)));
    when(repository.findOneByName("COP")).thenReturn(Optional.of(Rate.of("COP", 2983.4)));

    RateDto rates = storedRatesService.getRateFor("EUR", List.of("COP"));

    double copRateValue = rates.getRates().get("COP");

    assertThat(copRateValue, is(equalTo(2983.4 / 0.83)));
  }

}
