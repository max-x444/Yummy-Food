package com.nix.ua.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

public interface CrudController<T> {
    @GetMapping("/find-by-id/{id}")
    T findById(@PathVariable("id") String id);

    @GetMapping("/get-all")
    Iterable<T> getAll();

    @PostMapping("/create")
    T create(@RequestBody @Valid T item);

    @PutMapping("/update")
    T update(@RequestBody @Valid T item);

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable String id);
}