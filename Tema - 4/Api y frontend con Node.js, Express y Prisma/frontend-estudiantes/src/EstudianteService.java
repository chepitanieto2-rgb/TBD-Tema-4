
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import java.util.LinkedHashMap;
import java.util.Map;

public class EstudianteService {

    private static final String API_URL =
            "http://localhost:3000/estudiantes";

    private final HttpClient cliente = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    // CREAR JSON DEL ESTUDIANTE
    private String crearJson(
            String matricula,
            String nombre,
            String apellidos,
            String carrera,
            String correo) {

        Map<String, String> estudiante = new LinkedHashMap<>();

        estudiante.put("matricula", matricula);
        estudiante.put("nombre", nombre);
        estudiante.put("apellidos", apellidos);
        estudiante.put("carrera", carrera);
        estudiante.put("correo", correo);

        return gson.toJson(estudiante);
    }

    // POST: REGISTRAR ESTUDIANTE
    public boolean registrarEstudiante(
            String matricula,
            String nombre,
            String apellidos,
            String carrera,
            String correo) throws Exception {

        String json = crearJson(
                matricula, nombre, apellidos, carrera, correo
        );

        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> respuesta = cliente.send(
                solicitud,
                HttpResponse.BodyHandlers.ofString()
        );

        return verificarRespuesta(respuesta);
    }

    // GET: CONSULTAR ESTUDIANTES
    public String obtenerEstudiantes() throws Exception {

        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .GET()
                .build();

        HttpResponse<String> respuesta = cliente.send(
                solicitud,
                HttpResponse.BodyHandlers.ofString()
        );

        if (respuesta.statusCode() == 200) {
            return respuesta.body();
        }

        throw new Exception(
                "Error HTTP " + respuesta.statusCode()
                + ": " + respuesta.body()
        );
    }

    // PUT: ACTUALIZAR ESTUDIANTE
    public boolean actualizarEstudiante(
            int id,
            String matricula,
            String nombre,
            String apellidos,
            String carrera,
            String correo) throws Exception {

        String json = crearJson(
                matricula, nombre, apellidos, carrera, correo
        );

        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> respuesta = cliente.send(
                solicitud,
                HttpResponse.BodyHandlers.ofString()
        );

        return verificarRespuesta(respuesta);
    }

    // DELETE: ELIMINAR ESTUDIANTE
    public boolean eliminarEstudiante(int id) throws Exception {

        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/" + id))
                .DELETE()
                .build();

        HttpResponse<String> respuesta = cliente.send(
                solicitud,
                HttpResponse.BodyHandlers.ofString()
        );

        return verificarRespuesta(respuesta);
    }

    // VERIFICAR RESPUESTAS HTTP
    private boolean verificarRespuesta(
            HttpResponse<String> respuesta) throws Exception {

        int codigo = respuesta.statusCode();

        if (codigo >= 200 && codigo < 300) {
            return true;
        }

        throw new Exception(
                "Error HTTP " + codigo
                + ": " + respuesta.body()
        );
    }
}
