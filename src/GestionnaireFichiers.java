// Classe pour gérer les fichiers

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class GestionnaireFichiers {
    public static String readFile(File file) {
        StringBuilder fileContent = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent.toString();
    }

    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // Pas d'extension
        }
        return name.substring(lastIndexOf + 1);
    }
}