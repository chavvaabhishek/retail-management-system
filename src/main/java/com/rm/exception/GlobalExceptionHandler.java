package com.rm.exception;

import com.rm.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            ProductNotFoundException.class
    )
    public ResponseEntity<ErrorResponse>
    handleProductNotFound(
            ProductNotFoundException ex
    ) {

        ErrorResponse response =
                ErrorResponse.builder()
                        .message(ex.getMessage())
                        .status(404)
                        .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(
            EmployeeNotFoundException.class
    )
    public ResponseEntity<ErrorResponse>
    handleEmployeeNotFound(
            EmployeeNotFoundException ex
    ) {

        ErrorResponse response =
                ErrorResponse.builder()
                        .message(ex.getMessage())
                        .status(404)
                        .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(
            BillNotFoundException.class
    )
    public ResponseEntity<ErrorResponse>
    handleBillNotFound(
            BillNotFoundException ex
    ) {

        ErrorResponse response =
                ErrorResponse.builder()
                        .message(ex.getMessage())
                        .status(404)
                        .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(
            SupplierNotFoundException.class
    )
    public ResponseEntity<ErrorResponse>
    handleSupplierNotFound(
            SupplierNotFoundException ex
    ) {

        ErrorResponse response =
                ErrorResponse.builder()
                        .message(ex.getMessage())
                        .status(404)
                        .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(InventoryServiceUnavailableException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(
            RuntimeException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(
                        Map.of(
                                "message",
                                ex.getMessage()
                        )
                );
    }


}