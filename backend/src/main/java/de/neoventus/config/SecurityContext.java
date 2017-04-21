package de.neoventus.config;

import de.neoventus.persistence.entity.Permission;
import de.neoventus.rest.auth.NVUserDetailsService;
import de.neoventus.rest.auth.RestAuthenticationEntryPoint;
import de.neoventus.rest.auth.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

/**
 * spring security config class
 *
 * @author Dennis Thanner, Tim Heidelbach
 * @version 0.0.2
 **/
@EnableWebSecurity
@Configuration
public class SecurityContext extends WebSecurityConfigurerAdapter {


	private NVUserDetailsService userDetailsService;

	@Autowired
	public void setUserDetailsService(NVUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	/**
	 * configure security
	 *
	 * @param http the http sercurity
	 * @throws Exception the exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.exceptionHandling()
				.authenticationEntryPoint(new RestAuthenticationEntryPoint())
				.and()
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/api/user").hasRole(Permission.ADMIN.toString())
				.antMatchers(HttpMethod.POST, "/api/reservation").hasAnyRole(
				Permission.SERVICE.toString(),
				Permission.ADMIN.toString())
				.and()
				.formLogin()
				.successHandler(new SavedRequestAwareAuthenticationSuccessHandler())
				.failureHandler(new SimpleUrlAuthenticationFailureHandler())
				.and()
				.logout()
				.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
		;
	}

	/**
	 * set custom auth provider to be used for authentication
	 *
	 * @param auth the authentication manager builder
	 * @throws Exception the exception
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	}

	/**
	 * configure custom user details service and password encoder for auth mechanism
	 *
	 * @return auth provider
	 */
	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
		return authProvider;
	}

}
