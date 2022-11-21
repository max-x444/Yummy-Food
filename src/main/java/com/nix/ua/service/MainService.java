package com.nix.ua.service;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public abstract class MainService<T> {
    private final CrudRepository<T, String> crudRepository;

    protected MainService(CrudRepository<T, String> crudRepository) {
        this.crudRepository = crudRepository;
    }

    public Optional<T> findById(String id) {
        return crudRepository.findById(id);
    }

    public Iterable<T> getAll() {
        return crudRepository.findAll();
    }

    public abstract T create(T item);

    public abstract T update(T item);

    public void delete(String id) {
        if (crudRepository.existsById(id)) {
            crudRepository.deleteById(id);
        }
    }

    public void deleteAll(Iterable<T> items) {
        crudRepository.deleteAll(items);
    }
}