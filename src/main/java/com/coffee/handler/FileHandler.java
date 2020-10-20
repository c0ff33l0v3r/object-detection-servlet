package com.coffee.handler;

import java.io.File;

public abstract class FileHandler {
    public static String getFilepath(String filename) {
        try {
            return new File(FileHandler.class.getResource(filename).getFile()).getAbsolutePath();
        } catch (NullPointerException e) {
            throw new RuntimeException("File not found: " + filename);
        }
    }
}
