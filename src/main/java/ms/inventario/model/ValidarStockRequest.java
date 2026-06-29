package ms.inventario.model;

import lombok.Data;
import java.util.List;

@Data
public class ValidarStockRequest {
    private Long idSucursal;
    private List<ItemCantidad> items;

    @Data
    public static class ItemCantidad {
        private Long idLibro;
        private int cantidad;
    }
}
