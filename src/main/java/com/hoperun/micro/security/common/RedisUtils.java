package com.hoperun.micro.security.common;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RedisUtils {

	@Autowired
	private StringRedisTemplate template;

	/** 判断当前是否存在此key */
	public boolean isExist(String key) {
		return this.template.hasKey(key);
	}

	/** 存入数据 */
	public void set(String key, String value) {
		ValueOperations<String, String> ops = this.template.opsForValue();
		ops.set(key, value);
	}

	/** 获取数据 */
	public String get(String key) {
		ValueOperations<String, String> ops = this.template.opsForValue();
		return ops.get(key);
	}

	/** 删除数据 */
	public void del(String key) {
		this.template.delete(key);
	}

	/** 刷新过期时间 */
	public void expire(String key, int time) {
		this.template.expire(key, time, TimeUnit.MILLISECONDS);

	}

	/** 存入数据并设置过期时间 */
	public void save(String key, String value, int time) {
		ValueOperations<String, String> ops = this.template.opsForValue();
		ops.set(key, value, time, TimeUnit.MILLISECONDS);
	}

	public String getAdminIdByAccessToken(String accessToken) {
		return getUserByAccessToken(accessToken, PortalUtils.PRE_ADMIN_ACCESS_TOKEN);
	}

	public String getUserByAccessToken(String accessToken, String preHead) {
		if (accessToken == null || accessToken.isEmpty()) {
			return null;
		}
		String adminId = "";
		preHead = preHead == null ? "" : preHead;
		try {
			if (get(accessToken) == null) {
				return null;
			}
			adminId = get(accessToken).toString();
			if (!adminId.substring(0, preHead.length()).equalsIgnoreCase(preHead)) {
				return null;
			}
			adminId = adminId.substring(preHead.length());
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return adminId;
	}
}
