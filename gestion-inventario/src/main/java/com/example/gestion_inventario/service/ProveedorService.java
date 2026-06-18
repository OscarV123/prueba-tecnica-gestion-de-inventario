package com.example.gestion_inventario.service;

import com.example.gestion_inventario.dto.ProveedorDTO;
import com.example.gestion_inventario.entity.Proveedor;
import com.example.gestion_inventario.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public List<ProveedorDTO> listarTodos() {
        return proveedorRepository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public boolean existe(Long id) {
        return proveedorRepository.existsById(id);
    }

    public Optional<ProveedorDTO> buscarPorId(Long id) {
        return proveedorRepository.findById(id).map(this::convertirADto);
    }

    // Validación de RUC
    public ProveedorDTO guardar(ProveedorDTO proveedorDTO) {
        if (proveedorDTO.getId() == null) {
            Optional<Proveedor> existente = proveedorRepository.findByRuc(proveedorDTO.getRuc());

            if (existente.isPresent()) {
                throw new RuntimeException("Ya existe un proveedor registrado con el RUC: " + proveedorDTO.getRuc());
            }
        }

        Proveedor proveedor = convertirAEntidad(proveedorDTO);
        Proveedor proveedorGuardado = proveedorRepository.save(proveedor);
        return convertirADto(proveedorGuardado);
    }

    public void eliminar(Long id) {
        proveedorRepository.deleteById(id);
    }

    private ProveedorDTO convertirADto(Proveedor proveedor) {
        ProveedorDTO dto = new ProveedorDTO();
        dto.setId(proveedor.getId());
        dto.setNombreEmpresa(proveedor.getNombreEmpresa());
        dto.setRuc(proveedor.getRuc());
        dto.setNombreContacto(proveedor.getNombreContacto());
        dto.setTelefono(proveedor.getTelefono());
        dto.setEmail(proveedor.getEmail());
        return dto;
    }

    private Proveedor convertirAEntidad(ProveedorDTO dto) {
        Proveedor proveedor = new Proveedor();
        proveedor.setId(dto.getId());
        proveedor.setNombreEmpresa(dto.getNombreEmpresa());
        proveedor.setRuc(dto.getRuc());
        proveedor.setNombreContacto(dto.getNombreContacto());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setEmail(dto.getEmail());
        return proveedor;
    }
}