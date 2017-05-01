/**
 * Created by julian on 28.04.2017.
 */

import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.SideDish;
import de.neoventus.persistence.repository.MenuItemCategoryRepository;
import de.neoventus.persistence.repository.MenuItemRepository;
import de.neoventus.persistence.repository.SideDishRepository;
import de.neoventus.rest.dto.SideDishDto;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description:
 **/
public class SideDishRepositoryTest extends AbstractTest {
	@Autowired
	private MenuItemCategoryRepository menuItemCategoryRepository;

	@Autowired
	private MenuItemRepository menuItemRepository;

	@Autowired
	private SideDishRepository sideDishRepository;

	@Test
	public void testSideDish(){
		deleteAll();
		MenuItem u2 = new MenuItem();

		u2.setName("Menu");
		u2.setNumber(1);
		menuItemRepository.save(u2);
		MenuItem u = new MenuItem();
		u.setName("Menu2");
		menuItemRepository.save(u);
		SideDish sideDish = u.getSideDish();
		sideDish.addParentalMeal(u2.getSideDish());
		// sideDish.addParentalMeal(u2);
		sideDishRepository.save(sideDish);
		MenuItem u3 = new MenuItem();
		u3.setName("Kuchen");
		menuItemRepository.save(u3);
		SideDish d = u3.getSideDish();
		d.addParentalMeal(u2.getSideDish());
		sideDishRepository.save(d);


		Assert.assertNotNull(u);

		Assert.assertTrue(sideDish.getLastParentMeal().equals(u2));


	}

	@Test
	public void testSearchByName() {
		deleteAll();
		MenuItem u2 = new MenuItem();

		u2.setName("Menu");
		u2.setNumber(1);
		menuItemRepository.save(u2);
		MenuItem u = new MenuItem();
		u.setName("Menu2");
		menuItemRepository.save(u);
		SideDish sideDish = u.getSideDish();
		sideDish.addParentalMeal(u2.getSideDish());
		// sideDish.addParentalMeal(u2);
		sideDishRepository.save(sideDish);
		MenuItem u3 = new MenuItem();
		u3.setName("Kuchen");
		menuItemRepository.save(u3);
		SideDish d = u3.getSideDish();
		d.addParentalMeal(u2.getSideDish());
		sideDishRepository.save(d);



		d = sideDishRepository.findBySideDishName("Kuchen");


		Assert.assertNotNull(d);

		Assert.assertTrue(d.getSideDishName().equals("Kuchen"));
	}

	@Test
	public void pseudoMenuItem(){
		deleteAll();
		MenuItem u2 = new MenuItem();

		u2.setName("Menu");
		u2.setNumber(1);
		menuItemRepository.save(u2);
		MenuItem u3 = new MenuItem();
		u3.setName("Kuchen");
		menuItemRepository.save(u3);
		SideDish d = new SideDish("Salate");
		sideDishRepository.save(d);

		MenuItem u = new MenuItem();
		u.setName("Menu2");
		menuItemRepository.save(u);

		d.addParentalMeal(u.getSideDish());
		sideDishRepository.save(u.getSideDish());
		// sideDish.addParentalMeal(u2);
		//sideDishRepository.save(d);
	}


	/**
	 * test the custom implementation method for saving sideDish by dto
	 *
	 * @see de.neoventus.persistence.repository.advanced.impl.SideDishRepositoryImpl#save(SideDishDto)
	 */
	@Test
	public void testSaveByDto() {
		deleteAll();
		SideDishDto dto = new SideDishDto();
		dto.setNameDish("Ehh Macarena");
		dto.setMenuItemName("test1");
		dto.setTargetMenuItem("target1");


		sideDishRepository.save(dto);
		SideDish bigO = sideDishRepository.findBySideDishName("Ehh Macarena");

		Assert.assertNotNull(bigO);
	}

	/**
	 * test if the specified before save event works
	 *
	 * @see de.neoventus.persistence.event.MenuItemCategoryLifecycleEvents#onAfterSave(AfterSaveEvent)
	 */
	@Test
	public void testAfterSaveEvent() {

		SideDish u2 = new SideDish("Spice Girls - Wannabe");


		sideDishRepository.save(u2);

		SideDish u = new SideDish( "404 Not Found, man");

		u.addParentalMeal(u2);
		u = sideDishRepository.save(u);

		Assert.assertTrue(u2.getSubMeal().contains(u));

	}



	//@After
	public void deleteAll() {
		menuItemCategoryRepository.deleteAll();
		menuItemRepository.deleteAll();
		sideDishRepository.deleteAll();
	}
}
