package com.questnr.config;

import com.questnr.security.JwtAuthenticationEntryPoint;
import com.questnr.security.JwtAuthorizationTokenFilter;
import com.questnr.security.JwtTokenUtil;
import com.questnr.security.JwtUserDetailService;
import com.questnr.util.CaseInsensitiveRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailService jwtUserDetailsService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.cookieName}")
    private String tokenCookieName;

    @Value("${jwt.route.authentication.path}")
    private String authenticationPath;

    private static final String[] SECURE_ADMIN_PATTERNS = {"/admin", "/admin/*", "/admin/*/**", "/admin/*/**/**",
            "/admin/*/**/**/**", "/admin/*/**/**/**/**", "/admin/*/**/**/**/**/**", "/admin/*/**/**/**/**/**/**",
            "/admin/*/**/**/**/**/**/**/**", "/admin/*/**/**/**/**/**/**/**/"};

    private static final String[] SECURE_ADMIN_TECHOPS_PATTERNS = {"/admin_techops/*", "/admin_techops/*/**",
            "/admin_techops/*/**/**", "/admin_techops/*/**/**/**", "/admin_techops/*/**/**/**/**",
            "/admin_techops/*/**/**/**/**/**", "/admin_techops/*/**/**/**/**/**/**",
            "/admin_techops/*/**/**/**/**/**/**/**", "/admin_techops/*/**/**/**/**/**/**/**/"};

    private static final String[] SECURE_ADMIN_WRITER_PATTERNS = {"/admin_writer/*", "/admin_writer/*/**",
            "/admin_writer/*/**/**", "/admin_writer/*/**/**/**", "/admin_writer/*/**/**/**/**",
            "/admin_writer/*/**/**/**/**/**", "/admin_writer/*/**/**/**/**/**/**",
            "/admin_writer/*/**/**/**/**/**/**/**", "/admin_writer/*/**/**/**/**/**/**/**/"};

    private static final String[] SECURE_ADMIN_EDITOR_PATTERNS = {"/admin_editor/*", "/admin_editor/*/**",
            "/admin_editor/*/**/**", "/admin_editor/*/**/**/**", "/admin_editor/*/**/**/**/**",
            "/admin_editor/*/**/**/**/**/**", "/admin_editor/*/**/**/**/**/**/**",
            "/admin_editor/*/**/**/**/**/**/**/**", "/admin_editor/*/**/**/**/**/**/**/**/"};

    private static final String[] SECURE_ADMIN_SALES_PATTERNS = {"/admin_sales/*", "/admin_sales/*/**",
            "/admin_sales/*/**/**", "/admin_sales/*/**/**/**", "/admin_sales/*/**/**/**/**",
            "/admin_sales/*/**/**/**/**/**", "/admin_sales/*/**/**/**/**/**/**", "/admin_sales/*/**/**/**/**/**/**/**",
            "/admin_sales/*/**/**/**/**/**/**/**/"};

    private static final String[] SECURE_ADMIN_WRITER_EDITOR_PATTERNS = {"/admin_writer_editor/*",
            "/admin_writer_editor/*/**", "/admin_writer_editor/*/**/**", "/admin_writer_editor/*/**/**/**",
            "/admin_writer_editor/*/**/**/**/**", "/admin_writer_editor/*/**/**/**/**/**",
            "/admin_writer_editor/*/**/**/**/**/**/**", "/admin_writer_editor/*/**/**/**/**/**/**/**",
            "/admin_writer_editor/*/**/**/**/**/**/**/**/"};

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoderBean());
    }

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/").permitAll().antMatchers(HttpMethod.POST, "/api/v1/sign-up").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/sign-up").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/courses").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/search").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/update-password").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/forgot-password").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/admin/*").access("hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.GET, "/api/v1/admin/*/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.GET, "/api/v1/admin/**/**/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.PUT, "/api/v1/admin/*").access("hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.PUT, "/api/v1/admin/*/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.PUT, "/api/v1/admin/**/**/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.POST, "/api/v1/admin/*").access("hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.POST, "/api/v1/admin/*/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.POST, "/api/v1/admin/**/**/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.GET, "/api/v1/sales/*").access("hasRole('ROLE_SALES')")
                .antMatchers(HttpMethod.GET, "/api/v1/sales/*/**").access("hasRole('ROLE_SALES')")
                .antMatchers(HttpMethod.GET, "/api/v1/sales/**/**/**").access("hasRole('ROLE_SALES')")
                .antMatchers(HttpMethod.PUT, "/api/v1/sales/*").access("hasRole('ROLE_SALES')")
                .antMatchers(HttpMethod.PUT, "/api/v1/sales/*/**").access("hasRole('ROLE_SALES')")
                .antMatchers(HttpMethod.PUT, "/api/v1/sales/**/**/**").access("hasRole('ROLE_SALES')")
                .antMatchers(HttpMethod.POST, "/api/v1/sales/*").access("hasRole('ROLE_SALES')")
                .antMatchers(HttpMethod.POST, "/api/v1/sales/*/**").access("hasRole('ROLE_SALES')")
                .antMatchers(HttpMethod.POST, "/api/v1/sales/**/**/**").access("hasRole('ROLE_SALES')")
                .antMatchers(HttpMethod.GET, "/api/v1/editor/*/**").access("hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.GET, "/api/v1/editor/**/**/**").access("hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.PUT, "/api/v1/editor/*").access("hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.PUT, "/api/v1/editor/*/**").access("hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.PUT, "/api/v1/editor/**/**/**").access("hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.POST, "/api/v1/editor/*").access("hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.POST, "/api/v1/editor/*/**").access("hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.POST, "/api/v1/editor/**/**/**").access("hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.GET, "/api/v1/writer/*/**").access("hasRole('ROLE_WRITER')")
                .antMatchers(HttpMethod.GET, "/api/v1/writer/**/**/**").access("hasRole('ROLE_WRITER')")
                .antMatchers(HttpMethod.PUT, "/api/v1/writer/*").access("hasRole('ROLE_WRITER')")
                .antMatchers(HttpMethod.PUT, "/api/v1/writer/*/**").access("hasRole('ROLE_WRITER')")
                .antMatchers(HttpMethod.PUT, "/api/v1/writer/**/**/**").access("hasRole('ROLE_WRITER')")
                .antMatchers(HttpMethod.POST, "/api/v1/writer/*").access("hasRole('ROLE_WRITER')")
                .antMatchers(HttpMethod.POST, "/api/v1/writer/*/**").access("hasRole('ROLE_WRITER')")
                .antMatchers(HttpMethod.POST, "/api/v1/writer/**/**/**").access("hasRole('ROLE_WRITER')")
                .antMatchers(HttpMethod.GET, "/api/v1/admin_techops/*/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_TECHOPS')")
                .antMatchers(HttpMethod.GET, "/api/v1/admin_techops/*")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_TECHOPS')")
                .antMatchers(HttpMethod.GET, "/api/v1/admin_techops/**/**/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_TECHOPS')")
                .antMatchers(HttpMethod.PUT, "/api/v1/admin_techops/*")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_TECHOPS')")
                .antMatchers(HttpMethod.PUT, "/api/v1/admin_techops/*/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_TECHOPS')")
                .antMatchers(HttpMethod.PUT, "/api/v1/admin_techops/**/**/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_TECHOPS')")
                .antMatchers(HttpMethod.POST, "/api/v1/admin_techops/*")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_TECHOPS')")
                .antMatchers(HttpMethod.POST, "/api/v1/admin_techops/*/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_TECHOPS')")
                .antMatchers(HttpMethod.POST, "/api/v1/admin_techops/**/**/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_TECHOPS')")
                .antMatchers(HttpMethod.GET, "/api/v1/admin_writer/*/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_WRITER')")
                .antMatchers(HttpMethod.GET, "/api/v1/admin_writer/**/**/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_WRITER')")
                .antMatchers(HttpMethod.PUT, "/api/v1/admin_writer/*")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_WRITER')")
                .antMatchers(HttpMethod.PUT, "/api/v1/admin_writer/*/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_WRITER')")
                .antMatchers(HttpMethod.PUT, "/api/v1/admin_writer/**/**/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_WRITER')")
                .antMatchers(HttpMethod.POST, "/api/v1/admin_writer/*")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_WRITER')")
                .antMatchers(HttpMethod.POST, "/api/v1/admin_writer/*/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_WRITER')")
                .antMatchers(HttpMethod.POST, "/api/v1/admin_writer/**/**/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_WRITER')")
                .antMatchers(HttpMethod.GET, "/api/v1/admin_writer_editor/*/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_WRITER') or hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.GET, "/api/v1/admin_writer_editor/**/**/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_WRITER') or hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.PUT, "/api/v1/admin_writer_editor/*")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_WRITER') or hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.PUT, "/api/v1/admin_writer_editor/*/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_WRITER') or hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.PUT, "/api/v1/admin_writer_editor/**/**/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_WRITER') or hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.POST, "/api/v1/admin_writer_editor/*")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_WRITER') or hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.POST, "/api/v1/admin_writer_editor/*/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_WRITER') or hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.POST, "/api/v1/admin_writer_editor/**/**/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_WRITER') or hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.POST, "/api/v1/admin_editor/**/**/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.PATCH, "/api/v1/admin_editor/**/**/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.GET, "/api/v1/admin_editor/*/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.GET, "/api/v1/admin_editor/**/**/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.PUT, "/api/v1/admin_editor/*")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.PUT, "/api/v1/admin_editor/*/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.PUT, "/api/v1/admin_editor/**/**/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.POST, "/api/v1/admin_editor/*")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.POST, "/api/v1/admin_editor/*/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.POST, "/api/v1/admin_editor/**/**/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.POST, "/api/v1/admin_sales/**/**/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_SALES')")
                .antMatchers(HttpMethod.GET, "/api/v1/admin_sales/*/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_SALES')")
                .antMatchers(HttpMethod.GET, "/api/v1/admin_sales/**/**/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_SALES')")
                .antMatchers(HttpMethod.PUT, "/api/v1/admin_sales/*")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_SALES')")
                .antMatchers(HttpMethod.PUT, "/api/v1/admin_sales/*/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_SALES')")
                .antMatchers(HttpMethod.PUT, "/api/v1/admin_sales/**/**/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_SALES')")
                .antMatchers(HttpMethod.POST, "/api/v1/admin_sales/*")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_SALES')")
                .antMatchers(HttpMethod.POST, "/api/v1/admin_sales/*/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_SALES')")
                .antMatchers(HttpMethod.POST, "/api/v1/admin_sales/**/**/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_SALES')")
                .antMatchers(HttpMethod.POST, "/api/v1/user/**/**/**").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.GET, "/api/v1/user/*/**").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.GET, "/api/v1/user/**/**/**").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.PUT, "/api/v1/user/*").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.GET, "/api/v1/user/*").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.PUT, "/api/v1/user/*/**").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.PUT, "/api/v1/user/**/**/**").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.POST, "/api/v1/user/*").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.POST, "/api/v1/user/*/**").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.POST, "/api/v1/user/**/**/**").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.GET, "/api/v1/mobile/user/profile").access("hasRole('ROLE_USER')")

                .antMatchers(HttpMethod.GET, SECURE_ADMIN_PATTERNS).access("hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.GET, SECURE_ADMIN_TECHOPS_PATTERNS)
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_TECHOPS')")
                .antMatchers(HttpMethod.POST, SECURE_ADMIN_TECHOPS_PATTERNS)
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_TECHOPS')")
                .antMatchers(HttpMethod.GET, SECURE_ADMIN_WRITER_PATTERNS)
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_WRITER')")
                .antMatchers(HttpMethod.GET, SECURE_ADMIN_EDITOR_PATTERNS)
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
                .antMatchers(HttpMethod.GET, SECURE_ADMIN_SALES_PATTERNS)
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_SALES')")
                .antMatchers(HttpMethod.GET, SECURE_ADMIN_WRITER_EDITOR_PATTERNS)
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_WRITER') or hasRole('ROLE_EDITOR')")

                .antMatchers(HttpMethod.GET, "/approver_admin/*/**")
                .access("hasRole('ROLE_APPROVER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.GET, "/approver_techops_admin/*/**")
                .access("hasRole('ROLE_APPROVER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_TECHOPS')")
                .antMatchers(HttpMethod.GET, "/techops_admin/*/**")
                .access("hasRole('ROLE_TECHOPS') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.GET, "/documents/**")
                .access("hasRole('ROLE_USER')");

        httpSecurity.authorizeRequests().antMatchers("/resources/**").permitAll().anyRequest().permitAll();

        // Custom JWT based security filter
//
        JwtAuthorizationTokenFilter authenticationTokenFilter = new JwtAuthorizationTokenFilter(userDetailsService(),
                jwtTokenUtil, tokenHeader, tokenCookieName);
        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        CaseInsensitiveRequestFilter caseInsensitiveRequestFilter = new CaseInsensitiveRequestFilter();
        httpSecurity.addFilterAfter(caseInsensitiveRequestFilter, JwtAuthorizationTokenFilter.class);
        httpSecurity.headers().cacheControl();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // AuthenticationTokenFilter will ignore the below paths
        web.ignoring().antMatchers(HttpMethod.POST, authenticationPath)
                // allow anonymous resource requests
                .and().ignoring().antMatchers(HttpMethod.GET, "/", "/*.html", "**/favicon.ico", "/**/*.html",
                "/**/*.css", "/**/*.png", "/**/*.jpg", "/**/*.jpeg", "/**/*.svg", "/**/*.woff", "/**/*.ttf",
                "/**/*.js");
    }

    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }
}

