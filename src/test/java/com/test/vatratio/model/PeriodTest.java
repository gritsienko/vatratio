package com.test.vatratio.model;

import static org.junit.Assert.assertTrue;

import com.test.vatratio.AbstractTest;
import org.junit.Test;

public class PeriodTest extends AbstractTest {

    @Test
    public void testMinMaxRate() {
        Period period = new Period();
        double expectedMinRate = 1D;
        double expectedMaxRate = 100D;
        period.setRates(getRates(expectedMinRate, expectedMaxRate));

        assertTrue("Expected min rate is " + expectedMinRate + "; actual value is " + period.getMinRate(), expectedMinRate == period.getMinRate());
        assertTrue("Expected max rate is " + expectedMaxRate + "; actual value is " + period.getMaxRate(), expectedMaxRate == period.getMaxRate());
    }
}