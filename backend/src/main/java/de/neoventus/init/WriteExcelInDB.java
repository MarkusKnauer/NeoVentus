package de.neoventus.init;

import de.neoventus.persistence.entity.*;
import de.neoventus.persistence.repository.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description: Writes an Excelsheet into DB
 **/
@Component
public class WriteExcelInDB {


	private final MenuItemRepository menuItemRepository;

	private final MenuItemCategoryRepository menuItemCategoryRepository;

	private final UserRepository userRepository;

	private final DeskRepository deskRepository;

	private final OrderItemRepository orderItemRepository;

	private final BillingRepository billingRepository;

	private final ReservationRepository reservationRepository;
	private final SideDishRepository sideDishRepository;

	private final MongoTemplate mongoTemplate;

	private final static Logger LOGGER = Logger.getLogger(WriteExcelInDB.class.getName());
	private MenuItemCategory category = null;


	@Autowired
	public WriteExcelInDB(MenuItemRepository menuItemRepository, MenuItemCategoryRepository menuItemCategoryRepository, UserRepository userRepository, DeskRepository deskRepository, OrderItemRepository orderItemRepository, BillingRepository billingRepository, ReservationRepository reservationRepository, SideDishRepository sideDishRepository, MongoTemplate mongoTemplate) {
		this.menuItemRepository = menuItemRepository;
		this.menuItemCategoryRepository = menuItemCategoryRepository;
		this.userRepository = userRepository;
		this.deskRepository = deskRepository;
		this.orderItemRepository = orderItemRepository;
		this.billingRepository = billingRepository;
		this.reservationRepository = reservationRepository;
		this.sideDishRepository = sideDishRepository;
		this.mongoTemplate = mongoTemplate;
	}


// PLEASE have a look on the Excelsheet on Drive
	public void readExcelAndWriteintoDB(Path file) {
		LOGGER.info("Writes File in MongoDB");
		try {
			clearData();
			clearIndexes();
			// Excel-things
			Workbook workbook = new XSSFWorkbook(Files.newInputStream(file));
			writeMenuAndCategory(workbook.getSheet("MenuCard"));
			writeDesk(workbook.getSheet("DeskOverview"));
			writeUserDefault(workbook.getSheet("User"));
			// load Default-data
			new DefaultDemoDataIntoDB(deskRepository, userRepository, menuItemRepository, menuItemCategoryRepository, orderItemRepository, reservationRepository, billingRepository, sideDishRepository, mongoTemplate);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

// --------------------- START: SEMANTIC GROUP CATEGORY AND MENU -------------------------------------------------------
	private void writeMenuAndCategory(Sheet excelSheet) {
		LOGGER.info("Writes Menu-Excelsheet in MongoDB");
		Iterator<Row> row = excelSheet.iterator();
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
		category = new MenuItemCategory(value.get(1),!value.get(2).equals("Bar"));
		if (!value.get(0).equals("")) {
			category.setParent(menuItemCategoryRepository.findByName(value.get(0)));
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
		menuItemRepository.save(
			new MenuItem(
				category,
				value.get(0),
				value.get(1),
				Double.parseDouble(value.get(2)),
				value.get(3),
				value.get(4),
				"",
				new ArrayList<>()
			)
		);

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
				Desk desk = new Desk((int) Double.parseDouble(value.get(0)), (int) Double.parseDouble(value.get(1)), (int) Double.parseDouble(value.get(2)));
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
			User user = new User(value.get(0),value.get(1),value.get(2), bCryptPasswordEncoder.encode(value.get(3)),value.get(4),Permission.valueOf(value.get(5)));
			userRepository.save(user);

		}
	}


//	Convert Cell content to String
	private String returnValue(Cell currentCell) {

		switch (currentCell.getCellType()) {
			case Cell.CELL_TYPE_BOOLEAN:
				return String.valueOf(currentCell.getBooleanCellValue());
			case Cell.CELL_TYPE_NUMERIC:
				return String.valueOf(currentCell.getNumericCellValue());
			case Cell.CELL_TYPE_STRING:
				return String.valueOf(currentCell.getStringCellValue());
			case Cell.CELL_TYPE_BLANK:
				return "";
			default:
				return String.valueOf(currentCell);
		}

	}


	/**
	 * clear before regenerate to allow changes
	 */
	private void clearData() {
		deskRepository.deleteAll();
		menuItemRepository.deleteAll();
		userRepository.deleteAll();
		orderItemRepository.deleteAll();
		reservationRepository.deleteAll();
		menuItemCategoryRepository.deleteAll();
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
}
