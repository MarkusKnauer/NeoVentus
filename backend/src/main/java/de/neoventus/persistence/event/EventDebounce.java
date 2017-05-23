package de.neoventus.persistence.event;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * debounce utility class
 *
 * @author Dennis Thanner
 */
@Component
public class EventDebounce {

	private ConcurrentHashMap<String, Integer> debounceMap;
	private ConcurrentHashMap<String, ScheduledExecutorService> debounceTimers;

	public EventDebounce() {
		this.debounceMap = new ConcurrentHashMap<>();
		this.debounceTimers = new ConcurrentHashMap<>();
	}

	/**
	 * debounce a event to execute a function after the last event in a specific time period
	 *
	 * @param key
	 * @param millis
	 * @param cb
	 */
	public void debounce(String key, Integer millis, Function<Integer, Void> cb) {
		if (debounceMap.containsKey(key)) {
			debounceMap.put(key, debounceMap.get(key) + 1);
		} else {
			debounceMap.put(key, 1);
		}

		// reset timer if exists
		ScheduledExecutorService old = debounceTimers.remove(key);
		if (old != null && !old.isShutdown()) {
			old.shutdownNow();
		}

		ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
		timer.schedule(() -> {
			// on timer finish execute debounced method
			cb.apply(debounceMap.remove(key));
			debounceTimers.remove(key);
		}, millis, TimeUnit.MILLISECONDS);

		debounceTimers.put(key, timer);
	}

}
