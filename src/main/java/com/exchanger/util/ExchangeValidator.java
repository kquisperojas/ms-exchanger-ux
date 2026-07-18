package com.exchanger.util;

import com.exchanger.dto.ExchangeRequest;

public class ExchangeValidator {

    public static void validarRequest(ExchangeRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("El objeto ExchangeRequest no puede ser nulo");
        }

        if (request.getId() == null || request.getId() <= 0) {
            throw new IllegalArgumentException("El campo 'id' es obligatorio y debe ser mayor a 0");
        }

        if (request.getUserName() == null || request.getUserName().isBlank()) {
            throw new IllegalArgumentException("El campo 'userName' es obligatorio");
        }

        if (request.getMonedaOrigen() == null || request.getMonedaOrigen().isBlank()) {
            throw new IllegalArgumentException("El campo 'monedaOrigen' es obligatorio");
        }
        if (!request.getMonedaOrigen().matches("^[A-Za-z]{3}$")) {
            throw new IllegalArgumentException("La 'monedaOrigen' debe contener solo letras y exactamente 3 caracteres");
        }

        if (request.getMonedaDestino() == null || request.getMonedaDestino().isBlank()) {
            throw new IllegalArgumentException("El campo 'monedaDestino' es obligatorio");
        }
        if (!request.getMonedaDestino().matches("^[A-Za-z]{3}$")) {
            throw new IllegalArgumentException("La 'monedaDestino' debe contener solo letras y exactamente 3 caracteres");
        }

        if (request.getMonedaOrigen().equalsIgnoreCase(request.getMonedaDestino())) {
            throw new IllegalArgumentException("La moneda origen y destino no pueden ser iguales");
        }

        if (request.getMontoInicial() == null || request.getMontoInicial() <= 0) {
            throw new IllegalArgumentException("El monto inicial debe ser mayor a cero");
        }

        if (request.getTipoCambio() == null || request.getTipoCambio() <= 0) {
            throw new IllegalArgumentException("El tipo de cambio debe ser mayor a cero");
        }

        if (request.getMontoFinal() != null && request.getMontoFinal() <= 0) {
            throw new IllegalArgumentException("El monto final debe ser mayor a cero");
        }
    }
    // Validación para actualizar operación
    public static void validarActualizar(Long id, ExchangeRequest tx) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El parámetro 'id' es obligatorio y debe ser mayor a 0");
        }
        // Validar usuario
        if (tx.getUserName() == null || tx.getUserName().isBlank()) {
            throw new IllegalArgumentException("El campo 'userName' es obligatorio");
        }
        if (tx.getMonedaOrigen() == null || !tx.getMonedaOrigen().matches("^[A-Za-z]{3}$")) {
            throw new IllegalArgumentException("La 'monedaOrigen' debe contener solo letras y exactamente 3 caracteres");
        }
        if (tx.getMonedaDestino() == null || !tx.getMonedaDestino().matches("^[A-Za-z]{3}$")) {
            throw new IllegalArgumentException("La 'monedaDestino' debe contener solo letras y exactamente 3 caracteres");
        }
        if (tx.getMonedaOrigen().equalsIgnoreCase(tx.getMonedaDestino())) {
            throw new IllegalArgumentException("La moneda origen y destino no pueden ser iguales");
        }

        // Validar montos
        if (tx.getMontoInicial() == null || tx.getMontoInicial() <= 0) {
            throw new IllegalArgumentException("El 'montoInicial' debe ser mayor a 0");
        }
        if (tx.getTipoCambio() == null || tx.getTipoCambio() <= 0) {
            throw new IllegalArgumentException("El 'tipoCambio' debe ser mayor a 0");
        }
    }

    // Validación para eliminar operación
    public static void validarEliminar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El parámetro 'id' es obligatorio y siempre debe enviarse");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("El parámetro 'id' debe ser mayor a 0");
        }
    }

    // Validación para búsquedas por moneda
    public static void validarMoneda(String moneda, String tipo) {
        if (moneda == null || moneda.isBlank()) {
            throw new IllegalArgumentException("El campo '" + tipo + "' es obligatorio");
        }
        // Validar longitud
        if (moneda.length() != 3) {
            throw new IllegalArgumentException("El campo '" + tipo + "' debe tener exactamente 3 caracteres");
        }
        // Validar que sean solo letras
        if (!moneda.matches("^[A-Za-z]{3}$")) {
            throw new IllegalArgumentException("El campo '" + tipo + "' debe contener únicamente letras");
        }
    }

}
