//package com.example.ats.infrastructure.config;
//
//import com.example.ats.application.port.out.UserRepository;
//import com.example.ats.application.service.JWTService;
//import com.example.ats.domain.model.Role;
//import com.example.ats.domain.model.User;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.List;
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//    private static final String BEARER_PREFIX = "Bearer ";
//
//    private final JWTService jwtService;
//    private final UserRepository userRepository;
//
//    public JwtAuthenticationFilter(JWTService jwtService, UserRepository userRepository) {
//        this.jwtService = jwtService;
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        String authorizationHeader = request.getHeader("Authorization");
//
//        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = authorizationHeader.substring(BEARER_PREFIX.length());
//        if (!jwtService.isTokenValid(token) || SecurityContextHolder.getContext().getAuthentication() != null) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        try {
//            Long userId = jwtService.getUserIdFromToken(token);
//            Role role = jwtService.getRoleFromToken(token);
//            User user = userRepository.findById(userId);
//
//            if (Boolean.TRUE.equals(user.getActive()) && role == user.getRole()) {
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                        user,
//                        null,
//                        List.of(new SimpleGrantedAuthority("ROLE_" + role.name()))
//                );
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        } catch (RuntimeException ignored) {
//            SecurityContextHolder.clearContext();
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
