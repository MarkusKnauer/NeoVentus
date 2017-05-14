/**
 * Created by julian on 28.04.2017.
 *
 * @author: Julian Beck, Markus Knauer
 * @version: 0.0.2 edited (deleted tree structure) MK
 * 0.0.1 crated by JB
 * @description:
 **/

/**
 * @author: Julian Beck, Markus Knauer
 * @version: 0.0.2 edited (deleted tree structure) MK
 * 			0.0.1 crated by JB
 * @description:
 **/
//public class SideDishRepositoryTest extends AbstractTest {
//	@Autowired
//	private MenuItemCategoryRepository menuItemCategoryRepository;
//
//	@Autowired
//	private MenuItemRepository menuItemRepository;
//
//	@Autowired
//	private SideDishRepository sideDishRepository;

// FIXME ASAP
//	@Test
//	public void testSideDish(){
//		// Unit Test for old- Version of SideDishGroup
//
//		deleteAll();
//		MenuItem u2 = new MenuItem();
//
//		u2.setName("Menu");
//		u2.setNumber(1);
//		menuItemRepository.save(u2);
//		MenuItem u = new MenuItem();
//		u.setName("Menu2");
//		menuItemRepository.save(u);
//		SideDishGroup sideDish = u.getSideDishGroup();
//		sideDishRepository.save(sideDish);
//		MenuItem u3 = new MenuItem();
//		u3.setName("Kuchen");
//		menuItemRepository.save(u3);
//		SideDishGroup d = u3.getSideDishGroup();
//		sideDishRepository.save(d);
//
//
//		Assert.assertNotNull(u);
//
//
//
//	}
//	@Test
//	public void testSearchByName() {
//		// Unit Test for old- Version of SideDishGroup
//
//		deleteAll();
//		MenuItem u2 = new MenuItem();
//
//		u2.setName("Menu");
//		u2.setNumber(1);
//		menuItemRepository.save(u2);
//		MenuItem u = new MenuItem();
//		u.setName("Menu2");
//		menuItemRepository.save(u);
//		SideDishGroup sideDish = u.getSideDishGroup();
//		sideDishRepository.save(sideDish);
//		MenuItem u3 = new MenuItem();
//		u3.setName("Kuchen");
//		menuItemRepository.save(u3);
//		SideDishGroup d = u3.getSideDishGroup();
//		sideDishRepository.save(d);
//
//
//
//		d = sideDishRepository.findByName("Kuchen");
//
//
//		Assert.assertNotNull(d);
//
//		Assert.assertTrue(d.getName().equals("Kuchen"));
//	}
//
//	@Test
//	public void pseudoMenuItem(){
//		// Unit Test for old- Version of SideDishGroup
//
//		deleteAll();
//		MenuItem u2 = new MenuItem();
//
//		u2.setName("Menu");
//		u2.setNumber(1);
//		menuItemRepository.save(u2);
//		MenuItem u3 = new MenuItem();
//		u3.setName("Kuchen");
//		menuItemRepository.save(u3);
//		SideDishGroup d = new SideDishGroup("Salate");
//		sideDishRepository.save(d);
//
//		MenuItem u = new MenuItem();
//		u.setName("Menu2");
//		menuItemRepository.save(u);
//
//		d.addSideDish(u);
//		sideDishRepository.save(d);
//		// sideDish.addParentalMeal(u2);
//		//sideDishRepository.save(d);
//	}
//
//
//	/**
//	 * test the custom implementation method for saving sideDish by dto
//	 *
//	 * @see de.neoventus.persistence.repository.advanced.impl.SideDishRepositoryImpl#save(SideDishDto)
//	 */
//	@Test
//	public void testSaveByDto() {
//		deleteAll();
//		SideDishDto dto = new SideDishDto();
//		dto.setNameDish("Ehh Macarena");
//		dto.setMenuItemName("test1");
//		dto.setTargetMenuItem("target1");
//
//
//		sideDishRepository.save(dto);
//		SideDishGroup bigO = sideDishRepository.findByName("Ehh Macarena");
//
//		Assert.assertNotNull(bigO);
//	}
//
//	/**
//	 * test if the specified before save event works
//	 *
//	 * @see de.neoventus.persistence.event.MenuItemCategoryLifecycleEvents#onAfterSave(AfterSaveEvent)
//	 */
//
//
//	//@After
//	public void deleteAll() {
//		menuItemCategoryRepository.deleteAll();
//		menuItemRepository.deleteAll();
//		sideDishRepository.deleteAll();
//	}
//}
