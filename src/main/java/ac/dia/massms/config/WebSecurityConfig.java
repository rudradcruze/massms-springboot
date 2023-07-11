package ac.dia.massms.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/").permitAll()
	//			.antMatchers("/product/new/**").hasAnyAuthority("ADMIN", "CREATOR")
	//			.antMatchers("/product/edit/**").hasAnyAuthority("ADMIN", "EDITOR")
	//			.antMatchers("/product/delete/**").hasAuthority("ADMIN")
	//			.antMatchers("/meal/time/new/**").hasAnyAuthority("ADMIN", "CREATOR")
	//			.antMatchers("/meal/time/edit/**").hasAnyAuthority("ADMIN", "EDITOR")
	//			.antMatchers("/meal/time/delete/**").hasAuthority("ADMIN")
	//			.antMatchers("/meal/new/**").hasAnyAuthority("ADMIN", "CREATOR")
	//			.antMatchers("/meal/edit/**").hasAnyAuthority("ADMIN", "EDITOR")
	//			.antMatchers("/meal/delete/**").hasAuthority("ADMIN")
	//			.antMatchers("/meal/date/new/**").hasAnyAuthority("ADMIN", "CREATOR")
	//			.antMatchers("/meal/date/edit/**").hasAnyAuthority("ADMIN", "EDITOR")
	//			.antMatchers("/meal/date/delete/**").hasAuthority("ADMIN")
				.antMatchers("/user/**").hasAnyAuthority("ADMIN")
				.antMatchers("/mass/new").hasAnyAuthority("ADMIN", "MANAGER")
				.antMatchers("/mass/edit/**").hasAnyAuthority("ADMIN", "MANAGER")
				.antMatchers("/mass/delete/**").hasAnyAuthority("ADMIN", "MANAGER")
				.and()
			.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/do-login")
				.defaultSuccessUrl("/mass")
				.permitAll()
				.and()
			.logout()
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout")
				.permitAll()
				.and()
				.exceptionHandling().accessDeniedPage("/403");
	}
}
