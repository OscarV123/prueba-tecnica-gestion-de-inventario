package com.example.gestion_inventario.service;

import com.example.gestion_inventario.dto.CategoriaDTO;
import com.example.gestion_inventario.entity.Categoria;
import com.example.gestion_inventario.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaDTO> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public boolean existe(Long id) {
        return categoriaRepository.existsById(id);
    }

    public Optional<CategoriaDTO> buscarPorId(Long id) {
        return categoriaRepository.findById(id).map(this::convertirADto);
    }

    public CategoriaDTO guardar(CategoriaDTO categoriaDTO) {
        Categoria categoria = convertirAEntidad(categoriaDTO);
        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        return convertirADto(categoriaGuardada);
    }

    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }

    private CategoriaDTO convertirADto(Categoria categoria) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());
        return dto;
    }

    private Categoria convertirAEntidad(CategoriaDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setId(dto.getId());
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        return categoria;
    }
}