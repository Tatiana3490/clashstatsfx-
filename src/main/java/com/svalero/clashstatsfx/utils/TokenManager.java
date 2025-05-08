package com.svalero.clashstatsfx.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TokenManager {

    private static final String CONFIG_FILE = "config.properties";
    private static final String TOKEN_KEY = "clash.token";
    private static String token;

    static {
        try (InputStream input = TokenManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            Properties prop = new Properties();
            if (input != null) {
                prop.load(input);
                token = prop.getProperty(TOKEN_KEY);
            } else {
                throw new RuntimeException("Archivo de configuración no encontrado: " + CONFIG_FILE);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error cargando configuración: " + e.getMessage(), e);
        }
    }

    public static String getToken() {
        return token;
    }
}
