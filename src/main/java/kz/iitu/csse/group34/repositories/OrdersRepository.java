package kz.iitu.csse.group34.repositories;

import kz.iitu.csse.group34.entities.Items;
import kz.iitu.csse.group34.entities.Orders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    List<Orders> findAllByIdGreaterThan(Long id);
    List<Orders> findAllByDeletedAtNullOrderByCreatedAtDesc();
    Orders findByDeletedAtNullAndId(Long id);
    int countAllByDeletedAtNull();
    List<Orders> findAllByDeletedAtNull(Pageable pageable);
    boolean existsById(Long id);

}
