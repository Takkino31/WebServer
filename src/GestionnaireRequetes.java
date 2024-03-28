// Classe pour gérer les requêtes HTTP

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class GestionnaireRequetes extends Thread {
    
    private Socket clientSocket;

    public GestionnaireRequetes(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String requestLine = in.readLine();
            System.out.println("Requête reçue: " + requestLine);

            String[] parts = requestLine.split(" ");
            String method = parts[0];
            String requestedResource = parts[1];

            if (method.equals("GET")) {
                handleGetRequest(requestedResource, out);
            } else {
                String errorMessage = "Méthode HTTP non prise en charge: " + method;
                out.println(GenerateurReponse.generateErrorResponse(errorMessage));
            }

            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   
    private void handleGetRequest(String requestedResource, PrintWriter out) {
        String baseDirectory = "/home/yaya/Téléchargements";
        File requestedFile;
    
        // Traiter le cas où la requête est pour le répertoire racine
        if (requestedResource.equals("/") || requestedResource.isEmpty()) {
            requestedFile = new File(baseDirectory);
        } else {
            requestedFile = new File(baseDirectory, requestedResource);
        }
    
        try {
            requestedFile = requestedFile.getCanonicalFile();
        } catch (IOException e) {
            out.println(GenerateurReponse.generateErrorResponse("Erreur lors de la résolution du chemin du fichier."));
            return;
        }
    
        // Vérifier que le chemin demandé se trouve sous le répertoire de base
        if (!requestedFile.getAbsolutePath().startsWith(baseDirectory)) {
            out.println(GenerateurReponse.generateErrorResponse("Accès interdit."));
            return;
        }
    
        if (requestedFile.exists()) {
            if (requestedFile.isDirectory()) {
                out.println(GenerateurReponse.generateDirectoryListing(requestedFile, baseDirectory));
            } else {
                String fileExtension = GestionnaireFichiers.getFileExtension(requestedFile);
                if (fileExtension.equals("txt") || fileExtension.equals("html")) {
                    out.println(GenerateurReponse.generateSuccessResponse(GestionnaireFichiers.readFile(requestedFile)));
                } else if (fileExtension.equals("py")) {
                    InterpreteurPython interpreter = new InterpreteurPython();
                    out.println(GenerateurReponse.generateSuccessResponse(interpreter.interpretFile(requestedFile)));
                } else {
                    out.println(GenerateurReponse.generateErrorResponse("Type de fichier non pris en charge"));
                }
            }
        } else {
            out.println(GenerateurReponse.generateErrorResponse("404 Not Found"));
        }
    }
}