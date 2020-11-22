package operations;

import atoms.Atoms;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;


public class Freq {
	public static void main(String[] args) {
		Freq freq = new Freq();
		
		// setup Jedis
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(30);
        config.setMaxIdle(10);

        JedisPool jedisPool = new JedisPool(config, "127.0.0.1", 6379);
        Jedis jedis = jedisPool.getResource();
        
        // atoms
        Atoms atoms = new Atoms();
        
        // database
        freq.SetData(jedis, atoms);
        
        // search
        String key = "user3";
        String start = "20200922";
        String end = "20211001";
        int result = freq.Search(key, start, end, jedis);
        System.out.printf("Freqency for %s from %s to %s: %d", key, start, end, result);
        
        // close Jedis
        jedis.close();
        jedisPool.close();
        
	}
	
	// count frequency
	public int Search(String key, String start, String end, Jedis connection) {
        List<String> list = connection.lrange(key, 0, connection.llen(key));
        Collections.sort(list);
        
        int start_date = Integer.parseInt(start);
        int end_date = Integer.parseInt(end);
        int freq = 0;
        
        System.out.printf("Data for %s: \n", key);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
            if (Integer.parseInt(list.get(i))>=start_date && Integer.parseInt(list.get(i))<=end_date) {
            	freq += 1;
            }
        }
        return freq;
	}
	
	// setup initial database
    public void SetData(Jedis connection, Atoms atoms) {
    	String[] dates1 = {"20201113", "20201121", "20201201", "20201011"};
    	atoms.lpush(connection, "user1", dates1);
    	atoms.ltrim(connection, "user1", 0, 3);
    	
    	String[] dates2 = {"20201011"};
    	atoms.lpush(connection, "user2", dates2);
    	atoms.ltrim(connection, "user2", 0, 0);
    	
    	String[] dates3 = {"20200911", "20200921", "20201021", "20211111", "20210101", "20210201", "20211019"};
    	atoms.lpush(connection, "user3", dates3);
    	atoms.ltrim(connection, "user3", 0, 6);
    }
    
}


// Comparator for List Sort
class DateComparator implements Comparator{
	public int compare(String s1, String s2) {
		int inum1 = Integer.parseInt(s1);
		int inum2 = Integer.parseInt(s2);
				
		if(inum1>inum2){	//greater
			return -1;
		}else if(inum1==inum2){	//equals
			return 0;
		}else{	//less
			return 1;
		}
	}

	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		return 0;
	}
}

