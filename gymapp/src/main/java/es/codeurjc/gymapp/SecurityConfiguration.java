package es.codeurjc.gymapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import es.codeurjc.gymapp.repositories.RepositoryUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	public RepositoryUserDetailsService userDetailService;

	@Bean
	public PasswordEncoder passwordEncoder() {  
		return new BCryptPasswordEncoder(); 
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
    @Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.authenticationProvider(authenticationProvider());
		
		    http.authorizeHttpRequests(authorize -> authorize
					// PRIVATE PAGES
					.requestMatchers("/exercise/add").hasRole("ADMIN")
					.requestMatchers("/exercise/exercise_selectToDelete").hasRole("ADMIN")
					.requestMatchers("/exercise/delete").hasRole("ADMIN")
					.requestMatchers("/machinery/save").hasRole("ADMIN")
					.requestMatchers("/machinery/add").hasRole("ADMIN")
					.requestMatchers("/machinery/delete/{id}").hasRole("ADMIN")
					.requestMatchers("/trainer/add").hasRole("ADMIN")
					.requestMatchers("/trainer/{id}/delete").hasRole("ADMIN")
					.requestMatchers("/trainer/add/form").hasRole("ADMIN")
					.requestMatchers("/admin/users").hasRole("ADMIN")
					// PUBLIC PAGES
					.requestMatchers("/**").permitAll()
			)
			.formLogin(formLogin -> formLogin
					.loginPage("/account")
					.loginProcessingUrl("/account/login")
					.defaultSuccessUrl("/account")
					.failureUrl("/account/loginError") 
					.permitAll()
			)
			.logout(logout -> logout
					.logoutUrl("/account/logout")
					.logoutSuccessUrl("/")
					.permitAll()
			);

		return http.build();
	}

	@Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
			.userDetailsService(userDetailService)
			.passwordEncoder(passwordEncoder())
			.and()
			.build();  
    }
}

