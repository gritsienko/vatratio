package com.test.vatratio;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.test.vatratio.model.Period;

public abstract class AbstractTest {

    public List<Period> getPeriods() {
        List<Period> periods = new LinkedList<>();
        for (int i = 0; i < 5; ++i) {
            Period period = new Period();
            Map<String, Double> rates = getRates(1, 5);
            rates.putAll(getRates(5, 10));
            rates.putAll(getRates(10, 15));
            period.setRates(rates);
            periods.add(period);
        }
        return periods;
    }

    public Map<String, Double> getRates(double from, double to) {
        Map<String, Double> rates = new HashMap<>();
        for (double i = from; i <= to; ++i) {
            rates.put("rate" + i, i);
        }
        return rates;
    }
}
