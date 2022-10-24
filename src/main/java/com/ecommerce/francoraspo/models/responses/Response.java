package com.ecommerce.francoraspo.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    private Instant timestamp;
    private T data;
    private int respondeCode;
    private String status;
}
