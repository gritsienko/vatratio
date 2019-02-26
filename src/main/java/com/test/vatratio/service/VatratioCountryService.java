package com.test.vatratio.service;

import java.util.List;

import com.test.vatratio.model.Country;

public interface VatratioCountryService {
    List<Country> getCountriesSortedByVatAsc(int limit);
    List<Country> getCountriesSortedByVatDesc(int limit);
}
