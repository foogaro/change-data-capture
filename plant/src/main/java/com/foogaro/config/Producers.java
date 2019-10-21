
package com.foogaro.config;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

public class Producers {

    @Produces
    public Logger logger(final InjectionPoint ip) {
        return getLogger(ip.getMember()
                           .getDeclaringClass()
                           .getName());
    }

    @Produces
    public EntityManager entityManager() {
        //throw new UnsupportedOperationException("Not implemented yet");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("shop");
        return emf.createEntityManager();
    }
}
