package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.Billing;
import de.neoventus.persistence.repository.advanced.NVBillingRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dennis Thanner, Tim Heidelbach
 * @version 0.0.2 removed method findByBillingId
 *          0.0.1
 **/
@Repository
public interface BillingRepository extends CrudRepository<Billing, String>, NVBillingRepository {

}
