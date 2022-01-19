package se.danielmartensson.configurations.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@Configuration
@PropertySource("classpath:login.properties")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";
	public static final String LOGOUT = "/logout";

	@Value("${login.masterPassword}")
	private String masterPassword;

	@Value("${login.masterName}")
	private String masterName;
	
	@Value("${login.masterRights}")
	private String masterRights;
	
	@Value("${login.slavePassword}")
	private String slavePassword;

	@Value("${login.slaveName}")
	private String slaveName;
	
	@Value("${login.slaveRights}")
	private String slaveRights;

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

	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inMemoryUserDetailsManager());
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
    	// Create master user
	    UserDetails masterUser =
	        User.withUsername(masterName)
	            .password("{noop}" + masterPassword)
	            .roles(masterRights)
	            .build();
	    
		 // Create master user
		    UserDetails slaveUser =
		        User.withUsername(slaveName)
		            .password("{noop}" + slavePassword)
		            .roles(slaveRights)
		            .build();
        return new InMemoryUserDetailsManager(masterUser, slaveUser);
    }
}
