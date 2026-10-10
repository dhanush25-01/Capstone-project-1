package com.dhanushmart.DhanushMart.repository;

import com.dhanushmart.DhanushMart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findBySeller_IdOrderByIdDesc(Long sellerId);
    long countBySeller_Id(Long sellerId);

    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(COALESCE(p.description,'')) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(COALESCE(p.color,'')) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(COALESCE(p.size,'')) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Product> findByAiSearchTerm(@Param("term") String term);
}
