# Bitácora de Interacción y Prompt Engineering (PROMPTS.md)

**Proyecto:** Capa de Dominio en Java para Telemetría Agrícola (*AgroTech Llano*)  
**Rol de la IA:** Arquitecto de Software Senior & Code Reviewer Exigente  
**Metodología:** Método Socrático, Code Review iterativo y refactorización guiada por patrones de diseño y principios SOLID.

---

## 1. Prompt de Configuración Inicial (System Prompt)

**Entrada del Usuario:**
> Actúa estrictamente como un Arquitecto de Software Senior y Code Reviewer Exigente de la Universidad de los Llanos. Mi objetivo es refactorizar y dominar la Programación Orientada a Objetos en Java para Sistemas de Escala Agrícola e Industrial.
> REGLAS DE INTERACCIÓN OBLIGATORIAS:
> PROHIBIDO GENERAR CÓDIGO SOLUCIÓN: Bajo ninguna circunstancia me proporciones la solución completa en código, ni escribas clases o métodos por mí.
> MÉTODO SOCRÁTICO: Si te formulo una pregunta o solicito ayuda, responde ÚNICAMENTE con preguntas guía, diagramas conceptuales en texto, o pistas sobre inconsistencias lógicas.
> EVALUACIÓN DE CALIDAD DE CÓDIGO: Si te envío un bloque de mi código en Java, realiza un "Code Review" identificando: violación de encapsulamiento, malas prácticas, acoplamiento o ineficiencia sintáctica respecto a C++. No reescribas el código corregido.
> ENFOQUE INDUSTRIAL: Evalúa la robustez pensando en producción y mantenibilidad.
> ¿Entendido? Confirma tu rol únicamente con la frase: "Arquitecto Senior listo. Presenta tu análisis de requerimientos."

**Respuesta de la IA:**
> Arquitecto Senior listo. Presenta tu análisis de requerimientos.

---

## 2. Fase 1: Fundamentos de Abstracción y Dominio (C++ vs. Java)

### Iteración 1.1: Clases Abstractas y UML Initial
* **Prompt:** Explicación del concepto de clase abstracta en Java en comparación con las funciones virtuales puras de C++ (`= 0`) y análisis del diagrama UML del dominio agrícola.
* **Respuesta de la IA:**
  * Tabla comparativa entre C++ (`virtual`, punteros) y Java (`abstract`, referencias).
  * Justificación del modelo de dominio para `Sensor`.
  * Cuestionario socrático sobre errores de compilación, encapsulamiento (`private` vs `protected`) y despacho polimórfico.

---

## 3. Fase 2: Refactorización Iterativa de la Clase Base `Sensor`

### Iteración 2.1: Encapsulamiento y Concurrencia Inicial
* **Prompt (Código Enviado):** Primera versión de `Sensor.java` con setters, constructor vacío y `evaluarEstado()` abstracto.
* **Feedback de la IA:** 
  * Mutabilidad expuesta en `setId()` y `setUbicacion()`.
  * Riesgos de condición de carrera en entornos multihilo.
  * Incoherencia al forzar a las subclases a reimplementar la lógica básica de `evaluarEstado()`.

### Iteración 2.2: Inmutabilidad y Visibilidad de Memoria
* **Prompt (Código Enviado):** Implementación de atributos `final`, adición de `volatile boolean activo` y método concreto `evaluarEstado()`.
* **Feedback de la IA:** 
  * Falta de saneamiento en entradas (aceptación de cadenas vacías `""` o espacios).
  * Explicación de los límites de `volatile` frente a operaciones compuestas atómicas.
  * Rigidez en la lógica de diagnóstico para subclases.

### Iteración 2.3: Validación Estricta y Enumeraciones
* **Prompt (Código Enviado):** Uso de `.isBlank()`, `.trim()` y creación del `enum EstadoSensor`.
* **Feedback de la IA:**
  * Alerta sobre el orden de validaciones (ejecutar `.trim()` sobre referencias nulas genera `NullPointerException`).
  * Dilema del valor por defecto en primitivos (`double = 0.0`) vs la ausencia real de lectura.

### Iteración 2.4: Aplicación del Patrón *Template Method*
* **Prompt (Código Enviado):** Introducción de `lecturaTomada`, método `final tomarLectura()` que invoca al hook abstracto `protected abstract double realizarLectura()`.
* **Feedback de la IA:**
  * Identificación de bordes de falla: falta de validación de precondición (intentar leer con sensor inactivo).
  * Inconsistencia de estado en caso de excepciones de red/hardware durante la lectura.

### Iteración 2.5: Solución Aprobada de `Sensor.java`
* **Prompt (Código Enviado):** Inclusión de manejo de excepciones `try-catch`, flags `errorLectura`, `ultimaLectura` y estado `ERROR_LECTURA`.
* **Respuesta de la IA:** Aprobación del modelo. Validación de encapsulamiento, inmutabilidad, thread-safety y aplicación del patrón *Template Method*.

---

## 4. Fase 3: Conceptos Avanzados de Ejecución y Memoria (JVM vs. C++)

### Iteración 3.1: Despacho Dinámico de Métodos
* **Prompt:** Solicitud de profundización en el funcionamiento interno de la JVM.
* **Conceptos Explicados:**
  * **Callsites Monomórficos e Inlining (JIT):** Eliminación del costo del *lookup* de métodos.
  * **`invokevirtual` vs `invokeinterface`:** Diferencias entre tablas `vtable` (herencia lineal) e `itable` (múltiples interfaces).

### Iteración 3.2: Gestión de Memoria y Garbage Collection
* **Prompt:** Explicación de riesgos de memoria de C++ resueltos por el GC en Java.
* **Conceptos Explicados:**
  * **Punteros Colgantes / Use-After-Free:** Garantía mediante *Reachability Analysis* (Grafos de alcanzabilidad desde *GC Roots*).
  * **Double Free:** Imposibilidad sintáctica de liberación manual.
  * **Object Slicing:** Prevención por el uso exclusivo de referencias administradas en el Heap.
  * **Fugas de Memoria Lógicas:** Acumulación no intencionada de objetos en estructuras estáticas.

---

## 5. Fase 4: Principios SOLID (Principio de Abierto/Cerrado - OCP)

### Iteración 4.1: Evaluando OCP en `EstacionMonitoreo`
* **Prompt (Código Enviado):** Primera versión de `EstacionMonitoreo.java` usando `instanceof` para filtrar sensores críticos.
* **Feedback de la IA:** Identificación de violación directa del principio OCP. Acoplamiento fuerte de la estación con las clases hijas concretas y los umbrales numéricos de negocio.

### Iteración 4.2: Refactorización Polimórfica (Solución Final)
* **Prompt (Código Enviado):** Introducción de los métodos `esCritico()` en la clase base y `esUmbralCritico()` en las subclases.
* **Respuesta de la IA:** Validación de cumplimiento del principio OCP. La clase `EstacionMonitoreo` quedó 100% cerrada a la modificación y abierta a la extensión.

---

## 6. Arquitectura Final Lograda
1. **Inmutabilidad Identificativa:** Atributos del hardware protegidos como `final` y saneados con `.trim()`.
2. **Visibilidad en Concurrencia:** Variables de estado compartidas declaradas con `volatile`.
3. **Tipado Fuerte:** Enumeraciones (`EstadoSensor`) en lugar de cadenas de texto explícitas.
4. **Patrón Template Method:** Control centralizado de precondiciones y errores en `tomarLectura()`, delegando solo el algoritmo específico a `realizarLectura()`.
5. **Cumplimiento SOLID:** Delegación polimórfica completa de la evaluación de estado crítico (`esCritico()`), garantizando desacoplamiento en la capa de monitoreo.