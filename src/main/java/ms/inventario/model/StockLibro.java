package ms.inventario.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stock_libros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockLibro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idLibro;      // FK lógica hacia Libro
    private Long idSucursal;   // FK lógica hacia ms-sucursal

    private int stockMinimo;
    private int stockMaximo;
    private int stock;

    // ---------- FUTURA CONEXIÓN CON MONITOREOGE-MS (US-INV-04) ----------
    // Cuando se actualice este stock, si stock < stockMinimo,
    // se deberá llamar a monitoreoge-ms para generar una alerta de bajo stock.
    // También puede hacerse vía evento (RabbitMQ) en lugar de REST.
}
