package com.electrostore.pedidos.controller;

import com.electrostore.pedidos.entity.Producto;
import com.electrostore.pedidos.service.ElectroStoreService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ElectroStoreService service;

    // =====================================
    // LISTAR TODOS LOS PRODUCTOS
    // =====================================

    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(service.listarProductos());
    }

    // =====================================
    // OBTENER PRODUCTO POR ID
    // =====================================

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerProductoPorId(id));
    }

    // =====================================
    // REGISTRAR NUEVO PRODUCTO
    // =====================================

    @PostMapping
    public ResponseEntity<Producto> crear(
            @Valid @RequestBody Producto producto) {

        return new ResponseEntity<>(
                service.guardarProducto(producto),
                HttpStatus.CREATED
        );
    }

    // =====================================
    // ACTUALIZAR PRODUCTO
    // =====================================

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Producto producto) {

        Producto existente = service.obtenerProductoPorId(id);

        existente.setNombre(producto.getNombre());
        existente.setCategoria(producto.getCategoria());

        // Nuevos campos de la T2
        existente.setPrecioCompra(producto.getPrecioCompra());
        existente.setPrecioVenta(producto.getPrecioVenta());

        existente.setStock(producto.getStock());
        existente.setMarca(producto.getMarca());
        existente.setCodigoBarras(producto.getCodigoBarras());

        // Nuevo campo de la T2
        existente.setFechaIngreso(producto.getFechaIngreso());

        return ResponseEntity.ok(
                service.guardarProducto(existente)
        );
    }

    // =====================================
    // ELIMINAR PRODUCTO
    // =====================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        service.eliminarProducto(id);

        return ResponseEntity.noContent().build();
    }

    // =====================================
    // BUSCAR PRODUCTOS POR CATEGORIA
    // Named Query
    // =====================================

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> listarPorCategoriaNamed(
            @PathVariable String categoria) {

        return ResponseEntity.ok(
                service.buscarProductosPorCategoriaNamed(categoria)
        );
    }

    // =====================================
    // BUSCAR POR PRECIO MAXIMO DE VENTA
    // =====================================

    @GetMapping("/precio-max")
    public ResponseEntity<List<Producto>> listarPorPrecioMaximo(
            @RequestParam BigDecimal max) {

        return ResponseEntity.ok(
                service.buscarPorPrecioMaximo(max)
        );
    }

    // =====================================
    // BUSQUEDA AVANZADA
    // =====================================

    @GetMapping("/buscar-avanzado")
    public ResponseEntity<List<Producto>> buscarAvanzado(
            @RequestParam String keyword,
            @RequestParam Integer stockMin) {

        return ResponseEntity.ok(
                service.buscarPorFiltroAvanzadoEM(keyword, stockMin)
        );
    }
}