package se.danielmartensson.configurations.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity 
@Configuration 
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";
	public static final String LOGOUT = "/logout";
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http.csrf().disable()  
	        .requestCache().requestCache(new CustomRequestCache()) 
	        .and().authorizeRequests() 
	        .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()  

	        .anyRequest().authenticated()  

	        .and().formLogin()  
	        .loginPage(LOGIN_URL).permitAll()
	        .loginProcessingUrl(LOGIN_PROCESSING_URL)  
	        .failureUrl(LOGIN_FAILURE_URL)
	        .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL); 
	}

	@Override
	public void configure(WebSecurity web) {
	    web.ignoring().antMatchers(
	        "/VAADIN/**",
	        "/favicon.ico",
	        "/robots.txt",
	        "/manifest.webmanifest",
	        "/sw.js",
	        "/offline.html",
	        "/icons/**",
	        "/images/**",
	        "/styles/**",
	        "/h2-console/**");
	}
}