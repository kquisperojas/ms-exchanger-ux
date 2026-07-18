package com.exchanger.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExchangeRequest {

    private Long id;
    private String userName;
    private Double montoInicial;
    private Double montoFinal;
    private Double tipoCambio;
    private String monedaOrigen;
    private String monedaDestino;
    private LocalDateTime fechaProceso;
}
