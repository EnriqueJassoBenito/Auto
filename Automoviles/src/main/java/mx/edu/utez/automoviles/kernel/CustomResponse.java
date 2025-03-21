package mx.edu.utez.automoviles.kernel;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomResponse {
    private Map<String, Object> body;

    public ResponseEntity<?> getOkResponse(Object data) {
        body = new HashMap<>();
        body.put("message", "operacion exitosa");
        body.put("status","ok");
        if (data != null) {
            body.put("data", data);
        }

        return new ResponseEntity<>(body, HttpStatus.OK);

    }

    public ResponseEntity<?> getCreatedResponse() {
        body = new HashMap<>();
        body.put("message", "El registro fue creado exitosamente");
        body.put("status","CREATED");

        return new ResponseEntity<>(body, HttpStatus.CREATED);

    }
    public ResponseEntity<?> get400Response(int code) {
        body = new HashMap<>();
        body.put("message", code == 403 ? "Acceso denegado" : "El registro no pudo ser creado");
        body.put("status", code == 403 ? "FORBIDDEN" : "NOT_FOUND");

        return new ResponseEntity<>(body, code == 403 ? HttpStatus.FORBIDDEN : HttpStatus.NOT_FOUND);

    }

    public ResponseEntity<?> getErrorResponse(String message) {
        body = new HashMap<>();
        body.put("message", message);
        body.put("status", "BAD_REQUEST");

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);

    }

}
