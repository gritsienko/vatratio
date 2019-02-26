package com.test.vatratio.rest;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CountryResponse {
    private String countryName;
    private double maxVatRateValue;
    private double minVatRateValue;
}
