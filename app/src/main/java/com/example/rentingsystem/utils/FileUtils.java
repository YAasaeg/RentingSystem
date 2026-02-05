package com.example.rentingsystem.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    
    // Simulate getting images from D:\fangyuan
    // In Android Emulator, accessing host PC drive directly is tricky.
    // We will simulate this by scanning a specific directory in the emulator storage
    // or just listing files if we could map it.
    // For this specific requirement "D:\fangyuan", we will assume the files are somehow accessible
    // or we just simulate the file selection dialog showing these paths.
    
    // Since we are in an Android environment, we can't really read "D:\" from the Windows host directly unless mapped.
    // I will implement a logic that allows typing the path or selecting from a simulated list.
    
    public static List<String> getImagesFromDirectory(String directoryPath) {
        List<String> images = new ArrayList<>();
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (isImageFile(file.getName())) {
                        images.add(file.getAbsolutePath());
                    }
                }
            }
        }
        return images;
    }

    private static boolean isImageFile(String name) {
        String lower = name.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".png") || lower.endsWith(".jpeg");
    }
}
