package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.Billing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
@Repository
public interface BillingRepository extends CrudRepository<Billing, String> {
}
