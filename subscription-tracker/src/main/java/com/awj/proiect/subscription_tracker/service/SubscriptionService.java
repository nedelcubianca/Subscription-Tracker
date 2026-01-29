/** Clasa Service pentru logica de business a abonamentelor
 * @author Nedelcu Bianca-Nicoleta
 * @version 2 Decembrie 2025
 */
package com.awj.proiect.subscription_tracker.service;

import com.awj.proiect.subscription_tracker.model.Category;
import com.awj.proiect.subscription_tracker.model.Subscription;
import com.awj.proiect.subscription_tracker.model.BillingCycle;
import com.awj.proiect.subscription_tracker.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {
    private final SubscriptionRepository repository;

    @Autowired
    public SubscriptionService(SubscriptionRepository repository) {
        this.repository = repository;
    }

    public List<Subscription> getAllActiveSubscriptions() {
        return repository.findByIsActiveTrue();
    }

    public Optional<Subscription> getSubscriptionById(Long id) {
        return repository.findById(id);
    }

    public Subscription saveSubscription(Subscription subscription) {
        // Verifica daca exista deja un abonament cu acelasi nume
        Subscription existing = repository.findByServiceNameIgnoreCaseAndIsActiveTrue(subscription.getServiceName());

        if (subscription.getId() == null) {
            if (existing != null)
                throw new IllegalArgumentException("Există deja un abonament cu numele: " + subscription.getServiceName());
            subscription.setActive(true);
        } else {
            if (existing != null && !existing.getId().equals(subscription.getId()))
                throw new IllegalArgumentException("Există deja un abonament cu numele: " + subscription.getServiceName());
        }

        return repository.save(subscription);
    }

    public void deleteSubscription(Long id) {
        Optional<Subscription> sub = repository.findById(id);
        if (sub.isPresent()) {
            Subscription s = sub.get();
            s.setActive(false);
            repository.save(s);
        }
    }

    public BigDecimal calculateTotalMonthlyCost() {
        BigDecimal total = BigDecimal.ZERO;
        List<Subscription> subscriptions = getAllActiveSubscriptions();

        for (Subscription sub : subscriptions) {
            BigDecimal monthlyPrice;

            if (sub.getBillingCycle() == BillingCycle.YEARLY)
                monthlyPrice = sub.getPrice().divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
            else if (sub.getBillingCycle() == BillingCycle.WEEKLY)
                monthlyPrice = sub.getPrice().multiply(BigDecimal.valueOf(4.33)).setScale(2, RoundingMode.HALF_UP);
            else
                monthlyPrice = sub.getPrice();

            total = total.add(monthlyPrice);
        }
        return total;
    }

    public BigDecimal calculateTotalYearlyCost() {
        BigDecimal total = BigDecimal.ZERO;
        List<Subscription> subscriptions = getAllActiveSubscriptions();

        for (Subscription sub : subscriptions) {
            BigDecimal yearlyPrice;

            if (sub.getBillingCycle() == BillingCycle.MONTHLY)
                yearlyPrice = sub.getPrice().multiply(BigDecimal.valueOf(12));
            else if (sub.getBillingCycle() == BillingCycle.WEEKLY)
                yearlyPrice = sub.getPrice().multiply(BigDecimal.valueOf(52));
            else
                yearlyPrice = sub.getPrice();

            total = total.add(yearlyPrice);
        }
        return total;
    }

    public Subscription getMostExpensiveSubscription() {
        List<Subscription> subscriptions = getAllActiveSubscriptions();

        if (subscriptions.isEmpty())
            return null;

        Subscription mostExpensive = subscriptions.get(0);
        for (Subscription sub : subscriptions)
            if (sub.getPrice().compareTo(mostExpensive.getPrice()) > 0)
                mostExpensive = sub;

        return mostExpensive;
    }

    public Subscription getCheapestSubscription() {
        List<Subscription> subscriptions = getAllActiveSubscriptions();

        if (subscriptions.isEmpty())
            return null;

        Subscription cheapest = subscriptions.get(0);
        for (Subscription sub : subscriptions)
            if (sub.getPrice().compareTo(cheapest.getPrice()) < 0)
                cheapest = sub;

        return cheapest;
    }

    public Map<Category, BigDecimal> getCostsByCategory() {
        Map<Category, BigDecimal> costs = new HashMap<>();
        List<Subscription> subscriptions = getAllActiveSubscriptions();

        for (Subscription sub : subscriptions) {
            Category cat = sub.getCategory();
            BigDecimal currentTotal = costs.getOrDefault(cat, BigDecimal.ZERO);
            costs.put(cat, currentTotal.add(sub.getPrice()));
        }
        return costs;
    }

    public Map<Category, Long> countByCategory() {
        Map<Category, Long> counts = new HashMap<>();
        List<Subscription> subscriptions = getAllActiveSubscriptions();

        for (Subscription sub : subscriptions) {
            Category cat = sub.getCategory();
            Long currentCount = counts.getOrDefault(cat, 0L);
            counts.put(cat, currentCount + 1);
        }
        return counts;
    }

    public long getTotalSubscriptionsCount() {
        return getAllActiveSubscriptions().size();
    }

    public List<Subscription> getUpcomingSubscriptions() {
        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusDays(7);
        List<Subscription> upcoming = new ArrayList<>();

        for (Subscription sub : getAllActiveSubscriptions()) {
            LocalDate due = sub.getNextDueDate();
            if (!due.isBefore(today) && !due.isAfter(limit))
                upcoming.add(sub);
        }

        upcoming.sort(Comparator.comparing(Subscription::getNextDueDate));
        return upcoming;
    }

    public List<Subscription> getFilteredSubscriptions(String tab, String keyword, String sort) {
        List<Subscription> list;

        if ("upcoming".equals(tab))
            list = getUpcomingSubscriptions();
        else
            list = new ArrayList<>(getAllActiveSubscriptions());

        if (keyword != null && !keyword.trim().isEmpty()) {
            String lowerKeyword = keyword.toLowerCase().trim();
            List<Subscription> filtered = new ArrayList<>();

            for (Subscription s : list) {
                boolean matchesName = s.getServiceName().toLowerCase().contains(lowerKeyword);
                boolean matchesCategory = s.getCategory().name().toLowerCase().contains(lowerKeyword);

                if (matchesName || matchesCategory)
                    filtered.add(s);
            }
            list = filtered;
        }

        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "cost_desc":
                    list.sort(Comparator.comparing(Subscription::getPrice).reversed());
                    break;
                case "cost_asc":
                    list.sort(Comparator.comparing(Subscription::getPrice));
                    break;
                case "date":
                    list.sort(Comparator.comparing(Subscription::getNextDueDate));
                    break;
                case "name":
                    list.sort(Comparator.comparing(Subscription::getServiceName, String.CASE_INSENSITIVE_ORDER));
                    break;
            }
        }
        return list;
    }

    public List<Subscription> getByCategory(Category category) {
        List<Subscription> result = new ArrayList<>();

        for (Subscription s : getAllActiveSubscriptions()) {
            if (s.getCategory() == category)
                result.add(s);
        }
        return result;
    }
}