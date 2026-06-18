package com.example.gestion_inventario.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "proveedores")
@Data
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_empresa", nullable = false, length = 100)
    private String nombreEmpresa;

    @Column(nullable = false, length = 11, unique = true)
    private String ruc;

    @Column(name = "nombre_contacto", length = 100)
    private String nombreContacto;

    @Column(length = 15)
    private String telefono;

    @Column(length = 100)
    private String email;
}