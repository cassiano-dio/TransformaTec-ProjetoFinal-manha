package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.demo.models.Item;

public interface ItemRepository extends JpaRepository<Item, Long>{
    
    // Listando itens por status (named query)
    List<Item> findByStatus(boolean status);

    //Pesquisando um item por id (named query)
    Item findById(long id);

    // Listando itens por id de usuário (query convencional)
    @Query(value = "SELECT * FROM items i WHERE i.username = :username", nativeQuery = true)
    List<Item> findByUsername(@Param("username") String username);

    // Listando todos os itens (named query)
    List<Item> findAll();

    // Excluindo um item (named query)
    void deleteById(long id);

}
