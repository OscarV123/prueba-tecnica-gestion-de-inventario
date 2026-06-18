package com.example.gestion_inventario.repository;

import com.example.gestion_inventario.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByStockLessThan(Integer limite);
}
