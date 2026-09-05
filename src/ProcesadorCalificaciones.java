import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ProcesadorCalificaciones {

    public static void main(String[] args) {

        try (BufferedReader lector = new BufferedReader(new FileReader("calificaciones.txt"))) {

            String linea;

            while ((linea = lector.readLine()) != null) {
                try {
                    int calificacion = Integer.parseInt(linea.trim());
                    System.out.println(calificacion);
                } catch (NumberFormatException e) {
                    System.err.println("Formato numérico inválido: " + e.getMessage());
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("No se encontró el archivo.");
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }
}