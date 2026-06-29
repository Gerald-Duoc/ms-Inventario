package ms.inventario.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ms.inventario.model.StockLibroDTO;
import ms.inventario.model.ValidarStockRequest;
import ms.inventario.service.StockLibroService;

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
    @PostMapping("/añadir")
    public ResponseEntity<String> añadirStock(@RequestBody StockLibroDTO dto) {
        try {
            service.añadirStock(dto.getIdLibro(),dto.getIdSucursal(),dto.getStock());
        return ResponseEntity.ok("Stock añadido correctamente");
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    }
    @PostMapping("/reducir")
    public ResponseEntity<String> reducirStock(@RequestBody StockLibroDTO dto) {
        try {
            service.reducirStock(dto.getIdLibro(),dto.getIdSucursal(),dto.getStock());
            return ResponseEntity.ok("Stock reducido correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/validar")
    public ResponseEntity<Boolean> validarStock(@RequestBody ValidarStockRequest request) {
        boolean suficiente = service.validarStockSuficiente(request);
        return ResponseEntity.ok(suficiente);
    }
}
