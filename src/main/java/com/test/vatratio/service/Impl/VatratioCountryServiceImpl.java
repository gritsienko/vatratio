package com.test.vatratio.service.Impl;

import java.util.Comparator;
import java.util.List;

import com.test.vatratio.model.Country;
import com.test.vatratio.service.VatratioCountryService;
import com.test.vatratio.util.RestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VatratioCountryServiceImpl implements VatratioCountryService {

    @Autowired
    private RestService restService;

    @Override
    public List<Country> getCountriesSortedByVatAsc(int limit) {
        return getSortedCountries(Comparator.comparingDouble(Country::getMinRatePerCounty), limit);
    }

    @Override
    public List<Country> getCountriesSortedByVatDesc(int limit) {
        return getSortedCountries(Comparator.comparingDouble(Country::getMaxRatePerCounty).reversed(), limit);
    }

    private List<Country> getSortedCountries(Comparator<Country> comparator, int limit) {
        final List<Country> countries = restService.getCountries().getRates();
        countries.sort(comparator);
        log.info("Descending list: {}", countries);
        return countries.subList(0, Math.min(countries.size(), limit));
    }
}
