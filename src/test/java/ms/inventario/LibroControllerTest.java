package ms.inventario;

import com.fasterxml.jackson.databind.ObjectMapper;

import ms.inventario.controller.LibroController;
import ms.inventario.model.LibroDTO;
import ms.inventario.service.LibroService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibroController.class)
public class LibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LibroService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListar() throws Exception {
        LibroDTO dto = new LibroDTO();
        dto.setId(1L);
        dto.setNombre("Cien años");
        Mockito.when(service.listar()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Cien años")));
    }

    @Test
    void testBuscar() throws Exception {
        LibroDTO dto = new LibroDTO();
        dto.setId(1L);
        dto.setNombre("Cien años");
        Mockito.when(service.buscar(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/libros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Cien años")));
    }

    @Test
    void testCrear() throws Exception {
        LibroDTO entrada = new LibroDTO();
        entrada.setNombre("Nuevo");
        LibroDTO salida = new LibroDTO();
        salida.setId(1L);
        salida.setNombre("Nuevo");
        Mockito.when(service.guardar(any(LibroDTO.class))).thenReturn(salida);

        mockMvc.perform(post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void testActualizar() throws Exception {
        LibroDTO entrada = new LibroDTO();
        entrada.setNombre("Modificado");
        LibroDTO salida = new LibroDTO();
        salida.setId(1L);
        salida.setNombre("Modificado");
        Mockito.when(service.actualizar(eq(1L), any(LibroDTO.class))).thenReturn(salida);

        mockMvc.perform(put("/api/libros/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Modificado")));
    }

    @Test
    void testEliminar() throws Exception {
        mockMvc.perform(delete("/api/libros/1"))
                .andExpect(status().isNoContent());
    }
}