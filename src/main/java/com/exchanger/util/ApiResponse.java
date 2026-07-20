package com.exchanger.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class ApiResponse {
    private int status;
    private boolean error;
    private String message;
}
