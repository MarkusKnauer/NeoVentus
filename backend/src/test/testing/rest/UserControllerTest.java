package testing.rest;

import de.neoventus.rest.dto.UserDto;
import org.junit.Test;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * tests for user controller
 *
 * @author Dennis Thanner
 * @version 0.0.1
 */
public class UserControllerTest extends AbstractControllerTest {

	private static String controllerUrl = "/api/user";

	/**
	 * test get user principal method
	 *
	 * @see de.neoventus.rest.controller.UserController#getUserPrincipal(HttpServletResponse, Principal)
	 */
	@Test
	public void testGetUserPrincipal() throws Exception {
		this.mockMvc.perform(get(controllerUrl).session(this.authSession)).andExpect(status().isOk()).andExpect(jsonPath("principal.username").value("Test"));
	}

	/**
	 * test insert user by dto is ok
	 */
	@Test
	public void testInsertUserDtoOk() throws Exception {

		UserDto dto = new UserDto();
		dto.setUsername("dto username");

		this.mockMvc.perform(post(controllerUrl).session(this.authSession).content(this.json(dto))
			.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk());
	}

	/**
	 * test insert user by dto is not ok
	 */
	@Test
	public void testInsertUserDtoNotOk() throws Exception {

		UserDto dto = new UserDto();
		dto.setLastName("test lastname");

		this.mockMvc.perform(post(controllerUrl).session(this.authSession).content(this.json(dto))
			.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isBadRequest());
	}

}
