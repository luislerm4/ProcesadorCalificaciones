import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ProcesadorCalificaciones {

    public static void main(String[] args) {
        try {
            // Llama al método que propaga la excepción
            procesarArchivo("calificaciones.txt");
        } catch (IOException e) {
            System.err.println("No fue posible procesar el archivo: " + e.getMessage());
        }
    }

    // Parte VII: throws avisa que este método puede propagar IOException
    public static void procesarArchivo(String nombreArchivo) throws IOException {

        try (BufferedReader lector = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                String lineaLimpia = linea.trim();

                try {
                    // Validar línea vacía con throw
                    if (lineaLimpia.isEmpty()) {
                        throw new IllegalArgumentException("La línea no puede estar vacía.");
                    }

                    int calificacion = Integer.parseInt(lineaLimpia);

                    // Validar rango con throw
                    validarCalificacion(calificacion);

                    System.out.println("Calificación válida: " + calificacion);

                } catch (NumberFormatException e) {
                    System.err.println("Formato numérico inválido: " + lineaLimpia);
                } catch (IllegalArgumentException e) {
                    System.err.println("Dato inválido: " + e.getMessage());
                }
            }
        }
    }

    // Parte VI: throw lanza la excepción si no cumple el rango
    public static void validarCalificacion(int calificacion) {
        if (calificacion < 0 || calificacion > 100) {
            throw new IllegalArgumentException(
                    "La calificación debe estar entre 0 y 100: " + calificacion
            );
        }
    }
}