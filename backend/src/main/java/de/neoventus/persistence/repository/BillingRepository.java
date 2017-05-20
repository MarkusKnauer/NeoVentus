package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.Billing;
import de.neoventus.persistence.repository.advanced.NVBillingRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Dennis Thanner, Tim Heidelbach
 **/
@Repository
public interface BillingRepository extends CrudRepository<Billing, String>, NVBillingRepository {

	List<Billing> findByWaiter(String waiterId);

}
