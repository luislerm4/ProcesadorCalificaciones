import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ProcesadorCalificaciones {

    public static void main(String[] args) {
        try {
            procesarArchivo("calificaciones.txt");
        } catch (IOException e) {
            System.err.println("No fue posible procesar el archivo: " + e.getMessage());
        }
    }

    public static void procesarArchivo(String nombreArchivo) throws IOException {

        try (BufferedReader lector = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                String lineaLimpia = linea.trim();

                try {
                    if (lineaLimpia.isEmpty()) {
                        throw new IllegalArgumentException("La línea no puede estar vacía.");
                    }

                    int calificacion = Integer.parseInt(lineaLimpia);

                    // Llama a la validación que lanza la excepción personalizada
                    validarCalificacion(calificacion);

                    System.out.println("Calificación válida: " + calificacion);

                } catch (NumberFormatException e) {
                    System.err.println("Formato numérico inválido: " + lineaLimpia);
                } catch (IllegalArgumentException e) {
                    System.err.println("Dato inválido: " + e.getMessage());
                } catch (CalificacionInvalidaException e) {
                    // Captura de nuestra excepción personalizada
                    System.err.println("Error en los datos: " + e.getMessage());
                }
            }
        }
    }

    // Firma con throws CalificacionInvalidaException
    public static void validarCalificacion(int calificacion) throws CalificacionInvalidaException {
        if (calificacion < 0 || calificacion > 100) {
            throw new CalificacionInvalidaException(
                    "Calificación fuera de rango: " + calificacion
            );
        }
    }
}