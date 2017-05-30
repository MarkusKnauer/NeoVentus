package de.neoventus.init;

import de.neoventus.persistence.entity.*;
import de.neoventus.persistence.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description: Writes an Excelsheet into DB
 **/
@Component
public class WriteExcelInDB {


	private final static Logger LOGGER = Logger.getLogger(WriteExcelInDB.class.getName());
	private final MenuItemRepository menuItemRepository;
	private final MenuItemCategoryRepository menuItemCategoryRepository;
	private final UserRepository userRepository;
	private final DeskRepository deskRepository;
	private final OrderItemRepository orderItemRepository;
	private final BillingRepository billingRepository;
	private final ReservationRepository reservationRepository;
	private final SideDishRepository sideDishRepository;
	private final MongoTemplate mongoTemplate;
	private final WorkingPlanRepository workingPlanRepository;
	private MenuItemCategory category = null;

	private List<MenuItem> newMenu;


	@Autowired
	public WriteExcelInDB(MenuItemRepository menuItemRepository, MenuItemCategoryRepository menuItemCategoryRepository, UserRepository userRepository, DeskRepository deskRepository, OrderItemRepository orderItemRepository, BillingRepository billingRepository, ReservationRepository reservationRepository, SideDishRepository sideDishRepository, MongoTemplate mongoTemplate, WorkingPlanRepository workingPlanRepository) {
		this.menuItemRepository = menuItemRepository;
		this.menuItemCategoryRepository = menuItemCategoryRepository;
		this.userRepository = userRepository;
		this.deskRepository = deskRepository;
		this.orderItemRepository = orderItemRepository;
		this.billingRepository = billingRepository;
		this.reservationRepository = reservationRepository;
		this.sideDishRepository = sideDishRepository;
		this.mongoTemplate = mongoTemplate;
		this.workingPlanRepository = workingPlanRepository;
	}


// PLEASE have a look on the Excelsheet on Drive
	public void readExcelAndWriteintoDB(Path file) {
		LOGGER.info("Writes File in MongoDB");
		try {
			//clearData();
			//clearIndexes();
			// Excel-things
			Workbook workbook = new XSSFWorkbook(Files.newInputStream(file));
			List<User> user = userRepository.findAll();
			for(int i= 0; i< workbook.getNumberOfSheets(); i++){
				String kw = "KW "+ (i+1);
				LOGGER.info(""+workbook.getSheetName(i));
				if(workbook.getSheetName(i).equals("MenuCard")) {
					menuItemRepository.deleteAll();
					menuItemCategoryRepository.deleteAll();
					writeMenuAndCategory(workbook.getSheet("MenuCard"));
				}else if (workbook.getSheetName(i).equals("DeskOverview")){
					deskRepository.deleteAll();
					writeDesk(workbook.getSheet("DeskOverview"));
				}else if (workbook.getSheetName(i).equals("User")) {
					userRepository.deleteAll();
					workingPlanRepository.deleteAll();
					writeUserDefault(workbook.getSheet("User"));
					user = userRepository.findAll();
				}else if (workbook.getSheetName(i).equals(kw)) {
					if(user != null && !user.isEmpty()){
						writeWorkingPlan(workbook.getSheet(kw));
					}
				}
			}

			// load Default-data

			 new DefaultDemoDataIntoDB(deskRepository, userRepository, menuItemRepository, menuItemCategoryRepository, orderItemRepository, reservationRepository, billingRepository, sideDishRepository, mongoTemplate, workingPlanRepository);


		} catch (IOException e) {
			e.printStackTrace();
		}
	}



// --------------------- START: SEMANTIC GROUP CATEGORY AND MENU -------------------------------------------------------
	private void writeMenuAndCategory(Sheet excelSheet) {
		LOGGER.info("Writes Menu-Excelsheet in MongoDB");
		Iterator<Row> row = excelSheet.iterator();
		 newMenu = new ArrayList<MenuItem>();
		// Jump over the first two lines
		row.next();
		row.next();
		while (row.hasNext()) {

			Row currentRow = row.next();
			Iterator<Cell> cellIterator = currentRow.iterator();
			// Check if Line is a category
			if (currentRow.getCell(0).getStringCellValue().equals("Category")) {
				writeCategory(cellIterator);
			} else {
				writeMenuItem(cellIterator);
			}


		}

	}
	// Subfunction for Category
	private void writeCategory(Iterator<Cell> cellIterator) {
		// First cell isn't interessting for DB (value='Category')
		cellIterator.next();
		List<String> value = new ArrayList<String>();
		while (cellIterator.hasNext()) {
			Cell currentCell = cellIterator.next();
			value.add(returnValue(currentCell));

		}
		// Constructor Category: Name, Kitchen or Bar
		if(menuItemCategoryRepository.findByName(value.get(1))== null){
			category = new MenuItemCategory(value.get(1),!value.get(2).equals("Bar"));
			if (!value.get(0).equals("")) {
				category.setParent(menuItemCategoryRepository.findByName(value.get(0)));
			}
		} else{
			category = menuItemCategoryRepository.findByName(value.get(1));
			category.setForKitchen(!value.get(2).equals("Bar"));
			if (!value.get(0).equals("")) {
				category.setParent(menuItemCategoryRepository.findByName(value.get(0)));
			}
			LOGGER.info("Categorie exists");
		}


		menuItemCategoryRepository.save(category);
	}


	// Subfunction for MenuItem
	private void writeMenuItem(Iterator<Cell> cellIterator) {
		List<String> value = new ArrayList<String>();
		while (cellIterator.hasNext()) {
			Cell currentCell = cellIterator.next();
			value.add(returnValue(currentCell));
		}
		// MenuItem-Constructor: Category, name, Shortname, price, currency, description, mediaURL, notices
		MenuItem menuItem;
			if (menuItemRepository.findByName(value.get(0)) == null) {
				menuItem = new MenuItem(
					category,
					value.get(0),
					value.get(1),
					Double.parseDouble(value.get(2)),
					value.get(3),
					value.get(4),
					"",
					new ArrayList<>());

			} else {
				menuItem = menuItemRepository.findByName(value.get(0));
				menuItem.setMenuItemCategory(category);
				menuItem.setShortName(value.get(1));
				menuItem.setPrice(Double.parseDouble(value.get(2)));
				menuItem.setCurrency(value.get(3));
				menuItem.setDescription(value.get(4));
			}
			newMenu.add(menuItem);
			menuItemRepository.save(menuItem);
	}
// --------------------- END: SEMANTIC GROUP CATEGORY AND MENU ---------------------------------------------------------

	private void writeDesk(Sheet excelSheet){
		LOGGER.info("Writes Desk-Excelsheet in MongoDB");
		Iterator<Row> row = excelSheet.iterator();
		// Jump over the first two lines
		row.next();
		row.next();
		while (row.hasNext()) {
			Row currentRow = row.next();
			Iterator<Cell> cellIterator = currentRow.iterator();
			List<String> value = new ArrayList<String>();
			while (cellIterator.hasNext()) {
				Cell currentCell = cellIterator.next();
				value.add(returnValue(currentCell));
			}
			if(!value.get(0).equals("Gesamt")) {
				//Desk-Constructor: number, seats, max. Seats
				Desk desk;
				if(deskRepository.findByNumber((int) Double.parseDouble(value.get(0))) == null) {
					desk = new Desk((int) Double.parseDouble(value.get(0)), (int) Double.parseDouble(value.get(1)), (int) Double.parseDouble(value.get(2)));

				} else {
					desk = deskRepository.findByNumber((int) Double.parseDouble(value.get(0)));
					desk.setSeats((int) Double.parseDouble(value.get(1)));
					desk.setMaximalSeats((int) Double.parseDouble(value.get(1)));
				}
				deskRepository.save(desk);
			}
		}
	}

	private void writeUserDefault(Sheet excelSheet){
		LOGGER.info("Writes User-Excelsheet in MongoDB");
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		Iterator<Row> row = excelSheet.iterator();
		// Jump over the first two lines
		row.next();
		row.next();
		while (row.hasNext()) {
			Row currentRow = row.next();
			Iterator<Cell> cellIterator = currentRow.iterator();
			List<String> value = new ArrayList<String>();
			// First cell isn't interessting for DB
			cellIterator.next();
			while (cellIterator.hasNext()) {
				Cell currentCell = cellIterator.next();
				value.add(returnValue(currentCell));
			}
			// User-Constructor: Username, firstname, lastname, password, WorkingTimeModell, Permissions
			User user;
			if(userRepository.findByUsername(value.get(0))==null) {
				user = new User(value.get(0), value.get(1), value.get(2), bCryptPasswordEncoder.encode(value.get(3)), value.get(4), Permission.valueOf(value.get(5)));
			} else{
				user = userRepository.findByUsername(value.get(0));
				user.setFirstName(value.get(1));
				user.setLastName(value.get(2));
				user.setPassword(bCryptPasswordEncoder.encode(value.get(3)));
				user.setWorkingTimeModell(value.get(4));
				List<Permission> perm = user.getPermissions();
				// TODO: what about multiple permissions in excel sheet?
				if (!perm.contains(Permission.valueOf(value.get(5)))) {
					perm.add(Permission.valueOf(value.get(5)));
				}
				user.setPermissions(perm);
			}
			userRepository.save(user);
		}
	}


	private void writeWorkingPlan(Sheet excelSheet){
		LOGGER.info("Writes WorkingPlan-Excelsheet in MongoDB");

		List<Workingplan> workingplan = new ArrayList<Workingplan>();
		List<GregorianCalendar> calendars = new ArrayList<GregorianCalendar>();
		List<HelpShiftClass> helpShiftClassList = new ArrayList<>();

		Iterator<Row> row = excelSheet.iterator();
		// Jump over the first three lines

		row.next();
		row.next();
		while (row.hasNext()) {
			Row currentRow = row.next();
			Iterator<Cell> cellIterator = currentRow.iterator();
			Cell currentCell = cellIterator.next();
			if (currentRow.getCell(0).getStringCellValue().equals("Personen:")) break;
			HelpShiftClass helpShiftClass = new HelpShiftClass(currentRow.getCell(0).getStringCellValue());
				currentCell = cellIterator.next();
				helpShiftClass.setStartTime(returnValue(currentCell));
				currentCell = cellIterator.next();
				helpShiftClass.setEndTime(returnValue(currentCell));

			helpShiftClassList.add(helpShiftClass);
		}
	//	row.next();
		while (row.hasNext()) {
			Row currentRow = row.next();
			Iterator<Cell> cellIterator = currentRow.iterator();
				// Check if Line is a dateline
			if (currentRow.getCell(0).getStringCellValue().equals("---")) {
				cellIterator.next();
				while (cellIterator.hasNext()) {
					Cell currentCell = cellIterator.next();
					GregorianCalendar calendar = new GregorianCalendar(1900, 0, (int)(Double.parseDouble(returnValue(currentCell)))-1,1,0);
					calendars.add(calendar);
			//  Workingplan(Date createdPlan, Date startShift, Date endShift, List<Workingshift> workingshiftList, User waiter)
					workingplan.add(new Workingplan(
						new Date( System.currentTimeMillis()),
						calendar.getTime(),
						new ArrayList<>()
					));
				}
			} else {
				User user = userRepository.findByUsername(currentRow.getCell(0).getStringCellValue());
				// check if User existing in db
				if(user != null && user.getUsername().length() != 0){
					cellIterator.next();
					int i = 0;
					while (cellIterator.hasNext()) {
						Cell currentCell = cellIterator.next();
						String res= returnValue(currentCell);

					// Find available Shift
						HelpShiftClass helpShiftClass = null;
						for( int j = 0; j < helpShiftClassList.size(); j++){
							if(helpShiftClassList.get(j).getName().equals(res)){
								helpShiftClass = helpShiftClassList.get(j);
							}
						}
					// if shift is available -> write new Workingplan
						if(helpShiftClass != null){
							//Date time
							Calendar start = new GregorianCalendar(calendars.get(i).get(1),
								calendars.get(i).get(2),
								calendars.get(i).get(5),
								helpShiftClass.getStartHour(),
								helpShiftClass.getStartMinute());

							Calendar end = new GregorianCalendar(calendars.get(i).get(1),
								calendars.get(i).get(2),
								calendars.get(i).get(5),
								helpShiftClass.getEndHour(),
								helpShiftClass.getEndMinute());

							Workingshift workingshift =new Workingshift(start.getTime(),end.getTime(),user,null);
							Workingplan plan = workingplan.get(i);
							plan.addShift(workingshift);
							plan.setId(String.valueOf(new ObjectId()));
							workingplan.set(i, plan);

						}
						i++;
					}
				}
			}
		}
		BulkOperations bulkOrders = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED,Workingplan.class);
		bulkOrders.insert(workingplan);
		bulkOrders.execute();
	}




//	Convert Cell content to String
	private String returnValue(Cell currentCell) {
		FormulaEvaluator evaluator = currentCell.getSheet().getWorkbook().getCreationHelper ().createFormulaEvaluator();
		switch (currentCell.getCellType()) {
			case Cell.CELL_TYPE_BOOLEAN:
				return String.valueOf(currentCell.getBooleanCellValue());
			case Cell.CELL_TYPE_NUMERIC:
				return String.valueOf(currentCell.getNumericCellValue());
			case Cell.CELL_TYPE_STRING:
				return String.valueOf(currentCell.getStringCellValue());
			case Cell.CELL_TYPE_BLANK:
				return "";
			case Cell.CELL_TYPE_FORMULA:
				switch(currentCell.getCachedFormulaResultType()) {
					case Cell.CELL_TYPE_NUMERIC:
						return String.valueOf(currentCell.getNumericCellValue());
					case Cell.CELL_TYPE_STRING:
						return String.valueOf(currentCell.getRichStringCellValue());
				}
			default:
				return String.valueOf(currentCell);
		}

	}



	/**
	 * clear before regenerate to allow changes
	 */
	private void clearData() {
	//	deskRepository.deleteAll();
	//	menuItemRepository.deleteAll();
	//	userRepository.deleteAll();
	//	workingPlanRepository.deleteAll();
		orderItemRepository.deleteAll();
		reservationRepository.deleteAll();
	//	menuItemCategoryRepository.deleteAll();
		sideDishRepository.deleteAll();

	}
	/**
	 * clear existing indexes
	 */
	private void clearIndexes() {
		for (String collection : this.mongoTemplate.getDb().getCollectionNames()) {
			this.mongoTemplate.getDb().getCollection(collection).dropIndexes();
		}
	}

	class HelpShiftClass{
		private String name;
		private int startHour;
		private int endHour;
		private int startMinute;
		private int endMinute;

		public HelpShiftClass(String name) {
			this.name = name;
		}


		public void setStartTime(String startTime){
			double time = Double.parseDouble(startTime);
			setStartHour((int)((time)*24));
			setStartMinute((int)((time*24-getStartHour())*60));
		}

		public void setEndTime(String endTime){
			double time = Double.parseDouble(endTime);
			setEndHour((int)((time)*24));
			setEndMinute((int)((time*24-getEndHour())*60));

		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getStartHour() {
			return startHour;
		}

		public void setStartHour(int startHour) {
			this.startHour = startHour;
		}

		public int getEndHour() {
			return endHour;
		}

		public void setEndHour(int endHour) {
			this.endHour = endHour;
		}

		public int getStartMinute() {
			return startMinute;
		}

		public void setStartMinute(int startMinute) {
			this.startMinute = startMinute;
		}

		public int getEndMinute() {
			return endMinute;
		}

		public void setEndMinute(int endMinute) {
			this.endMinute = endMinute;
		}

		@Override
		public String toString() {
			return "HelpShiftClass{" +
				"name='" + name + '\'' +
				", startHour=" + startHour +
				", endHour=" + endHour +
				", startMinute=" + startMinute +
				", endMinute=" + endMinute +
				'}';
		}
	}
}
