package com.test.vatratio.model;

import java.util.Collections;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "rates")
public class Period {

    private String effective_from;
    private Map<String, Double> rates;
    private double maxRate;
    private double minRate;

    public void setRates(final Map<String, Double> rates) {
        this.rates = rates;
        maxRate = Collections.max(rates.entrySet(), Map.Entry.comparingByValue()).getValue();
        minRate = Collections.min(rates.entrySet(), Map.Entry.comparingByValue()).getValue();
    }
}
