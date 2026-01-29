/** Interfața Repository pentru accesul la datele Subscription
 * @author Nedelcu Bianca-Nicoleta
 * @version 2 Decembrie 2025
 */
package com.awj.proiect.subscription_tracker.repository;

import com.awj.proiect.subscription_tracker.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByIsActiveTrue();
    Subscription findByServiceNameIgnoreCaseAndIsActiveTrue(String serviceName);
}