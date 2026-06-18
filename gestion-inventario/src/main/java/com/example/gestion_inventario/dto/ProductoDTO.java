package com.example.gestion_inventario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private Long categoriaId;
    private Long proveedorId;
}
