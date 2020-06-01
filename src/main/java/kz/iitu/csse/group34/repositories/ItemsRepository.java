package kz.iitu.csse.group34.repositories;

import kz.iitu.csse.group34.entities.Categories;
import kz.iitu.csse.group34.entities.Items;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemsRepository extends JpaRepository<Items, Long> {

    List<Items> findAllByIdGreaterThan(Long id);
    List<Items> findAllByDeletedAtNullOrderByCreatedAtDesc();
    List<Items> findAllByDeletedAtNullOrderByPriceAsc();
    List<Items> findAllByDeletedAtNullOrderByPriceDesc();
    List<Items> findAllByCategoriesLike(Categories categories);
    Items findByDeletedAtNullAndId(Long id);
    int countAllByDeletedAtNull();
    List<Items> findAllByDeletedAtNull(Pageable pageable);
    boolean existsById(Long id);
}