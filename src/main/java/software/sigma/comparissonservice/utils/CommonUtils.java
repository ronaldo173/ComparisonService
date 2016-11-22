package software.sigma.comparissonservice.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

/**
 * Common utils for service.
 * 
 * @author alexandr.efimov
 *
 */
public final class CommonUtils {

	private static final Logger LOGGER = Logger.getLogger(CommonUtils.class);
	/**
	 * Encoding for reading from file.
	 */
	private static final String FILE_ENCODING = "UTF-8";

	/**
	 * Until class, not for creating - use static methods.
	 */
	private CommonUtils() {
		throw new IllegalAccessError();
	}

	/**
	 * Convert content of file to string.
	 * 
	 * @param pathToFile
	 *            is path to file
	 * @return string with file content
	 */
	public static String readFileToString(final String pathToFile) {
		String result = "";
		String correctPath = pathToFile;

		if (pathToFile == null || pathToFile.isEmpty()) {
			return result;
		}

		// if path starts with '/' - delete /
		if (pathToFile.startsWith("/")) {
			correctPath = pathToFile.substring(1);
		}

		Charset encoding = Charset.forName(FILE_ENCODING);
		byte[] bytesFromFile = null;
		try {
			bytesFromFile = Files.readAllBytes(Paths.get(correctPath));
			result = new String(bytesFromFile, encoding);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return result;
	}

}
