package ms.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ms.inventario.model.Libro;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
}
