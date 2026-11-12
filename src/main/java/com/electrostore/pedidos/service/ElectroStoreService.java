package com.electrostore.pedidos.service;

import com.electrostore.pedidos.dto.ItemPedidoRequestDTO;
import com.electrostore.pedidos.dto.PedidoRequestDTO;
import com.electrostore.pedidos.entity.DetallePedido;
import com.electrostore.pedidos.entity.Pedido;
import com.electrostore.pedidos.entity.Producto;
import com.electrostore.pedidos.exception.BadRequestException;
import com.electrostore.pedidos.exception.ResourceNotFoundException;
import com.electrostore.pedidos.repository.PedidoRepository;
import com.electrostore.pedidos.repository.ProductoCustomRepositoryImpl;
import com.electrostore.pedidos.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ElectroStoreService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoCustomRepositoryImpl productoCustomRepository;

    // ==========================================
    // 1. LISTAR PRODUCTOS
    // ==========================================

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    // ==========================================
    // 2. BUSCAR PRODUCTO POR ID
    // ==========================================

    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con el ID: " + id
                ));
    }

    // ==========================================
    // 3. CREAR O ACTUALIZAR PRODUCTO
    // ==========================================

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    // ==========================================
    // 4. ELIMINAR PRODUCTO
    // ==========================================

    public void eliminarProducto(Long id) {

        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "No se puede eliminar. Producto no encontrado con ID: " + id
            );
        }

        productoRepository.deleteById(id);
    }

    // ==========================================
    // 5. BUSCAR POR CATEGORIA - NAMED QUERY
    // ==========================================

    public List<Producto> buscarProductosPorCategoriaNamed(String categoria) {
        return productoRepository.findByCategoriaNamed(categoria);
    }

    // ==========================================
    // 6. BUSCAR POR PRECIO MAXIMO DE VENTA
    // ==========================================

    public List<Producto> buscarPorPrecioMaximo(BigDecimal precioMax) {
        return productoRepository.buscarPorPrecioMaximoJPQL(precioMax);
    }

    // ==========================================
    // 7. BUSQUEDA AVANZADA
    // ==========================================

    public List<Producto> buscarPorFiltroAvanzadoEM(
            String keyword,
            Integer stockMin) {

        return productoCustomRepository.buscarPorFiltrosAvanzados(
                keyword,
                stockMin
        );
    }

    // ==========================================
    // 8. CREAR PEDIDO
    // ==========================================

    @Transactional
    public Pedido crearPedido(PedidoRequestDTO request) {

        Pedido pedido = new Pedido();

        pedido.setCliente(request.getCliente());

        // Datos del pedido
        pedido.setEstadoPedido(request.getEstadoPedido());
        pedido.setMetodoPago(request.getMetodoPago());

        pedido.setDescuento(
                request.getDescuento() != null
                        ? request.getDescuento()
                        : BigDecimal.ZERO
        );

        BigDecimal montoTotalAcumulado = BigDecimal.ZERO;

        // Recorrer los productos del pedido
        for (ItemPedidoRequestDTO item : request.getItems()) {

            Producto producto = productoRepository.findById(
                    item.getProductoId()
            ).orElseThrow(() -> new ResourceNotFoundException(
                    "Producto no encontrado con el ID: "
                            + item.getProductoId()
            ));

            // Verificar stock disponible
            if (producto.getStock() < item.getCantidad()) {

                throw new BadRequestException(
                        "Stock insuficiente para el producto: "
                                + producto.getNombre()
                                + ". Stock disponible: "
                                + producto.getStock()
                                + ", solicitado: "
                                + item.getCantidad()
                );
            }

            // Descontar stock
            producto.setStock(
                    producto.getStock() - item.getCantidad()
            );

            productoRepository.save(producto);

            // ==========================================
            // CAMBIO T2: UTILIZAR PRECIO DE VENTA
            // ==========================================

            BigDecimal subtotal = producto.getPrecioVenta()
                    .multiply(
                            BigDecimal.valueOf(item.getCantidad())
                    );

            montoTotalAcumulado = montoTotalAcumulado.add(subtotal);

            // Crear detalle del pedido
            DetallePedido detalle = new DetallePedido();

            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());

            // CAMBIO T2: PRECIO UNITARIO DE VENTA
            detalle.setPrecioUnitario(
                    producto.getPrecioVenta()
            );

            detalle.setSubtotal(subtotal);

            pedido.addDetalle(detalle);
        }

        // ==========================================
        // 9. APLICAR DESCUENTO
        // ==========================================

        BigDecimal montoFinal = montoTotalAcumulado.subtract(
                pedido.getDescuento()
        );

        if (montoFinal.compareTo(BigDecimal.ZERO) < 0) {
            montoFinal = BigDecimal.ZERO;
        }

        pedido.setMontoTotal(montoFinal);

        return pedidoRepository.save(pedido);
    }

    // ==========================================
    // 10. LISTAR PEDIDOS
    // ==========================================

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    // ==========================================
    // 11. OBTENER PEDIDO POR ID
    // ==========================================

    public Pedido obtenerPedidoPorId(Long id) {

        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pedido no encontrado con el ID: " + id
                ));
    }
}