package com.svalero.clashstatsfx.utils;

import java.io.*;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipExporter {

    public static CompletableFuture<Void> zipFile(String sourceFilePath, String zipFilePath) {
        return CompletableFuture.runAsync(() -> {
            try (
                    FileOutputStream fos = new FileOutputStream(zipFilePath);
                    ZipOutputStream zos = new ZipOutputStream(fos);
                    FileInputStream fis = new FileInputStream(sourceFilePath)
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