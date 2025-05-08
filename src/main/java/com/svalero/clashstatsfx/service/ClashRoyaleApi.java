package com.svalero.clashstatsfx.service;

import com.svalero.clashstatsfx.model.BattleLogEntry;
import com.svalero.clashstatsfx.model.CardResponse;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

import java.util.List;


 /*Interfaz para definir los endpoints de la API de Clash Royale.
 Retrofit la implementa automáticamente. Traduce los metodos Java en peticiones HTTP y parsea el resultado en objetos.*/

public interface ClashRoyaleApi {


     /* Para obtener el historial de batallas de un jugador.*/
    @GET("players/{tag}/battlelog")
    Observable<List<BattleLogEntry>> getBattleLog(
            @Header("Authorization") String auth,                //Token de autorización Bearer
            @Path(value = "tag", encoded = false) String tag     //Este es el tag del jugador que incluye "#" y retrofit lo codifica
    );

    /*ParaObtiene la lista de cartas disponibles.*/
    @GET("cards")
    Observable<CardResponse> getCards(
            @Header("Authorization") String auth
    );
}
