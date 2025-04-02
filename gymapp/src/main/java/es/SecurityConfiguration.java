package es;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
}

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
    @Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.authenticationProvider(authenticationProvider());
		
		
		    http.authorizeHttpRequests(authorize -> authorize
					// PUBLIC PAGES
					.requestMatchers("/").permitAll()
					// PRIVATE PAGES
					.requestMatchers("/private").hasAnyRole("USER", "ADMIN")
					.requestMatchers("/admin").hasRole("ADMIN")
			)
			.formLogin(formLogin -> formLogin
					.loginPage("/account/login")
					.failureUrl("/account/login?error")
					.defaultSuccessUrl("/account/accountMessage")
					.permitAll()
			)
			.logout(logout -> logout
					.logoutUrl("/")
					.logoutSuccessUrl("/")
					.permitAll()
			);
		
		// Disable CSRF at the moment
		http.csrf(csrf -> csrf.disable());

		return http.build();
	}
}

