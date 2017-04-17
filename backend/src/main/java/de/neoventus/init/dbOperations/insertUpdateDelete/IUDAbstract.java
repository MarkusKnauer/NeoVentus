package de.neoventus.init.dbOperations.insertUpdateDelete;/**
 * Created by julian on 17.04.2017.
 */

import de.neoventus.persistence.entity.AbstractDocument;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description: Insert, Update, Delete on DB
 **/
public abstract class IUDAbstract {
    protected int collectionSize;
    public void insert(AbstractDocument abs){

    }
    public void update(AbstractDocument abs){

    }
    public void delete(AbstractDocument abs){

    }
    protected void setCollectionSize(int size){ collectionSize =size;}
    public int getCollectionSize(){ return collectionSize;}
}
