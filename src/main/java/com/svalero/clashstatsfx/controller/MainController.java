package com.svalero.clashstatsfx.controller;

import com.svalero.clashstatsfx.model.BattleLogEntry;
import com.svalero.clashstatsfx.model.Card;
import com.svalero.clashstatsfx.model.CardResponse;
import com.svalero.clashstatsfx.service.RetrofitClient;
import com.svalero.clashstatsfx.utils.CsvExporter;
import com.svalero.clashstatsfx.utils.TokenManager;
import com.svalero.clashstatsfx.utils.ZipExporter;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.List;

public class MainController {

    // Elementos de la UI
    @FXML private TextField searchField;
    @FXML private TextField tagField;
    @FXML private TableView<Card> cardsTable;
    @FXML private TableView<BattleLogEntry> battleTable;

    @FXML private TableColumn<Card, String> nameColumn;
    @FXML private TableColumn<Card, String> rarityColumn;
    @FXML private TableColumn<Card, Integer> elixirColumn;
    @FXML private TableColumn<Card, String> typeColumn;
    @FXML private TableColumn<Card, ImageView> imageColumn;

    @FXML private TableColumn<BattleLogEntry, String> battleTypeColumn;
    @FXML private TableColumn<BattleLogEntry, String> battleTimeColumn;
    @FXML private TableColumn<BattleLogEntry, String> playerNameColumn;
    @FXML private TableColumn<BattleLogEntry, String> opponentNameColumn;
    @FXML private TableColumn<BattleLogEntry, String> resultColumn;

    private final ObservableList<Card> allCards = FXCollections.observableArrayList();
    private final FilteredList<Card> filteredCards = new FilteredList<>(allCards, p -> true);
    private final ObservableList<BattleLogEntry> battleLog = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configuración de columnas de la tabla de cartas
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        rarityColumn.setCellValueFactory(data -> new SimpleStringProperty("N/A"));
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty("N/A"));
        elixirColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getMaxLevel()).asObject());
        imageColumn.setCellValueFactory(data -> {
            ImageView imageView = new ImageView(data.getValue().getIconUrls().getMedium());
            imageView.setFitHeight(40);
            imageView.setPreserveRatio(true);
            return new SimpleObjectProperty<>(imageView);
        });
        cardsTable.setItems(filteredCards);

        // Filtro en tiempo real
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredCards.setPredicate(card -> newVal == null || newVal.isEmpty() || card.getName().toLowerCase().contains(newVal.toLowerCase()));
        });

        // Configuración de columnas de la tabla de batallas
        battleTable.setItems(battleLog);
        battleTimeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBattleTime()));
        battleTypeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
        playerNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPlayerName()));
        opponentNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOpponentName()));
        resultColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getResult()));
    }

    @FXML
    private void onLoadCards() {
        String token = TokenManager.getToken();

        RetrofitClient.getApi()
                .getCards(token)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(response -> Platform.runLater(() -> {
                    allCards.setAll(response.getItems());
                    System.out.println("Cartas cargadas: " + allCards.size());
                }), error -> Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "No se pudieron cargar las cartas: " + error.getMessage())));
    }

    @FXML
    private void onLoadBattleLog() {
        String rawTag = tagField.getText().trim();
        if (rawTag.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Campo vacío", "Introduce un tag de jugador.");
            return;
        }

        String encodedTag = rawTag.replace("#", "%23");
        String token = TokenManager.getToken();

        RetrofitClient.getApi()
                .getBattleLog(token, encodedTag)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(response -> Platform.runLater(() -> {
                    if (response.isEmpty()) {
                        showAlert(Alert.AlertType.INFORMATION, "Sin batallas", "El jugador no tiene batallas recientes.");
                    } else {
                        battleLog.setAll(response);
                    }
                }), error -> Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar el historial: " + error.getMessage())));
    }

    @FXML
    private void onExportCsv() {
        if (allCards.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Primero debes cargar las cartas.");
            return;
        }

        try {
            String csvPath = "cartas.csv";
            String zipPath = "cartas.zip";
            CsvExporter.exportCardsToCsv(allCards, csvPath);

            ZipExporter.zipFile(csvPath, zipPath).thenRun(() -> Platform.runLater(() ->
                    showAlert(Alert.AlertType.INFORMATION, "Exportación completa", "Se creó 'cartas.zip' correctamente.")
            )).exceptionally(e -> {
                Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Fallo al comprimir: " + e.getMessage()));
                return null;
            });

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Fallo al exportar CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
