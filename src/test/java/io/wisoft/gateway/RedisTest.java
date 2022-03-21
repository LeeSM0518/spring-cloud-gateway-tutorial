package io.wisoft.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisTest {

  @Autowired
  StringRedisTemplate redisTemplate;

  @Test
  public void testStrings() {
    final String key = "test";
    final String expected = "1";

    final ValueOperations<String, String> stringStringValueOperations = redisTemplate.opsForValue();

    stringStringValueOperations.set(key, expected);
    final String actual = stringStringValueOperations.get(key);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void removeTest() {
    final String cache = "cache";
    redisTemplate.delete(cache);
    final ValueOperations<String, String> stringStringValueOperations = redisTemplate.opsForValue();
    final String result = stringStringValueOperations.get(cache);
    assertThat(result).isNull();
  }

}
