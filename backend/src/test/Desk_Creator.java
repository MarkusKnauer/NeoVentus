/**
 * Created by julian on 09.04.2017.
 */

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.repository.DeskRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description: Class for creating an the Table-landscape
 **/
public class Desk_Creator extends AbstractTest {
    private final int MAX_DESKS = 10;
    @Autowired
    private DeskRepository deskRepository;

    @Test
    public void testCreateTable(){
        deskRepository.deleteAll();
        for (int i = 0; i < MAX_DESKS; i++){
            Desk des = new Desk();
            des.setNumber(i+1);
            des.setSeats((int)(Math.random()*5)+3);
            deskRepository.save(des);
        }
        //order = orderRepository.findByUsername("Test 1");

        //Assert.assertNotNull(order);

        //Assert.assertTrue(order.getUsername().equals("Test 1"));
    }

    /**
     * clear the data written
     */
    @After
    public void deleteAll() {

        deskRepository.deleteAll();
    }

}
