package com.example.gestion_inventario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProveedorDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private String nombreEmpresa;
    private String ruc;
    private String nombreContacto;
    private String telefono;
    private String email;
}
