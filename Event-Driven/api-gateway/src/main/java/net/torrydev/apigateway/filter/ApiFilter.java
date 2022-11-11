package net.torrydev.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class ApiFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Do some checking here - e.g. Log info about the request
        ServerHttpRequest request = exchange.getRequest();
        log.info(" Request Received ==>> {}", request);
        // session, authentication / authorisation
        log.trace(" Request Local address {} And Remote address {}",
                request.getLocalAddress(), request.getRemoteAddress());
        log.trace(" Session ==> {}", exchange.getSession().blockOptional().get());
        return chain.filter(exchange);
    }
}
