package com.alp.service;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.client.RestTemplate;

import com.alp.dto.WeatherForecastResponse;
import com.alp.dto.WeatherResponse;
import com.alp.dto.WeatherForecastResponse.Forecast;


@Service
public class WeatherService
{

  @Value("${weather_city}")
  private String apiKey;
  
  @Value("${weather.api.url_weather}")
  private String apiUrl_w;
  
  @Value("${weather.api.url_forecast}")
  private String apiUrl_f;


  @Value("${weather.api.units}")
  private String units;

  // Get current weather for a specific city
  public WeatherResponse getWeather(String city) {
     
          String url = apiUrl_w + "?q=" + city + "&appid=" + apiKey + "&units=" + units;
          RestTemplate restTemplate = new RestTemplate();
         
          String response = restTemplate.getForObject(url, String.class);
   
          // Parse the response
          JSONObject json = new JSONObject(response);
   
          WeatherResponse weatherResponse = new WeatherResponse();
          weatherResponse.setCityName(json.getString("name"));
          weatherResponse.setTemperature(json.getJSONObject("main").getDouble("temp"));
          weatherResponse.setHumidity(json.getJSONObject("main").getInt("humidity"));
          weatherResponse.setFeelsLike(json.getJSONObject("main").getDouble("feels_like"));
          weatherResponse.setPressure(json.getJSONObject("main").getInt("pressure"));
          weatherResponse.setWeatherDescription(json.getJSONArray("weather").getJSONObject(0).getString("description"));
          weatherResponse.setWindSpeed(json.getJSONObject("wind").getDouble("speed"));
   
          return weatherResponse;
  }
  private String formatTimestamp(long timestamp) {
      // Convert the Unix timestamp to a Date object
      Date date = new Date(timestamp * 1000); // Multiply by 1000 to convert to milliseconds
      
      // Define the format for "Mon dd/MM/yyyy"
      SimpleDateFormat sdf = new SimpleDateFormat("EEE dd/MM/yyyy");
      
      // Return the formatted date as a string
      return sdf.format(date);
  }

  public WeatherForecastResponse getWeatherForecast(String city) {
      // Prepare the API URL
	  String url = apiUrl_f + "?q=" + city + "&appid=" + apiKey + "&units=" + units;
	  RestTemplate restTemplate = new RestTemplate();

      // Call the API and get the response as a string
      String response = restTemplate.getForObject(url, String.class);

      // Parse the JSON response
      JSONObject json = new JSONObject(response);
      JSONArray list = json.getJSONArray("list");

      // Prepare the WeatherForecastResponse object
      WeatherForecastResponse forecastResponse = new WeatherForecastResponse();
      forecastResponse.setCityName(json.getJSONObject("city").getString("name"));
      
      // List to store aggregated day-wise forecast data
      List<WeatherForecastResponse.Forecast> aggregatedForecasts = new ArrayList<>();

      
   // Initialize variables for each day
      double dayMaxTemp = Double.MIN_VALUE;
      double dayMinTemp = Double.MAX_VALUE;
      String dayWeatherDescription = "";
      int count = 0;
      String currentDay = "";

      for (int i = 0; i < list.length(); i++) {
          JSONObject forecastJson = list.getJSONObject(i);
          long timestamp = forecastJson.getLong("dt");
          String formattedDate = formatTimestamp(timestamp);
          double tempMax = forecastJson.getJSONObject("main").getDouble("temp_max");
          double tempMin = forecastJson.getJSONObject("main").getDouble("temp_min");
          String weatherDescription = forecastJson.getJSONArray("weather").getJSONObject(0).getString("description");

          // Check if we have moved to the next day
          if (!formattedDate.equals(currentDay)) {
              if (count > 0) {
                  // Save the aggregated data for the previous day
                  WeatherForecastResponse.Forecast dayForecast = new WeatherForecastResponse.Forecast();
                  dayForecast.setFormattedDate(currentDay);
                  dayForecast.setTempMax(dayMaxTemp);
                  dayForecast.setTempMin(dayMinTemp);
                  dayForecast.setWeatherDescription(dayWeatherDescription);
                  aggregatedForecasts.add(dayForecast);
              }

              // Reset variables for the new day
              currentDay = formattedDate;
              dayMaxTemp = tempMax;
              dayMinTemp = tempMin;
              dayWeatherDescription = weatherDescription;
              count = 1;
          } else {
              // Aggregate the data for the same day
              dayMaxTemp = Math.max(dayMaxTemp, tempMax);
              dayMinTemp = Math.min(dayMinTemp, tempMin);
            count++;
              // You can choose to average weather descriptions, or just keep the first one
          }
      }

      // Add the final day's forecast after the loop
      WeatherForecastResponse.Forecast dayForecast = new WeatherForecastResponse.Forecast();
      dayForecast.setFormattedDate(currentDay);
      dayForecast.setTempMax(dayMaxTemp);
      dayForecast.setTempMin(dayMinTemp);
       dayForecast.setWeatherDescription(dayWeatherDescription);
      aggregatedForecasts.add(dayForecast);

      // Set the aggregated daily forecasts in the response
      forecastResponse.setForecastList(aggregatedForecasts);

      return forecastResponse;
     
  }
}