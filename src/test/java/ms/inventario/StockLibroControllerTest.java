package ms.inventario;

import com.fasterxml.jackson.databind.ObjectMapper;

import ms.inventario.controller.StockLibroController;
import ms.inventario.model.StockLibroDTO;
import ms.inventario.model.ValidarStockRequest;
import ms.inventario.service.StockLibroService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StockLibroController.class)
public class StockLibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockLibroService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListar() throws Exception {
        StockLibroDTO dto = new StockLibroDTO();
        dto.setId(1L);
        dto.setIdLibro(1L);
        dto.setIdSucursal(1L);
        dto.setStock(20);
        when(service.listar()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/stock-libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void testBuscar_Found() throws Exception {
        StockLibroDTO dto = new StockLibroDTO();
        dto.setId(1L);
        dto.setStock(20);
        when(service.buscar(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/stock-libros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock", is(20)));
    }

    @Test
    void testBuscar_NotFound() throws Exception {
        when(service.buscar(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/stock-libros/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCrear() throws Exception {
        StockLibroDTO entrada = new StockLibroDTO();
        entrada.setIdLibro(1L);
        entrada.setIdSucursal(1L);
        entrada.setStock(20);
        StockLibroDTO salida = new StockLibroDTO();
        salida.setId(1L);
        salida.setIdLibro(1L);
        salida.setIdSucursal(1L);
        salida.setStock(20);
        when(service.guardar(any(StockLibroDTO.class))).thenReturn(salida);

        mockMvc.perform(post("/api/stock-libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void testActualizar() throws Exception {
        StockLibroDTO entrada = new StockLibroDTO();
        entrada.setStock(30);
        StockLibroDTO salida = new StockLibroDTO();
        salida.setId(1L);
        salida.setStock(30);
        when(service.actualizar(eq(1L), any(StockLibroDTO.class))).thenReturn(salida);

        mockMvc.perform(put("/api/stock-libros/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock", is(30)));
    }

    @Test
    void testEliminar() throws Exception {
        mockMvc.perform(delete("/api/stock-libros/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testAñadirStock() throws Exception {
        StockLibroDTO dto = new StockLibroDTO();
        dto.setIdLibro(1L);
        dto.setIdSucursal(1L);
        dto.setStock(10);

        mockMvc.perform(post("/api/stock-libros/añadir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Stock añadido correctamente"));
    }

    @Test
    void testReducirStock_Success() throws Exception {
        StockLibroDTO dto = new StockLibroDTO();
        dto.setIdLibro(1L);
        dto.setIdSucursal(1L);
        dto.setStock(5);

        mockMvc.perform(post("/api/stock-libros/reducir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Stock reducido correctamente"));
    }

    @Test
    void testReducirStock_Error() throws Exception {
        StockLibroDTO dto = new StockLibroDTO();
        dto.setIdLibro(1L);
        dto.setIdSucursal(99L);
        dto.setStock(5);
        doThrow(new RuntimeException("No existe stock del libro en la sucursal indicada"))
                .when(service).reducirStock(1L, 99L, 5);

        mockMvc.perform(post("/api/stock-libros/reducir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No existe stock del libro en la sucursal indicada"));
    }

    @Test
    void testValidarStock() throws Exception {
        ValidarStockRequest request = new ValidarStockRequest();
        request.setIdSucursal(1L);
        ValidarStockRequest.ItemCantidad item = new ValidarStockRequest.ItemCantidad();
        item.setIdLibro(1L);
        item.setCantidad(10);
        request.setItems(List.of(item));
        when(service.validarStockSuficiente(any(ValidarStockRequest.class))).thenReturn(true);

        mockMvc.perform(post("/api/stock-libros/validar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
