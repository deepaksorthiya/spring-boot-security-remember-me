package com.example.config;

import javax.sql.DataSource;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.example.security.CustomAuthenticationProvider;
import com.example.security.CustomJdbcUserDetailManager;
import com.example.security.CustomWebAuthenticationDetailsSource;

/**
 * @author Deepak Katariya
 * @date Nov 28, 2020
 */
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private CustomWebAuthenticationDetailsSource customWebAuthenticationDetailsSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/js/**", "/css/**", "/images/**", "/webjars/**", "/**/favicon.ico",
				"/h2-console/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
			http
			.formLogin()
			.authenticationDetailsSource(customWebAuthenticationDetailsSource)
			.and()
			.rememberMe().tokenRepository(jdbcTokenRepository()).userDetailsService(userDetailsServiceBean())
			.and()
			.authorizeRequests().anyRequest().authenticated();
		// @formatter:on

	}

	private PersistentTokenRepository jdbcTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		return tokenRepository;
	}

	// Expose the UserDetailsService as a Bean
	@Bean(BeanIds.USER_DETAILS_SERVICE)
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {
		CustomJdbcUserDetailManager userDetailsServiceBean = new CustomJdbcUserDetailManager();
		userDetailsServiceBean.setDataSource(dataSource);
		userDetailsServiceBean.setRolePrefix("ROLE_");
		userDetailsServiceBean.setAuthenticationManager(authenticationManagerBean());
		return userDetailsServiceBean;
	}

	// Expose the Authentication Manager as a Bean
	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() throws Exception {
		final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsServiceBean());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
