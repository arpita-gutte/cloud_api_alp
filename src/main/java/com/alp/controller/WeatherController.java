package com.alp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alp.dto.WeatherForecastResponse;
import com.alp.dto.WeatherResponse;
import com.alp.service.WeatherService;

@RestController
public class WeatherController 
{

    @Autowired
   private  WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }
    // Get current weather for a specific city
//    @GetMapping("/weather")
//   public Demo2WeatherResponse getWeather(@RequestParam String city) 
//    {
//       return weatherService.getWeather(city);
//    } 
//    @GetMapping("/weather/forecast")
//    public WeatherForecastResponse getWeatherForecast(@RequestParam String city) {
//        return weatherService.getWeatherForecast(city);
//    }
    
    //Get current weather for a specific city
    @GetMapping("/weather")
   public ResponseEntity<?> getWeather(@RequestParam String city) 
    {
    	try {
            // Validate city parameter
            if (city == null || city.trim().isEmpty()) {
                throw new IllegalArgumentException("City name cannot be empty.");
            }
            if (city.length() < 3) {
                throw new IllegalArgumentException("City name must be at least 3 characters long.");
            }
 
            // If valid, call the service method to get weather

         // If valid, call the service method to get weather
            WeatherResponse weatherResponse = weatherService.getWeather(city);
            return new ResponseEntity<>(weatherResponse, HttpStatus.OK);
 
    	

    	//return weatherService.getWeather(city);
    	}
    	catch (IllegalArgumentException e) {
            // Return custom error response with 400 Bad Request status
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    } 
    @GetMapping("/weather/forecast")
    public ResponseEntity<?> getWeatherForecast(@RequestParam String city) {
    	 try {
             // Validate city parameter
             if (city == null || city.trim().isEmpty()) {
                 throw new IllegalArgumentException("City name cannot be empty.");
             }
             if (city.length() < 3) {
                 throw new IllegalArgumentException("City name must be at least 3 characters long.");
             }

             // If valid, call the service method to get the forecast
             WeatherForecastResponse weatherForecastResponse = weatherService.getWeatherForecast(city);
             return new ResponseEntity<>(weatherForecastResponse, HttpStatus.OK);
 
    	
    	//return weatherService.getWeatherForecast(city);
    	 }catch (IllegalArgumentException e) {
             // Return custom error response with 400 Bad Request status
             Map<String, String> errorResponse = new HashMap<>();
             errorResponse.put("error", e.getMessage());
             return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
         }
    }


 }