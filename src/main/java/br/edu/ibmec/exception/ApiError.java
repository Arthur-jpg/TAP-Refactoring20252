package br.edu.ibmec.exception;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

/**
 * DTO de erros padronizados retornados pela API.
 */
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String path,
        String code,
        Map<String, String> details) {

    public ApiError(int status, String error, String path, String code, Map<String, String> details) {
        this(Instant.now(), status, error, path, code, details == null ? Collections.emptyMap() : details);
    }
}
