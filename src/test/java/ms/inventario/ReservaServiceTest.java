package ms.inventario;

import ms.inventario.model.Reserva;
import ms.inventario.model.ReservaDTO;
import ms.inventario.repository.ReservaRepository;
import ms.inventario.service.ReservaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    private ReservaRepository repository;

    @InjectMocks
    private ReservaService service;

    private Reserva reserva;

    @BeforeEach
    void setUp() {
        reserva = new Reserva(1L, 1L, 2, 101L);
    }

    @Test
    void testGuardar() {
        when(repository.save(any(Reserva.class))).thenReturn(reserva);
        ReservaDTO dto = new ReservaDTO();
        dto.setIdLibro(1L);
        dto.setCantidad(2);
        dto.setIdOrden(101L);
        ReservaDTO resultado = service.guardar(dto);
        assertThat(resultado.getIdLibro()).isEqualTo(1L);
        verify(repository).save(any(Reserva.class));
    }

    @Test
    void testListar() {
        when(repository.findAll()).thenReturn(List.of(reserva));
        List<ReservaDTO> resultado = service.listar();
        assertThat(resultado).hasSize(1);
    }

    @Test
    void testBuscar() {
        when(repository.findById(1L)).thenReturn(Optional.of(reserva));
        Optional<ReservaDTO> resultado = service.buscar(1L);
        assertThat(resultado).isPresent();
    }

    @Test
    void testEliminar() {
        service.eliminar(1L);
        verify(repository).deleteById(1L);
    }
}