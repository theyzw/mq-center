package com.yk.mqcenter.util;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.util.CollectionUtils;

/**
 * redis工具
 *
 * @author yzw
 * @date 2019/07/18 11:14
 */
public class RedisUtil {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    //=============================common============================

    /**
     * 设置key失效时间
     *
     * @param key      键
     * @param time     时间
     * @param timeUnit 单位
     * @return
     */
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            logger.error("设置key失效时间失败。key={}, time={}, timeUnit={}", key, time, timeUnit);
            return false;
        }
    }

    /**
     * 设置key失效时间（秒）
     *
     * @param key  键
     * @param time 时间
     * @return
     */
    public boolean expire(String key, long time) {
        return expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 设置key失效时间
     *
     * @param key  键
     * @param date
     * @return
     */
    public boolean expireAt(String key, Date date) {
        try {
            if (date != null) {
                redisTemplate.expireAt(key, date);
            }
            return true;
        } catch (Exception e) {
            logger.error("设置key失效时间失败。key={}, date={}", key, date);
            return false;
        }
    }

    /**
     * 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) -1永久有效；-2key不存在
     */
    @SuppressWarnings("all")
    public long ttl(String key) {
        //-1永久有效；-2key不存在
        return redisTemplate.getExpire(key);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return
     */
    @SuppressWarnings("all")
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            logger.error("判断key是否存在失败。key={}", key);
            return false;
        }
    }

    /**
     * 删除key
     *
     * @param key
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 删除以prefix为前缀的key
     *
     * @param prefix 前缀
     */
    public void delPrefix(String prefix) {
        if (StringUtils.isBlank(prefix)) {
            return;
        }

        //每次遍历数量
        int limit = 100;

        String pattern = prefix + "*";
        Set<String> keySet = scan(pattern, limit);
        if (!CollectionUtils.isEmpty(keySet)) {
            redisTemplate.delete(keySet);
        }
    }

    /**
     * scan查询key
     *
     * @param pattern 正则
     * @param limit   每次遍历个数
     * @return 所有匹配的key。与limit无关
     */
    @SuppressWarnings("unchecked")
    private Set<String> scan(String pattern, int limit) {
        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(limit).build();

        return (Set<String>) redisTemplate.execute((RedisCallback) connection -> {
            Set<String> binaryKeys = new HashSet<>();

            Cursor<byte[]> cursor = connection.scan(options);
            while (cursor.hasNext()) {
                binaryKeys.add(new String(cursor.next()));
            }
            return binaryKeys;
        });
    }

    //============================String=============================

    /**
     * string get
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * string set
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            logger.error("string set失败。key={}, value={}", key, value);
            return false;
        }
    }

    /**
     * string set并设置超时时间
     *
     * @param key      键
     * @param value    值
     * @param time     如果time小于等于0 将设置无限期
     * @param timeUnit
     * @return
     */
    public boolean set(String key, Object value, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, timeUnit);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            logger.error("string set并设置超时时间失败。key={}, value={}, time={}, timeUnit={}", key, value, time, timeUnit);
            return false;
        }
    }

    /**
     * string设置并设置超时时间(秒)
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) 如果time小于等于0 将设置无限期
     * @return
     */
    public boolean set(String key, Object value, long time) {
        return set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * string setnx
     *
     * @param key   键
     * @param value 值
     * @return
     */
    @SuppressWarnings("all")
    public boolean setnx(String key, Object value) {
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, value);
        } catch (Exception e) {
            logger.error("string setnx失败。key={}, value={}", key, value);
            return false;
        }
    }

    /**
     * string setnx
     *
     * @param key   键
     * @param value 值
     * @return
     */
    @SuppressWarnings("all")
    public boolean setnx(String key, Object value, long time, TimeUnit timeUnit) {
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, value, time, timeUnit);
        } catch (Exception e) {
            logger.error("string setnx失败。key={}, value={}, time={}, timeUnit={}", key, value, time, timeUnit);
            return false;
        }
    }

    /**
     * string setnx(秒)
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean setnx(String key, Object value, long time) {
        return setnx(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * string value增加delta
     *
     * @param key   键
     * @param delta 可以为负值
     * @return
     */
    @SuppressWarnings("all")
    public long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * string value自增
     *
     * @param key 键
     * @return
     */
    @SuppressWarnings("all")
    public long incr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    //=============================hash============================

    /**
     * hash get
     *
     * @param key   键 不能为null
     * @param filed 项 不能为null
     * @return 值
     */
    public Object hget(String key, String filed) {
        return redisTemplate.opsForHash().get(key, filed);
    }

    /**
     * hash set
     *
     * @param key   键
     * @param filed 项
     * @param value 值
     * @return
     */
    public boolean hset(String key, String filed, Object value) {
        try {
            redisTemplate.opsForHash().put(key, filed, value);
            return true;
        } catch (Exception e) {
            logger.error("hash set失败。key={}, filed={}, value={}", key, filed, value);
            return false;
        }
    }

    /**
     * hash set
     *
     * @param key      键
     * @param filed    项
     * @param value    值
     * @param time     时间
     * @param timeUnit
     * @return
     */
    public boolean hset(String key, String filed, Object value, long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForHash().put(key, filed, value);
            if (time > 0) {
                expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            logger.error("hash set失败。key={}, filed={}, value={}", key, filed, value);
            return false;
        }
    }

    /**
     * hash set（秒）
     *
     * @param key   键
     * @param filed 项
     * @param value 值
     * @param time  时间
     * @return
     */
    public boolean hset(String key, String filed, Object value, long time) {
        return hset(key, filed, value, time, TimeUnit.SECONDS);
    }

    /**
     * hash mset
     *
     * @param key 键
     * @param map 对应多个键值
     * @return
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            logger.error("hash mset失败。key={}, map={}", key, map);
            return false;
        }
    }

    /**
     * hash mset
     *
     * @param key 键
     * @param map 对应多个键值
     * @return
     */
    public boolean hmset(String key, Map<String, Object> map, long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            logger.error("hash mset失败。key={}, map={}", key, map);
            return false;
        }
    }

    /**
     * hash mset（秒）
     *
     * @param key 键
     * @param map 对应多个键值
     * @return
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        return hmset(key, map, time, TimeUnit.SECONDS);
    }

    /**
     * hash 删除filed
     *
     * @param key   键 不能为null
     * @param filed 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... filed) {
        redisTemplate.opsForHash().delete(key, filed);
    }

    /**
     * hash 判断hash中是否有field
     *
     * @param key   键 不能为null
     * @param filed 项 不能为null
     * @return
     */
    public boolean hexists(String key, String filed) {
        return redisTemplate.opsForHash().hasKey(key, filed);
    }

    /**
     * hash 获取key对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hgetall(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * hash 增加delta
     *
     * @param key   键
     * @param filed 项
     * @param delta 允许负值
     * @return
     */
    public double hincr(String key, String filed, long delta) {
        return redisTemplate.opsForHash().increment(key, filed, delta);
    }

    /**
     * hash 自增
     *
     * @param key   键
     * @param filed 项
     * @return
     */
    public double hincr(String key, String filed) {
        return redisTemplate.opsForHash().increment(key, filed, 1L);
    }

    //============================set=============================

    /**
     * set members
     *
     * @param key 键
     * @return
     */
    public Set<Object> smembers(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            logger.error("set members失败。key={}", key);
            return null;
        }
    }

    /**
     * set sismember
     *
     * @param key   键
     * @param value 值
     * @return
     */
    @SuppressWarnings("all")
    public boolean sismember(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            logger.error("set sismember失败。key={}", key);
            return false;
        }
    }

    /**
     * set sadd
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    @SuppressWarnings("all")
    public long sadd(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            logger.error("set sadd失败。key={}", key);
            return 0;
        }
    }

    /**
     * set sadd
     *
     * @param key      键
     * @param time     时间
     * @param timeUnit 单位
     * @param values   值 可以是多个
     * @return 成功个数
     */
    @SuppressWarnings("all")
    public long sadd(String key, long time, TimeUnit timeUnit, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time, timeUnit);
            }
            return count;
        } catch (Exception e) {
            logger.error("set sadd失败。key={}", key);
            return 0;
        }
    }

    /**
     * set sadd
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sadd(String key, long time, Object... values) {
        return sadd(key, time, TimeUnit.SECONDS, values);
    }

    /**
     * set scard
     *
     * @param key 键
     * @return
     */
    @SuppressWarnings("all")
    public long scard(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            logger.error("set scard失败。key={}", key);
            return 0;
        }
    }

    /**
     * set 移除值为value的member
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    @SuppressWarnings("all")
    public long sRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            logger.error("sRemove失败。key={}", key);
            return 0;
        }
    }

    //============================list=============================

    /**
     * list lrange 获取start到end之间元素
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public List<Object> lrange(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            logger.error("list lrange失败。key={}", key);
            return null;
        }
    }

    /**
     * list llen list长度
     *
     * @param key 键
     * @return
     */
    @SuppressWarnings("all")
    public long llen(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            logger.error("list llen失败。key={}", key);
            return 0;
        }
    }

    /**
     * list lindex 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lindex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            logger.error("list lindex失败。key={}", key);
            return null;
        }
    }

    /**
     * list rpush 在列表中添加一个或多个值
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean rpush(String key, Object... value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            logger.error("list rpush失败。key={}", key);
            return false;
        }
    }

    /**
     * list rpush 在列表中添加一个或多个值
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean rpush(String key, long time, TimeUnit timeUnit, Object... value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            logger.error("list rpush失败。key={}", key);
            return false;
        }
    }

    /**
     * list rpush 在列表中添加一个或多个值（秒）
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean rpush(String key, long time, Object... value) {
        return rpush(key, time, TimeUnit.SECONDS, value);
    }

    //============================zset=============================
}
