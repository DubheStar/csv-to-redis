package com.xpcomrade.example;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

import java.util.ResourceBundle;


public class RedisClientWrapper {
	private static JedisPool jedisPool = null;
	private static int dbIndex;
	private static RedisClientWrapper instance = null;

	private RedisClientWrapper() {
	}

	static {
		ResourceBundle bundle = ResourceBundle.getBundle("redis");
		if (bundle == null) {
			throw new IllegalArgumentException("[redis.properties] is not found!");
		}
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(Integer.valueOf(bundle.getString("redis.pool.maxTotal")));
		config.setMinIdle(Integer.valueOf(bundle.getString("redis.pool.minIdle")));
		config.setMaxIdle(Integer.valueOf(bundle.getString("redis.pool.maxIdle")));
		config.setMaxWaitMillis(Long.valueOf(bundle.getString("redis.pool.maxWait")));
		config.setTestWhileIdle(Boolean.valueOf(bundle.getString("redis.pool.testWhileIdle")));
		config.setTestOnBorrow(Boolean.valueOf(bundle.getString("redis.pool.testOnBorrow")));
		config.setTestOnReturn(Boolean.valueOf(bundle.getString("redis.pool.testOnReturn")));
		jedisPool = new JedisPool(config, bundle.getString("redis.ip"), Integer.valueOf(bundle.getString("redis.port")));
		dbIndex = Integer.valueOf(bundle.getString("redis.db.index"));
	}

	public static RedisClientWrapper getInstance() {
		if (instance == null) {
			return new RedisClientWrapper();
		}
		return instance;
	}

	public Jedis getResource() {
		Jedis jedis = jedisPool.getResource();
		jedis.select(dbIndex);
		return jedis;
	}

	public void retrunResource(Jedis jedis) {
		jedisPool.returnResource(jedis);
	}

	public void returnBrokenResource(Jedis jedis) {
		jedisPool.returnBrokenResource(jedis);
	}

	public void set(String key, String value) throws JedisException {
		Jedis jedis = null;
		try {
			jedis = this.getResource();
			jedis.set(key, value);
			retrunResource(jedis);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			throw new JedisException(e);
		}
	}

	public String get(String key) throws JedisException {
		Jedis jedis = null;
		try {
			jedis = this.getResource();
			String value = jedis.get(key);
			retrunResource(jedis);
			return value;
		} catch (Exception e) {
			returnBrokenResource(jedis);
			throw new JedisException(e);
		}
	}

	public void flushDB() throws JedisException {
		Jedis jedis = null;
		try {
			jedis = this.getResource();
			jedis.flushDB();
			retrunResource(jedis);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			throw new JedisException(e);
		}
	}

	public void cache(String key, String value, int seconds) throws JedisException {
		Jedis jedis = null;
		try {
			jedis = this.getResource();
			jedis.set(key, value);
			jedis.expire(key, seconds);
			retrunResource(jedis);
		} catch (Exception e) {
			returnBrokenResource(jedis);
			throw new JedisException(e);
		}
	}
}
