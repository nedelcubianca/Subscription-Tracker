/** Clasa entitate pentru reprezentarea unui abonament
 * @author Nedelcu Bianca-Nicoleta
 * @version 2 Decembrie 2025
 */
package com.awj.proiect.subscription_tracker.model;

import jakarta.validation.constraints.Pattern;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Numele abonamentului este obligatoriu.")
    @Size(min = 2, max = 100, message = "Numele trebuie să aibă între 2 și 100 de caractere.")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "Numele poate conține doar litere, cifre și spații.")
    @Column(nullable = false)
    private String serviceName;

    @NotNull(message = "Prețul este obligatoriu.")
    @Positive(message = "Prețul trebuie să fie mai mare decât 0.")
    @Column(nullable = false)
    private BigDecimal price;

    @NotNull(message = "Moneda este obligatorie.")
    private String currency = "RON";

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Frecvența de plată este obligatorie.")
    private BillingCycle billingCycle;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Categoria este obligatorie.")
    private Category category;

    @NotNull(message = "Completează toate câmpurile pentru dată (zi/lună/an).")
    @PastOrPresent(message = "Data de început nu poate fi în viitor.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    private boolean isActive = true;

    public Subscription() {}

    public Subscription(String serviceName, BigDecimal price, BillingCycle billingCycle, Category category, LocalDate startDate) {
        this.serviceName = serviceName;
        this.price = price;
        this.billingCycle = billingCycle;
        this.category = category;
        this.startDate = startDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BillingCycle getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(BillingCycle billingCycle) {
        this.billingCycle = billingCycle;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDate getNextDueDate() {
        LocalDate today = LocalDate.now();
        LocalDate next = this.startDate;

        while (!next.isAfter(today)) {
            switch (this.billingCycle) {
                case WEEKLY:
                    next = next.plusWeeks(1);
                    break;
                case MONTHLY:
                    next = next.plusMonths(1);
                    break;
                case YEARLY:
                    next = next.plusYears(1);
                    break;
            }
        }
        return next;
    }

    public String getRelativeDueDate() {
        LocalDate today = LocalDate.now();
        LocalDate next = getNextDueDate();
        long days = ChronoUnit.DAYS.between(today, next);

        if (days == 0)
            return "Astăzi";
        if (days == 1)
            return "Mâine";
        if (days < 7)
            return "în " + days + " zile";
        if (days < 30)
            if (days / 7 == 1)
                return "într-o săptămână";
            else
                return "în " + (days / 7) + " săptămâni";
        if (days < 60)
            return "în 1 lună";

        return "în " + (days / 30) + " luni";
    }
}