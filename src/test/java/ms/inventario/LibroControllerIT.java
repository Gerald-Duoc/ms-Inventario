package ms.inventario;

import com.fasterxml.jackson.databind.ObjectMapper;
import ms.inventario.model.LibroDTO;
import ms.inventario.model.Categoria;
import ms.inventario.repository.LibroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LibroControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LibroRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDb() {
        repository.deleteAll();
    }

    @Test
    void testCrearYListar() throws Exception {
        LibroDTO dto = new LibroDTO();
        dto.setNombre("Test");
        dto.setDescripcion("Desc");
        dto.setEditorial("Edit");
        dto.setAutor("Autor");
        dto.setPrecioCompra(100.0);
        dto.setPrecioVenta(200.0);
        dto.setCategoria(Categoria.FICCION);

        mockMvc.perform(post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre", org.hamcrest.Matchers.is("Test")));
    }
}