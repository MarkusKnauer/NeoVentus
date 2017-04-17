package de.neoventus.init.dbOperations.insertUpdateDelete;/**
 * Created by julian on 17.04.2017.
 */

import de.neoventus.persistence.entity.*;
import de.neoventus.persistence.repository.*;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description: Insert, Update, Delete on DB
 **/
public class IUDBillingItem extends IUDAbstract {
    private BillingRepository billingRepository;
    private Billing billing;
    public IUDBillingItem(BillingRepository billingRepository){
        this.billingRepository=billingRepository;
    }
    @Override
    public void insert(AbstractDocument abs) {
        billing = (Billing)abs;
        setCollectionSize(getCollectionSize()+1);
        billingRepository.save(billing);
    }

    @Override
    public void update(AbstractDocument abs) {
        billing = (Billing)abs;
        billingRepository.save(billing);
    }
    @Override
    public void delete(AbstractDocument abs) {
        billing = (Billing)abs;
        setCollectionSize(getCollectionSize()-1);
        billingRepository.delete(billing);
    }
}
