package com.restaurant.algorithms;

import com.restaurant.models.Order;

import java.util.List;

/**
 * Common contract for all scheduling algorithms used to optimize
 * the restaurant order queue.
 */
public interface SchedulingAlgorithm {

    /**
     * Human‑readable name of the algorithm, used in logs / UI.
     */
    String getName();

    /**
     * Returns a new list of orders representing the optimized execution order.
     * The input list must not be modified.
     */
    List<Order> schedule(List<Order> orders);
}
