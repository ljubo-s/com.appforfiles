package com.appforfiles.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.context.request.RequestContextHolder;

import com.appforfiles.model.Log;
import com.appforfiles.model.User;
import com.appforfiles.service.LogService;
import com.appforfiles.service.UserService;

@Configuration
@EnableAutoConfiguration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;
	@Autowired
	private LogService logService;

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	private final String ADMIN = "ADMIN";
	private final String USER = "USER";

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
// @formatter:off
        http.authorizeRequests()
            .antMatchers("/admin/**").hasAnyAuthority(ADMIN)
            .antMatchers("/user/**").hasAnyAuthority(ADMIN, USER)
            .anyRequest().authenticated()
            .and()
            .formLogin().loginPage("/login").permitAll()
            .failureUrl("/login")  
            .successHandler(new AuthenticationSuccessHandler() {
// @formatter:on           	
					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
						
						Date inTime = new Date(System.currentTimeMillis());
						Log log = new Log();
						User user = new User();

						user = userService.getUserByUsername(auth.getName());

						String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();

						log.setUser(user);
						log.setUser_id(user.getId());
						log.setLoginTime(inTime);
						log.setSid(sessionId);

						logService.saveOrUpdate(log);

						if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {

							redirectStrategy.sendRedirect(request, response, "/admin/dashboard");

						} else if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USER"))) {

							redirectStrategy.sendRedirect(request, response, "/user/dashboard");
						}

					}
// @formatter:off
                }).permitAll()
            	  .and()
                  .logout().clearAuthentication(true)
                  .logoutUrl("/logout")
                  .addLogoutHandler(new LogoutHandler() {
// @formatter:on                     
					public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

						Date time = new Date(System.currentTimeMillis());

						try {
							HttpSession session = request.getSession(false);
							Log log = logService.getLogBySessionId(session.getId());

							if (log != null) {
								log.setLogoutTime(time);
								logService.saveOrUpdate(log);
							}
//							session.invalidate();
						} catch (Exception e) {

						}
					}
// @formatter:off                     
                }).invalidateHttpSession(true)
                  .deleteCookies("JSESSIONID")
                  .logoutSuccessUrl("/login")
                  .permitAll()
                  .and()
                  .exceptionHandling()
                  .accessDeniedPage("/403");
     
        http.csrf().disable(); 
// @formatter:on
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/resources/**","/plugins/**","/dist/**","/static/**", "/css/**", "/js/**", "/img/**", "/icon/**");
	}

}