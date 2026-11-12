package com.electrostore.pedidos.repository;

import com.electrostore.pedidos.entity.Producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // ==========================================
    // 1. JPQL CON PARÁMETROS NOMBRADOS
    // Búsqueda por precio máximo de venta
    // ==========================================

    @Query("""
        SELECT p FROM Producto p
        WHERE p.precioVenta <= :precioMax
        ORDER BY p.precioVenta DESC
    """)
    List<Producto> buscarPorPrecioMaximoJPQL(
            @Param("precioMax") BigDecimal precioMax
    );

    // ==========================================
    // 2. NAMED QUERY
    // Declarada en la entidad Producto.java
    // ==========================================

    List<Producto> findByCategoriaNamed(
            @Param("categoria") String categoria
    );
}
