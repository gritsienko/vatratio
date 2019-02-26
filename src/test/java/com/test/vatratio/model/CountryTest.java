package com.test.vatratio.model;

import static org.junit.Assert.*;

import com.test.vatratio.AbstractTest;
import org.junit.Test;

public class CountryTest extends AbstractTest {

    @Test
    public void testMinMaxRatePerCountry() {
        Country country = new Country();
        country.setPeriods(getPeriods());
        double expectedMinRate = 1D;
        double expectedMaxRate = 15D;
        assertTrue("Expected min rate is " + expectedMinRate + ", actual is " + country.getMinRatePerCounty(),expectedMinRate == country.getMinRatePerCounty());
        assertTrue("Expected max rate is " + expectedMaxRate + ", actual is " + country.getMaxRatePerCounty(),expectedMaxRate == country.getMaxRatePerCounty());
    }
}