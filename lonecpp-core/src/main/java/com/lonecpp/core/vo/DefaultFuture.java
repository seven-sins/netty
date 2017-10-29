package com.lonecpp.core.vo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.lonecpp.core.common.Status;

/**
 * @author seven sins
 * @date 2017年10月28日 下午2:17:17
 */
public class DefaultFuture {
	static final Logger LOG = Logger.getLogger(DefaultFuture.class);
	static final Map<Long, DefaultFuture> FUTURES = new ConcurrentHashMap<Long, DefaultFuture>();
	final long start = System.currentTimeMillis();
	long id;
	volatile Result result;
	long timeout;
	volatile Lock lock = new ReentrantLock();
	volatile Condition condition = lock.newCondition();
	
	public DefaultFuture() {
		
	}

	public DefaultFuture(Request request) {
		id = request.getId();
		FUTURES.put(id, this);
	}

	public Result get() {
		lock.lock();
		while (!hasDone()) {
			try {
				condition.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}

		return result;
	}

	public Result get(long timeout) {
		long start = System.currentTimeMillis();
		lock.lock();
		while (!hasDone()) {
			try {
				condition.await(timeout, TimeUnit.SECONDS);
				if (System.currentTimeMillis() - start >= timeout) {
					break;
				}
			} catch (InterruptedException e) {
				LOG.error(e);
			} finally {
				lock.unlock();
			}
		}

		return result;
	}

	/**
	 * 收到服务器响应
	 * @param result
	 */
	public static void recive(Result result) {
		if(!Status.SUCCESS.equals(result.getStatus())) {
			LOG.error(JSONObject.toJSONString(result));
			return;
		}
		// 找到result相对应的DefaultFuture
		DefaultFuture future = FUTURES.remove(result.getId());
		if (future == null) {
			return;
		}
		Lock lock = future.getLock();
		lock.lock();
		try {
			future.setResult(result);
			Condition condition = future.getCondition();
			if (condition != null) {
				condition.signal();
			}
		} catch (Exception e) {
			LOG.error(e);
		} finally {
			lock.unlock();
		}
	}

	private boolean hasDone() {
		return result != null ? true : false;
	}

	public long getId() {
		return id;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public Lock getLock() {
		return lock;
	}

	public void setLock(Lock lock) {
		this.lock = lock;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public long getStart() {
		return start;
	}

	static class FutureTimeOutThread extends Thread {

		@Override
		public void run() {

			while (true) {
				for (long futureId : FUTURES.keySet()) {
					DefaultFuture future = FUTURES.get(futureId);
					if (future == null) {
						FUTURES.remove(futureId);
						continue;
					}
					if (future.getTimeout() > 0) {
						if ((System.currentTimeMillis() - future.getStart()) > future.getTimeout()) {
							Result result = new Result(Status.TIMEOUT, "请求超时");
							result.setId(future.getId());
							DefaultFuture.recive(result);
						}
					}
				}
			}
		}
	}

	static {
		FutureTimeOutThread timeOutThread = new FutureTimeOutThread();
		timeOutThread.setDaemon(true);
		timeOutThread.start();

	}
}
