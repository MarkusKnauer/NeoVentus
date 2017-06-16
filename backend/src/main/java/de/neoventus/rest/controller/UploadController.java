package de.neoventus.rest.controller;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description:
 **/

import de.neoventus.init.WriteExcelInDB;
import de.neoventus.persistence.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Controller
public class UploadController {

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
	private WriteExcelInDB wexc;

	//Save the uploaded file to this folder
	private static String UPLOADED_FOLDER = "";
	private final String URL_PATH = "/api/upload";
	@Autowired
	public UploadController(MenuItemRepository menuItemRepository, MenuItemCategoryRepository menuItemCategoryRepository, UserRepository userRepository, DeskRepository deskRepository, OrderItemRepository orderItemRepository, BillingRepository billingRepository, ReservationRepository reservationRepository, SideDishRepository sideDishRepository, MongoTemplate mongoTemplate, WorkingPlanRepository workingPlanRepository){
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
		 this.wexc =new WriteExcelInDB(
			menuItemRepository,
			menuItemCategoryRepository,
			userRepository,
			deskRepository,
			orderItemRepository,
			billingRepository,
			reservationRepository,
			sideDishRepository,
			mongoTemplate,
			 workingPlanRepository);
	}


	@GetMapping(URL_PATH)
	public String index() {
		return "uploadHTML";
	}

	@PostMapping(URL_PATH+"/updateData/uploadExcel")
	public String excelFileUploadUpdateData(@RequestParam("file") MultipartFile file,
								  RedirectAttributes redirectAttributes) {

		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:uploadStatus";
		}

		try {
			// Get the file and save it somewhere
			byte[] bytes = file.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
			Files.write(path,bytes);
			redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded '" + file.getOriginalFilename() + "'");
			// Check if file is a Excelformat
			if(file.getOriginalFilename().endsWith(".xlsx")){
				this.wexc.readExcelAndWriteintoDB(path,false);
			} else{
				LOGGER.info("No Excel-file");
				redirectAttributes.addFlashAttribute("message",
					"Sorry it isn't an Excelformat (.xlsx) '" + file.getOriginalFilename() + "'");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:"+URL_PATH+"/uploadStatus";
	}

	// Multiple Upload
	@PostMapping(URL_PATH+"/allNew/uploadExcel")
	public String excelFileUploadAllNew(@RequestParam("file") MultipartFile[] multipartFiles,
								  RedirectAttributes redirectAttributes) {

		if (multipartFiles == null || multipartFiles.length == 0 ) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:uploadStatus";
		}

		for(int i = 0; i < multipartFiles.length; i++){
			if(multipartFiles[i].getOriginalFilename().contains("Basedata")){
				MultipartFile tmp = multipartFiles[0];
				multipartFiles[0] = multipartFiles[i];
				multipartFiles[i] = tmp;

			}
		}


		LOGGER.info("Length: " + multipartFiles.length);
		try {
			clearData();
			clearIndexes();
			// Get the file and save it somewhere
			for(MultipartFile files: multipartFiles){
				byte[] bytes = files.getBytes();
				Path path = Paths.get(UPLOADED_FOLDER + files.getOriginalFilename());
				Files.write(path,bytes);
				redirectAttributes.addFlashAttribute("message",
					"You successfully uploaded '" + files.getOriginalFilename() + "'");
				// Check if file is a Excelformat
				if(files.getOriginalFilename().endsWith(".xlsx")){
					this.wexc.readExcelAndWriteintoDB(path, true);
				} else{
					LOGGER.info("No Excel-file");
					redirectAttributes.addFlashAttribute("message",
						"Sorry it isn't an Excelformat (.xlsx) '" + files.getOriginalFilename() + "'");
				}


			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:"+URL_PATH+"/uploadStatus";
	}

	@GetMapping(URL_PATH+"/uploadStatus")
	public String uploadStatus() {
		return "uploadStatus";
	}




	/**
	 * clear before regenerate to allow changes
	 */
	private void clearData() {
		deskRepository.deleteAll();
		menuItemRepository.deleteAll();
		userRepository.deleteAll();
		workingPlanRepository.deleteAll();
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
