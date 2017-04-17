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
public class IUDDesk extends IUDAbstract {
    private DeskRepository deskRepository;
    private Desk desk;
    public IUDDesk(DeskRepository deskRepository){
        setCollectionSize((int)deskRepository.count());
        this.deskRepository = deskRepository;
    }

    @Override
    public void insert(AbstractDocument abs) {
        desk = (Desk)abs;
        setCollectionSize(getCollectionSize()+1);
        desk.setNumber(getCollectionSize());
        deskRepository.save(desk);
    }

    @Override
    public void update(AbstractDocument abs) {
        desk = (Desk)abs;
        deskRepository.save(desk);
    }
    @Override
    public void delete(AbstractDocument abs) {
        desk = (Desk)abs;
        setCollectionSize(getCollectionSize()-1);
        deskRepository.delete(desk);
    }
}
