package com.xpcomrade.example;

import redis.clients.jedis.*;

import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by xpcomrade on 2016/3/4.
 * Copyright (c) 2016, xpcomrade@gmail.com All Rights Reserved.
 * Description: TODO(这里用一句话描述这个类的作用). <br/>
 */
public class JedisShard {
    private static ShardedJedisPool jedisPool = null;
    private static int dbIndex;
    private static JedisShard instance = null;

    private JedisShard() {
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

        JedisShardInfo jedisShardInfo1 = new JedisShardInfo(bundle.getString("redis.ip"), Integer.valueOf(bundle.getString("redis.port")));
        JedisShardInfo jedisShardInfo2 = new JedisShardInfo(bundle.getString("redis.ip"), Integer.valueOf(bundle.getString("redis.port")));
        List<JedisShardInfo> list = new LinkedList<JedisShardInfo>();
        list.add(jedisShardInfo1);
        list.add(jedisShardInfo2);

        // 初始化ShardedJedisPool
        jedisPool = new ShardedJedisPool(config, list);

        dbIndex = Integer.valueOf(bundle.getString("redis.db.index"));
    }

    public static JedisShard getInstance() {
        if (instance == null) {
            return new JedisShard();
        }
        return instance;
    }

    public ShardedJedis getResource() {
        ShardedJedis jedis = jedisPool.getResource();
        return jedis;
    }

    public void retrunResource(ShardedJedis jedis) {
        jedisPool.returnResource(jedis);
    }

    public void returnBrokenResource(ShardedJedis jedis) {
        jedisPool.returnBrokenResource(jedis);
    }
}
