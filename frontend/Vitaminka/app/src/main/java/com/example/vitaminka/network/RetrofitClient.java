package com.example.vitaminka.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    private static final String BASE_URL = "http://172.28.112.1:8080/api/v1/";

    private static RetrofitClient instance;
    private final PharmacyApi api;

    private RetrofitClient(TokenProvider tokenProvider) {
        // Логирование HTTP-трафика (только для Debug!)
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(tokenProvider))
                .addInterceptor(logging)            // убрать в Release
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(PharmacyApi.class);
    }

    /** Инициализация — вызвать один раз в Application.onCreate(). */
    public static void init(TokenProvider tokenProvider) {
        if (instance == null) {
            instance = new RetrofitClient(tokenProvider);
        }
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            throw new IllegalStateException(
                    "RetrofitClient не инициализирован. Вызовите init() в Application.");
        }
        return instance;
    }

    public PharmacyApi getApi() { return api; }
}