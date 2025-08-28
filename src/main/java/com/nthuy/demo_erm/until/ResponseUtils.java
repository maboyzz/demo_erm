package com.nthuy.demo_erm.until;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {

    public static <T> ResponseEntity<T> created(T body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    public static <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok(body);
    }

    public static <T> ResponseEntity<T> noContent() {
        return ResponseEntity.noContent().build();
    }
}