package kz.iitu.csse.group34.repositories;

import kz.iitu.csse.group34.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    List<Categories> findAllByIdGreaterThan(Long id);


}