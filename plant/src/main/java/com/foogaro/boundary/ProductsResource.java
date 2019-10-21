package com.foogaro.boundary;

import com.foogaro.entity.Product;
import com.foogaro.entity.Status;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductsResource {

    @Inject
    private Logger logger;
    @Inject
    private EntityManager em;

    @POST
    public Response create(Product product, @Context UriInfo uriInfo) {
        try {
            em.persist(product);
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(Long.toString(product.getId()));
            return Response.created(builder.build()).build();
        } catch (Throwable t) {
            return Response.serverError().build();
        }
    }

    @GET
    public Response read() {
        Product product = em.find(Product.class, null);
        return Response.ok(product).build();
    }

    @PATCH
    @PUT
    public Response update(Product product, @Context UriInfo uriInfo) {
        Product current = (Product)getById(product.getId()).getEntity();
        em.getTransaction().begin();
        current.setCode(product.getCode());
        current.setDescription(product.getDescription());
        current.setInstock(product.getInstock());
        current.setStatus(product.getStatus());
        current.setPrice(product.getPrice());
        current.setCheckin(product.getCheckin());
        em.getTransaction().commit();
        return Response.ok(current).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        Product product = (Product)getById(id).getEntity();
        em.getTransaction().begin();
        product.setStatus(Status.DELETED);
        em.getTransaction().commit();
        return Response.ok().build();
    }

    @GET
    @Path("{id}")
    public Response getById(@PathParam("id") Long id) {
        logger.log(Level.INFO, "Looking for product {0}", id);

        Optional<Product> product = Optional.ofNullable(em.find(Product.class, id));
        if (product.isPresent()) {
            return Response.ok(product.get()).build();
        }

        return Response.noContent().build();
    }

}
