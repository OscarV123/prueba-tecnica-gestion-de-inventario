package com.example.gestion_inventario.repository;

import com.example.gestion_inventario.entity.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long>{
    List<Transaccion> findByProductoIdOrderByFechaRegistroDesc(Long productoId);
}
