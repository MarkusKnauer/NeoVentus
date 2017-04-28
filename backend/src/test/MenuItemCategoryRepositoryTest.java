/**
 * Created by julian on 28.04.2017.
 */

import de.neoventus.persistence.entity.MenuItemCategory;
import de.neoventus.persistence.repository.MenuItemCategoryRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description:
 **/
public class MenuItemCategoryRepositoryTest extends AbstractTest {
	@Autowired
	private MenuItemCategoryRepository menuItemCategoryRepository;

	@Test
	public void testTier(){
		menuItemCategoryRepository.deleteAll();
		MenuItemCategory category1 = new MenuItemCategory();
		MenuItemCategory category2 = new MenuItemCategory();
		MenuItemCategory category3 = new MenuItemCategory();

		category1.setParent(null);
		category1.setName("root");
		menuItemCategoryRepository.save(category1);
		category2.setParent(category1);
		category2.setName("Layer1");
		menuItemCategoryRepository.save(category2);
		category2 = new MenuItemCategory();
		category2.setParent(category1);
		category2.setName("Layer11");
		menuItemCategoryRepository.save(category2);
		category3.setParent(category2);
		category3.setName("Layer2");
		menuItemCategoryRepository.save(category3);


	}
	private MenuItemCategory addCategory(String name, MenuItemCategory parent){
		MenuItemCategory child = new MenuItemCategory(name);
		child.setParent(parent);
		return child;
	}
	@Test
	public void testdata(){
		menuItemCategoryRepository.deleteAll();
		ArrayList<MenuItemCategory> tmp = new ArrayList<MenuItemCategory>();

		tmp.add(addCategory("root",null));
		tmp.add(addCategory("Appetizer",tmp.get(0)));
		tmp.add(addCategory("Main_Dish",tmp.get(0)));
		tmp.add(addCategory("Dessert",tmp.get(0)));
		tmp.add(addCategory("Drinks",tmp.get(0)));
		//2nd level
		tmp.add(addCategory("Warm_Appetizer",tmp.get(1)));
		tmp.add(addCategory("Cold_Appetizer",tmp.get(1)));
		//main Dish
		tmp.add(addCategory("Fish",tmp.get(2)));
		tmp.add(addCategory("Meat",tmp.get(2)));
		tmp.add(addCategory("Vegetarian",tmp.get(2)));
		//Drinks
		tmp.add(addCategory("Alc-free",tmp.get(4)));
		tmp.add(addCategory("Beer",tmp.get(4)));
		tmp.add(addCategory("Wine",tmp.get(4)));
		tmp.add(addCategory("HotDrinks",tmp.get(4)));
		tmp.add(addCategory("Firewater",tmp.get(4)));
		for (MenuItemCategory cat: tmp) menuItemCategoryRepository.save(cat);
	}
	//@After
	public void deleteAll() {
		menuItemCategoryRepository.deleteAll();
	}
}
