//package com.doer.mraims.loanprocess.auth.filter;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
////    private final String SECRET_KEY = "your-secret-key";
//
//    @Value("${access.token.secret}")
//    private String SECRET_KEY;
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String authorizationHeader = request.getHeader("Authorization");
//
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            String token = authorizationHeader.substring(7);
//
//            try {
//                Claims claims = Jwts.parserBuilder()
//                        .setSigningKey(SECRET_KEY.getBytes())
//                        .build()
//                        .parseClaimsJws(token)
//                        .getBody();
//
//                logger.info("Token claims: " + claims);
//                String user_id = claims.getSubject();
//
//                if (username != null) {
//                    // Create a token based on the username (You can create your custom user details here)
//                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                            username, null, null); // Set authorities if needed
//                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                    // Set authentication in SecurityContextHolder
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//            } catch (Exception e) {
//                logger.error("Token validation failed: " + e.getMessage());
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
//
