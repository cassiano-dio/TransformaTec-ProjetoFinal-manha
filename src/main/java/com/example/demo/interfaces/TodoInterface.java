package com.example.demo.interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.payload.response.TodoResponse;

// Declarando a URL da API externa
@FeignClient(url = "https://jsonplaceholder.typicode.com/todos", name="TODOS")
public interface TodoInterface {
    
    // Consumindo endpoint da API externa
    // Retornando um objeto com a resposta da API consumida
    @GetMapping("/{id}")
    TodoResponse getTodoById(@PathVariable("id") long id);

}
