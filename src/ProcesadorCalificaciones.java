import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ProcesadorCalificaciones {

    public static void main(String[] args) {
        try {
            double promedio = procesarArchivo("calificaciones.txt");
            System.out.println("Promedio final: " + promedio);

        } catch (IOException e) {
            System.err.println("Error al acceder al archivo: " + e.getMessage());
        } catch (SinDatosValidosException e) {
            System.err.println("Error de proceso: " + e.getMessage());
        }
    }

    public static double procesarArchivo(String nombreArchivo)
            throws IOException, SinDatosValidosException {

        int suma = 0;
        int contadorValidos = 0;

        try (BufferedReader lector = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                String lineaLimpia = linea.trim();

                try {
                    if (lineaLimpia.isEmpty()) {
                        throw new IllegalArgumentException("La línea está vacía.");
                    }

                    int calificacion = Integer.parseInt(lineaLimpia);
                    validarCalificacion(calificacion);

                    // Si pasa las validaciones, sumamos
                    suma += calificacion;
                    contadorValidos++;
                    System.out.println("Calificación procesada: " + calificacion);

                } catch (NumberFormatException e) {
                    System.err.println("Ignorando dato no numérico: " + lineaLimpia);
                } catch (IllegalArgumentException e) {
                    System.err.println("Ignorando línea inválida: " + e.getMessage());
                } catch (CalificacionInvalidaException e) {
                    System.err.println("Ignorando nota fuera de rango: " + e.getMessage());
                }
            }
        }
        if (contadorValidos == 0) {
            throw new SinDatosValidosException("El archivo no contiene ninguna calificación válida para promediar.");
        }

        return (double) suma / contadorValidos;
    }

    public static void validarCalificacion(int calificacion) throws CalificacionInvalidaException {
        if (calificacion < 0 || calificacion > 100) {
            throw new CalificacionInvalidaException("Valor fuera de rango (0-100): " + calificacion);
        }
    }
}