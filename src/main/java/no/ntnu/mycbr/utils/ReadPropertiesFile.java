package no.ntnu.mycbr.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import static no.ntnu.mycbr.utils.Constants.*;

public class ReadPropertiesFile {

	private static final Logger LOG = Logger.getLogger(ReadPropertiesFile.class.getName());

	private static Properties properties = new Properties();

	private static String fileNmae = PROPERITIES_FILE;

	static {
		properties = loadProperties(DATA_PATH+fileNmae);
	}

	public static Properties getProperties() {
		return properties;
	}

	private static Properties loadProperties(String fileName) {

		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(fileName);

			// load a properties file
			prop.load(input);

			// get the property value and print it out
//			System.out.println(PROJECT_NAME);
//			System.out.println(CONCEPT_NAME);
//			System.out.println(CASEBASE_NAME);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return prop;
	}

//	public static void main(String[] args) {
//		//		String name = File.separator + "resources" + File.separator
//		//				+ "config.properties";
//		//		InputStream ins = ClassLoader.getSystemClassLoader()
//		//				.getResourceAsStream(name);
//		//		System.out.println(ins);
//
//		System.out.println(ReadPropertiesFile.getProperties());
//	}
}



/**
 * Load properties.
 * 
 * @param filePath
 *            the file path
 * @return the properties
 */
//	public static Properties loadProperties(String filePath) {
//		//Properties properties = new Properties();
//		ClassLoader classLoader = Thread.currentThread()
//				.getContextClassLoader();
//		InputStream is = classLoader.getResourceAsStream(filePath);
//		if (is != null) {
//			try {
//				properties.load(is);
//			} catch (Exception e) {
//				LOG.error("Failed to read using ContextClassLoader, from "
//						+ filePath + " file." + e.getMessage());
//				LOG.debug("Failed to read using ContextClassLoader, from "
//						+ filePath + " file." + e.getMessage());
//			} finally {
//				try {
//					is.close();
//				} catch (IOException e) {
//					LOG.error("Exception in closing input stream. "
//							+ e.getMessage());
//					LOG.debug(
//							"Exception in closing input stream. "
//									+ e.getMessage(), e);
//				}
//			}
//		} else {
//			try {
//
//				File srcFile = new File(filePath);
//
//				if ((srcFile.exists()) && (srcFile.isFile())) {
//					is = new FileInputStream(srcFile);
//				} else {
//					File f = new File(filePath);
//					is = new FileInputStream(f);
//				}
//				if (is != null) {
//					properties.load(is);
//				}
//
//			} catch (Exception e) {
//				LOG.error("Failed to read from " + filePath + " file."
//						+ e.getMessage());
//				LOG.debug("Failed to read from " + filePath + " file."
//						+ e.getMessage());
//			} finally {
//				try {
//					if (is != null)
//						is.close();
//				} catch (IOException e) {
//					LOG.("Exception in closing input stream. "
//							+ e.getMessage());
//					LOG.debug(
//							"Exception in closing input stream. "
//									+ e.getMessage(), e);
//				}
//			}
//		}
//
//		return properties;
//	}


//	private static void loadProperty() {
//		
//		LOGGER.info("Loading properities from file : " + fileNmae);
//
//		// ProperitiesHandler.class.getClassLoader().getResourceAsStream(fileNmae);
//		try {
//			// properties.load(ClassLoader.getSystemClassLoader().getResourceAsStream(fileNmae));
//			properties.load(ProperitiesHandler.class.getClassLoader()
//					.getResourceAsStream(fileNmae));
//		} catch (IOException e) {
//			LOGGER.error("Load config property fail, file name:{}", fileNmae, e);
//		}
//	}

//	public static Properties getProperties() {
//		LOG.fine("Getting the Properities : " + properties);
//		return properties;
//	}
