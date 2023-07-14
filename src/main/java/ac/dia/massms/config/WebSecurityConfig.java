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
				.antMatchers("/mass/**/member/request").hasAnyAuthority("USER", "MEMBER")
				.antMatchers("/mass/**/meal/date/member/**").hasAnyAuthority("USER", "MEMBER")
				.antMatchers("/mass/**/meal").hasAnyAuthority("USER", "MEMBER", "MANAGER", "ADMIN")
				.antMatchers("/mass/**/meal/date").hasAnyAuthority("USER", "MEMBER", "MANAGER")
				.antMatchers("/mass/**/meal/date/member/**").hasAnyAuthority("USER", "MEMBER", "MANAGER")
				.antMatchers("/mass/**/meal/time/new").hasAnyAuthority("ADMIN", "CREATOR", "MANAGER")
				.antMatchers("/user/**").hasAnyAuthority("ADMIN")
				.and()
			.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/do-login")
				.defaultSuccessUrl("/")
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
