package com.homework.security;

import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ResourceUtils;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
	@Autowired
	private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				// 授权管理，permitAll()为不需要任何权限，即游客即可访问。.authenticated()则为必须登录才可访问。
				.authorizeRequests()//
				.regexMatchers(HttpMethod.GET, "/").permitAll()// 主页
				.regexMatchers(HttpMethod.GET, "/login").permitAll()// 跳转到登录页面
				.regexMatchers(HttpMethod.GET, "/[0-9]*").permitAll()// 传入某个参数后跳转到主页
				.regexMatchers(HttpMethod.GET, "/up/[0-9]*").authenticated()// 排序
				.regexMatchers(HttpMethod.GET, "/down/[0-9]*").authenticated()// 排序
				.regexMatchers(HttpMethod.GET, "/delete/[0-9]*").authenticated()// 删除资源
				.regexMatchers(HttpMethod.GET, "/update/[0-9]*").authenticated()// 跳转到资源修改页
				.regexMatchers(HttpMethod.POST, "/update").authenticated()// 修改资源
				
				.regexMatchers(HttpMethod.POST, "/upload/directory").authenticated()// 新建文件夹
				.regexMatchers(HttpMethod.POST, "/upload/file").authenticated()// 上传文件

				.regexMatchers(HttpMethod.POST, "/download/[0-9]*").permitAll()// 下载

				.anyRequest().permitAll()// 剩下的url一律不需要任何权限

				// 认证管理
				// loginPage()设置登录url，当用户访问需要登录才能访问的路径且此时用户没有登录时会自动重定向到该url。
				// loginProcessingUrl()设置认证url，当用户访问该路径时会自动跳转到MyUserDetailsService进行认证处理。
				.and().formLogin().loginPage("/login").loginProcessingUrl("/authentication/authenticate")
				.successHandler(myAuthenticationSuccessHandler).failureHandler(myAuthenticationFailureHandler)

				// 退出管理
				// logoutUrl()设置退出url，当用户访问该url时会删除用户的登录数据，并重定向到logoutSuccessUrl()中所设置的路径，同时删除deleteCookies()中设置的cookie
				.and().logout().logoutUrl("/authentication/logout").logoutSuccessUrl("/").deleteCookies("JSESSIONID")
				.and().csrf().disable();
	}

	// 用于注册后自动登录
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
