package de.neoventus.persistence.repository.advanced.impl;/**
 * Created by julian on 19.04.2017.
 */

import de.neoventus.persistence.entity.Billing;
import de.neoventus.persistence.repository.BillingRepository;
import de.neoventus.persistence.repository.advanced.BillingRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description:
 **/
public class BillingRepositoryImpl implements BillingRepositoryCustom {
    @Autowired
    private BillingRepository billingRepository;

    @Override
    public void insertBilling(Billing item) {
        item.setBillingID((int)billingRepository.count()+1);
        billingRepository.save(item);
    }

    @Override
    public void updateBilling(Billing item) {
        billingRepository.save(item);
    }

    @Override
    public void deleteBilling(Billing item) {
        billingRepository.delete(item);
    }
}
