package com.test.vatratio.util;

import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.vatratio.model.Vatratio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class RestService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${vat.countries.url}")
    private String vatCountriesUrl;

    @Value("${vat.countries.update.interval.millis}")
    private int updateVatInterval;

    private ObjectMapper objectMapper;
    private AtomicLong lastTimeVatServiceWasQuerried = new AtomicLong(System.currentTimeMillis());
    private Vatratio cachedVatRatio;

    public RestService() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    public Vatratio getCountries() {
        superSimpleCachingRefreshVatRatio();
        return cachedVatRatio;
    }

    private void superSimpleCachingRefreshVatRatio() {
        if (shouldRefreshVatRatio()) {
            refreshVatRatio();
        }
    }

    private boolean shouldRefreshVatRatio() {
        return (System.currentTimeMillis() - lastTimeVatServiceWasQuerried.longValue() > updateVatInterval)
                || cachedVatRatio == null;
    }

//  Suppose in multi-threaded environment this would be a bottleneck; tried to avoid it by using AtomicLong
    private void refreshVatRatio() {
        ResponseEntity<String> response = restTemplate.getForEntity(vatCountriesUrl, String.class, new Object[]{});
        try {
            cachedVatRatio = objectMapper.readValue(response.getBody(), Vatratio.class);
            final long newRefreshTimeMillis = System.currentTimeMillis();
            log.info("Refresh time set to {}", newRefreshTimeMillis);
            lastTimeVatServiceWasQuerried.getAndSet(newRefreshTimeMillis);
        } catch (Exception ex) {
            log.error("Some problems occured while refreshing vat ratio: {} \n {}. \n Returning outdated result", response, ex);
        }
    }
}
