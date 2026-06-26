package ms.inventario.repository;

import ms.inventario.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    // Futuro: findByOrden(Long idOrden) para liberar reservas al cancelar una orden
}