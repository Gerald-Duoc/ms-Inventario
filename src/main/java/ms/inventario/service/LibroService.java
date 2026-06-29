package ms.inventario.service;

import lombok.RequiredArgsConstructor;
import ms.inventario.model.Libro;
import ms.inventario.model.LibroDTO;
import ms.inventario.repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibroService {

    private final LibroRepository repository;

    private LibroDTO toDTO(Libro l) {
        LibroDTO dto = new LibroDTO();
        dto.setId(l.getId());
        dto.setNombre(l.getNombre());
        dto.setDescripcion(l.getDescripcion());
        dto.setEditorial(l.getEditorial());
        dto.setAutor(l.getAutor());
        dto.setPrecioCompra(l.getPrecioCompra());
        dto.setPrecioVenta(l.getPrecioVenta());
        dto.setCategoria(l.getCategoria());
        dto.setFechaCreacion(l.getFechaCreacion());
        return dto;
    }

    private Libro toEntity(LibroDTO dto) {
        Libro l = new Libro();
        l.setId(dto.getId());
        l.setNombre(dto.getNombre());
        l.setDescripcion(dto.getDescripcion());
        l.setEditorial(dto.getEditorial());
        l.setAutor(dto.getAutor());
        l.setPrecioCompra(dto.getPrecioCompra());
        l.setPrecioVenta(dto.getPrecioVenta());
        l.setCategoria(dto.getCategoria());
        l.setFechaCreacion(dto.getFechaCreacion());
        return l;
    }

    public List<LibroDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<LibroDTO> buscar(Long id) {
        return repository.findById(id).map(this::toDTO);
    }

    public LibroDTO guardar(LibroDTO dto) {
        Libro l = toEntity(dto);
        l.setId(null);
        return toDTO(repository.save(l));
    }

    public LibroDTO actualizar(Long id, LibroDTO dto) {
        Libro existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        existente.setNombre(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());
        existente.setEditorial(dto.getEditorial());
        existente.setAutor(dto.getAutor());
        existente.setPrecioCompra(dto.getPrecioCompra());
        existente.setPrecioVenta(dto.getPrecioVenta());
        existente.setCategoria(dto.getCategoria());
        // fechaCreacion no se modifica
        return toDTO(repository.save(existente));
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
    public List<LibroDTO> buscarPorNombre(String nombre) {

        return repository.findByNombreContainingIgnoreCase(nombre)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }
    
}