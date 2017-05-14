
package de.neoventus.init;

import de.neoventus.persistence.entity.*;
import de.neoventus.persistence.repository.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Dennis Thanner, Julian Beck, Markus Knauer, Tim Heidelbach
 * @version 0.0.9 corrected spelling mistakes - DS
 * 			0.0.8 menu category fix - DT
 *          0.0.7 add SideDish bsp-data - MK
 *          0.0.6 add menuItem Category bsp-data-generator -JB
 *          0.0.5 added random order items init - DT
 *          0.0.4 changed to new repositories
 *          0.0.3 Indirectly insert, Update, delete on DB (Class InsertUpdateDelete) +
 *          and repository access in ControlEntityObjects- JB
 *          0.0.2 Refactor default demo data in separate class
 **/

class DefaultDemoDataIntoDB {

	private static final int MAX_DESKS = 20;
	private static final int MAX_SEATS = 96;

	private final static Logger LOGGER = Logger.getLogger(DefaultDemoDataIntoDB.class.getName());
	private MenuItemRepository menuItemRepository;
	private MenuItemCategoryRepository menuItemCategoryRepository;
	private UserRepository userRepository;
	private DeskRepository deskRepository;
	private OrderItemRepository orderItemRepository;
	private BillingRepository billingRepository;
	private ReservationRepository reservationRepository;
	private SideDishRepository sideDishRepository;
	private MongoTemplate mongoTemplate;

	// init documents saved for class based access
	private List<Desk> desks;
	private List<MenuItem> menuItems;
	private List<User> users;

	public DefaultDemoDataIntoDB(DeskRepository deskRepository, UserRepository userRepository,
								 MenuItemRepository menuItemRepository, MenuItemCategoryRepository menuItemCategoryRepository, OrderItemRepository orderItemRepository,
								 ReservationRepository reservationRepository, BillingRepository billingRepository, SideDishRepository sideDishRepository, MongoTemplate mongoTemplate) {

		this.menuItemRepository = menuItemRepository;
		this.menuItemCategoryRepository = menuItemCategoryRepository;
		this.userRepository = userRepository;
		this.deskRepository = deskRepository;
		this.billingRepository = billingRepository;
		this.reservationRepository = reservationRepository;
		this.orderItemRepository = orderItemRepository;
		this.sideDishRepository = sideDishRepository;
		this.mongoTemplate = mongoTemplate;
		this.desks = new ArrayList<>();
		this.menuItems = new ArrayList<>();
		this.users = new ArrayList<>();
        clearData();
        clearIndexes();
		generateDesks();
		generateMenuItems();
		generateUser();
		updateUserDesk();
		generateOrderItem();
		generateSideDish();
	}

	/**
	 * add specified demo menu items to database
	 */
	private void generateMenuItems() {
		LOGGER.info("Init demo menu item data");
		generateMenuCategories();
		MenuItem[] menu = {
			// Vorspeise
				// kalte Vorspeise - 2
			new MenuItem(menuItemCategoryRepository.findByName("Kalte Vorspeise"), "kleiner Salat","klein S.", 4.80, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Kalte Vorspeise"), "Matjes-Filet","Matjes", 6.30, "EUR", "in leichter Dillsahne mit Äpfeln, Senfgurken und Zwiebeln dazu eine dampfende Ofenkartoffel", "", new ArrayList<>()),
				// warme Vorspeise - 2
			new MenuItem(menuItemCategoryRepository.findByName("Warme Vorspeise"), "Weinbergschnecken","W-Schneck" ,7.40, "EUR", "ein halbes Dutzend, mit würziger Kräuterbutter und Baguette","", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Warme Vorspeise"), "Toast “Hawaii”","T-Hawa", 6.70, "EUR", "gegrillte Ananas mit Hinterschinken und Käse überbacken auf Toast", "", new ArrayList<>()),
				// Suppen - 4
			new MenuItem(menuItemCategoryRepository.findByName("Suppen"), "Bärlauchcremesuppe mit Räucherlachs", "Bär-Cre-Su",4.80, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Suppen"), "Ochsen-Bouillon","O-Bou", 4.80, "EUR", "mit Fleisch und Leberspätzle", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Suppen"), "Zwiebelsuppe Provence","Zwie-Pro", 4.60, "EUR", "mit Weißwein und den typischen Kräutern der Provence verfeinert und würzigem Käse im Ofen überbacken", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Suppen"), "Tomaten-Rahmsuppe", "To-Rahm",4.60, "EUR", "von frischen Tomaten, mit Sahneschaum und Speckcroûtons", "", new ArrayList<>()),
			// Hauptspeise
				// Fisch - 2
			new MenuItem(menuItemCategoryRepository.findByName("Fischgerichte"), "Lachsfilet", "Lachs",14.60, "EUR", "mit Tagliatelle und Tomaten", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Fischgerichte"), "Zander-Salatteller","Zander" ,13.80, "EUR", "mit gebratenem Zanderfilet", "", new ArrayList<>()),
				// Fleisch - 8 bzw. 9
			new MenuItem(menuItemCategoryRepository.findByName("Fleischgerichte"), "Schnitzel “Wiener Art”","Schnitzel", 10.90, "EUR", "mit Pommes und Salat", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Fleischgerichte"), "Schweinerückensteak", "R-Steak",14.20, "EUR", "mit Rahmpfeffersauce und Kartoffelrösti", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Fleischgerichte"), "Pasta Bolognese","Bolo", 11.90, "EUR", "mit Tomaten und Parmesan", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Fleischgerichte"), "Hüftsteak","H-Steak", 14.70, "EUR", "aus dem besten Stück der Rinderkeule 180g mit knusprig geröstetes Bratkartoffeln und würzige Kräuterbutter", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Fleischgerichte"), "Filetsteak", "F-Steak",24.90, "EUR", "das zarte Stück vom Rind 200g mit knusprigen Ofenkartoffeln und würzige Kräuterbutter", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Fleischgerichte"), "Putenbruststeak","P-Steak", 10.20, "EUR", "zart und kalorienarm 200g mit Naturreis und würzige Kräuterbutter", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Fleischgerichte"), "Filet vom Lammrücken","Lammfilet", 17.20, "EUR", "mariniert in feinem Kräuteröl und auf dem Grill gebraten, mit Kräuterbutter aus der Provence", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Fleischgerichte"), "Spare Ribs 300g", "Spare-300",9.90, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Fleischgerichte"), "Spare Ribs 550g", "Spare-550",13.20, "EUR", "", "", new ArrayList<>()),
			// Vegetarisch - 2
			new MenuItem(menuItemCategoryRepository.findByName("Vegetarische Gerichte"), "Hausgemachte Kartoffel Gnocchi", "Gnocchi",12.30, "EUR", "Gnocchi mit mediterranem Gemüse, Fetakäse, Rucola & Parmesan", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Vegetarische Gerichte"), "Hausgemachte Käsespätzle", "K-Spätzle",9.80, "EUR", "mit grünem Salatteller", "", new ArrayList<>()),
			// Kindergerichte - 3
			new MenuItem(menuItemCategoryRepository.findByName("Kinder Gerichte"), "Großer Pommes-Teller", "Kind-Pommes",3.70, "EUR", "mit Mayo oder Ketchup", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Kinder Gerichte"), "Paniertes Schnitzel", "Kind-Schnitzel",6.10, "EUR", "mit Pommes Frites", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Kinder Gerichte"), "Chicken Nuggets", "Kind-Nuggets",5.80, "EUR", "mit Pommes Frites und Mayo oder Ketchup", "", new ArrayList<>()),
			//Nachspeise - 3
			new MenuItem(menuItemCategoryRepository.findByName("Nachspeise"), "Drei Kugeln Eis","Eis", 5.10, "EUR", "nach Wahl mit Sahne", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Nachspeise"), "warmer hausgemachter Apfelstrudel","Apfelstrudel", 6.20, "EUR", "mit Vanillesoße und einer Kugel Vanilleeis oder Sahne", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Nachspeise"), "Rhabarber-Torte","Rha-to", 6.80, "EUR", "mit Rosmarinstreusel und cremigem Himbeersorbet", "", new ArrayList<>()),
			// Getränke
				// Aperitifs - 6
			new MenuItem(menuItemCategoryRepository.findByName("Aperitifs"), "Martini rot","Martin-Rot", 3.50, "EUR", "5cl", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Aperitifs"), "Martini weiß","Martin-weiß", 3.50, "EUR", "5cl", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Aperitifs"), "Sherry Sandemann, trocken","Sherry-tro" ,3.50, "EUR", "5cl", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Aperitifs"), "Sherry Sandemann, medium","Sherry-med", 3.50, "EUR", "5cl", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Aperitifs"), "Kir Royal, Prosecco mit Cassis", "Prosecco",3.50, "EUR", "1,10l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Aperitifs"), "Aperol Sekt","Aperol", 3.30, "EUR", "1,10l", "", new ArrayList<>()),
				// Alkoholfrei - 13
			new MenuItem(menuItemCategoryRepository.findByName("Alkoholfreie Getränke"), "Coca Cola","Cola", 3.10, "EUR", "0,40l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Alkoholfreie Getränke"), "Coca Cola light","Cola light", 2.90, "EUR", "0,40l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Alkoholfreie Getränke"), "Fanta Orange","Fanta", 3.10, "EUR", "0,40l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Alkoholfreie Getränke"), "Cola Mix","Cola Mix", 2.90, "EUR", "0,40l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Alkoholfreie Getränke"), "Softdrinks klein", "Kle-Soft",2.50, "EUR", "0,30l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Alkoholfreie Getränke"), "Schweppes Ginger Ale","Ginger Ale", 2.80, "EUR", "0,20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Alkoholfreie Getränke"), "Schweppes Bitter Lemon","Bitter Lemon", 2.80, "EUR", "0,20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Alkoholfreie Getränke"), "Bionade Holunder","Bio-Holunder", 2.70, "EUR", "0,33l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Alkoholfreie Getränke"), "Bionade Ingwer-Orange","Bio-Ingwer", 2.70, "EUR", "0,33l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Alkoholfreie Getränke"), "Kleines Wasser mit Kohlensäure","Kle-Wa mit", 2.20, "EUR", "0,25l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Alkoholfreie Getränke"), "Kleines Wasser ohne Kohlensäure","Kle-Wa ohne", 2.20, "EUR", "0,25l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Alkoholfreie Getränke"), "Großes Wasser mit Kohlensäure","Gro-Wa mit", 3.10, "EUR", "0,75l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Alkoholfreie Getränke"), "Großes Wasser ohne Kohlensäure","Gro-Wa ohne", 3.20, "EUR", "0,75l", "", new ArrayList<>()),
				// Säfte - 4
			new MenuItem(menuItemCategoryRepository.findByName("Säfte"), "Apfelsaft","A-Saft", 2.90, "EUR", "0.20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Säfte"), "Orangensaft","O-Saft", 2.90, "EUR", "0.20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Säfte"), "Multivitaminsaft","Multi", 2.90, "EUR", "0.20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Säfte"), "Tomatensaft","T-Saft", 2.90, "EUR", "0.20l", "", new ArrayList<>()),
				// Saftschorle - 4 Klein / 4 Groß
			new MenuItem(menuItemCategoryRepository.findByName("Saftschorle"), "Kleines Apfelsaftschorle","A-Schorle klein", 2.90, "EUR", "0.20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Saftschorle"), "Kleines Orangensaftschorle","O-Schorle klein", 2.90, "EUR", "0.20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Saftschorle"), "Kleines Multivitaminsaftschorle","Multi-Schorle klein", 2.90, "EUR", "0.20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Saftschorle"), "Kleines Tomatensaftschorle","T-Schorle klein", 2.90, "EUR", "0.20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Saftschorle"), "Großes Apfelsaftschorle","A-Schorle groß", 2.90, "EUR", "0.20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Saftschorle"), "Großes Orangensaftschorle","O-Schorle groß", 2.90, "EUR", "0.20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Saftschorle"), "Großes Multivitaminsaftschorle","Multi-Schorle groß", 2.90, "EUR", "0.20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Saftschorle"), "Großes Tomatensaftschorle","T-Schorle groß", 2.90, "EUR", "0.20l", "", new ArrayList<>()),
				//Bier - 8
			new MenuItem(menuItemCategoryRepository.findByName("Bier"), "Jever Fun alkoholfrei","Jever Fun", 3.10, "EUR", "0.25l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Bier"), "Kleines Warsteiner","Klein-Warstein", 3.10, "EUR", "0.25l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Bier"), "Großes Warsteiner","Gro-Warstein", 3.60, "EUR", "0.50l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Bier"), "Bitburger","Bitburger", 3.10, "EUR", "0.33l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Bier"), "Radler","Radler", 3.00, "EUR", "0.30l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Bier"), "Guinness","Guinness", 3.40, "EUR", "0.50l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Bier"), "Ruß","Ruß", 3.40, "EUR", "0.50l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Bier"), "Cola-Weizen", "Cola-Weizen",3.40, "EUR", "0.50l", "", new ArrayList<>()),
				// Wein
					// Weißwein - 5
			new MenuItem(menuItemCategoryRepository.findByName("Weißwein"), "Cabernet Sauvignon Weiß", "Cabernet Weiß",3.90, "EUR", "0.20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Weißwein"), "Pinot Grigio", "Pinot", 3.90, "EUR", "0.20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Weißwein"), "Chardonnay","Chardonnay", 4.70, "EUR", "0.20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Weißwein"), "Pinot Grigio des Veneto IGT", "Pinot IGT", 4.50, "EUR", "0.20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Weißwein"), "Riesling trocken", "Riesling", 4.20, "EUR", "0.20l", "", new ArrayList<>()),
					// Rotwein - 4
			new MenuItem(menuItemCategoryRepository.findByName("Rotwein"), "Cabernet Sauvignon Rot", "Cabernet Rot",3.90, "EUR", "0.20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Rotwein"), "Casa Solar","Casa", 3.90, "EUR", "0.20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Rotwein"), "Bordeaux Rouge","Bordeaux", 4.90, "EUR", "0.20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Rotwein"), "Trollinger mit Lemberger", "Lem-Tro",4.70, "EUR", "0.20l", "", new ArrayList<>()),
					// Rosé - 2
			new MenuItem(menuItemCategoryRepository.findByName("Rose"), "Côtes de Provence Rosé", "Cotes-rose",4.60, "EUR", "0.20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Rose"), "Talheimer Schlossberg","Talheimer", 4.80, "EUR", "0.20l", "", new ArrayList<>()),
					// Weinmixgetränke - 2
			new MenuItem(menuItemCategoryRepository.findByName("Mixwein"), "Weinschorle","Weinschorle", 3.10, "EUR", "0.20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Mixwein"), "Hugo", "Hugo",3.90, "EUR", "0.20l", "", new ArrayList<>()),
					// Schaumwein
			new MenuItem(menuItemCategoryRepository.findByName("Schaumwein"),"Prosecco 0.75l", "Gro-Prosecco", 21.90, "EUR", "5cl", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Schaumwein"),"Prosecco 0.10l", "Klein-Prosecco", 3.90, "EUR", "5cl", "", new ArrayList<>()),

				// Schnaps - 7
			new MenuItem(menuItemCategoryRepository.findByName("Spirituosen"), "Jägermeister","Jägi", 2.40, "EUR", "2cl", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Spirituosen"), "Sambuca","Sambuca", 3.90, "EUR", "2cl", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Spirituosen"), "Tequila Silver","Tequila", 2.60, "EUR", "2cl", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Spirituosen"), "Malteserkreuz","Malte", 2.60, "EUR", "2cl", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Spirituosen"), "Williamsbirne","Willi", 2.70, "EUR", "2cl", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Spirituosen"), "Fernet Branca","Branca", 2.80, "EUR", "2cl", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Spirituosen"), "Ramazzotti","Rama", 2.80, "EUR", "2cl", "", new ArrayList<>()),

				// Heiße Getränke - 7
			new MenuItem(menuItemCategoryRepository.findByName("Heiße Getränke"), "Tasse Kaffee","Kaffee", 1.50, "EUR", "mit Milch, Zucker", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Heiße Getränke"), "Cappuccino","Cappu", 2.10, "EUR", "mit Milchschaum", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Heiße Getränke"), "Latte Macchiato","Latte", 2.80, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Heiße Getränke"), "Heiße Schokolade","Schoki", 2.10, "EUR", "mit Schlagsahne", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Heiße Getränke"), "Tasse Espresso","Espresso", 1.50, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Heiße Getränke"), "Glas Tee","Tee", 2.10, "EUR", "mit Kandis und Zitrone", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Heiße Getränke"), "Doppelter Espresso","D-Espersso", 1.50, "EUR", "mit Milch, Zucker", "", new ArrayList<>()),

			//Beilagen- extra kaufen
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Spare Ribs"), "Baked Potato","Bei-Potato", 2.90, "EUR", "frische Ofenkartoffel", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Spare Ribs"), "Pommes frites","Bei-Pommes", 2.70, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Spare Ribs"), "Rösti","Bei-Rösti", 2.70, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Spare Ribs"), "Kroketten","Bei-Kroketten", 2.70, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Spare Ribs"), "Country-Kartoffel","Bei-Country", 2.90, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Spare Ribs"), "Butterreis","Bei-Butter", 1.70, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Spare Ribs"), "Red Beans","Bei-redbean", 3.10, "EUR", "kräftige rote Bohnen mit Speck und Zwiebeln", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Spare Ribs"), "Maiskolben vom Grill","Bei-Kolben", 3.40, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Spare Ribs"), "Frische Champignons","Bei-Champig", 3.10, "EUR", "mit Rahmsauce", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Spare Ribs"), "Frische Sour Cream","Bei-Sour", 1.50, "EUR", "", "", new ArrayList<>()),

			//Beilagen Standard - Sollen nicht im Menu auftauchen
			// Beilage für Pommes
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Std"), "Ketchup","Ketchup", 0.00, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Std"), "Mayonaisse","Mayo", 0.00, "EUR", "", "", new ArrayList<>()),
			// Dessertzubehör
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Std"), "Vanilleeis","Vanilleeis", 0.00, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Std"), "Schokoladeneis","Schokoeis", 0.00, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Std"), "Erdbeereis","Erdbeereis", 0.00, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Std"), "Walnußeis","Walnußeis", 0.00, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Std"), "Zitroneneis","Zitroneneis", 0.00, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Std"), "Schlagsahne","S-Sahne", 0.00, "EUR", "", "", new ArrayList<>()),
			// Kaffee
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Std"), "Kaffeesahne","K-Sahne", 0.00, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Std"), "Milch","Milch", 0.00, "EUR", "", "", new ArrayList<>()),
			// Mixt Wein
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Std"), "Rotwein","R-Wein", 0.00, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Std"), "Weißwein","W-Wein", 0.00, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Std"), "Saurer Sprudel","Sauer", 0.00, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Std"), "Süßer Sprudel","Süßer", 0.00, "EUR", "", "", new ArrayList<>()),
			// Tees
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Std"), "Schwarztee","Schwa-Tee", 0.00, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Std"), "Grüntee","Grüntee", 0.00, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Std"), "Waldfruchttee","Wald-Tee", 0.00, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Beilage - Std"), "Pfefferminztee","Pfeff-Tee", 0.00, "EUR", "", "", new ArrayList<>()),

		};
		for (MenuItem m : menu) this.menuItems.add(menuItemRepository.save(m));
	}

	/**
	 * generate demo restaurant desks
	 */
	private void generateDesks() {
		LOGGER.info("Init demo restaurant desks");
		// NeoVentus Restaurant graphic
		this.desks.add(deskRepository.save(new Desk(10)));
		this.desks.add(deskRepository.save(new Desk(2)));
		this.desks.add(deskRepository.save(new Desk(2)));
		this.desks.add(deskRepository.save(new Desk(2)));
		this.desks.add(deskRepository.save(new Desk(2)));
		this.desks.add(deskRepository.save(new Desk(2)));
		this.desks.add(deskRepository.save(new Desk(2)));
		this.desks.add(deskRepository.save(new Desk(4)));
		this.desks.add(deskRepository.save(new Desk(4)));
		this.desks.add(deskRepository.save(new Desk(2)));
		this.desks.add(deskRepository.save(new Desk(2)));
		this.desks.add(deskRepository.save(new Desk(2)));
		this.desks.add(deskRepository.save(new Desk(2)));
		this.desks.add(deskRepository.save(new Desk(10)));
		this.desks.add(deskRepository.save(new Desk(4)));
		this.desks.add(deskRepository.save(new Desk(4)));
		this.desks.add(deskRepository.save(new Desk(4)));
		this.desks.add(deskRepository.save(new Desk(4)));
		this.desks.add(deskRepository.save(new Desk(4)));
		this.desks.add(deskRepository.save(new Desk(4)));
		// Bar
		this.desks.add(deskRepository.save(new Desk(10)));
	}

	/**
	 * generate demo User
	 */
	private void generateUser() {
		LOGGER.info("Init demo User");
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	// todo: Teilzeit/Vollzeit Markierung
		// generate eight waiters
		this.users.add(userRepository.save(new User("Karl","Karl","Karlson", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Karmen", "Karmen","Kernel",bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Konsti","Konstantin","Kavos", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Kim","Kimberley","Klar", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Katha","Katherina","Keller", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Knut", "Knut","Knutovic",bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		// Teilzeit
		this.users.add(userRepository.save(new User("Kurt", "Kurt","Kordova",bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Katja","Katja","Klein", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));

		// chiefs (or chefs) with 'T' and Tibor is ONLY used for wash the dishes
		this.users.add(userRepository.save(new User("Tim","Timothy", "Totenworth", bCryptPasswordEncoder.encode("tim"), Permission.CHEF)));
		this.users.add(userRepository.save(new User("Tibor","Tibor", "Tarnomoglou", bCryptPasswordEncoder.encode("tim"), Permission.CHEF)));
		this.users.add(userRepository.save(new User("Tami","Tamara","Tanenbaum", bCryptPasswordEncoder.encode("tim"), Permission.CHEF)));
		this.users.add(userRepository.save(new User("Tati","Tatajana", "Tovonoski", bCryptPasswordEncoder.encode("tim"), Permission.CHEF)));
		this.users.add(userRepository.save(new User("Tanar","Tanar","Tenkin", bCryptPasswordEncoder.encode("tim"), Permission.CHEF)));
		// Teilzeit
		this.users.add(userRepository.save(new User("Tobi","Tobias", "Trottwar", bCryptPasswordEncoder.encode("tim"), Permission.CHEF)));
		//CEO
		this.users.add(userRepository.save(new User("Walter","Walter", "Wald", bCryptPasswordEncoder.encode("walter"), Permission.CEO)));

	}

	// DANGER! Here must be Parametres in use for dynamic assignment
	private void updateUserDesk() {
		User u = userRepository.findByUsername("Karl");
		u.getDesks().add(deskRepository.findByNumber(1));
		u.getDesks().add(deskRepository.findByNumber(2));
		u.getDesks().add(deskRepository.findByNumber(3));
		u.getDesks().add(deskRepository.findByNumber(4));
		userRepository.save(u);

		u = userRepository.findByUsername("Katja");
		u.getDesks().add(deskRepository.findByNumber(5));
		u.getDesks().add(deskRepository.findByNumber(6));
		u.getDesks().add(deskRepository.findByNumber(7));
		userRepository.save(u);

		u = userRepository.findByUsername("Knut");
		u.getDesks().add(deskRepository.findByNumber(8));
		u.getDesks().add(deskRepository.findByNumber(9));
		u.getDesks().add(deskRepository.findByNumber(10));
		userRepository.save(u);

	}

	private void generateOrderItem() {
		LOGGER.info("Creating random orders");
		List<OrderItem> orderItems = new ArrayList<>();
		List<User> waiter = new ArrayList<User>();
		List<OrderItemState> states;
		OrderItemState state;
		// Only for Waiters
		users.parallelStream().forEach(user -> {if (user.getPermissions().contains(Permission.SERVICE)) waiter.add(user);});

		OrderItem ord;
		for (int i = 0; i < 50; i++) {
			ord = new OrderItem(
					waiter.get((int) (Math.random() * waiter.size())),
					this.desks.get((int) (Math.random() * this.desks.size())),
					this.menuItems.get((int) (Math.random() * this.menuItems.size())),
					"");
			// For BI-Group new Timestemp
			states = new ArrayList<>();
			state = new OrderItemState(OrderItemState.State.NEW);
			Long millitime =System.currentTimeMillis()-((long)(Math.random()* 7200000)-7200000);
			state.setDate(new Date(millitime));
			states.add(state);
			ord.setStates(states);


			orderItems.add(ord);
		}

		orderItemRepository.save(orderItems);
		LOGGER.info("Finished creating random orders");
	}


	// ------------- START Semantic group: SideDish -------------------------
	private void generateSideDish() {
		LOGGER.info("Creating Sidedishes");
		SideDish sideDish;
		MenuItem menuItem;

		// ----------------------------- Pommes ------------------------------------------------
		sideDish= saveSideDish("Pommesbeilage", "Mayonaisse", "Ketchup");
		saveMenuSideDishItem(sideDish,"Chicken Nuggets", "Paniertes Schnitzel", "Großer Pommes-Teller", "Pommes frites","Schnitzel “Wiener Art”");
		// ----------------------------- Eissorten ---------------------------------------------
		sideDish = saveSideDish("Eissorten","Vanilleeis","Schokoladeneis","Erdbeereis","Walnußeis","Zitroneneis");
		saveMenuSideDishItem(sideDish,"Drei Kugeln Eis","Chicken Nuggets", "Paniertes Schnitzel", "Großer Pommes-Teller");
		// ----------------------------- Kaffee ------------------------------------------------
		sideDish = saveSideDish("Kaffee","Kaffeesahne","Milch");
		saveMenuSideDishItem(sideDish,"Tasse Kaffee");
		// ----------------------------- Sprudel ------------------------------------------------
		sideDish= saveSideDish("Sprudelwahl", "Saurer Sprudel", "Süßer Sprudel");
		saveMenuSideDishItem(sideDish,"Rotwein","Weißwein");
		// ----------------------------- Weinwahl -----------------------------------------------
		sideDish = saveSideDish("Weinwahl","Rotwein","Weißwein");
		saveMenuSideDishItem(sideDish,"Weinschorle");
		// ----------------------------- Apfelstrudel -------------------------------------------
		sideDish = saveSideDish("Apfelstrudel","Vanilleeis","Schlagsahne");
		saveMenuSideDishItem(sideDish,"warmer hausgemachter Apfelstrudel");
		// ----------------------------- Tee ----------------------------------------------------
		sideDish = saveSideDish("Teesorten","Schwarztee","Grüntee","Waldfruchttee","Pfefferminztee");
		saveMenuSideDishItem(sideDish,"Glas Tee");
		// ----------------------------- Spare-Rib ---------------------------------------------
		sideDish = saveSideDish("Spare Ribs","Baked Potato","Pommes frites","Rösti","Krokettten","Country-Kartoffeln","Butterreis","Red Beans","Maiskolben vom Grill","Frische Champignons","Frische Sour Cream");
		saveMenuSideDishItem(sideDish,"Spare Ribs 300g","Spare Ribs 550g");
		// -------------------------------------------------------------------------------------

	}
	private SideDish saveSideDish(String sidename, String ... items){
		SideDish sideDish = new SideDish(sidename);
		this.sideDishRepository.save(sideDish);
		for(String i: items){
			sideDish.addSideDish(menuItemRepository.findByName(i));
		}
		this.sideDishRepository.save(sideDish);
		return sideDish;
	}

	private void saveMenuSideDishItem(SideDish sideDish, String ... menunames){
		MenuItem menuItem;
		for(String s : menunames){
			menuItem = menuItemRepository.findByName(s);
			menuItem.setSideDish(sideDish);
			menuItemRepository.save(menuItem);
		}

	}
	// ------------- END OF Semantic group: SideDish ----------------------------

	// ------------- START Semantic group: MenuCategory -------------------------
	private void generateMenuCategories() {
		LOGGER.info("Generate menu Items");
		ArrayList<MenuItemCategory> tmp = new ArrayList<MenuItemCategory>();

		tmp.add(addCategory("Vorspeise", null));
		tmp.add(addCategory("Hauptspeise", null));
		tmp.add(addCategory("Nachspeise", null));
		tmp.add(addCategory("Getränke", null));
		tmp.add(addCategory("Kinder Gerichte", null));
		tmp.add(addCategory("Beilage - Spare Ribs", null));
		tmp.add(addCategory("Beilage - Std", null));
		//2nd level
		tmp.add(addCategory("Warme Vorspeise", tmp.get(0)));
		tmp.add(addCategory("Kalte Vorspeise", tmp.get(0)));
		tmp.add(addCategory("Suppen", tmp.get(0)));
		//main Dish
		tmp.add(addCategory("Fischgerichte", tmp.get(1)));
		tmp.add(addCategory("Fleischgerichte", tmp.get(1)));
		tmp.add(addCategory("Vegetarische Gerichte", tmp.get(1)));
		//Drinks
		tmp.add(addCategory("Aperitifs", tmp.get(3)));
		tmp.add(addCategory("Alkoholfreie Getränke", tmp.get(3)));
		tmp.add(addCategory("Säfte", tmp.get(3)));
		tmp.add(addCategory("Saftschorle", tmp.get(3)));
		tmp.add(addCategory("Bier", tmp.get(3)));
		tmp.add(addCategory("Wein", tmp.get(3)));
		tmp.add(addCategory("Heiße Getränke", tmp.get(3)));
		tmp.add(addCategory("Spirituosen", tmp.get(3)));

		// Wine
		MenuItemCategory cat = menuItemCategoryRepository.findByName("Wein");
		tmp.add(addCategory("Weißwein",cat));
		tmp.add(addCategory("Rotwein",cat));
		tmp.add(addCategory("Rose",cat));
		tmp.add(addCategory("Mixwein",cat));
		tmp.add(addCategory("Schaumwein",cat));
	}

	// Subfunction for generateMenuCategories
	private MenuItemCategory addCategory(String name, MenuItemCategory parent) {
		MenuItemCategory child = new MenuItemCategory(name);
		child.setParent(parent);
		return menuItemCategoryRepository.save(child);
	}
// -------------END OF Semantic group: MenuCategory -------------------------

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
