package com.example.ordermanagement.Repository;

import com.example.ordermanagement.Dtos.CartItemResponseDTO;
import com.example.ordermanagement.Entity.LineItem;
import com.example.ordermanagement.Entity.LineItemKey;
import com.example.ordermanagement.Entity.Order;
import com.example.ordermanagement.Enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LineItemRepository extends JpaRepository<LineItem , LineItemKey> {

    @Query("select l.lineItemKey.orderID, l.lineItemKey.productID ,l.quantity ,p.name , p.category,price.price from LineItem l inner join l.order o  inner join l.product p inner join p.prices price where o.id = :orderID and o.orderStatus= :status and price.last=true")
    Page<Object[]> getCartInfoByOrderAndOrderStatus(@Param("orderID") Long orderID
            , @Param("status") OrderStatus orderStatus , Pageable pageable);


    @Query("select l.quantity ,price.price from LineItem l inner join l.order o  inner join l.product p inner join p.prices price where o.id = :orderID and o.orderStatus= :status and price.last=true")
    List<Object[]> getQuantityAndPriceLineItemOfOrder(@Param("orderID") Long orderID
            , @Param("status") OrderStatus orderStatus);
}
