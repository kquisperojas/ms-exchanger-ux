package com.exchanger.controller;

import com.exchanger.dto.ExchangeRequest;
import com.exchanger.service.ExchangeService;
import com.exchanger.util.SuccessResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/experiencia")
public class ExchangeController {

    private final ExchangeService service;

    public ExchangeController(ExchangeService service) {
        this.service = service;
    }

    // Registrar nueva operación
    @PostMapping("/registrarCambio")
    public Mono<SuccessResponse<ExchangeRequest>> registrarCambio(@RequestBody ExchangeRequest request) {
        return service.registrarCambio(request);
    }

    // Buscar por moneda origen
    @GetMapping("/origen/{monedaOrigen}")
    public Flux<ExchangeRequest> buscarPorMonedaOrigen(@PathVariable String monedaOrigen) {
        return service.buscarPorMonedaOrigen(monedaOrigen);
    }

    // Buscar por moneda destino
    @GetMapping("/destino/{monedaDestino}")
    public Flux<ExchangeRequest> buscarPorMonedaDestino(@PathVariable String monedaDestino) {
        return service.buscarPorMonedaDestino(monedaDestino);
    }

    // Listar todas las operaciones
    @GetMapping("/listarOperaciones")
    public Flux<ExchangeRequest> listarOperaciones() {
        return service.listarOperaciones();
    }

    // Actualizar operación
    @PutMapping("/actualizar/{id}")
    public Mono<SuccessResponse<ExchangeRequest>> actualizar(@PathVariable Long id,
                                                             @RequestBody ExchangeRequest tx) {
        return service.actualizar(id, tx);
    }

    // Eliminar operación
    @DeleteMapping("/eliminar/{id}")
    public Mono<String> eliminar(@PathVariable Long id) {
        return service.eliminar(id);
    }
}
