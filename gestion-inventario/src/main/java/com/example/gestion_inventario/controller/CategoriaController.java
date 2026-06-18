package com.example.gestion_inventario.controller;

import com.example.gestion_inventario.dto.CategoriaDTO;
import com.example.gestion_inventario.service.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listarTodas() {
        return ResponseEntity.ok(
                categoriaService.listarTodas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> buscarPorId(@PathVariable Long id) {
        return categoriaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO> guardar(@RequestBody CategoriaDTO categoriaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoriaService.guardar(categoriaDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> actualizar(@PathVariable Long id,
                                                   @RequestBody CategoriaDTO categoriaDTO) {

        if (categoriaService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        categoriaDTO.setId(id);

        return ResponseEntity.ok(
                categoriaService.guardar(categoriaDTO)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        if (!categoriaService.existe(id)) {
            return ResponseEntity.notFound().build();
        }

        categoriaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}