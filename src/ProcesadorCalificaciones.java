import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ProcesadorCalificaciones {

    public static void main(String[] args) {

        BufferedReader lector = null;

        try {
            lector = new BufferedReader(new FileReader("calificaciones.txt"));
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
            System.err.println("Error de entrada/salida: " + e.getMessage());
        } finally {
            if (lector != null) {
                try {
                    lector.close();
                } catch (IOException e) {
                    System.err.println("No fue posible cerrar el archivo.");
                }
            }
        }
    }
}