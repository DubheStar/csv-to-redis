package com.xpcomrade.example;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;


public class RedisClientWrapper {
	private static JedisPool jedisPool = null;
	private static int dbIndex;
	private static RedisClientWrapper instance = null;

	private RedisClientWrapper() {
	}

	static {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(100);
		poolConfig.setMaxIdle(5);
		poolConfig.setTestOnBorrow(false);
		jedisPool = new JedisPool(poolConfig, "172.16.1.77", 6379);
		dbIndex = 0;
	}

	public static RedisClientWrapper getInstance() {
		if (instance == null) {
			return new RedisClientWrapper();
		}
		return instance;
	}

	public static Jedis getResource() {
		Jedis jedis = jedisPool.getResource();
		jedis.select(dbIndex);
		return jedis;
	}

	public static void retrunResource(Jedis jedis) {
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
