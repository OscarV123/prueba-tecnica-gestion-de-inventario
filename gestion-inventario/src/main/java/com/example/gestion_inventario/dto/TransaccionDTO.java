package com.example.gestion_inventario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TransaccionDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private Long productoId;
    private String tipo;
    private Integer cantidad;
    private String motivo;
    private LocalDateTime fechaRegistro;
}
