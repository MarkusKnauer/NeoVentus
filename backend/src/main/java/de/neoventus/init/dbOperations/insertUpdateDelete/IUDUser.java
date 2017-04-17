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
public class IUDUser extends IUDAbstract {
    private UserRepository userRepository;
    private User user;
    public IUDUser(UserRepository userRepository){
        this.userRepository =userRepository;
    }


    @Override
    public void insert(AbstractDocument abs) {
        user = (User)abs;
        //user.setNumber((int)(userRepository.count()+1));
        userRepository.save(user);
    }

    @Override
    public void update(AbstractDocument abs) {
        user = (User)abs;
        userRepository.save(user);
    }

    @Override
    public void delete(AbstractDocument abs) {
        userRepository.delete((User) abs);
    }

}
