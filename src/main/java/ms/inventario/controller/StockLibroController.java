package ms.inventario.controller;

import lombok.RequiredArgsConstructor;
import ms.inventario.model.StockLibroDTO;
import ms.inventario.service.StockLibroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-libros")
@RequiredArgsConstructor
public class StockLibroController {

    private final StockLibroService service;

    @GetMapping
    public ResponseEntity<List<StockLibroDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockLibroDTO> buscar(@PathVariable Long id) {
        return service.buscar(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StockLibroDTO> crear(@RequestBody StockLibroDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockLibroDTO> actualizar(@PathVariable Long id,
                                                    @RequestBody StockLibroDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
