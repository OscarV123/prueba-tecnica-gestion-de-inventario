package com.example.gestion_inventario.controller;

import com.example.gestion_inventario.dto.TransaccionDTO;
import com.example.gestion_inventario.service.TransaccionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @GetMapping
    public ResponseEntity<List<TransaccionDTO>> listarTodas() {
        return ResponseEntity.ok(
                transaccionService.listarTodas()
        );
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<TransaccionDTO>> listarPorProducto(
            @PathVariable Long productoId) {

        return ResponseEntity.ok(
                transaccionService.listarPorProducto(productoId)
        );
    }

    @PostMapping
    public ResponseEntity<TransaccionDTO> registrarTransaccion(
            @RequestBody TransaccionDTO transaccionDTO) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transaccionService.registrarTransaccion(transaccionDTO));
    }
}