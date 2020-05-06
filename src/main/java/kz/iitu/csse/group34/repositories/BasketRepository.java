package kz.iitu.csse.group34.repositories;

import kz.iitu.csse.group34.entities.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    List<Basket> findAllByDeletedAtNullOrderByCreatedAtDesc();
    List<Basket> findAll();
    boolean existsById(Long id);
}
