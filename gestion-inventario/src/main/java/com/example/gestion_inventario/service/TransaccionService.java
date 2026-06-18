package com.example.gestion_inventario.service;

import com.example.gestion_inventario.dto.TransaccionDTO;
import com.example.gestion_inventario.entity.Producto;
import com.example.gestion_inventario.entity.Transaccion;
import com.example.gestion_inventario.repository.ProductoRepository;
import com.example.gestion_inventario.repository.TransaccionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final ProductoRepository productoRepository;

    public TransaccionService(TransaccionRepository transaccionRepository, ProductoRepository productoRepository) {
        this.transaccionRepository = transaccionRepository;
        this.productoRepository = productoRepository;
    }

    public List<TransaccionDTO> listarTodas() {
        return transaccionRepository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public List<TransaccionDTO> listarPorProducto(Long productoId) {
        return transaccionRepository.findByProductoIdOrderByFechaRegistroDesc(productoId).stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TransaccionDTO registrarTransaccion(TransaccionDTO transaccionDTO) {
        Producto producto = productoRepository.findById(transaccionDTO.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if ("INGRESO".equalsIgnoreCase(transaccionDTO.getTipo())) {
            producto.setStock(producto.getStock() + transaccionDTO.getCantidad());

        } else if ("EGRESO".equalsIgnoreCase(transaccionDTO.getTipo())) {
            if (producto.getStock() < transaccionDTO.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para realizar el egreso");
            }
            producto.setStock(producto.getStock() - transaccionDTO.getCantidad());

        } else {
            throw new RuntimeException("Tipo de transacción no válido. Use INGRESO o EGRESO");
        }

        productoRepository.save(producto);

        Transaccion transaccion = convertirAEntidad(transaccionDTO);
        transaccion.setProducto(producto);
        Transaccion transaccionGuardada = transaccionRepository.save(transaccion);

        return convertirADto(transaccionGuardada);
    }


    private TransaccionDTO convertirADto(Transaccion transaccion) {
        TransaccionDTO dto = new TransaccionDTO();
        dto.setId(transaccion.getId());
        dto.setTipo(transaccion.getTipo());
        dto.setCantidad(transaccion.getCantidad());
        dto.setMotivo(transaccion.getMotivo());
        dto.setFechaRegistro(transaccion.getFechaRegistro());

        if (transaccion.getProducto() != null) {
            dto.setProductoId(transaccion.getProducto().getId());
        }
        return dto;
    }

    private Transaccion convertirAEntidad(TransaccionDTO dto) {
        Transaccion transaccion = new Transaccion();
        transaccion.setId(dto.getId());
        transaccion.setTipo(dto.getTipo());
        transaccion.setCantidad(dto.getCantidad());
        transaccion.setMotivo(dto.getMotivo());
        transaccion.setFechaRegistro(dto.getFechaRegistro());

        return transaccion;
    }
}