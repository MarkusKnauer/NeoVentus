package de.neoventus.persistence.repository.customs;/**
 * Created by julian on 19.04.2017.
 */

import de.neoventus.persistence.entity.User;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description:
 **/
public interface UserRepositoryCustom {

    // User-Operations
    public void insertUser(User user);
    public void updateUser(User user);
    public void deleteUser(User user);
    public void setDefaultUsers();
}
