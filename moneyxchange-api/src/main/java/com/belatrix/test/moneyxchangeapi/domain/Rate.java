package com.belatrix.test.moneyxchangeapi.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Rate extends BaseEntity {

  @Id
  private String name;

  private Double usdRate;

  @Override
  protected Object getIdentifier() {
    return getName();
  }
}
