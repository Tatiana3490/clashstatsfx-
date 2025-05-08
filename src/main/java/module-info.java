module com.svalero.clashstatsfx  {
    requires com.google.gson;
    requires io.reactivex.rxjava3;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires retrofit2;
    requires retrofit2.adapter.rxjava3;
    requires retrofit2.converter.gson;
    requires java.sql;
    requires okhttp3;

    opens com.svalero.clashstatsfx.model to javafx.fxml, com.google.gson;
    opens com.svalero.clashstatsfx.controller to javafx.fxml;

    exports com.svalero.clashstatsfx;
    exports com.svalero.clashstatsfx.controller;
}