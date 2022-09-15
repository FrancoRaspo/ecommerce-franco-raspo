package com.ecommerce.francoraspo.controllers;

import com.ecommerce.francoraspo.models.Animal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EcommerceController {
    private static final String template = "Hola, %s!";

    @GetMapping("/hola")
    public String hola() {
        System.out.println("Hola mundo");
        return "Hola mundo";
    }

    @GetMapping("/animales")
    public List<Animal> animales() {

        return List.of(
                new Animal("Bruce", 10, true),
                new Animal("Cleo", 2, false)
                );
    }


}
