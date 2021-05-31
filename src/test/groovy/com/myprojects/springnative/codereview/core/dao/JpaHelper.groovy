package com.myprojects.springnative.codereview.core.dao


import javax.persistence.EntityManager

class JpaHelper {

    private final EntityManager entityManager

    JpaHelper(EntityManager entityManager) {
        this.entityManager = entityManager
    }

    public <T> T detach(T entity) {
        entityManager.clear()
        entityManager.detach(entity)
        return entity
    }

    public void clearContext() {
        entityManager.clear()
    }
}