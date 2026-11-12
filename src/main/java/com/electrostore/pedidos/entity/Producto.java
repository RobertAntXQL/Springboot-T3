package com.electrostore.pedidos.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "productos")
@NamedQueries({
    @NamedQuery(
        name = "Producto.findByCategoriaNamed",
        query = "SELECT p FROM Producto p WHERE LOWER(p.categoria) = LOWER(:categoria)"
    )
})
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String categoria;

    // Precio de compra
    @Column(name = "precio_compra", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioCompra;

    // Precio de venta
    @Column(name = "precio_venta", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioVenta;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false, length = 50)
    private String marca;

    @Column(name = "codigo_barras", nullable = false, unique = true, length = 100)
    private String codigoBarras;

    // Fecha de ingreso
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    // Constructor vacío obligatorio para JPA
    public Producto() {}

    // Constructor con parámetros
    public Producto(
            String nombre,
            String categoria,
            BigDecimal precioCompra,
            BigDecimal precioVenta,
            Integer stock,
            String marca,
            String codigoBarras,
            LocalDate fechaIngreso) {

        this.nombre = nombre;
        this.categoria = categoria;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.marca = marca;
        this.codigoBarras = codigoBarras;
        this.fechaIngreso = fechaIngreso;
    }

    // ==========================
    // GETTERS Y SETTERS
    // ==========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
}