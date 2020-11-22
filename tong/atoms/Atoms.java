package atoms;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import java.util.List;

public class Atoms {
    
    public Long incr(Jedis connection, String key, Integer val) {
    	return connection.incrBy(key, val);
    }
    
    // String
    public void set(Jedis connection, String key, String val) {
    	connection.set(key, val);
    }
    
    public String get(Jedis connection, String key) {
    	return connection.get(key);
    }
    
    // List
    public void lpush(Jedis connection, String key, String[] vals) {
    	connection.lpush(key, vals);
    }

    public void ltrim(Jedis connection, String key, long start, long end) {
    	connection.ltrim(key, start, end);
    } 
    
}
