package ms.inventario.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idLibro;   // FK lógica hacia Libro
    private int cantidad;
    private Long idOrden;   // FK lógica hacia Orden (futuro ms-tienda-web)

    // ---------- FUTURA CONEXIÓN CON TIENDA WEB (US-INV-13 y 14) ----------
    // Al crear una reserva, la tienda web debe validar stock y restarlo del StockLibro.
    // Al eliminar una reserva (cancelación de orden), se debe sumar la cantidad al stock.
    // La lógica real se implementará cuando exista el ms de Tienda Web.
}
