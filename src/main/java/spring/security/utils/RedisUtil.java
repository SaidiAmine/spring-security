package spring.security.utils;


import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@Service
public class RedisUtil {

    private static final String redisHost = "localhost";
    private static final Integer redisPort = 6379;

    private static JedisPool pool = null;

    public RedisUtil() {
        //configure our pool connection
        pool = new JedisPool(redisHost, redisPort);

    }

    public void sadd(String key, String value, Long unixTime) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.sadd(key, value);
            jedis.expireAt(key, unixTime);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.srem(key, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}