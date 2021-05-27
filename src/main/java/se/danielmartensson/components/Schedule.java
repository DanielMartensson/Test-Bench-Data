package se.danielmartensson.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import se.danielmartensson.entities.LxFlow;
import se.danielmartensson.entities.LxCurrent;
import se.danielmartensson.entities.LxData;
import se.danielmartensson.entities.RsqData;
import se.danielmartensson.lists.CompleteDataMeasurement;
import se.danielmartensson.repositories.LxDataRepository;
import se.danielmartensson.repositories.RsqDataRepository;
import se.danielmartensson.tools.GetSetClassInformation;

@Component
@PropertySource("classpath:schedule.properties")
public class Schedule {

	public static final String VALVE_TYPE_RSQ = "rsq";	
	public static final int LENGTH_OF_NOT_CURREPTED_FILE = 1072;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${schedule.testBenchPathCSVFolder}")
	private String testBenchPathCSVFolder;
	
	@Value("${schedule.scheduleEnable}")
	private boolean scheduleEnable;
	
	@Autowired
	private LxDataRepository lxDataRepository;
	
	@Autowired
	private RsqDataRepository rsqDataRepository;

	@Autowired
	private FTPConnection ftpConnection;
	
	@Scheduled(fixedDelayString = "${schedule.testBenchIntervall}")
	public void testBenchDownloader() {
		// Check if we should run this schedule.
		if(!scheduleEnable)
			return;

		// Download the csv files
		ftpConnection.scannFolderOverFTP(testBenchPathCSVFolder);

		// Scan folder of all .csv files
		File folderPath = new File(testBenchPathCSVFolder);

		// Only .csv files
		folderPath.list(takeOnlyFilesThatEndsWith(".csv"));

		// Create list
		File[] listedCSVFiles = folderPath.listFiles();
		
		// Check
		if (listedCSVFiles == null) {
			logger.info("Error: Cannot read file path to the CSV files!");
			return;
		}

		if (listedCSVFiles.length == 0) {
			logger.info("No CSV files where detected. Just continue");
			return;
		}

		// Sort on last modified
		Arrays.sort(listedCSVFiles, (f1, f2) -> {
			return new Date(f1.lastModified()).compareTo(new Date(f2.lastModified()));
		});

		// Load to the database
		for (File CSVFile : listedCSVFiles) {
			// Begin to read each row
			try {
				BufferedReader br = new BufferedReader(new FileReader(CSVFile));
				String[] dataRow = br.readLine().split(";");
				int columnLength = dataRow.length;
				if(columnLength != LENGTH_OF_NOT_CURREPTED_FILE) {
					// Corrupted file
					br.close();
					CSVFile.delete();
					continue;
				}
				
				if (dataRow != null) {
					// Fill data to the complete measurement object
					CompleteDataMeasurement completeDataMeasurement = new CompleteDataMeasurement();
					GetSetClassInformation.setFillFields(completeDataMeasurement, dataRow, 1); // Because 0 = "#"
					
					// Fill the entities and save
					if(!completeDataMeasurement.getValveType().toLowerCase().contains(VALVE_TYPE_RSQ)){
						LxData lxData = new LxData();
						LxFlow lxFlow = new LxFlow();
						LxCurrent lxCurrent = new LxCurrent();
						
						GetSetClassInformation.copyFieldsToDestination(completeDataMeasurement, lxData);
						GetSetClassInformation.copyFieldsToDestination(completeDataMeasurement, lxCurrent);
						GetSetClassInformation.copyFieldsToDestination(completeDataMeasurement, lxFlow);
						
						lxData.setLxCurrent(lxCurrent);	
						lxData.setLxFlow(lxFlow);		
						lxDataRepository.save(lxData);
	
					}else {
						RsqData rsqData = new RsqData();
						GetSetClassInformation.copyFieldsToDestination(completeDataMeasurement, rsqData);		
						rsqDataRepository.save(rsqData);
					}					
				}
				br.close();
				CSVFile.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * This returns a filter that ends with endPart
	 * 
	 * @param endPart
	 * @return
	 */
	private FilenameFilter takeOnlyFilesThatEndsWith(String endPart) {
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File f, String name) {
				return name.endsWith(endPart);
			}
		};
		return filter;
	}
}