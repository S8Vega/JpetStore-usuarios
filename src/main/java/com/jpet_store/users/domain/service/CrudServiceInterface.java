package com.jpet_store.users.domain.service;

import java.util.List;

public interface CrudServiceInterface<Entity, Id> {
    public Entity findById(Id id);

    public List<Entity> findAll();

    public Entity save(Entity entity);

    public void deleteById(Id id);
}
