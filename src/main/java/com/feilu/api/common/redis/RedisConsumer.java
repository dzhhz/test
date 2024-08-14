package com.feilu.api.common.redis;

import com.feilu.api.service.system.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
public class RedisConsumer {

	private JedisPool jedisPool;

	private OrderService orderService;

	@Autowired
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	@Autowired
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	@Value("${redis.queue.synOrderKey}")
	private String synOrder;

	@PostConstruct
	public void init() {
		new Thread(() -> {
			while (true) {
				try (Jedis jedis = jedisPool.getResource()) {
					// 阻塞地弹出消息
					List<String> list = jedis.brpop(0, synOrder);
					if (list != null && list.size() > 1) {
						String orderId = list.get(1);
						consumeMessage(orderId, synOrder);
					}
				} catch (Exception e) {
					log.error("订单同步消费事件异常(redis)", e);
				}
			}
		}).start();
	}

	@Async
	public void consumeMessage(String orderId, String key) {
		if (synOrder.equals(key)) {
            log.info("[订单同步]:{}", orderId);
			orderService.syncOrder(orderId);
		}
	}
}
