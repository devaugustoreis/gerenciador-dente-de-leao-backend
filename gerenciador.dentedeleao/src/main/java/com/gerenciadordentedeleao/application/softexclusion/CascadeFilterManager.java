package com.gerenciadordentedeleao.application.softexclusion;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Component
public class CascadeFilterManager {

    @PersistenceContext
    private final EntityManager em;

    public CascadeFilterManager(EntityManager em) {
        this.em = em;
    }

    public void apply(String filterName) {
        var session = em.unwrap(Session.class);
        if (session.getEnabledFilter(filterName) == null) {
            session.enableFilter(filterName);
        }
    }


}
