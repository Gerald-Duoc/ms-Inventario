package ms.inventario;

import ms.inventario.model.StockLibro;
import ms.inventario.model.StockLibroDTO;
import ms.inventario.model.ValidarStockRequest;
import ms.inventario.repository.StockLibroRepository;
import ms.inventario.service.StockLibroService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockLibroServiceTest {

    @Mock
    private StockLibroRepository repository;

    @InjectMocks
    private StockLibroService service;

    private StockLibro stockLibro;
    private StockLibroDTO stockLibroDTO;

    @BeforeEach
    void setUp() {
        stockLibro = new StockLibro(1L, 1L, 1L, 5, 50, 20);
        stockLibroDTO = new StockLibroDTO();
        stockLibroDTO.setId(1L);
        stockLibroDTO.setIdLibro(1L);
        stockLibroDTO.setIdSucursal(1L);
        stockLibroDTO.setStockMinimo(5);
        stockLibroDTO.setStockMaximo(50);
        stockLibroDTO.setStock(20);
    }

    @Test
    void testGuardar() {
        when(repository.save(any(StockLibro.class))).thenReturn(stockLibro);
        StockLibroDTO resultado = service.guardar(stockLibroDTO);
        assertThat(resultado.getIdLibro()).isEqualTo(1L);
        verify(repository).save(any(StockLibro.class));
    }

    @Test
    void testListar() {
        when(repository.findAll()).thenReturn(List.of(stockLibro));
        List<StockLibroDTO> resultado = service.listar();
        assertThat(resultado).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    void testBuscar() {
        when(repository.findById(1L)).thenReturn(Optional.of(stockLibro));
        Optional<StockLibroDTO> resultado = service.buscar(1L);
        assertThat(resultado).isPresent();
        verify(repository).findById(1L);
    }

    @Test
    void testActualizar() {
        when(repository.findById(1L)).thenReturn(Optional.of(stockLibro));
        when(repository.save(any(StockLibro.class))).thenAnswer(inv -> inv.getArgument(0));
        StockLibroDTO dto = new StockLibroDTO();
        dto.setStock(30);
        StockLibroDTO resultado = service.actualizar(1L, dto);
        assertThat(resultado.getStock()).isEqualTo(30);
        verify(repository).save(stockLibro);
    }

    @Test
    void testEliminar() {
        service.eliminar(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void testAñadirStock_Existing() {
        when(repository.findAll()).thenReturn(List.of(stockLibro));
        when(repository.save(any(StockLibro.class))).thenAnswer(inv -> inv.getArgument(0));
        service.añadirStock(1L, 1L, 10);
        assertThat(stockLibro.getStock()).isEqualTo(30);
        verify(repository).save(stockLibro);
    }

    @Test
    void testAñadirStock_New() {
        when(repository.findAll()).thenReturn(List.of());
        service.añadirStock(2L, 2L, 15);
        verify(repository).save(any(StockLibro.class));
    }

    @Test
    void testReducirStock_Success() {
        when(repository.findAll()).thenReturn(List.of(stockLibro));
        when(repository.save(any(StockLibro.class))).thenAnswer(inv -> inv.getArgument(0));
        service.reducirStock(1L, 1L, 5);
        assertThat(stockLibro.getStock()).isEqualTo(15);
        verify(repository).save(stockLibro);
    }

    @Test
    void testReducirStock_NotFound() {
        when(repository.findAll()).thenReturn(List.of());
        assertThrows(RuntimeException.class,
                () -> service.reducirStock(99L, 99L, 5));
    }

    @Test
    void testReducirStock_Insuficiente() {
        when(repository.findAll()).thenReturn(List.of(stockLibro));
        assertThrows(RuntimeException.class,
                () -> service.reducirStock(1L, 1L, 100));
    }

    @Test
    void testValidarStockSuficiente_True() {
        when(repository.findAll()).thenReturn(List.of(stockLibro));
        ValidarStockRequest request = new ValidarStockRequest();
        request.setIdSucursal(1L);
        ValidarStockRequest.ItemCantidad item = new ValidarStockRequest.ItemCantidad();
        item.setIdLibro(1L);
        item.setCantidad(10);
        request.setItems(List.of(item));
        boolean resultado = service.validarStockSuficiente(request);
        assertThat(resultado).isTrue();
    }

    @Test
    void testValidarStockSuficiente_False() {
        when(repository.findAll()).thenReturn(List.of(stockLibro));
        ValidarStockRequest request = new ValidarStockRequest();
        request.setIdSucursal(1L);
        ValidarStockRequest.ItemCantidad item = new ValidarStockRequest.ItemCantidad();
        item.setIdLibro(1L);
        item.setCantidad(100);
        request.setItems(List.of(item));
        boolean resultado = service.validarStockSuficiente(request);
        assertThat(resultado).isFalse();
    }

    @Test
    void testBuscarPorLibroYSucursal() {
        when(repository.findByIdLibroAndIdSucursal(1L, 1L)).thenReturn(Optional.of(stockLibro));
        Optional<StockLibroDTO> resultado = service.buscarPorLibroYSucursal(1L, 1L);
        assertThat(resultado).isPresent();
        verify(repository).findByIdLibroAndIdSucursal(1L, 1L);
    }
}
