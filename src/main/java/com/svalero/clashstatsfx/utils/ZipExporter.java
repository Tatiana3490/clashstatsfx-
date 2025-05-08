package com.svalero.clashstatsfx.utils;

import java.io.*;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/*Clase para comprimir archivos en formato ZIP de manera asíncrona.*/
public class ZipExporter {

    /*Comprime un archivo en un nuevo archivo ZIP usando CompletableFuture para ejecutar en segundo plano.*/


    public static CompletableFuture<Void> zipFile(String sourceFilePath, String zipFilePath) {  //se completa cuando el proceso de compresión termina.
        return CompletableFuture.runAsync(() -> {
            try (
                    FileOutputStream fos = new FileOutputStream(zipFilePath);   //Ruta de salida del archivo ZIP.
                    ZipOutputStream zos = new ZipOutputStream(fos);
                    FileInputStream fis = new FileInputStream(sourceFilePath)   //Ruta del archivo a comprimir.
            ) {
                ZipEntry zipEntry = new ZipEntry(new File(sourceFilePath).getName());
                zos.putNextEntry(zipEntry);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) >= 0) {
                    zos.write(buffer, 0, length);
                }

                zos.closeEntry();
            } catch (IOException e) {
                throw new RuntimeException("Error al comprimir archivo: " + e.getMessage(), e);
            }
        });
    }
}
