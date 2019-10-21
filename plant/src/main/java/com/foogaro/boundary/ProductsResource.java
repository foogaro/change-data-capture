package com.foogaro.boundary;

import org.jakartaeeprojects.coffee.orders.control.OrderInMemory;
import org.jakartaeeprojects.coffee.orders.control.OrderNotificationService;
import org.jakartaeeprojects.coffee.orders.control.OrderRepository;
import org.jakartaeeprojects.coffee.orders.entity.CoffeeOrder;
import org.jakartaeeprojects.coffee.orders.entity.CoffeeRequest;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.event.Event;
import javax.enterprise.event.NotificationOptions;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrdersResource {

    @Inject
    private Logger logger;
    @Inject
    private Event<CoffeeOrder> coffeeOrderEvent;
    @Resource
    private ManagedExecutorService managedExecutorService;
    @Inject
    private OrderNotificationService notificationService;

    @Inject
    @OrderInMemory
    private OrderRepository orderRepository;

    @GET
    public Response getOrders() {
        Collection<CoffeeOrder> orders = this.orderRepository.getAll();
        return Response.ok(orders).build();
    }

    @GET
    @Path("{id}")
    public Response getOrder(@PathParam("id") Long id) {
        logger.log(Level.INFO, "Looking for order {0}", id);

        Optional<CoffeeOrder> targetOrder = this.orderRepository.getById(id);

        if (!targetOrder.isPresent()) {
            throw new NotFoundException("Missing order " + id);
        }

        return Response.ok(targetOrder.get()).build();
    }

    @POST
    public Response newOrder(@Valid CoffeeRequest request,
                             @Context UriInfo uriInfo) {
        final CoffeeOrder order = orderRepository.create(request);
        logger.log(Level.INFO, "Processing order {0}", order.getId());

        CompletionStage<CoffeeOrder> orderCompletionStage = coffeeOrderEvent.fireAsync(order,
                NotificationOptions.ofExecutor(managedExecutorService));
        orderCompletionStage.thenAccept(notificationService::notifyOrderStatus);

        JsonObject orderJson = Json.createObjectBuilder()
                .add("order", order.getId())
                .build();

        URI orderUri = uriInfo.getAbsolutePathBuilder()
                .path(order.getId().toString())
                .build();

        return Response.accepted(orderJson)
                .location(orderUri)
                .build();
    }

}
