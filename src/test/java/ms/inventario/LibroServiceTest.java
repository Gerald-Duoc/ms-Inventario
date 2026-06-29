package ms.inventario;

import ms.inventario.model.Libro;
import ms.inventario.model.LibroDTO;
import ms.inventario.model.Categoria;
import ms.inventario.repository.LibroRepository;
import ms.inventario.service.LibroService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LibroServiceTest {

    @Mock
    private LibroRepository repository;

    @InjectMocks
    private LibroService service;

    private Libro libro;

    @BeforeEach
    void setUp() {
        libro = new Libro(1L, "Cien años", "Novela", "Sudamericana", "García Márquez",
                5000.0, 9900.0, Categoria.FICCION, new Date());
    }

    @Test
    void testGuardar() {
        when(repository.save(any(Libro.class))).thenReturn(libro);
        LibroDTO dto = new LibroDTO();
        dto.setNombre("Cien años");
        LibroDTO resultado = service.guardar(dto);
        assertThat(resultado.getNombre()).isEqualTo("Cien años");
        verify(repository).save(any(Libro.class));
    }

    @Test
    void testListar() {
        when(repository.findAll()).thenReturn(List.of(libro));
        List<LibroDTO> resultado = service.listar();
        assertThat(resultado).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    void testBuscar() {
        when(repository.findById(1L)).thenReturn(Optional.of(libro));
        Optional<LibroDTO> resultado = service.buscar(1L);
        assertThat(resultado).isPresent();
        verify(repository).findById(1L);
    }

    @Test
    void testActualizar() {
        when(repository.findById(1L)).thenReturn(Optional.of(libro));
        when(repository.save(any(Libro.class))).thenAnswer(inv -> inv.getArgument(0));
        LibroDTO dto = new LibroDTO();
        dto.setNombre("Nuevo título");
        LibroDTO resultado = service.actualizar(1L, dto);
        assertThat(resultado.getNombre()).isEqualTo("Nuevo título");
        verify(repository).save(libro);
    }

    @Test
    void testEliminar() {
        service.eliminar(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void testBuscarPorNombre() {
        when(repository.findByNombreContainingIgnoreCase("cien")).thenReturn(List.of(libro));
        List<LibroDTO> resultado = service.buscarPorNombre("cien");
        assertThat(resultado).hasSize(1);
    }

    @Test
    void testObtenerPrecio() {
        when(repository.findById(1L)).thenReturn(Optional.of(libro));
        Double precio = service.obtenerPrecio(1L);
        assertThat(precio).isEqualTo(9900.0);
    }
}