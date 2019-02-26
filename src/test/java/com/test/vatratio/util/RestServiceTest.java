package com.test.vatratio.util;

import static org.junit.Assert.assertTrue;

import com.test.vatratio.model.Vatratio;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class RestServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RestService restService;

    private String mockResponseBody = "{\"details\":\"http://github.com/adamcooke/vat-rates\",\"version\":null,\"rates\":"
            + "[{\"name\":\"Spain\",\"code\":\"ES\",\"country_code\":\"ES\",\"periods\":[{\"effective_from\":\"0000-01-01\",\"rates\":{\"super_reduced\":4.0,\"reduced\":10.0,\"standard\":21.0}}]},"
            + "{\"name\":\"Bulgaria\",\"code\":\"BG\",\"country_code\":\"BG\",\"periods\":[{\"effective_from\":\"0000-01-01\",\"rates\":{\"reduced\":9.0,\"standard\":20.0}}]},"
            + "{\"name\":\"Hungary\",\"code\":\"HU\",\"country_code\":\"HU\",\"periods\":[{\"effective_from\":\"0000-01-01\",\"rates\":{\"reduced1\":5.0,\"reduced2\":18.0,\"standard\":27.0}}]}]}";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRestServiceResponseMapping() {
        Mockito
                .when(restTemplate.getForEntity(Mockito.any(), Mockito.any(), new Object[]{}))
                .thenReturn(new ResponseEntity<>(mockResponseBody, HttpStatus.OK));

        Vatratio vatRatio = restService.getCountries();
        assertTrue(vatRatio.getDetails().equals("http://github.com/adamcooke/vat-rates"));
        assertTrue(vatRatio.getRates().size() == 3);
    }
}