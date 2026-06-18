package com.example.gestion_inventario.controller;

import com.example.gestion_inventario.dto.ProveedorDTO;
import com.example.gestion_inventario.service.ProveedorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public ResponseEntity<List<ProveedorDTO>> listarTodos() {
        return ResponseEntity.ok(
                proveedorService.listarTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDTO> buscarPorId(@PathVariable Long id) {
        return proveedorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProveedorDTO> guardar(@RequestBody ProveedorDTO proveedorDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(proveedorService.guardar(proveedorDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorDTO> actualizar(@PathVariable Long id,
                                                   @RequestBody ProveedorDTO proveedorDTO) {

        if (!proveedorService.existe(id)) {
            return ResponseEntity.notFound().build();
        }

        proveedorDTO.setId(id);

        return ResponseEntity.ok(
                proveedorService.guardar(proveedorDTO)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        if (proveedorService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        proveedorService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}