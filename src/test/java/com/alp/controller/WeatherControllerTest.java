package com.alp.controller;

import com.alp.dto.WeatherResponse;
import com.alp.dto.WeatherForecastResponse;
import com.alp.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
 
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
 
import java.util.ArrayList;
 
@ExtendWith(MockitoExtension.class)
public class WeatherControllerTest {
 
    private MockMvc mockMvc;
 
    @Mock
    private WeatherService weatherService;
 
    @InjectMocks
    private WeatherController weatherController;
 
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController).build();
    }
 
    @Test
    void testGetWeather_validCity() throws Exception {
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setCityName("TestCity");
        weatherResponse.setTemperature(22.5);
        weatherResponse.setHumidity(60);
        weatherResponse.setFeelsLike(21.5);
        weatherResponse.setPressure(1013);
        weatherResponse.setWeatherDescription("Clear sky");
        weatherResponse.setWindSpeed(5.5);
 
        when(weatherService.getWeather("TestCity")).thenReturn(weatherResponse);
 
        mockMvc.perform(get("/weather?city=TestCity"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityName").value("TestCity"))
                .andExpect(jsonPath("$.temperature").value(22.5))
                .andExpect(jsonPath("$.humidity").value(60));
    }
 
    @Test
    void testGetWeather_invalidCity_empty() throws Exception {
        mockMvc.perform(get("/weather?city="))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("City name cannot be empty."));
    }
 
    @Test
    void testGetWeather_invalidCity_tooShort() throws Exception {
        mockMvc.perform(get("/weather?city=AB"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("City name must be at least 3 characters long."));
    }
 
    @Test
    void testGetWeatherForecast_validCity() throws Exception {
        WeatherForecastResponse forecastResponse = new WeatherForecastResponse();
        forecastResponse.setCityName("TestCity");
        forecastResponse.setForecastList(new ArrayList<>());
 
        when(weatherService.getWeatherForecast("TestCity")).thenReturn(forecastResponse);
 
        mockMvc.perform(get("/weather/forecast?city=TestCity"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityName").value("TestCity"));
    }
 
    @Test
    void testGetWeatherForecast_invalidCity_empty() throws Exception {
        mockMvc.perform(get("/weather/forecast?city="))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("City name cannot be empty."));
    }
 
    @Test
    void testGetWeatherForecast_invalidCity_tooShort() throws Exception {
        mockMvc.perform(get("/weather/forecast?city=AB"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("City name must be at least 3 characters long."));
    }
}
