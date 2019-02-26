package com.test.vatratio.model;

import java.util.Comparator;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@ToString(exclude = { "periods", "code", "country_code" })
public class Country {
    List<Period> periods;
    private String name;
    private String code;
    private String country_code;
    private double maxRatePerCounty;
    private double minRatePerCounty;

    public void setPeriods(final List<Period> periods) {
        this.periods = periods;
        maxRatePerCounty = periods.stream().max(Comparator.comparingDouble(Period::getMaxRate)).get().getMaxRate();
        minRatePerCounty = periods.stream().min(Comparator.comparingDouble(Period::getMinRate)).get().getMinRate();
    }
}
