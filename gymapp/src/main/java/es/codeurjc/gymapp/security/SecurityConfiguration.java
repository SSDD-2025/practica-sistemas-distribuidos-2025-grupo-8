package es.codeurjc.gymapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import es.codeurjc.gymapp.repositories.RepositoryUserDetailsService;
import es.codeurjc.gymapp.security.jwt.JwtRequestFilter;
import es.codeurjc.gymapp.security.jwt.UnauthorizedHandlerJwt;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	public RepositoryUserDetailsService userDetailService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
  	private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

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
	@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
		
		http.authenticationProvider(authenticationProvider());
		
		http
			.securityMatcher("/api/**")
			.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));
		
		http
			.authorizeHttpRequests(authorize -> authorize
                    // PRIVATE ENDPOINTS /* 
					//exercises
                    .requestMatchers(HttpMethod.GET,"/api/exercises/**").hasAnyRole("USER","ADMIN")
                    .requestMatchers(HttpMethod.POST,"/api/exercises").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE,"/api/exercises/**").hasRole("ADMIN") 
					//machineries
					.requestMatchers(HttpMethod.GET,"/api/machineries/**").hasAnyRole("USER","ADMIN")
                    .requestMatchers(HttpMethod.POST,"/api/machineries").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE,"/api/machineries/**").hasRole("ADMIN")
					//routines
					.requestMatchers(HttpMethod.GET,"/api/routines").hasAnyRole("USER","ADMIN")
					.requestMatchers(HttpMethod.GET,"/api/routines/**").hasAnyRole("USER","ADMIN")
                    .requestMatchers(HttpMethod.POST,"/api/routines").hasAnyRole("USER","ADMIN")
					.requestMatchers(HttpMethod.PUT,"/api/routines/**").hasAnyRole("USER","ADMIN")
                    .requestMatchers(HttpMethod.DELETE,"/api/routines/**").hasAnyRole("USER","ADMIN")  
					//trainer				
                    .requestMatchers(HttpMethod.POST,"/api/trainer").hasRole("ADMIN")
					.requestMatchers(HttpMethod.PUT,"/api/trainer/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE,"/api/trainer/**").hasRole("ADMIN") 
					//user
					//.requestMatchers(HttpMethod.GET,"/api/user").hasAnyRole("USER","ADMIN")
					.requestMatchers(HttpMethod.GET,"/api/user/**").hasRole("ADMIN")
					.requestMatchers(HttpMethod.PUT,"/api/user/**").hasAnyRole("USER","ADMIN")
                    .requestMatchers(HttpMethod.DELETE,"/api/user/**").hasAnyRole("USER","ADMIN") 
					// PUBLIC ENDPOINTS 
					.anyRequest().permitAll()
			);
		
        // Disable Form login Authentication
        http.formLogin(formLogin -> formLogin.disable());

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf(csrf -> csrf.disable());

        // Disable Basic Authentication
        http.httpBasic(httpBasic -> httpBasic.disable());

        // Stateless session
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Add JWT Token filter
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

    @Bean
	@Order(2)
	public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
		
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

