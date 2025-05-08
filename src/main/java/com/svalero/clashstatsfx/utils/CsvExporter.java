package com.svalero.clashstatsfx.utils;

import com.svalero.clashstatsfx.model.Card;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/*Para exportar una lista de cartas a un archivo CSV.*/
public class CsvExporter {

    /*Exporta una lista de objetos Card a un archivo CSV en el formato:Name,MaxLevel,ImageURL*/
    public static void exportCardsToCsv(List<Card> cards, String filePath) throws IOException {
        FileWriter writer = new FileWriter(filePath);   // Ruta del archivo CSV de salida

        // Escribir cabecera
        writer.write("Name,MaxLevel,ImageURL\n");

        // Escribir cada carta como una fila en el CSV
        for (Card card : cards) {
            writer.write(String.format("\"%s\",%d,\"%s\"\n",
                    card.getName(),
                    card.getMaxLevel(),
                    card.getIconUrls().getMedium()));
        }

        writer.close();
    }
}
