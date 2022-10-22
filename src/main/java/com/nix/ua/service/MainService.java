package com.nix.ua.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public abstract class MainService<T> {
    private final CrudRepository<T, String> crudRepository;

    @Autowired
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
}