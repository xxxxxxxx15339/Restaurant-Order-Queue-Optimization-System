package com.restaurant.algorithms;

import com.restaurant.models.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SPTschedulerTest {

    @Test
    void sortsByEstimatedPrepTimeAscending() {
        List<Order> orders = new ArrayList<>();
        orders.add(buildOrder(1, 3, 15));
        orders.add(buildOrder(2, 2, 5));
        orders.add(buildOrder(3, 1, 10));

        SchedulingAlgorithm algo = new SPTscheduler();
        List<Order> result = algo.schedule(orders);

        assertEquals(List.of(2, 3, 1),
                result.stream().map(Order::getOrderId).toList());
    }

    @Test
    void keepsOriginalListUnmodified() {
        List<Order> orders = new ArrayList<>();
        orders.add(buildOrder(1, 3, 15));
        orders.add(buildOrder(2, 2, 5));

        SchedulingAlgorithm algo = new SPTscheduler();
        algo.schedule(orders);

        // Original order should remain [1,2]
        assertEquals(List.of(1, 2),
                orders.stream().map(Order::getOrderId).toList());
    }

    @Test
    void ordersWithNullEstimatedTimeGoLast() {
        List<Order> orders = new ArrayList<>();
        orders.add(buildOrder(1, 1, null));  // no estimate
        orders.add(buildOrder(2, 1, 10));

        SchedulingAlgorithm algo = new SPTscheduler();
        List<Order> result = algo.schedule(orders);

        assertEquals(List.of(2, 1),
                result.stream().map(Order::getOrderId).toList());
    }

    private Order buildOrder(int id, int priority, Integer estTime) {
        Order o = new Order();
        o.setOrderId(id);
        o.setPriority(priority);
        o.setEstimatedPrepTime(estTime);
        return o;
    }
}


