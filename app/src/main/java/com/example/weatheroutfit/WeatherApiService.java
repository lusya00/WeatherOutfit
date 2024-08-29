package com.example.weatheroutfit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeather(@Query("q") String location, @Query("appid") String apiKey, @Query("units") String units);
}
