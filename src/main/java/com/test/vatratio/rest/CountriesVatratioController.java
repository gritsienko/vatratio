package com.test.vatratio.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Size;

import com.test.vatratio.model.Country;
import com.test.vatratio.service.VatratioCountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vat-countries")
@Slf4j
@Validated
public class CountriesVatratioController {

    @Autowired
    private VatratioCountryService vatRatioCountryService;

    @GetMapping(value = "{numberOfCountries}/min")
    public ResponseEntity<List<CountryResponse>> getMinVatRatioCountries(@Size(min = 1, max = 6, message = "Number of countries in URL must be between 1 and 6")
    @PathVariable("numberOfCountries") String numberOfCountries) {
        return getCountries(numberOfCountries, (numOfCountries) -> vatRatioCountryService.getCountriesSortedByVatAsc(numOfCountries));
    }

    @GetMapping(value = "{numberOfCountries}/max")
    public ResponseEntity<List<CountryResponse>> getMaxVatRatioCountries(@Size(min = 1, max = 6, message = "Number of countries in URL must be between 1 and 6")
    @PathVariable("numberOfCountries") final String numberOfCountries) {
        return getCountries(numberOfCountries, (numOfCountries) -> vatRatioCountryService.getCountriesSortedByVatDesc(numOfCountries));
    }

    @GetMapping(value = "{numberOfCountries}/both")
    public ResponseEntity<List<CountryResponse>> getMaxAndMinVatRatioCountries(
            @Size(min = 1, max = 6, message = "Number of countries in URL must be between 1 and 6")
            @PathVariable("numberOfCountries") final String numberOfCountries) {
        List<CountryResponse> combinedCountries = getMinVatRatioCountries(numberOfCountries).getBody();
        combinedCountries.addAll(getMaxVatRatioCountries(numberOfCountries).getBody());

        return new ResponseEntity<>(combinedCountries, HttpStatus.OK);
    }

    private ResponseEntity<List<CountryResponse>> getCountries(String numberOfCountries, Function<Integer, List<Country>> queryCountriesFunction) {
        int numbOfCountries;
        try {
            numbOfCountries = Integer.parseInt(numberOfCountries);
        } catch (NumberFormatException ex) {
            log.error("Invalid request, numberOfCountries not a number: {}", numberOfCountries);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.I_AM_A_TEAPOT);
        }
        final List<Country> countries = queryCountriesFunction.apply(numbOfCountries);
        return new ResponseEntity<>(mapToCountryResponse(countries), HttpStatus.OK);
    }

    private List<CountryResponse> mapToCountryResponse(final List<Country> countryList) {
        return countryList.stream()
                .map(this::createResponseFromCountry)
                .collect(Collectors.toList());
    }

    private CountryResponse createResponseFromCountry(final Country country) {
        return CountryResponse.builder()
                .countryName(country.getName())
                .maxVatRateValue(country.getMaxRatePerCounty())
                .minVatRateValue(country.getMinRatePerCounty()).build();
    }

    @GetMapping(value = "/")
    public ResponseEntity<String> getMaxVatRatioCountries() {
        return new ResponseEntity<>("Wellcome to VAT Countries Data page", HttpStatus.OK);
    }

    @ExceptionHandler(value = { ConstraintViolationException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleResourceNotFoundException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage() + "\n");
        }
        return strBuilder.toString();
    }
}
