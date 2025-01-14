package de.neoventus.rest.controller.ExceptionHandler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description:
 **/
@ControllerAdvice
public class GlobalExceptionHandler {

	//https://jira.spring.io/browse/SPR-14651
	//Spring 4.3.5 supports RedirectAttributes
	@ExceptionHandler(MultipartException.class)
	public String handleError1(MultipartException e, RedirectAttributes redirectAttributes) {

		redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
		return "redirect:/uploadStatus";

	}

    /* Spring < 4.3.5
	@ExceptionHandler(MultipartException.class)
    public String handleError2(MultipartException e) {

        return "redirect:/errorPage";

    }*/

}
