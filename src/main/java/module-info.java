module com.svalero.clashstatsfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires retrofit2;
    requires retrofit2.converter.gson;
    requires retrofit2.adapter.rxjava3;
    requires io.reactivex.rxjava3;
    requires com.google.gson;
    requires java.sql;

    opens com.svalero.clashstatsfx to javafx.fxml;
    opens com.svalero.clashstatsfx.controller to javafx.fxml;
    opens com.svalero.clashstatsfx.model to com.google.gson;

    exports com.svalero.clashstatsfx;
    exports com.svalero.clashstatsfx.controller;
}
