/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.CorsFilter;
import tech.qiantong.qdata.security.config.properties.PermitAllUrlProperties;
import tech.qiantong.qdata.security.filter.JwtAuthenticationTokenFilter;
import tech.qiantong.qdata.security.handle.AuthenticationEntryPointImpl;
import tech.qiantong.qdata.security.handle.LogoutSuccessHandlerImpl;

/**
 * spring security configuration
 *
 * @author qdata
 */
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
public class SecurityConfig
{
    /**
     * Custom user authentication logic
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Authentication failure handling class
     */
    @Autowired
    private AuthenticationEntryPointImpl unauthorizedHandler;

    /**
     * Exit processing class
     */
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    /**
     * token authentication filter
     */
    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    /**
     * Cross domain filter
     */
    @Autowired
    private CorsFilter corsFilter;

    /**
     * Addresses that allow anonymous access
     */
    @Autowired
    private PermitAllUrlProperties permitAllUrl;

    /**
     * Authentication implementation
     */
    @Bean
    public AuthenticationManager authenticationManager()
    {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }

    /**
     * anyRequest | Matches all request paths
     * access | It can be accessed when the SpringEl expression result is true
     * anonymous | Anonymous can access
     * denyAll | User cannot access
     * fullyAuthenticated | Users are fully authenticated and can access (automatic login without remember-me)
     * hasAnyAuthority | If there are parameters and the parameters represent permissions, any one of them can be accessed
     * hasAnyRole | If there are parameters and the parameters represent roles, any one of the roles can access
     * hasAuthority | If there is a parameter and the parameter represents the permission, then its permission can be accessed
     * hasIpAddress | If there is a parameter, the parameter represents the IP address. If the user IP matches the parameter, it can be accessed
     * hasRole | If there is a parameter and the parameter represents a role, its role can access
     * permitAll | Users can access at will
     * rememberMe | Allow access to users logged in via remember-me
     * authenticated | Accessible after logging in as cacheManager user
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception
    {
        return httpSecurity
                // CSRF disabled because session is not used
                .csrf(csrf -> csrf.disable())
                // Disable HTTP response headers
                .headers((headersCustomizer) -> {
                    headersCustomizer.cacheControl(cache -> cache.disable()).frameOptions(options -> options.sameOrigin());
                })
                // Authentication failure handling class
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                // Based on token, so no session is required
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Annotation marks URLs that allow anonymous access
                .authorizeHttpRequests((requests) -> {
                    permitAllUrl.getUrls().forEach(url -> requests.antMatchers(url).permitAll());
                    // For login login registration register verification code captchaImage allows anonymous access
                    requests.antMatchers("/login","/updater/**","/register","/**", "/captchaImage", "/flyflow/**","/services/**","/jmreport/**", "/rp/**", "/drag/**", "/jimubi/**").permitAll()
                            // Static resources, accessible anonymously
                            .antMatchers(HttpMethod.GET, "/",
                                    "/*.html",
                                    "/**/*.html",
                                    "/**/*.css",
                                    "/**/*.js",
                                    "/static/**",
                                    "/index/**",
                                    "/admin/**",
                                    "/assets/**",
                                    "/profile/**",
                                    "/sso/**",
                                    "/favicon.ico",
                                    "/docs/**"
                            ).permitAll()
                            .antMatchers("/swagger-ui.html",
                                    "/swagger-resources/**",
                                    "/webjars/**",
                                    "/*/api-docs",
                                    "/v3/api-docs/**",
                                    "/druid/**",
                                    "/websocket/**",
                                    "/payment/**",
                                    "/syncData/**",
                                    "/sys/**",
                                    "/oauth2/**",
                                    // Scheduler whitelist
                                    "/mc/taskExecutor/runExecuteTask/**"
                            ).permitAll()
                            // All requests except the above require authentication and authentication
                            .anyRequest().authenticated();
                })
                // Add Logout filter
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler))
                // Add JWT filter
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // Add CORS filter
                .addFilterBefore(corsFilter, JwtAuthenticationTokenFilter.class)
                .addFilterBefore(corsFilter, LogoutFilter.class)
                .build();
    }

    /**
     * Strong hash encryption implementation
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
