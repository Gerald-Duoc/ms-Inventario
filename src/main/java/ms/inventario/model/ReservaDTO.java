package ms.inventario.model;

import lombok.Data;

@Data
public class ReservaDTO {
    private Long id;
    private Long idLibro;
    private int cantidad;
    private Long idOrden;
}
