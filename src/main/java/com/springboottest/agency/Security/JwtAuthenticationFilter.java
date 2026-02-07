

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

// @Component
// public class JwtAuthenticationFilter extends OncePerRequestFilter {

//     private final JwtHelper jwtHelper;
//     private final UserDetailsService userDetailsService;

//     public JwtAuthenticationFilter(
//             JwtHelper jwtHelper,
//             UserDetailsService userDetailsService
//     ) {
//         this.jwtHelper = jwtHelper;
//         this.userDetailsService = userDetailsService;
//     }

//     // ✅ IMPORTANT: SKIP FILTER FOR OPTIONS + PUBLIC APIs
//    @Override
// protected boolean shouldNotFilter(HttpServletRequest request) {
//     String path = request.getServletPath();
//     return path.startsWith("/auth/")
//         || path.startsWith("/inventory/")
//         || path.startsWith("/vendor/")
//         || path.startsWith("/stock/")
//         || path.startsWith("/sale/")
//         || path.startsWith("/purchase/")
//         || path.startsWith("/user/")
//         || path.startsWith("/swagger")
//         || path.startsWith("/v3/api-docs");
// }


//     @Override
//     protected void doFilterInternal(
//             HttpServletRequest request,
//             HttpServletResponse response,
//             FilterChain filterChain
//     ) throws ServletException, IOException {

//         String authHeader = request.getHeader("Authorization");

//         // ✅ If no JWT, just continue (Spring Security will handle auth)
//         if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//             filterChain.doFilter(request, response);
//             return;
//         }

//         String token = authHeader.substring(7);
//         String username;

//         try {
//             username = jwtHelper.extractUsername(token);
//         } catch (Exception e) {
//             response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT");
//             return;
//         }

//         if (SecurityContextHolder.getContext().getAuthentication() == null) {

//             UserDetails userDetails =
//                     userDetailsService.loadUserByUsername(username);

//             if (jwtHelper.validateToken(token, userDetails)) {

//                 UsernamePasswordAuthenticationToken authentication =
//                         new UsernamePasswordAuthenticationToken(
//                                 userDetails,
//                                 null,
//                                 userDetails.getAuthorities()
//                         );

//                 authentication.setDetails(
//                         new WebAuthenticationDetailsSource()
//                                 .buildDetails(request)
//                 );

//                 SecurityContextHolder.getContext()
//                         .setAuthentication(authentication);
//             }
//         }

//         filterChain.doFilter(request, response);
//     }
// }



@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtHelper jwtHelper,
            UserDetailsService userDetailsService
    ) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
    }

    // ✅ SKIP JWT FOR PUBLIC APIS
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI(); // 🔥 IMPORTANT CHANGE

        return path.startsWith("/auth")
            || path.startsWith("/user")
            || path.startsWith("/vendor")
            || path.startsWith("/inventory")
            || path.startsWith("/sale")
            || path.startsWith("/purchase")
            || path.startsWith("/stock")
            || path.startsWith("/swagger")
            || path.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // ✅ No JWT → continue
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
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

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            if (jwtHelper.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
