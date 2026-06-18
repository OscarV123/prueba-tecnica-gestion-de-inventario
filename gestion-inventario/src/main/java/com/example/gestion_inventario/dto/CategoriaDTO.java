package com.example.gestion_inventario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CategoriaDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private String nombre;
    private String descripcion;
}
