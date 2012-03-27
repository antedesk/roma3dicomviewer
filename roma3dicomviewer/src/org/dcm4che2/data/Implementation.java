package org.dcm4che2.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.dcm4che2.util.CloseUtils;

/**
 * @author gunter zeilinger(gunterze@gmail.com)
 * @version $Revision: 5551 $ $Date: 2007-11-27 15:24:27 +0100 (Tue, 27 Nov 2007) $
 * @since Aug 22, 2005
 *
 */
public class Implementation {

	private static final String IMPL_PROPERTIES = "org/dcm4che2/data/Implementation.properties";
	private static String classUID;
	private static String versionName;

	static  {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream is;
		if (cl == null || (is = cl.getResourceAsStream(IMPL_PROPERTIES)) == null) {
			is = Implementation.class.getClassLoader().getResourceAsStream(IMPL_PROPERTIES);
			if (is == null) {
				throw new ConfigurationError("Missing Resource: " + IMPL_PROPERTIES);
			}
		}
		Properties p = new Properties();
		try {
			p.load(is);
		} catch (IOException e) {
			throw new ConfigurationError("Failed to load Resource: " + IMPL_PROPERTIES);
		} finally {
			CloseUtils.safeClose(is);
		}
		classUID = p.getProperty("classUID");
		versionName = p.getProperty("versionName");
	}

	public static final String classUID() {
		return classUID;
	}

	public static final String versionName() {
		return versionName;
	}
}

