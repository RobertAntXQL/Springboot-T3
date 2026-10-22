package com.electrostore.pedidos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

public class PedidoRequestDTO {

    @NotBlank(message = "El nombre del cliente no puede estar vacío")
    private String cliente;

    @NotBlank(message = "El estado del pedido es obligatorio")
    private String estadoPedido;

    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;

    @NotNull(message = "El descuento no puede ser nulo")
    @PositiveOrZero(message = "El descuento no puede ser negativo")
    private BigDecimal descuento;

    @NotEmpty(message = "El pedido debe contener al menos un producto")
    @Valid // Aplica la validación en cada elemento de la lista
    private List<ItemPedidoRequestDTO> items;

    public PedidoRequestDTO() {}

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getEstadoPedido() { return estadoPedido; }
    public void setEstadoPedido(String estadoPedido) { this.estadoPedido = estadoPedido; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }

    public List<ItemPedidoRequestDTO> getItems() { return items; }
    public void setItems(List<ItemPedidoRequestDTO> items) { this.items = items; }
}