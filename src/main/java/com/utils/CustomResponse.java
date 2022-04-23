package com.utils;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CustomResponse {
    private Map<String, Object> response = new HashMap<>();

    public CustomResponse(HttpStatus httpStatus, String message, Object data) {
        response.put("status_code", httpStatus.value());
        response.put("status", httpStatus);
        if (message != null)
            response.put("message", message);
        if (data != null)
            response.put("data", data);

    }

    public ResponseEntity<Object> getResponse() {
        return ResponseEntity
                .status((HttpStatus) this.response.get("status"))
                .body(this.response);
    }
}
