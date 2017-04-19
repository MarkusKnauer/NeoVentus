package de.neoventus.persistence.repository.advanced;/**
 * Created by julian on 19.04.2017.
 */

import de.neoventus.persistence.entity.Billing;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description:
 **/
public interface BillingRepositoryCustom {
    //Billing-Operations
    public void insertBilling(Billing item);
    public void updateBilling(Billing item);
    public void deleteBilling(Billing item);
}
