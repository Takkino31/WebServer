// Classe pour générer les réponses HTTP

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class GenerateurReponse {
    public static String generateSuccessResponse(String content) {
        return "HTTP/1.1 200 OK\r\n\r\n" + content;
    }

    public static String generateErrorResponse(String errorMessage) {
        return "HTTP/1.1 400 Bad Request\r\n\r\n" + errorMessage;
    }

    public static String generateDirectoryListing(File directory, String baseDirectory) {
        List<String> fileList = new ArrayList<>();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                String relativePath = file.getAbsolutePath().substring(baseDirectory.length() + 1);
                if (file.isDirectory()) {
                    fileName = "<a href=\"/" + relativePath + "/\">" + fileName + "/</a>";
                } else {
                    fileName = "<a href=\"/" + relativePath + "\">" + fileName + "</a>";
                }
                fileList.add(fileName);
            }
        }
        return generateSuccessResponse(String.join("<br>", fileList));
    }
}