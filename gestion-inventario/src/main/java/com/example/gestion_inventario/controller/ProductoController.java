package com.example.gestion_inventario.controller;

import com.example.gestion_inventario.dto.ProductoDTO;
import com.example.gestion_inventario.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listarTodos() {
        List<ProductoDTO> productos = productoService.listarTodos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> buscarPorId(@PathVariable Long id) {
        return productoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> guardar(@RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productoService.guardar(productoDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Long id,
                                                  @RequestBody ProductoDTO productoDTO) {

        if (productoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        productoDTO.setId(id);

        return ResponseEntity.ok(
                productoService.guardar(productoDTO)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        return productoService.buscarPorId(id)
                .map(producto -> {
                    productoService.eliminar(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/stock-bajo/{limite}")
    public ResponseEntity<List<ProductoDTO>> reportarStockBajo(@PathVariable Integer limite) {
        return ResponseEntity.ok(
                productoService.reportarStockBajo(limite)
        );
    }
}
