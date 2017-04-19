package de.neoventus.persistence.repository.advanced.impl;/**
 * Created by julian on 19.04.2017.
 */

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.repository.DeskRepository;
import de.neoventus.persistence.repository.advanced.DeskRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description:
 **/
public class DeskRepositoryImpl implements DeskRepositoryCustom {

    @Autowired
    DeskRepository deskRepository;

    @Override
    public void insertDesk(Desk desk) {
        desk.setNumber((int)deskRepository.count()+1);
        deskRepository.save(desk);
    }

    @Override
    public void updateDesk(Desk desk) {
        deskRepository.save(desk);
    }

    @Override
    public void deleteDesk(Desk desk) {
        deskRepository.delete(desk);
    }


}
