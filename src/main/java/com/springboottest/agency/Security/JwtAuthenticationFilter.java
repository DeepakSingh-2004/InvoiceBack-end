// package com.springboottest.agency.Security;

// import java.io.IOException;

// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// @Component
// public class JwtAuthenticationFilter extends OncePerRequestFilter {

//     private final JwtHelper jwtHelper;
//     private final UserDetailsService userDetailsService;

//     // ✅ Constructor Injection to avoid circular dependency
//     public JwtAuthenticationFilter(JwtHelper jwtHelper, UserDetailsService userDetailsService) {
//         this.jwtHelper = jwtHelper;
//         this.userDetailsService = userDetailsService;
//     }

//     @Override
//     protected void doFilterInternal(HttpServletRequest request,
//                                     HttpServletResponse response,
//                                     FilterChain filterChain)
//             throws ServletException, IOException {

//         // ✅ JWT token ko request header se nikalna
//         String authHeader = request.getHeader("Authorization");
//         String username = null;
//         String token = null;

//         if (authHeader != null && authHeader.startsWith("Bearer ")) {
//             token = authHeader.substring(7);
//             try {
//                 username = jwtHelper.extractUsername(token);
//             // } catch (Exception e) {
//             //     System.out.println("⚠️ Invalid token: " + e.getMessage());
//              } catch (Exception e) {
//                   response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT");
//                   return; // 🔴 VERY IMPORTANT
//             }
//         }

//         // ✅ Agar username mila aur user abhi tak authenticate nahi hai to authenticate karo
//         if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//             UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

//             if (jwtHelper.validateToken(token, userDetails)) {
//                 UsernamePasswordAuthenticationToken authentication =
//                         new UsernamePasswordAuthenticationToken(
//                                 userDetails, null, userDetails.getAuthorities());

//                 authentication.setDetails(
//                         new WebAuthenticationDetailsSource().buildDetails(request));

//                 SecurityContextHolder.getContext().setAuthentication(authentication);
//             }
//         }

//         // ✅ Filter chain ko continue karna
//         filterChain.doFilter(request, response);
//     }
// }





package com.springboottest.agency.Security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtHelper jwtHelper, UserDetailsService userDetailsService) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
    }

   @Override
protected boolean shouldNotFilter(HttpServletRequest request) {
  String path = request.getServletPath();
  return path.startsWith("/auth/")
      || path.startsWith("/inventory/");
}


    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        System.out.println("JWT FILTER → " + uri);

        // ✅ ONLY AUTH / LOGIN APIs ARE PUBLIC
        if (uri.startsWith("/auth") ) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔐 ALL OTHER APIs REQUIRE JWT
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
             response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing JWT");
        return;
         }

        String token = authHeader.substring(7);
        String username;

        try {
            username = jwtHelper.extractUsername(token);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT");
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtHelper.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
