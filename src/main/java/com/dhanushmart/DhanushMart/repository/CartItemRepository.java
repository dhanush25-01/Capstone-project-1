package com.dhanushmart.DhanushMart.repository;
import com.dhanushmart.DhanushMart.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface CartItemRepository extends JpaRepository<CartItem,Long>{
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
}