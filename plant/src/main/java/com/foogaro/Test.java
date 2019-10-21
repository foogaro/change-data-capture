package com.foogaro;

import com.foogaro.entity.Product;
import com.foogaro.entity.Status;
import com.github.javafaker.Faker;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;

public class Test {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("shop");
        EntityManager em = emf.createEntityManager();
        Faker faker = new Faker();

        em.getTransaction().begin();
        Product product = new Product();
        product.setCheckin(faker.date().between(new Date(1), new Date()));
        product.setCode(faker.code().asin());
        product.setDescription(faker.commerce().productName());
        product.setInstock(faker.number().randomDigit());
        product.setPrice(faker.number().randomDouble(2,0l,faker.number().randomNumber()));
        product.setStatus(product.getInstock() > 0 ? Status.AVAILABLE : Status.NOT_AVAILABLE);
        em.persist(product);
        em.getTransaction().commit();
        System.out.println("Product: " + product);

        Product find = em.find(Product.class,product.getId());
        System.out.println("Find Product: " + find);

        em.getTransaction().begin();
        find.setInstock(0);
        find.setStatus(Status.NOT_AVAILABLE);
        em.getTransaction().commit();
        System.out.println("Find Product committed: " + find);

        em.getTransaction().begin();
        em.remove(find);
        em.getTransaction().commit();
        System.out.println("Find Product removed: " + find);

        find = em.find(Product.class,product.getId());
        System.out.println("Not found: " + find);

    }
}
