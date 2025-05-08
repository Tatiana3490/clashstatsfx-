package com.svalero.clashstatsfx.service;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/*Clase encargada de configurar Retrofit para consumir la API de Clash Royale.*/
/*Implementa el patrón Singleton (solo se crea una vez, no hace multiples conexiones ni configuraciones y es mas
eficiente y seguro) para mantener una única instancia de la API*/
public class RetrofitClient {

    // URL base de la API REST de Clash Royale
    private static final String BASE_URL = "https://api.clashroyale.com/v1/";

    // Instancia única del servicio generado por Retrofit
    private static ClashRoyaleApi api;

    /*Devuelve una instancia del servicio ClashRoyaleApi.Si no se ha creado aún, la construye con la configuración de Retrofit.*/
    public static ClashRoyaleApi getApi() {
        if (api == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Conversor JSON -> objetos Java
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // Integración con RxJava3
                    .build();

            // Retrofit crea la implementación de la interfaz ClashRoyaleApi
            api = retrofit.create(ClashRoyaleApi.class);
        }
        return api;
    }
}
