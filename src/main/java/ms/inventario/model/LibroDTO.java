package ms.inventario.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class LibroDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String editorial;
    private String autor;
    private double precioCompra;
    private double precioVenta;
    private Categoria categoria;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaCreacion;
    private int unidadesVendidas;
}