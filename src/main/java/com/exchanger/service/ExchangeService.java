package com.exchanger.service;

import com.exchanger.dto.ExchangeRequest;
import com.exchanger.exception.GlobalExceptionHandler;
import com.exchanger.util.ExchangeValidator;
import com.exchanger.util.SuccessResponse;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Service
public class ExchangeService {

    private final WebClient gorestClient;
    private final WebClient supportClient;

    public ExchangeService(
            WebClient.Builder builder,
            @Value("${api.gorest.base-url}") String gorestUrl,
            @Value("${api.soporte.base-url}") String supportUrl) {

        this.gorestClient = builder.baseUrl(gorestUrl).build();
        this.supportClient = builder.baseUrl(supportUrl).build();
    }

    // Registrar nueva operación de cambio
    public Mono<SuccessResponse<ExchangeRequest>> registrarCambio(ExchangeRequest request) {
        try {
            ExchangeValidator.validarRequest(request);
        } catch (IllegalArgumentException ex) {
            return Mono.error(new GlobalExceptionHandler.BadRequestException(ex.getMessage()));
        }

        log.info("Inicia proceso: {}", request.getId());
        return gorestClient.get()
                .uri("/users/" + request.getId())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .switchIfEmpty(Mono.error(new GlobalExceptionHandler.ResourceNotFoundException(
                        "Usuario con ID " + request.getId() + " no encontrado")))
                .flatMap(user -> {
                    String userName = user.get("name").asText();
                    Double finalAmount = request.getMontoInicial() * request.getTipoCambio();

                    Map<String, Object> body = Map.of(
                            "userName", userName,
                            "montoInicial", request.getMontoInicial(),
                            "tipoCambio", request.getTipoCambio(),
                            "montoFinal", finalAmount,
                            "monedaOrigen", request.getMonedaOrigen(),
                            "monedaDestino", request.getMonedaDestino());

                    return supportClient.post()
                            .uri("/registrarCambio")
                            .bodyValue(body)
                            .retrieve()
                            .bodyToMono(ExchangeRequest.class)
                            .map(resp -> new SuccessResponse<>(
                                    200,
                                    "Intercambio registrado con usuario: " + userName, resp
                            ))
                            .onErrorMap(e -> new GlobalExceptionHandler.BadRequestException(
                                    "Error al registrar cambio: " + e.getMessage()));
                });
    }

    // Listar todas las operaciones
    public Flux<ExchangeRequest> listarOperaciones() {
        return supportClient.get()
                .uri("/listarOperaciones")
                .retrieve()
                .bodyToFlux(ExchangeRequest.class)
                .onErrorMap(e -> new GlobalExceptionHandler.GatewayTimeoutException(
                        "Error al listar operaciones: " + e.getMessage()));
    }

    // Buscar operaciones por moneda origen
    public Flux<ExchangeRequest> buscarPorMonedaOrigen(String monedaOrigen) {
        ExchangeValidator.validarMoneda(monedaOrigen, "monedaOrigen");
        return supportClient.get()
                .uri("/origen/{monedaOrigen}", monedaOrigen)
                .retrieve()
                .bodyToFlux(ExchangeRequest.class)
                .switchIfEmpty(Flux.error(new GlobalExceptionHandler.ResourceNotFoundException(
                        "No se encontraron operaciones con moneda origen: " + monedaOrigen)))
                .onErrorMap(e -> new GlobalExceptionHandler.GatewayTimeoutException(
                        "Error al buscar operaciones por moneda origen: " + e.getMessage()));
    }

    // Buscar operaciones por moneda destino
    public Flux<ExchangeRequest> buscarPorMonedaDestino(String monedaDestino) {
        ExchangeValidator.validarMoneda(monedaDestino, "monedaDestino");
        return supportClient.get()
                .uri("/destino/{monedaDestino}", monedaDestino)
                .retrieve()
                .bodyToFlux(ExchangeRequest.class)
                .switchIfEmpty(Flux.error(new GlobalExceptionHandler.ResourceNotFoundException(
                        "No se encontraron operaciones con moneda destino: " + monedaDestino)))
                .onErrorMap(e -> new GlobalExceptionHandler.GatewayTimeoutException(
                        "Error al buscar operaciones por moneda destino: " + e.getMessage()));
    }

    // Actualizar operación por ID
    public Mono<SuccessResponse<ExchangeRequest>> actualizar(Long id, ExchangeRequest tx) {
        try {
            ExchangeValidator.validarActualizar(id, tx);
        } catch (IllegalArgumentException ex) {
            return Mono.error(new GlobalExceptionHandler.BadRequestException(ex.getMessage()));
        }
        return supportClient.put()
                .uri("/actualizar/{id}", id)
                .bodyValue(tx)
                .retrieve()
                .bodyToMono(ExchangeRequest.class)
                .map(resp -> new SuccessResponse<>(
                        200, "Operación con ID " + id + " actualizada correctamente", resp
                ))
                .onErrorMap(WebClientResponseException.NotFound.class,
                        e -> new GlobalExceptionHandler.ResourceNotFoundException(
                                "El ID ingresado " + id + " no se encuentra en la base de datos"))
                .onErrorMap(WebClientResponseException.BadRequest.class,
                        e -> new GlobalExceptionHandler.BadRequestException(
                                "Datos inválidos para actualizar operación: " + e.getMessage()))
                .onErrorMap(e -> new GlobalExceptionHandler.BadRequestException(
                        "Error al actualizar operación: " + e.getMessage()));
    }

    // Eliminar operación por ID
    public Mono<String> eliminar(Long id) {
        try {
            ExchangeValidator.validarEliminar(id);
        } catch (IllegalArgumentException ex) {
            return Mono.error(new IllegalArgumentException(ex.getMessage()));
        }
        return supportClient.delete()
                .uri("/eliminar/{id}", id)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorMap(WebClientResponseException.NotFound.class,
                        e -> new GlobalExceptionHandler.ResourceNotFoundException(
                                "El ID ingresado " + id + " no se encuentra en la base de datos"))
                .onErrorMap(WebClientResponseException.BadRequest.class,
                        e -> new GlobalExceptionHandler.BadRequestException(
                                "Datos inválidos para eliminar operación: " + e.getMessage()))
                .onErrorMap(e -> new GlobalExceptionHandler.BadRequestException(
                        "Error al eliminar operación: " + e.getMessage()));
    }

}
