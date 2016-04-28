package com.wxchat.cases;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;

public class ScheduledExecutor extends ScheduledThreadPoolExecutor {
	private static final ScheduledExecutor instance = new ScheduledExecutor();

	private ScheduledExecutor() {
		super(2);
	}

	public static ScheduledExecutor getExecutor() {
		return instance;
	}

	private final ReentrantLock lock = new ReentrantLock();
	private final Map<String, String> temp = new HashMap<String, String>();

	public void enter(String key) {
		lock.lock();
		try {
			temp.put(key, null);
		} finally {
			lock.unlock();
		}
	}

	public boolean doing(String key) {
		lock.lock();
		try {
			if (temp.containsKey(key)) {
				return true;
			} else {
				return false;
			}
		} finally {
			lock.unlock();
		}
	}

	public void quit(String key) {
		lock.lock();
		try {
			temp.remove(key);
		} finally {
			lock.unlock();
		}
	}
}
