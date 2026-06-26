package ms.inventario.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "libros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private String editorial;
    private String autor;
    private double precioCompra;
    private double precioVenta;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
}
