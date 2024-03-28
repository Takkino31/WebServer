import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class InterpreteurPython {
    public String interpretFile(File file) {
        String filePath = file.getAbsolutePath();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python3", filePath);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("<br>");
            }
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return output.toString();
            } else {
                return "Une erreur s'est produite lors de l'exécution du code Python.";
            }
        } catch (IOException | InterruptedException e) {
            return "Une erreur s'est produite lors de l'exécution du code Python.";
        }
    }
}