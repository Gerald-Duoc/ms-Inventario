package ms.inventario.service;

import lombok.RequiredArgsConstructor;
import ms.inventario.model.Reserva;
import ms.inventario.model.ReservaDTO;
import ms.inventario.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository repository;

    private ReservaDTO toDTO(Reserva r) {
        ReservaDTO dto = new ReservaDTO();
        dto.setId(r.getId());
        dto.setIdLibro(r.getIdLibro());
        dto.setCantidad(r.getCantidad());
        dto.setIdOrden(r.getIdOrden());
        return dto;
    }

    private Reserva toEntity(ReservaDTO dto) {
        Reserva r = new Reserva();
        r.setId(dto.getId());
        r.setIdLibro(dto.getIdLibro());
        r.setCantidad(dto.getCantidad());
        r.setIdOrden(dto.getIdOrden());
        return r;
    }

    public List<ReservaDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<ReservaDTO> buscar(Long id) {
        return repository.findById(id).map(this::toDTO);
    }

    public ReservaDTO guardar(ReservaDTO dto) {
        // ---------- FUTURA CONEXIÓN CON STOCKLIBRO (US-INV-13) ----------
        // Antes de guardar, se debe validar que el StockLibro tenga stock suficiente
        // y luego restar la cantidad del stock. Por ahora solo se guarda la reserva.
        Reserva r = toEntity(dto);
        r.setId(null);
        return toDTO(repository.save(r));
    }

    public void eliminar(Long id) {
        // ---------- FUTURA CONEXIÓN CON STOCKLIBRO (US-INV-14) ----------
        // Al eliminar una reserva, se debe sumar la cantidad al StockLibro correspondiente.
        repository.deleteById(id);
    }
}