package io.wisoft.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

  final StringRedisTemplate redisTemplate;

  public CustomFilter(final StringRedisTemplate redisTemplate) {
    super(Config.class);
    this.redisTemplate = redisTemplate;
  }

  @Override
  public GatewayFilter apply(final Config config) {
    return (((exchange, chain) -> {
      final ServerHttpRequest request = exchange.getRequest();
      final ServerHttpResponse response = exchange.getResponse();

      log.info("Custom PreFilter : request id -> {}", request.getId());

      final ValueOperations<String, String> operations = redisTemplate.opsForValue();
      final String cache = operations.get("cache");
      if (cache == null) {
        operations.set("cache", "CachedService - Hello World!");
      } else {
        final byte[] bytes = cache.getBytes(StandardCharsets.UTF_8);
        final DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Flux.just(buffer));
      }

      return chain.filter(exchange).then(Mono.fromRunnable(() -> {
        log.info("Custom PostFilter : response code -> {}", response.getStatusCode());
      }));
    }));
  }

  public static class Config {

  }

}
