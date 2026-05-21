package com.tenantflow.security;

import com.tenantflow.exception.RateLimitException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    /*
     * Store request counts
     */
    private final Map<String, Integer> requestCounts = new ConcurrentHashMap<>();

    /*
     * Store timestamps
     */
    private final Map<String, Long> requestTimes = new ConcurrentHashMap<>();

    /*
     * Limit
     */
    private static final int MAX_REQUESTS = 10;

    /*
     * Time window
     */
    private static final long WINDOW = 60 * 1000; // 1 minute

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String client = request.getRemoteAddr();
        long currentTime = System.currentTimeMillis();

        requestTimes.putIfAbsent(client, currentTime);
        requestCounts.putIfAbsent(client, 0);

        long windowStart = requestTimes.get(client);

        /*
         * Reset window
         */
        if (currentTime - windowStart > WINDOW) {
            requestTimes.put(client, currentTime);
            requestCounts.put(client, 0);
        }

        /*
         * Increment count
         */
        int count = requestCounts.get(client) + 1;
        requestCounts.put(client, count);

        /*
         * Limit exceeded
         */
        if (count > MAX_REQUESTS) {
            // We can directly send the error response if we want, or throw an exception.
            // Using HandlerExceptionResolver is required for Spring Security filters to reach @ControllerAdvice.
            // But since Spring Boot 3+, throwing an exception in a filter might result in a 500 error 
            // if not explicitly forwarded to the ErrorController. 
            // To ensure it hits the GlobalExceptionHandler, we can use the HandlerExceptionResolver.
            // But for simplicity and following the tutorial, we throw it. 
            // In a real app, you might want to do: response.sendError(429, "Too many requests");
            throw new RateLimitException("Too many requests. Try again later.");
        }

        filterChain.doFilter(request, response);
    }
}
