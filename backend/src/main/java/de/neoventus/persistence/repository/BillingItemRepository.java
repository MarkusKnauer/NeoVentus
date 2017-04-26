package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.BillingItem;
import de.neoventus.persistence.repository.advanced.NVBillingItemRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Tim Heidelbach
 * @version 0.0.1
 */
@Repository
public interface BillingItemRepository extends CrudRepository<BillingItem, String>, NVBillingItemRepository {

}
