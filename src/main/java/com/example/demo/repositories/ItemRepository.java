package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.demo.models.Item;

public interface ItemRepository extends JpaRepository<Item, Long>{
    
    
    List<Item> findByStatus(Boolean status);

    Item findById(long id);

    @Query(value = "SELECT * FROM items i WHERE i.user_id = :u_id", nativeQuery = true)
    List<Item> findByUser(@Param("u_id") long id);

    List<Item> findAll();

}
