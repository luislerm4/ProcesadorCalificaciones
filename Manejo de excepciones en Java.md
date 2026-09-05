* Nombre: Luis Angel Lerma Coss
* Expediente: 225203072

## **1\. Objetivo de aprendizaje**

Al finalizar la práctica, el estudiante será capaz de **identificar, capturar, propagar y generar excepciones en Java**, aplicando buenas prácticas de manejo de errores mediante `try`, `catch`, `finally`, `throws`, `throw`, excepciones específicas y `try-with-resources`.

### **Resultados de aprendizaje**

El estudiante podrá:

* Diferenciar excepciones verificadas y no verificadas.  
* Utilizar correctamente `try`, `catch` y `finally`.  
* Propagar excepciones con `throws`.  
* Lanzar excepciones mediante `throw`.  
* Crear y utilizar una excepción personalizada.  
* Utilizar `try-with-resources`.  
* Aplicar criterios básicos de buenas prácticas en el manejo de excepciones.


# **2\. Situación de trabajo**

Se desarrollará una aplicación denominada **ProcesadorCalificaciones**, cuya función será leer un archivo de texto con calificaciones y calcular su promedio.

Archivo de ejemplo:

85  
90  
78  
abc  
95  
110  
70

Durante el procesamiento pueden ocurrir distintos problemas:

* el archivo no existe;  
* una línea no contiene un número;  
* una calificación está fuera del rango permitido;  
* ocurre un error de lectura.

La práctica consiste en hacer que la aplicación maneje estos problemas de manera controlada.

# **Parte I. Observar una excepción sin manejar**

## **3\. Crear el programa inicial**

Crear:

import java.io.BufferedReader;  
import java.io.FileReader;  
import java.io.IOException;

public class ProcesadorCalificaciones {

    public static void main(String\[\] args) throws IOException {

        BufferedReader lector \=  
            new BufferedReader(  
                new FileReader("calificaciones.txt")  
            );

        String linea;

        while ((linea \= lector.readLine()) \!= null) {  
            int calificacion \= Integer.parseInt(linea);  
            System.out.println(calificacion);  
        }

        lector.close();  
    }  
}

Ejecutar primero con un archivo válido y después con un archivo que contenga:

85  
90  
abc  
75

### **Actividad**

Registrar:

1. ¿Qué excepción aparece?  java.lang.NumberFormatException
2. ¿En qué línea ocurre?   En la línea 17 de ProcesadorCalificaciones.java
3. ¿Continúa la ejecución?  No, el programa se detuvo despues de imprimir 85 y 90 y el 75 no se termino de procesar.
4. ¿Qué información proporciona el *stack trace*? Te dice cual fue el texto que corrompio el codigo y en que linea fue.

El material explica que el objeto excepción contiene información sobre el tipo de error y el estado del programa cuando ocurrió, y que lanzar una excepción consiste en crear ese objeto y pasarlo al runtime.

# **Parte II. Capturar una excepción**

## **4\. Utilizar `try-catch`**

Modificar el procesamiento:

try {

    int calificacion \=  
        Integer.parseInt(linea);

    System.out.println(calificacion);

} catch (NumberFormatException e) {

    System.out.println(  
        "Valor inválido: " \+ linea  
    );  
}

Ejecutar nuevamente.

### **Observar**

Ahora el programa no termina cuando encuentra:

abc

sino que procesa las líneas siguientes.


# **5\. Actividad de aprendizaje 1: comparar comportamiento**

Ejecutar dos versiones:

### **Versión A**

Sin `try-catch`.

### **Versión B**

Con:

catch (NumberFormatException e)

Completar:

| Aspecto | Sin manejo                       | Con manejo                                    |
| ----- |----------------------------------|-----------------------------------------------|
| ¿Termina el programa? | Si, el programa explota de golpe | No, termina todo el ciclo limpio              |
| ¿Se muestra el error? | Lanza un strack trace rojo feo   | Muestra un mensaje controlado y limpio        |
| ¿Se procesan las líneas posteriores? | No, ignora todo lo que sigue     | Si, se brinca el error y procesa lo que falta |
| ¿Puede recuperarse el programa? | No                               | Si                                            |

### **Reflexión**

¿Qué ventaja proporciona capturar una excepción que el programa puede anticipar?


# **Parte III. Múltiples excepciones**

## **6\. Capturar errores de archivo**

Ahora eliminar o cambiar el nombre de:

calificaciones.txt

La creación de:

new FileReader("calificaciones.txt")

puede generar una excepción de archivo no encontrado.

Modificar:

try {

    BufferedReader lector \=  
        new BufferedReader(  
            new FileReader("calificaciones.txt")  
        );

} catch (java.io.FileNotFoundException e) {

    System.err.println(  
        "No se encontró el archivo."  
    );  
}


# **7\. Capturar excepciones específicas primero**

Construir:

try {

    // procesamiento

} catch (NumberFormatException e) {

    System.err.println(  
        "Formato numérico inválido: "  
        \+ e.getMessage()  
    );

} catch (IllegalArgumentException e) {

    System.err.println(  
        "Argumento inválido: "  
        \+ e.getMessage()  
    );  
}


### **Pregunta**

¿Por qué sería incorrecto invertir el orden anterior?
NumberFormatException es una subclase (hija) de IllegalArgumentException. Si pones la clase general primero, esa atrapa todo y nunca deja pasar nada al bloque de abajo. Java se da cuenta de eso y te da un error de compilación diciendo que el segundo catch es código inalcanzable (unreachable code). Las excepciones específicas van arriba y las generales abajo.

# **Parte IV. `finally`**

## **8\. Agregar limpieza de recursos**

Ejemplo:

BufferedReader lector \= null;

try {

    lector \=  
        new BufferedReader(  
            new FileReader("calificaciones.txt")  
        );

    // procesamiento

} catch (IOException e) {

    System.err.println(  
        "Error de entrada/salida: "  
        \+ e.getMessage()  
    );

} finally {

    if (lector \!= null) {  
        try {  
            lector.close();  
        } catch (IOException e) {  
            System.err.println(  
                "No fue posible cerrar el archivo."  
            );  
        }  
    }  
}



### **Actividad**

Probar:

* archivo correcto;  
* archivo inexistente;  
* contenido inválido.

¿En qué situaciones se ejecuta `finally`?
Se ejecuta siempre, pase lo que pase. No importa si el archivo se leyó completo sin fallos, si saltó un error de formato o si el archivo ni siquiera existía, el código dentro de finally se corre sí o sí al terminar el bloque try-catch.


# **Parte V. `try-with-resources`**

## **9\. Simplificar el cierre de recursos**

El material recomienda también `try-with-resources` para objetos que implementan `AutoCloseable`; el recurso se cierra automáticamente al finalizar el bloque.

Reescribir:

try (  
    BufferedReader lector \=  
        new BufferedReader(  
            new FileReader("calificaciones.txt")  
        )  
) {

    String linea;

    while ((linea \= lector.readLine()) \!= null) {

        System.out.println(linea);  
    }

} catch (IOException e) {

    System.err.println(  
        "Error al leer el archivo: "  
        \+ e.getMessage()  
    );  
}

### **Comparación**

Responder:

* ¿Qué código desapareció?   Desapareció la declaración de BufferedReader lector = null, el bloque finally  y el try-catch anidado que llamaba lector.close().
* ¿Quién cierra ahora el archivo?  Java lo cierra en automático al salir del bloque try. por la implementacion de AutoClosable
* ¿Qué versión resulta más clara?La versión con try-with-resources por que tiene menos lineas de codigo y es mas facil de leer

---

# **Parte VI. Lanzar excepciones con `throw`**

## **10\. Validar calificaciones**

Crear:

public static void validarCalificacion(  
        int calificacion) {

    if (calificacion \< 0 ||  
        calificacion \> 100\) {

        throw new IllegalArgumentException(  
            "La calificación debe estar entre 0 y 100: "  
            \+ calificacion  
        );  
    }  
}

Utilizar:

int calificacion \=  
    Integer.parseInt(linea);

validarCalificacion(calificacion);

El material señala que `throw` requiere un objeto `Throwable` y se utiliza para lanzar explícitamente una excepción.

# **11\. Actividad de aprendizaje 2: diseñar validaciones**

Agregar validaciones para detectar:

* línea vacía;  
* número negativo;  
* número mayor de 100\.

Ejemplo:

if (linea.isBlank()) {  
    throw new IllegalArgumentException(  
        "La línea no puede estar vacía."  
    );  
}

Cada equipo deberá definir:

| Condición | Excepción                | Mensaje |
| ----- |--------------------------| ----- |
| Línea vacía | IllegalArgumentException | "La línea no puede estar vacía." |
| Valor no numérico | NumberFormatException                         | "Formato numérico inválido: " + linea |
| Valor \< 0 |            IllegalArgumentException              |"La calificación debe estar entre 0 y 100: " + calificacion  |
| Valor \> 100 |    IllegalArgumentException                      |"La calificación debe estar entre 0 y 100: " + calificacion  |

El material recomienda utilizar excepciones lo más específicas posible y evitar declarar genéricamente `throws Exception`.

# **Parte VII. Propagar excepciones con `throws`**

## **12\. Crear método de lectura**

Separar responsabilidades:

public static void procesarArchivo(  
        String nombreArchivo)  
        throws IOException {

    try (  
        BufferedReader lector \=  
            new BufferedReader(  
                new FileReader(nombreArchivo)  
            )  
    ) {

        String linea;

        while ((linea \= lector.readLine()) \!= null) {  
            System.out.println(linea);  
        }  
    }  
}

Y en `main`:

try {

    procesarArchivo(  
        "calificaciones.txt"  
    );

} catch (IOException e) {

    System.err.println(  
        "No fue posible procesar el archivo."  
    );  
}

El material indica que, en ciertos casos, es preferible que un método superior en la pila de llamadas maneje la excepción; en tal situación, el método que puede generarla la especifica mediante `throws`.


# **Parte VIII. Crear una excepción personalizada**

## **13\. Definir `CalificacionInvalidaException`**

El material señala que pueden crearse clases propias de excepción cuando sea necesario representar problemas específicos de la aplicación.

Crear:

public class CalificacionInvalidaException  
        extends Exception {

    public CalificacionInvalidaException(  
            String mensaje) {

        super(mensaje);  
    }  
}

Modificar:

public static void validarCalificacion(  
        int calificacion)  
        throws CalificacionInvalidaException {

    if (calificacion \< 0 ||  
        calificacion \> 100\) {

        throw new CalificacionInvalidaException(  
            "Calificación fuera de rango: "  
            \+ calificacion  
        );  
    }  
}

Capturar:

catch (CalificacionInvalidaException e) {

    System.err.println(  
        "Error en los datos: "  
        \+ e.getMessage()  
    );  
}  
---

# **Parte IX. Actividad integradora**

## **14\. Construir un procesador robusto**

La aplicación deberá leer:

85  
90  
abc  
78  
110  
72  
\-5  
95

Y comportarse aproximadamente así:

Calificación válida: 85  
Calificación válida: 90  
Dato ignorado: abc  
Calificación válida: 78  
Calificación fuera de rango: 110  
Calificación válida: 72  
Calificación fuera de rango: \-5  
Calificación válida: 95

Promedio de valores válidos: 84.00

### **Requisitos**

La solución deberá utilizar:

1. `try`.  
2. Al menos dos bloques `catch`.  
3. `try-with-resources`.  
4. `throws`.  
5. `throw`.  
6. `NumberFormatException`.  
7. `IOException`.  
8. Una excepción personalizada.  
9. Mensajes descriptivos.

---

# **15\. Código base sugerido**

import java.io.BufferedReader;  
import java.io.FileReader;  
import java.io.IOException;

public class ProcesadorCalificaciones {

    public static void main(String\[\] args) {

        try {

            double promedio \=  
                calcularPromedio(  
                    "calificaciones.txt"  
                );

            System.out.printf(  
                "Promedio: %.2f%n",  
                promedio  
            );

        } catch (IOException e) {

            System.err.println(  
                "No fue posible procesar el archivo: "  
                \+ e.getMessage()  
            );  
        }  
    }

    public static double calcularPromedio(  
            String archivo)  
            throws IOException {

        double suma \= 0;  
        int contador \= 0;

        try (  
            BufferedReader lector \=  
                new BufferedReader(  
                    new FileReader(archivo)  
                )  
        ) {

            String linea;

            while ((linea \= lector.readLine()) \!= null) {

                try {

                    int calificacion \=  
                        Integer.parseInt(  
                            linea.trim()  
                        );

                    validarCalificacion(  
                        calificacion  
                    );

                    suma \+= calificacion;  
                    contador++;

                } catch (NumberFormatException e) {

                    System.err.println(  
                        "Dato no numérico: "  
                        \+ linea  
                    );

                } catch (  
                    CalificacionInvalidaException e  
                ) {

                    System.err.println(  
                        e.getMessage()  
                    );  
                }  
            }  
        }

        return suma / contador;  
    }

    public static void validarCalificacion(  
            int calificacion)  
            throws CalificacionInvalidaException {

        if (calificacion \< 0 ||  
            calificacion \> 100\) {

            throw new CalificacionInvalidaException(  
                "Calificación fuera de rango: "  
                \+ calificacion  
            );  
        }  
    }  
}  
---

# **Parte X. Aplicar buenas prácticas**

## **16\. Actividad de aprendizaje 3: Code Review**

Analizar el siguiente código:

try {

    int numero \=  
        Integer.parseInt(valor);

} catch (Throwable e) {

}

Identificar al menos tres problemas.

### **Aspectos esperados**

* Captura `Throwable`.  
* Ignora completamente la excepción.  
* No utiliza una excepción específica.  
* No proporciona información del error.

Proponer una versión mejorada.


# **17\. Documentar excepciones**

El material recomienda documentar mediante `@throws` las excepciones especificadas por un método.

Ejemplo:

/\*\*  
 \* Valida una calificación.  
 \*  
 \* @param calificacion valor a validar  
 \* @throws CalificacionInvalidaException  
 \*         si el valor está fuera del rango 0-100  
 \*/  
public static void validarCalificacion(  
        int calificacion)  
        throws CalificacionInvalidaException {  
    // ...  
}

### **Actividad**

Agregar Javadoc a:

* `validarCalificacion()`;  
* `calcularPromedio()`.

# **18\. Buenas prácticas a verificar**

Antes de entregar, revisar:

| Buena práctica | Cumple |
| ----- | ----- |
| Utiliza excepciones específicas | ☐ |
| No captura `Throwable` | ☐ |
| No ignora excepciones | ☐ |
| Usa mensajes descriptivos | ☐ |
| Captura primero excepciones específicas | ☐ |
| Utiliza `try-with-resources` | ☐ |
| Documenta `throws` | ☐ |
| No utiliza `throws Exception` sin necesidad | ☐ |

El material también advierte contra registrar una excepción y volver a lanzarla innecesariamente, pues puede producir múltiples mensajes para el mismo problema; cuando se requiere agregar contexto, propone envolver la excepción preservando la causa original.

# **19\. Reto final**

Modificar la aplicación para recibir el nombre del archivo por argumento:

java ProcesadorCalificaciones calificaciones.txt

Si no se proporciona el argumento:

Uso:  
java ProcesadorCalificaciones \<archivo\>

El programa deberá manejar adecuadamente:

* archivo inexistente;  
* línea vacía;  
* texto no numérico;  
* calificación fuera de rango;  
* error de lectura;  
* archivo sin ninguna calificación válida.

Para el último caso, diseñar una excepción:

SinDatosValidosException  
---

# **20\. Entregables**

Cada estudiante entregar:

* Liga a repositorio
* `ProcesadorCalificaciones.java`  
* `CalificacionInvalidaException.java`  
* `SinDatosValidosException.java`, si se realiza el reto  
* `calificaciones.txt`  
* evidencia de las ejecuciones;  
* respuestas a las preguntas de reflexión;  
* breve explicación de qué excepciones son *checked* y cuáles *unchecked* dentro de su solución.

# **21\. Preguntas de reflexión**

1. ¿Qué diferencia existe entre lanzar y capturar una excepción?  lanzar es reportar que ocurrio creando la excepcion y sacandola fuera del flujo normal, capturar es atrapar esa excepcion cuando salta para poder manejarla y asi poder evitar que el programa explote
2. ¿Qué función tiene `try`?   Envolver el codigo que es propenso a fallar para estar pendientes si salta una excepcion
3. ¿Qué función tiene `catch`?  Recibir la excepcion si algo falla dentro del bloque try y poder manejarla
4. ¿Cuándo resulta útil `finally`?  Cuando necesitas ejecutar codigo si o si al final, sin importar si todo salio bien o si salio una excepcion 
5. ¿Qué ventaja tiene `try-with-resources`?  Cierra automaticamente recursos en cuanto el bloque try termina ahorrando escribir bloques finally gigantes y evitando fugas de memoria 
6. ¿Cuál es la diferencia entre `throw` y `throws`?  throw Accion de lanzar una excepcion explicitamente en el codigo y Throws Advierte en la firma del metodo para avisar que ese metodo puede arrojar una excepcion y quien lo llame debe hacerse cargo. 
7. ¿Por qué conviene utilizar excepciones específicas?  Porque te permite saber con precisión qué falló exactamente y responder adecuadamente a cada tipo de problema, en lugar de tratar todos los errores por igual
8. ¿Cuándo tiene sentido crear una excepción personalizada?  Cuando necesitas representar un error propio de la lógica o reglas de negocio de tu app que Java no tiene contemplado en sus clases por defecto
9. ¿Por qué no se recomienda capturar `Throwable`?  Porque Throwable también atrapa los Error graves de la máquina virtual, los cuales el programa no puede ni debe intentar reparar
10. ¿Qué efecto tiene ignorar una excepción?  Deja un "bloque catch vacío", haciendo que el programa continúe corriendo con datos corruptos o incompletos y volviendo casi imposible rastrear dónde estuvo el fallo real
11. ¿Qué información debería proporcionar un buen mensaje de excepción?  Debe explicar claramente qué pasó, qué dato causó el problema y idealmente qué se esperaba recibir
12. ¿En qué casos conviene propagar una excepción en lugar de capturarla inmediatamente?Cuando el método actual no tiene la información o la responsabilidad necesaria para solucionar el problema, y es mejor dejar que el método superior (como el main o una interfaz de usuario) decida qué hacer

---

# **22\. Criterios de evaluación**

| Criterio | Ponderación |
| ----- | ----- |
| Identificación correcta de situaciones excepcionales | 10% |
| Uso correcto de `try-catch` | 20% |
| Uso de excepciones específicas | 15% |
| Uso correcto de `throw` y `throws` | 15% |
| Implementación de excepción personalizada | 10% |
| Uso de `try-with-resources` | 10% |
| Aplicación de buenas prácticas | 10% |
| Calidad y claridad del código | 5% |
| Reflexión y evidencias | 5% |
| **Total** | **100%** |


