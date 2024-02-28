package org.stadium.userapi.security.jwt;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.stadium.corelib.domain.UserSession;
import org.stadium.userapi.security.DomainUserDetailsService;
import org.stadium.userapi.security.UserNotActivatedException;
import org.stadium.userapi.security.enumaration.JwtTokenStatus;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Autowired
    private JwtTokenProvider tokenProvider;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    @Autowired
    private DomainUserDetailsService userDetailsService;


    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getJwtFromRequest(httpServletRequest);
        if (!httpServletRequest.getServletPath().contains("refresh")) {
            JwtTokenStatus jwtTokenStatus = tokenProvider.validateToken(jwt).get("status");

            if (jwtTokenStatus == JwtTokenStatus.EXPIRED || jwtTokenStatus == JwtTokenStatus.MALFORMED) {
                StringBuilder sb = new StringBuilder();
                sb.append("{ ");
                sb.append("\"error\": \"Unauthorized\",");
                sb.append("\"message\": \"Invalid Token.\",");
                sb.append("\"path\": \"")
                        .append(httpServletRequest.getRequestURL())
                        .append("\"");
                sb.append("} ");
                httpServletResponse.setContentType("application/json");
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpServletResponse.getWriter().write(sb.toString());
                return;
            }
            try {
                if (StringUtils.hasText(jwt) && jwtTokenStatus == JwtTokenStatus.VALID) {
                    Long userId = tokenProvider.getUserIdFromJWT(jwt);
                    UserSession session = userDetailsService.loadUserSessionByJwt(jwt);
                    UserDetails userDetails = userDetailsService.loadUserById(userId, session);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, session, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (UserNotActivatedException | UsernameNotFoundException ex) {
                logger.error("Could not set user authentication in security context", ex);
                throw ex;
            } catch (Exception ex) {
                logger.error("Could not set user authentication in security context", ex);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
