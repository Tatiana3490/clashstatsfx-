package com.svalero.clashstatsfx.utils;

import com.svalero.clashstatsfx.model.Card;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvExporter {

    public static void exportCardsToCsv(List<Card> cards, String filePath) throws IOException {
        FileWriter writer = new FileWriter(filePath);

        // Cabecera
        writer.write("Name,MaxLevel,ImageURL\n");

        for (Card card : cards) {
            writer.write(String.format("\"%s\",%d,\"%s\"\n",
                    card.getName(),
                    card.getMaxLevel(),
                    card.getIconUrls().getMedium()));
        }

        writer.close();
    }
}