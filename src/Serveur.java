// Classe principale

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Serveur {
    public static void main(String[] args) {
        try {
            @SuppressWarnings("resource")
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Serveur démarré sur le port 8080");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                GestionnaireRequetes handler = new GestionnaireRequetes(clientSocket);
                handler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}