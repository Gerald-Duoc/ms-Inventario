package ms.inventario.model;

import lombok.Data;

@Data
public class StockLibroDTO {
    private Long id;
    private Long idLibro;
    private Long idSucursal;
    private int stockMinimo;
    private int stockMaximo;
    private int stock;
}
