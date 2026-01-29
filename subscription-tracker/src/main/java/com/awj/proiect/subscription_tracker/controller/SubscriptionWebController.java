/** Controller pentru interfața web Thymeleaf
 * @author Nedelcu Bianca-Nicoleta
 * @version 2 Decembrie 2025
 */
package com.awj.proiect.subscription_tracker.controller;

import com.awj.proiect.subscription_tracker.model.Subscription;
import com.awj.proiect.subscription_tracker.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
public class SubscriptionWebController {

    private final SubscriptionService service;

    @Autowired
    public SubscriptionWebController(SubscriptionService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String viewHomePage(Model model, @RequestParam(value = "keyword", required = false) String keyword, @RequestParam(value = "sort", required = false) String sort, @RequestParam(value = "tab", defaultValue = "all") String tab) {
        long startTime = System.nanoTime();

        List<Subscription> resultList = service.getFilteredSubscriptions(tab, keyword, sort);
        model.addAttribute("listSubscriptions", resultList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("activeTab", tab);
        model.addAttribute("totalMonthlyCost", service.calculateTotalMonthlyCost());
        model.addAttribute("totalYearlyCost", service.calculateTotalYearlyCost());
        model.addAttribute("mostExpensive", service.getMostExpensiveSubscription());
        model.addAttribute("cheapest", service.getCheapestSubscription());
        model.addAttribute("chartData", service.getCostsByCategory());
        model.addAttribute("subscriptionCount", service.getTotalSubscriptionsCount());

        long durationMs = (System.nanoTime() - startTime) / 1_000_000;
        System.out.println(">>> Dashboard încărcat în: " + durationMs + " ms");

        return "index";
    }

    @GetMapping("/showNewSubscriptionForm")
    public String showNewSubscriptionForm(Model model) {
        Subscription subscription = new Subscription();
        model.addAttribute("subscription", subscription);
        return "new_subscription";
    }

    @PostMapping("/saveSubscription")
    public String saveSubscription(@Valid @ModelAttribute("subscription") Subscription subscription, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "new_subscription";
        try {
            service.saveSubscription(subscription);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("serviceName", "error.subscription", e.getMessage());
            return "new_subscription";
        }

        long startTime = System.nanoTime();
        service.saveSubscription(subscription);
        long durationMs = (System.nanoTime() - startTime) / 1_000_000;
        System.out.println(">>> Abonament salvat în: " + durationMs + " ms");

        return "redirect:/";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable Long id, Model model) {
        Optional<Subscription> subscription = service.getSubscriptionById(id);

        if (subscription.isPresent())
            model.addAttribute("subscription", subscription.get());
        else
            throw new IllegalArgumentException(">>> Abonament invalid cu ID: " + id);

        return "new_subscription";
    }

    @GetMapping("/deleteSubscription/{id}")
    public String deleteSubscription(@PathVariable Long id) {
        long startTime = System.nanoTime();
        service.deleteSubscription(id);
        long durationMs = (System.nanoTime() - startTime) / 1_000_000;
        System.out.println(">>> Abonament șters în: " + durationMs + " ms");

        return "redirect:/";
    }
}