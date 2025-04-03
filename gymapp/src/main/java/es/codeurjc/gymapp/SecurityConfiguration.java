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
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
	/* 
	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		UserDetails user = User.builder()
				.username("user")
				.password(passwordEncoder().encode("pass"))
				.roles("USER")
				.build();
		UserDetails admin = User.builder()
				.username("admin")
				.password(passwordEncoder().encode("adminpass"))
				.roles("USER","ADMIN")
				.build();
		return new InMemoryUserDetailsManager(user, admin);
	}
	*/
    @Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.authenticationProvider(authenticationProvider());
		
		    http.authorizeHttpRequests(authorize -> authorize
					// PUBLIC PAGES
					.requestMatchers("/**").permitAll()
					// PRIVATE PAGES
					//.requestMatchers("/exercise/add").hasAnyRole("USER", "ADMIN")
					.requestMatchers("/exercise/add").hasRole("ADMIN")
					.requestMatchers("/exercise/exercise_selectToDelete").hasRole("ADMIN")
					.requestMatchers("/exercise/delete").hasRole("ADMIN")
					.requestMatchers("/machinery/save").hasRole("ADMIN")
					.requestMatchers("/machinery/add").hasRole("ADMIN")
					.requestMatchers("/machinery/delete/{id}").hasRole("ADMIN")
					.requestMatchers("/trainer/add").hasRole("ADMIN")
					.requestMatchers("/trainer/{id}/delete").hasRole("ADMIN")
					.requestMatchers("/trainer/add/form").hasRole("ADMIN")
					.requestMatchers("/trainer/image/{id}").hasRole("ADMIN")
					.requestMatchers("/admin/users").hasRole("ADMIN")
			)
			/* .formLogin(formLogin -> formLogin
					.loginPage("/account/login")
					.failureUrl("/account/login?error") 
					.defaultSuccessUrl("/account")
					.permitAll()
			)*/
			.logout(logout -> logout
					.logoutUrl("/account/logout")
					.logoutSuccessUrl("/")
					.permitAll()
			);
		
		// Disable CSRF at the moment
		http.csrf(csrf -> csrf.disable());

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

