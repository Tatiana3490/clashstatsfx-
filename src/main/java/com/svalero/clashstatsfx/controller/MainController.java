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

public class MainController {

    // Elementos de la interfaz
    @FXML private TextField searchField;
    @FXML private TextField tagField;
    @FXML private TableView<Card> cardsTable;
    @FXML private TableColumn<Card, String> nameColumn;
    @FXML private TableColumn<Card, String> rarityColumn;
    @FXML private TableColumn<Card, Integer> elixirColumn;
    @FXML private TableColumn<Card, String> typeColumn;
    @FXML private TableColumn<Card, ImageView> imageColumn;

    @FXML private TableView<BattleLogEntry> battleTable;
    @FXML private TableColumn<BattleLogEntry, String> battleTypeColumn;
    @FXML private TableColumn<BattleLogEntry, String> battleTimeColumn;
    @FXML private TableColumn<BattleLogEntry, String> playerNameColumn;
    @FXML private TableColumn<BattleLogEntry, String> opponentNameColumn;
    @FXML private TableColumn<BattleLogEntry, String> resultColumn;

    // Datos en memoria
    private final ObservableList<Card> allCards = FXCollections.observableArrayList();
    private final FilteredList<Card> filteredCards = new FilteredList<>(allCards, p -> true);
    private final ObservableList<BattleLogEntry> battleLog = FXCollections.observableArrayList();

    // Inicialización al cargar la interfaz
    @FXML
    public void initialize() {
        // Configuración de columnas de cartas
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        rarityColumn.setCellValueFactory(data -> new SimpleStringProperty("N/A"));
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty("N/A"));
        elixirColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getMaxLevel()).asObject());
        imageColumn.setCellValueFactory(data -> {
            String imageUrl = data.getValue().getIconUrls().getMedium();
            ImageView imageView = new ImageView(imageUrl);
            imageView.setFitHeight(40);
            imageView.setPreserveRatio(true);
            return new SimpleObjectProperty<>(imageView);
        });

        cardsTable.setItems(filteredCards);

        // Filtrado por nombre de carta
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredCards.setPredicate(card -> {
                if (newVal == null || newVal.isEmpty()) return true;
                return card.getName().toLowerCase().contains(newVal.toLowerCase());
            });
        });

        // Configuración de columnas de batallas
        battleTable.setItems(battleLog);
        battleTimeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBattleTime()));
        battleTypeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
        playerNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPlayerName()));
        opponentNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOpponentName()));
        resultColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getResult()));

        // Valor por defecto para pruebas
        tagField.setText("#L8999VJC");
    }

    // Cargar cartas desde la API
    @FXML
    private void onLoadCards() {
        String token = TokenManager.getToken();

        RetrofitClient.getApi()
                .getCards(token)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(response -> Platform.runLater(() -> {
                    allCards.setAll(response.getItems());
                    System.out.println("Cartas cargadas: " + filteredCards.size());
                }), error -> Platform.runLater(() -> {
                    showAlert(Alert.AlertType.ERROR, "Error", "No se pudieron cargar las cartas: " + error.getMessage());
                    error.printStackTrace();
                }));
    }

    // Exportar cartas a CSV y ZIP
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
                    showAlert(Alert.AlertType.INFORMATION, "Exportación completa",
                            "Se han creado los archivos 'cartas.csv' y 'cartas.zip'.")
            )).exceptionally(e -> {
                Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Fallo al comprimir: " + e.getMessage()));
                return null;
            });

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Fallo al exportar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Cargar historial de batallas de un jugador
    @FXML
    private void onLoadBattleLog() {
        String rawTag = tagField.getText().trim(); //para obtener el texto que introduzca el usuario

        if (rawTag.isEmpty()) {     //para validar que no esté vacío
            showAlert(Alert.AlertType.WARNING, "Tag vacío", "Introduce un tag de jugador.");
            return;
        }

        String token = TokenManager.getToken();     //Token de acceso a la API que está centralizado en TokenManager

        // Debug para consola: muestra lo que se está enviando a la API
        System.out.println("=== DEBUG API REQUEST ===");
        System.out.println("Token: " + token.substring(0, 30) + "...");
        System.out.println("Tag sin codificar: " + rawTag);
        System.out.println("URL esperada: https://api.clashroyale.com/v1/players/" + rawTag + "/battlelog");

        // Llamamos al endpoint Retrofit usando el tag tal cual (Retrofit codifica solo)
        RetrofitClient.getApi()
                .getBattleLog(token, rawTag)
                .subscribeOn(Schedulers.io())           // Ejecuta en segundo plano
                .observeOn(Schedulers.single())         // Observa desde otro hilo para no bloquear la app
                .subscribe(
                        response -> Platform.runLater(() -> {   // Aviso si el jugador no tiene batallas recientes
                            if (response.isEmpty()) {
                                showAlert(Alert.AlertType.INFORMATION, "Sin batallas", "El jugador no tiene batallas recientes.");
                            }
                            battleLog.setAll(response); // Cargamos la lista de batallas en la tabla
                        }),
                        error -> Platform.runLater(() -> {  // Aviso i hay error en la llamada
                            showAlert(Alert.AlertType.ERROR, "Error al cargar", "Excepción: " + error.getMessage());
                            error.printStackTrace();
                        })
                );
    }

    // Método auxiliar para mostrar alertas
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
