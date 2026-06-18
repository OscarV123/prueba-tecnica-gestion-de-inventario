package com.example.gestion_inventario.service;

import com.example.gestion_inventario.dto.ProductoDTO;
import com.example.gestion_inventario.entity.Categoria;
import com.example.gestion_inventario.entity.Producto;
import com.example.gestion_inventario.entity.Proveedor;
import com.example.gestion_inventario.repository.CategoriaRepository;
import com.example.gestion_inventario.repository.ProductoRepository;
import com.example.gestion_inventario.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;

    public ProductoService(ProductoRepository productoRepository,
                           CategoriaRepository categoriaRepository,
                           ProveedorRepository proveedorRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.proveedorRepository = proveedorRepository;
    }

    public List<ProductoDTO> listarTodos() {
        return productoRepository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public Optional<ProductoDTO> buscarPorId(Long id) {
        return productoRepository.findById(id).map(this::convertirADto);
    }

    public ProductoDTO guardar(ProductoDTO productoDTO) {
        Proveedor proveedor = proveedorRepository.findById(productoDTO.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        Producto producto = convertirAEntidad(productoDTO);
        Producto productoGuardado = productoRepository.save(producto);
        return convertirADto(productoGuardado);
    }

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    public List<ProductoDTO> reportarStockBajo(Integer limite) {
        return productoRepository.findByStockLessThan(limite).stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    private ProductoDTO convertirADto(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());

        if (producto.getCategoria() != null) {
            dto.setCategoriaId(producto.getCategoria().getId());
        }

        if (producto.getProveedor() != null) {
            dto.setProveedorId(producto.getProveedor().getId());
        }

        return dto;
    }

    private Producto convertirAEntidad(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setId(dto.getId());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());

        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            producto.setCategoria(categoria);
        }

        if (dto.getProveedorId() != null) {
            Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
            producto.setProveedor(proveedor);
        }

        return producto;
    }
}