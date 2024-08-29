package com.example.weatheroutfit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private WeatherApiService weatherApiService;
    private ImageView outfitImageView; // ImageView untuk menampilkan gambar outfit
    private EditText locationEditText;
    private Button getWeatherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi ImageView, EditText, dan Button
        outfitImageView = findViewById(R.id.outfitImageView);
        locationEditText = findViewById(R.id.editTextText);
        getWeatherButton = findViewById(R.id.button);

        // Inisialisasi Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Inisialisasi WeatherApiService
        weatherApiService = retrofit.create(WeatherApiService.class);

        // Memberikan aksi pada tombol ketika diklik
        getWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mendapatkan lokasi dari EditText
                String location = locationEditText.getText().toString().trim();
                if (!location.isEmpty()) {
                    // Panggil method untuk melakukan pemanggilan API cuaca
                    callWeatherApi(location, "e22209ad76d882778816776ac966525f", "metric");
                } else {
                    Toast.makeText(MainActivity.this, "Please enter location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method untuk melakukan pemanggilan API cuaca
    private void callWeatherApi(String location, String apiKey, String units) {
        Call<WeatherResponse> call = weatherApiService.getWeather(location, apiKey, units);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    // Data cuaca berhasil diterima
                    WeatherResponse weatherResponse = response.body();
                    float temperature = weatherResponse.getMain().getTemp();
                    // Lakukan sesuatu dengan data cuaca, misalnya tampilkan suhu
                    Toast.makeText(MainActivity.this, "Temperature in " + location + ": " + temperature + "°C", Toast.LENGTH_SHORT).show();

                    // Menentukan rekomendasi outfit berdasarkan suhu
                    setOutfitRecommendation(temperature);
                } else {
                    // Gagal mendapatkan data cuaca
                    Toast.makeText(MainActivity.this, "Failed to get weather data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                // Terjadi kesalahan saat melakukan pemanggilan API
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method untuk menentukan rekomendasi outfit berdasarkan suhu
    private void setOutfitRecommendation(float temperature) {
        if (temperature < 10) {
            // Suhu < 10°C, rekomendasikan jaket tebal
            outfitImageView.setImageResource(R.drawable.tebal);
        } else if (temperature >= 10 && temperature <= 20) {
            // Suhu 10-20°C, rekomendasikan jaket ringan
            outfitImageView.setImageResource(R.drawable.sedang);
        } else {
            // Suhu > 20°C, rekomendasikan kaos
            outfitImageView.setImageResource(R.drawable.tipis);
        }
    }
}
