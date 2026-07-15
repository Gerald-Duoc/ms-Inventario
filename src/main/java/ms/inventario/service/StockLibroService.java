package ms.inventario.service;

import lombok.RequiredArgsConstructor;
import ms.inventario.model.StockLibro;
import ms.inventario.model.StockLibroDTO;
import ms.inventario.model.ValidarStockRequest;
import ms.inventario.repository.StockLibroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockLibroService {

    private final StockLibroRepository repository;

    private StockLibroDTO toDTO(StockLibro s) {
        StockLibroDTO dto = new StockLibroDTO();
        dto.setId(s.getId());
        dto.setIdLibro(s.getIdLibro());
        dto.setIdSucursal(s.getIdSucursal());
        dto.setStockMinimo(s.getStockMinimo());
        dto.setStockMaximo(s.getStockMaximo());
        dto.setStock(s.getStock());
        return dto;
    }

    private StockLibro toEntity(StockLibroDTO dto) {
        StockLibro s = new StockLibro();
        s.setId(dto.getId());
        s.setIdLibro(dto.getIdLibro());
        s.setIdSucursal(dto.getIdSucursal());
        s.setStockMinimo(dto.getStockMinimo());
        s.setStockMaximo(dto.getStockMaximo());
        s.setStock(dto.getStock());
        return s;
    }

    public List<StockLibroDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<StockLibroDTO> buscar(Long id) {
        return repository.findById(id).map(this::toDTO);
    }

    public StockLibroDTO guardar(StockLibroDTO dto) {
        StockLibro s = toEntity(dto);
        s.setId(null);
        return toDTO(repository.save(s));
    }

    public StockLibroDTO actualizar(Long id, StockLibroDTO dto) {
        StockLibro existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("StockLibro no encontrado"));
        existente.setIdLibro(dto.getIdLibro());
        existente.setIdSucursal(dto.getIdSucursal());
        existente.setStockMinimo(dto.getStockMinimo());
        existente.setStockMaximo(dto.getStockMaximo());
        existente.setStock(dto.getStock());

        // ---------- FUTURA CONEXIÓN CON MONITOREOGE-MS (US-INV-04) ----------
        // Si existente.getStock() < existente.getStockMinimo()
        // se debe enviar una alerta a monitoreoge-ms (ej. vía RestTemplate o RabbitMQ).

        return toDTO(repository.save(existente));
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public void añadirStock(Long idLibro, Long idSucursal, int cantidad) {
        Optional<StockLibro> existente = repository.findAll().stream()
                .filter(s -> s.getIdLibro().equals(idLibro) && s.getIdSucursal().equals(idSucursal))
                .findFirst();

        if (existente.isPresent()) {
            StockLibro s = existente.get();
            s.setStock(s.getStock() + cantidad);
            repository.save(s);
        } else {
            StockLibro nuevo = new StockLibro();
            nuevo.setIdLibro(idLibro);
            nuevo.setIdSucursal(idSucursal);
            nuevo.setStock(cantidad);
            nuevo.setStockMinimo(0);
            nuevo.setStockMaximo(100);
            repository.save(nuevo);
        }
    }

    public void reducirStock(Long idLibro, Long idSucursal, int cantidad) {
        StockLibro stock = repository.findAll().stream()
                .filter(s -> s.getIdLibro().equals(idLibro) && s.getIdSucursal().equals(idSucursal))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No existe stock del libro en la sucursal indicada"));

        if (stock.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + stock.getStock());
        }

        stock.setStock(stock.getStock() - cantidad);
        repository.save(stock);

        // ---------- FUTURA CONEXIÓN CON MONITOREOGE‑MS ----------
        // Si después de reducir, stock.getStock() < stock.getStockMinimo(),
        // se debe enviar alerta a monitoreoge‑ms (cuando esté implementado).
    }

    public boolean validarStockSuficiente(ValidarStockRequest request) {
        System.out.println("[DEBUG] validarStock: idSucursal=" + request.getIdSucursal());
        for (ValidarStockRequest.ItemCantidad item : request.getItems()) {
            System.out.println("[DEBUG]   item: idLibro=" + item.getIdLibro() + ", cantidad=" + item.getCantidad());
            StockLibro stock = repository.findAll().stream()
                .filter(s -> s.getIdLibro().equals(item.getIdLibro()) && s.getIdSucursal().equals(request.getIdSucursal()))
                .findFirst().orElse(null);
            System.out.println("[DEBUG]   encontrado: " + (stock != null
                ? "stock=" + stock.getStock() + ", idLibro=" + stock.getIdLibro() + ", idSucursal=" + stock.getIdSucursal()
                : "null"));
            if (stock == null || stock.getStock() < item.getCantidad()) {
                return false;
            }
        }
        return true;
    }
    public Optional<StockLibroDTO> buscarPorLibroYSucursal(Long idLibro, Long idSucursal) {
        return repository.findByIdLibroAndIdSucursal(idLibro, idSucursal)
                .map(this::toDTO);
    }
}