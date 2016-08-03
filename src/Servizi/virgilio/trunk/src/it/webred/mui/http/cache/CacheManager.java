package it.webred.mui.http.cache;

import it.webred.mui.http.MuiApplication;
import it.webred.mui.http.MuiHttpConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.TreeMap;

import net.skillbill.logging.Logger;

public class CacheManager implements Runnable, MuiHttpConstants {

	private static CacheManager _instance;

	// protected HashMap<String, Object> _cache = new HashMap<String, Object>();

	protected HashMap<String, CacheRefresher> _cacheRefreshers = new HashMap<String, CacheRefresher>();

	protected TreeMap<Long, String> _cacheAlarms = new TreeMap<Long, String>();

	protected TreeMap<String, Long> _alarmsCache = new TreeMap<String, Long>();

	private boolean _stop = true;

	private Thread _t = null;

	private String cacheFilePrefix;
	
	private CacheManager(String cacheFilePrefix) {
		this.cacheFilePrefix=cacheFilePrefix;
	}

	private Object reloadFromFile(String name) {
		Object res = null;
		File inobj = new File(System.getProperty("java.io.tmpdir")
				+ System.getProperty("file.separator") + cacheFilePrefix + name);
		if (inobj.exists()) {
			try {
				/* Open the file and set to read objects from it. */
				FileInputStream istream = new FileInputStream(inobj);
				ObjectInputStream q = new ObjectInputStream(istream);
				res = q.readObject();
				q.close();
			} catch (Exception e) {
				Logger.log().error(this.getClass().getName(),
						"error while reading from file cache entry = " + name,
						e);
				// NOOP
			}

		}
		return res;
	}

	private void writeToFile(String name, Object val) {
		File inobj = new File(System.getProperty("java.io.tmpdir")
				+ System.getProperty("file.separator") + cacheFilePrefix + name);
		try {
			/* Open the file and set to write objects to it. */
			FileOutputStream istream = new FileOutputStream(inobj);
			ObjectOutputStream q = new ObjectOutputStream(istream);
			q.writeObject(val);
			q.close();
		} catch (Exception e) {
			Logger.log().error(this.getClass().getName(),
					"error while writing to file cache entry = " + name, e);
			// NOOP
		}

	}

	public void start() {
		_stop = false;
		if (_t == null || !_t.isAlive()) {
			_t = new Thread(this);
			_t.start();
		}
	}

	public void stop() {
		_stop = true;
		while (_t.isAlive()) {
			try {
				Thread.sleep(CACHE_SLEEP_CYCLE);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static CacheManager getCacheManager(String cacheFilePrefix) {
		if(_instance ==  null)
		{
			synchronized (CacheManager.class) {
				if (_instance == null) {
					_instance = new CacheManager(cacheFilePrefix);
				}
				}
		}
		return _instance;
	}

	public Object get(String name) {
		// synchronized (_cache) {
		// return _cache.get(name);
		// }
		try {
			return MuiApplication.getMuiApplication().getServletContext()
					.getAttribute(name);
		} catch (Throwable e) {
			return null;
		}
	}

	public void invalidate(String name) {
		synchronized (_cacheAlarms) {
			long newAlarm = System.currentTimeMillis() - 1000;
			long alarm = _alarmsCache.get(name);
			_cacheAlarms.remove(alarm);
			_cacheAlarms.put(newAlarm, name);
			_alarmsCache.put(name, newAlarm);
		}
	}

	public void add(String name, CacheRefresher refresher) {
		if (!_alarmsCache.containsKey(name)) {
			synchronized (_cacheAlarms) {
				if (!_alarmsCache.containsKey(name)) {
					_cacheRefreshers.put(name, refresher);
					long now = System.currentTimeMillis();
					Object value = reloadFromFile(name);
					if (value == null) {
						_cacheAlarms.put(now, name);
						_alarmsCache.put(name, now);
						step();
					} else {
						_cacheAlarms.put(now +CACHE_DURATION/2, name);
						_alarmsCache.put(name, now + CACHE_DURATION/2);
						MuiApplication.getMuiApplication().getServletContext()
								.setAttribute(name, value);
						Logger.log().debug(this.getClass().getName(),
								"updated cache entry from file = " + name);
					}
				} else {
					step();
				}
			}
		} else {
			step();
		}
	}

	public void run() {
		while (!_stop) {
			step();
			try {
				Thread.sleep(CACHE_SLEEP_CYCLE);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void step() {
		long now = System.currentTimeMillis();
		Long alarm;
		while (!_cacheAlarms.isEmpty()
				&& (alarm = _cacheAlarms.firstKey()) <= now) {
			synchronized (_cacheAlarms) {
				if ((alarm = _cacheAlarms.firstKey()) <= now) {
					String name = _cacheAlarms.get(alarm);
					Logger.log().debug(this.getClass().getName(),
					name+" cacheAlarms elapsed!!");
					updateElapsedCacheEntry(now, alarm, name);
				}
			}
		}
	}
	
	public void forceCacheUpdate(String name){
		long now = System.currentTimeMillis()+1000*CACHE_DURATION;
		synchronized (_cacheAlarms) {
			Logger.log().debug(this.getClass().getName(),
			"executing forced caching update for "+name);
			updateElapsedCacheEntry(now, _alarmsCache.get(name), name);
		}
	}
	
	private void updateElapsedCacheEntry(long now, Long alarm, String name) {
		CacheRefresher refresher = _cacheRefreshers.get(name);
		if (refresher != null) {
			try {

				Object value = refresher.doRefresh();
				// synchronized (_cache) {
				// _cache.put(name, value);
				// }
				MuiApplication.getMuiApplication()
						.getServletContext().setAttribute(name,
								value);
				Logger.log().debug(this.getClass().getName(),
						"updated cache entry = " + name);
				writeToFile(name,value);
				Logger.log().debug(this.getClass().getName(),
						"updated cache file entry = " + name);
			} catch (Exception e) {
				Logger.log().error(
						this.getClass().getName(),
						"error while updating cache entry = "
								+ name, e);
				// NOOP
			}
		} else {
			Logger.log().error(this.getClass().getName(),
					"refresher is null!!");
			return;
		}

		long newnow = System.currentTimeMillis();
		//se uso il forced è <0
		long drift = (newnow - now > 0 ? newnow - now : 0);
		_cacheAlarms.remove(alarm);
		_cacheAlarms.put(newnow + CACHE_DURATION + drift,
				name);
		try {
			_alarmsCache.put(name, newnow + CACHE_DURATION
					+ drift);
		} catch (NullPointerException e) {
		}
	}
}
