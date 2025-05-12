package com.svalero.clashstatsfx.service;

import com.svalero.clashstatsfx.model.BattleLogEntry;
import com.svalero.clashstatsfx.model.CardResponse;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

import java.util.List;

public interface ClashRoyaleApi {

    @GET("players/{tag}/battlelog")
    Observable<List<BattleLogEntry>> getBattleLog(
            @Header("Authorization") String auth,
            @Path(value = "tag", encoded = false) String tag
    );

    @GET("cards")
    Observable<CardResponse> getCards(
            @Header("Authorization") String auth
    );
}