package software.sigma.comparissonservice;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import software.sigma.comparissonservice.service.SortServiceImpl;
import software.sigma.comparissonservice.utils.CommonUtils;
import software.sigma.comparissonservice.vo.InputDataVO;
import software.sigma.comparissonservice.vo.SortOrderFieldVO;

public class TestSortPerformance {

	private static final String ENCODING = "UTF-8";
	private SortServiceImpl sortService = new SortServiceImpl();

	@Test
	public void testSort() throws Exception {
		URL urlFileXmlForSort = getClass()
				.getResource("/software/sigma/comparissonservice/resources/dataForPerformanceTest.xml");
		URL urlFileXmlOutput = getClass()
				.getResource("/software/sigma/comparissonservice/resources/dataForPerformanceTest_OUTPUT.xml");
		String xmlContentForSort = CommonUtils.readFileToString(urlFileXmlForSort.getFile());

		List<SortOrderFieldVO> sortOrder = new ArrayList<>();

		Collections.addAll(sortOrder, new SortOrderFieldVO("price", "asc"), new SortOrderFieldVO("categoryId"),
				new SortOrderFieldVO("name"));

		InputDataVO inputData = new InputDataVO();
		inputData.setDataForSort(xmlContentForSort);
		inputData.setSortOrder(sortOrder);

		// GET TIME OF EXECUTION
		long startTime = System.nanoTime();
		String sortedData = sortService.sort(inputData);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime); // divide by 1000000 to get
												// milliseconds.
		System.err.println("TIME -> " + duration / 1000000);

		saveToFileString(urlFileXmlOutput.getPath(), sortedData);
	}

	private void saveToFileString(String path, String sortedData) throws Exception {
		File file = new File(path);
		if (!file.exists()) {
			System.err.println("File not exists!");
			return;
		}
		System.out.println("Path to result: " + file);

		try (Writer writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(file), Charset.forName(ENCODING)))) {
			writer.write(sortedData);
		}

	}

}
