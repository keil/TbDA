package dk.brics.tajs.dependency;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * instance to read dataflow.policy properties
 * 
 * @author Matthias Keil
 */
public class DependencyProperties extends Properties {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * singleton instance
	 */
	private static DependencyProperties instance = null;

	/**
	 * property file
	 */
	private String POLICY = "src/dk/brics/tajs/dependency/dataflow.policy";

	/**
	 * property name: TRACE
	 */
	public static String TRACE = "trace";

	/**
	 * @return DependencyProperties
	 */
	public synchronized static DependencyProperties getInstance() {
		if (instance == null)
			instance = new DependencyProperties();
		return instance;
	}

	/**
	 * new DependencyProperties
	 */
	private DependencyProperties() {
		try {
			BufferedInputStream inStream = new BufferedInputStream(
					new FileInputStream(POLICY));
			this.load(inStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param key
	 * @return string property value
	 */
	public String getString(String key) {
		return this.getProperty(key);
	}

	/**
	 * @param key
	 * @return int property value
	 */
	public int getInt(String key) {
		return Integer.parseInt(this.getProperty(key));
	}

	/**
	 * @param key
	 * @return boolean property value
	 */
	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(this.getProperty(key));
	}

	/**
	 * @param key
	 * @return list, with string property values
	 */
	public List<String> getStringArray(String key) {
		String value = this.getProperty(key);

		StringTokenizer stringTokenizer = new StringTokenizer(value, ";");
		List<String> list = new ArrayList<String>();

		while (stringTokenizer.hasMoreElements()) {
			list.add(((String) stringTokenizer.nextElement()).trim());
		}

		return list;
	}
}