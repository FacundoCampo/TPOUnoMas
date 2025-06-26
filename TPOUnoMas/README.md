# TPO UnoMas

**UnoMas** es una aplicación de escritorio desarrollada en **Java con Swing** que permite gestionar partidos deportivos entre usuarios, seleccionando deportes, estrategias de emparejamiento y participando de encuentros. El proyecto está estructurado bajo el patrón de diseño **MVC (Modelo-Vista-Controlador)**, simulando una persistencia en memoria.

---

## 🏗️ Arquitectura y Patrones Aplicados

Este proyecto fue diseñado teniendo en cuenta principios de diseño sólidos (SOLID) y GRASP, e implementa varios **patrones de diseño** reconocidos:

### ✅ Patrones de Diseño

| Patrón       | Uso                                                       | Justificación                                                          |
|--------------|------------------------------------------------------------|------------------------------------------------------------------------|
| **MVC**      | Separación de lógica de negocio, interfaz y controladores | Favorece la mantenibilidad y escalabilidad del sistema                 |
| **Strategy** | Emparejamiento por cercanía, nivel o historial            | Permite intercambiar algoritmos en tiempo de ejecución (**OCP**)       |
| **State**    | Manejo del ciclo de vida de un partido                    | Encapsula comportamientos por estado (**SRP**, **Polimorfismo GRASP**) |
| **Adapter**  | Envío de notificaciones por distintos servicios           | Unifica interfaces externas de email.                                  |
| **Observer** | Notificación a usuarios ante cambios en partidos          | Bajo acoplamiento entre sujeto (`Partido`) y observadores (`Usuario`)  |
| **Repository** | Acceso abstracto a "base de datos"                      | Desacopla lógica de negocio de la persistencia (**DIP**, **SRP**)      |
| **Factory**  | Creación de estrategias de emparejamiento                 | Centraliza la lógica de instanciación y mejora extensibilidad          |

---

## 🚀 Funcionalidades

- Registro de nuevos usuarios con preferencias deportivas y niveles.
- Creación de partidos seleccionando deporte, fecha, duración, estrategia de emparejamiento.
- Emparejamiento automático de jugadores según estrategia elegida.
- Visualización de partidos disponibles e historial.
- Participación y eliminación de partidos.
- Transición automática del estado del partido según jugadores y tiempo.
- Notificación a los jugadores sobre eventos importantes del partido.

---

## ⚙️ Tecnologías Utilizadas

- **Java SE**
- **Swing** (interfaz gráfica)
- **Principios SOLID & GRASP**
- **DTOs & Mappers** (desacoplamiento entre capas)
- **Simulación de persistencia** (con clases estáticas)

---

## 👤 Usuario para Pruebas

```plaintext
Email: Usuario1@mail.com  
Contraseña: clave1
