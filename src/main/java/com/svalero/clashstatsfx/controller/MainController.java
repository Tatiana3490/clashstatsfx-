package com.svalero.clashstatsfx.controller;

import com.svalero.clashstatsfx.model.Card;
import com.svalero.clashstatsfx.model.CardResponse;
import com.svalero.clashstatsfx.service.RetrofitClient;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import com.svalero.clashstatsfx.utils.CsvExporter;
import com.svalero.clashstatsfx.utils.ZipExporter;


import java.io.IOException;


public class MainController {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<Card> cardsTable;
    @FXML
    private TableColumn<Card, String> nameColumn;
    @FXML
    private TableColumn<Card, String> rarityColumn;
    @FXML
    private TableColumn<Card, Integer> elixirColumn;
    @FXML
    private TableColumn<Card, String> typeColumn;
    @FXML
    private TableColumn<Card, javafx.scene.image.ImageView> imageColumn;


    private final ObservableList<Card> allCards = FXCollections.observableArrayList();
    private final FilteredList<Card> filteredCards = new FilteredList<>(allCards, p -> true);


    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        rarityColumn.setCellValueFactory(data -> new SimpleStringProperty("N/A")); // No hay rareza en la API básica
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty("N/A"));   // Tampoco hay "type" directo
        elixirColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getMaxLevel()).asObject());
        cardsTable.setItems(filteredCards);
        imageColumn.setCellValueFactory(data -> {
            String imageUrl = data.getValue().getIconUrls().getMedium();
            javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(imageUrl);
            imageView.setFitHeight(40);
            imageView.setPreserveRatio(true);
            return new javafx.beans.property.SimpleObjectProperty<>(imageView);
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredCards.setPredicate(card -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return card.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });


    }

    @FXML
    private void onLoadCards() {
        RetrofitClient.getApi()
                .getCards()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(response -> {
                    Platform.runLater(() -> {
                        allCards.setAll(response.getItems());
                        System.out.println("Cartas cargadas: " + filteredCards.size());
                    });
                }, error -> {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("No se pudieron cargar las cartas");
                        alert.setContentText(error.getMessage());
                        alert.showAndWait();
                    });
                    error.printStackTrace();
                });
    }

    @FXML
    private void onExportCsv() {
        if (allCards.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Sin datos para exportar");
            alert.setContentText("Primero debes cargar las cartas.");
            alert.showAndWait();
            return;
        }

        try {
            String csvPath = "cartas.csv";
            String zipPath = "cartas.zip";

            CsvExporter.exportCardsToCsv(allCards, csvPath);

            ZipExporter.zipFile(csvPath, zipPath).thenRun(() -> {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Exportación completa");
                    alert.setHeaderText(null);
                    alert.setContentText("Se han creado los archivos 'cartas.csv' y 'cartas.zip'.");
                    alert.showAndWait();
                });
            }).exceptionally(e -> {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Fallo al comprimir");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                });
                return null;
            });

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Fallo al exportar");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }


}
