package ms.inventario.repository;

import ms.inventario.model.StockLibro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockLibroRepository extends JpaRepository<StockLibro, Long> {
    // Futuro: findBySucursal(Long idSucursal) para consultar stock en una sucursal
}
