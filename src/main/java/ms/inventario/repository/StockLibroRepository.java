package ms.inventario.repository;

import ms.inventario.model.StockLibro;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockLibroRepository extends JpaRepository<StockLibro, Long> {
    Optional<StockLibro> findByIdLibroAndIdSucursal(Long idLibro, Long idSucursal);
}

