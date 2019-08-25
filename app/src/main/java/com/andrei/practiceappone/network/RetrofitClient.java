package com.andrei.practiceappone.network;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://my-json-server.typicode.com/";

    private static Retrofit clientInstance;

    private RetrofitClient() {
        // Private empty constructor
    }

    /**
     * Instantiates and returns a {@link Retrofit} client instance.
     */
    public static Retrofit getInstance() {
        if (clientInstance == null) {
            synchronized (Retrofit.class) {
                if (clientInstance == null) {
                    clientInstance = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(MoshiConverterFactory.create())
                            .build();
                }
            }
        }
        return clientInstance;
    }
}
