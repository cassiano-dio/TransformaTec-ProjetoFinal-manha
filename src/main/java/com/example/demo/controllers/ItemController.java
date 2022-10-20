package com.example.demo.controllers;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.interfaces.CryptoPriceInterface;
import com.example.demo.interfaces.TodoInterface;
import com.example.demo.models.Item;
import com.example.demo.payload.response.CryptoPriceResponse;
import com.example.demo.payload.response.TodoResponse;
import com.example.demo.repositories.ItemRepository;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class ItemController{

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CryptoPriceInterface cryptoPriceInterface;

    @Autowired
    private TodoInterface todoInterface;

    // Criando um novo item
    @ApiOperation(value = "Registrando novo item", consumes = "application/json", produces = "application/json")
    //@PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/items")
    public ResponseEntity<Item> createItem(@RequestBody Item item, HttpServletRequest httpRequest){

        //Obtendo o nome do usuário do Token JWT do Header
        Principal principal = httpRequest.getUserPrincipal();

        System.out.println("*********************");
        System.out.println(principal.getName());
        System.out.println("*********************");

        item.setUsername(principal.getName());

        TodoResponse todoResponse = todoInterface.getTodoById(item.getTodoId());

        CryptoPriceResponse cryptoPriceResponse = cryptoPriceInterface.getPriceBySymbol(item.getSymbol());

        item.setDescription(todoResponse.getTitle());
        item.setStatus(todoResponse.isCompleted());

        item.setPrice(cryptoPriceResponse.getPrice());

        Item _item = itemRepository.save(item);

        return new ResponseEntity<Item>(_item, HttpStatus.OK);
    };

    // Buscando um item
    @ApiOperation(value = "Buscando item por Id", consumes = "application/json", produces = "application/json")
    @GetMapping("/items/{id}")
    public ResponseEntity<Item> getById(@PathVariable("id") long id){

        Item item = itemRepository.findById(id);
    
        return new ResponseEntity<Item>(item, HttpStatus.OK);
    }
    
    //Listando todos os itens
    @ApiOperation(value = "Listando todos os itens", consumes = "application/json", produces = "application/json")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/items")
    public ResponseEntity<List<Item>> listItems(){

        List<Item> items = itemRepository.findAll();

        if(items.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<Item>>(items, HttpStatus.OK);
    }

    // Pesquisando items por username com Query Parameters
    @ApiOperation(value = "Listando itens por username - ADMIN", consumes = "application/json", produces = "application/json")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/items/user")// .../admin/items/user?username=cassiano
    public ResponseEntity<List<Item>> adminListItemsByUserId(@RequestParam String username){

        List<Item> items = itemRepository.findByUsername(username); 

        if(items.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<Item>>(items, HttpStatus.OK);
    }

    // Pesquisando items por usuário logado
    @ApiOperation(value = "Listando itens por username de usuário logado", consumes = "application/json", produces = "application/json")
    //@PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/items/user")
    public ResponseEntity<List<Item>> listItemsByUserId(HttpServletRequest httpRequest){

        // Obtendo username do Token
        Principal principal = httpRequest.getUserPrincipal();

        List<Item> items = itemRepository.findByUsername(principal.getName()); 

        if(items.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<Item>>(items, HttpStatus.OK);
    }

    // Pesquisando items por status com Query Parameters
    @ApiOperation(value = "Listando items por status", consumes = "application/json", produces = "application/json")
    @GetMapping("/items/status")// .../items/status?status=true/false
    //@PreAuthorize("hasRole('ROLE_USER') OR hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Item>> listItemsByStatus(@RequestParam boolean status){

        List<Item> items = itemRepository.findByStatus(status);

        if(items.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<Item>>(items, HttpStatus.OK);
    }

    // Removendo um item
    @ApiOperation(value = "Excluindo um item", consumes = "application/json", produces = "application/json")
    @DeleteMapping("/items/{id}")
    //@PreAuthorize("hasRole('ROLE_USER') OR hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteItem(@PathVariable("id") long id){

        itemRepository.deleteById(id);
    
        return new ResponseEntity<Object>("Item excluído com sucesso", HttpStatus.OK);

    }

    // Atualizando um item
    @ApiOperation(value = "Atualizando informações de um item", consumes = "application/json", produces = "application/json")
    @PutMapping("/items/{id}")
    //@PreAuthorize("hasRole('ROLE_USER') OR hasRole('ROLE_ADMIN')")
    public ResponseEntity<Item> updateItem(@PathVariable("id") long id, @RequestBody Item item){

        Item _item = itemRepository.findById(id);

        if(_item == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        _item.setUsername(item.getUsername());
        _item.setName(item.getName());
        _item.setPrice(item.getPrice());
        _item.setDescription(item.getDescription());
        _item.setStatus(item.getStatus());

        itemRepository.save(_item);

        return new ResponseEntity<Item>(_item, HttpStatus.OK);
    }
}
