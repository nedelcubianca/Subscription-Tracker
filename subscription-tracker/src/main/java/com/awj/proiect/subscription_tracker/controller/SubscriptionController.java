/** RestController pentru gestionarea cererilor HTTP (API REST)
 * @author Nedelcu Bianca-Nicoleta
 * @version 2 Decembrie 2025
 */
package com.awj.proiect.subscription_tracker.controller;

import com.awj.proiect.subscription_tracker.model.Category;
import com.awj.proiect.subscription_tracker.model.Subscription;
import com.awj.proiect.subscription_tracker.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService service;

    @Autowired
    public SubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        List<Subscription> subscriptions = service.getAllActiveSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getSubscriptionById(@PathVariable Long id) {
        Optional<Subscription> subscription = service.getSubscriptionById(id);

        if (subscription.isPresent())
            return ResponseEntity.ok(subscription.get());
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createSubscription(@Valid @RequestBody Subscription subscription) {
        try {
            Subscription saved = service.saveSubscription(subscription);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubscription(@PathVariable Long id, @Valid @RequestBody Subscription subscription) {
        Optional<Subscription> existing = service.getSubscriptionById(id);

        if (existing.isPresent()) {
            subscription.setId(id);
            try {
                Subscription saved = service.saveSubscription(subscription);
                return ResponseEntity.ok(saved);
            } catch (IllegalArgumentException e){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        }else{
            return ResponseEntity.notFound().build();
            }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        service.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/total-monthly")
    public ResponseEntity<BigDecimal> getTotalMonthlyCost() {
        BigDecimal total = service.calculateTotalMonthlyCost();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/stats/total-yearly")
    public ResponseEntity<BigDecimal> getTotalYearlyCost() {
        BigDecimal total = service.calculateTotalYearlyCost();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/stats/by-category")
    public ResponseEntity<Map<Category, BigDecimal>> getCostsByCategory() {
        Map<Category, BigDecimal> costs = service.getCostsByCategory();
        return ResponseEntity.ok(costs);
    }

    @GetMapping("/stats/count-by-category")
    public ResponseEntity<Map<Category, Long>> getCountByCategory() {
        Map<Category, Long> counts = service.countByCategory();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/stats/most-expensive")
    public ResponseEntity<Subscription> getMostExpensive() {
        Subscription expensive = service.getMostExpensiveSubscription();

        if (expensive != null)
            return ResponseEntity.ok(expensive);
        else
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/cheapest")
    public ResponseEntity<Subscription> getCheapest() {
        Subscription cheapest = service.getCheapestSubscription();

        if (cheapest != null)
            return ResponseEntity.ok(cheapest);
        else
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/count")
    public ResponseEntity<Long> getTotalCount() {
        long count = service.getTotalSubscriptionsCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Subscription>> getUpcoming() {
        List<Subscription> upcoming = service.getUpcomingSubscriptions();
        return ResponseEntity.ok(upcoming);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Subscription>> getByCategory(@PathVariable Category category) {
        List<Subscription> subscriptions = service.getByCategory(category);
        return ResponseEntity.ok(subscriptions);
    }
}