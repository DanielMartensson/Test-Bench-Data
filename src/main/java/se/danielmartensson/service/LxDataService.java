package se.danielmartensson.service;

import java.util.List;

import org.springframework.stereotype.Service;

import se.danielmartensson.entities.LxData;
import se.danielmartensson.repositories.LxDataRepository;

@Service
public class LxDataService {

	private final LxDataRepository lxDataRepository;

	public LxDataService(LxDataRepository lxDataRepository) {
        this.lxDataRepository = lxDataRepository;
    }

    public List<LxData> findAll() {
        return lxDataRepository.findAll();
    }

    public long count() {
        return lxDataRepository.count();
    }

    public LxData save(LxData lxData) {
    	if(lxData.getLxCurrent() == null || lxData.getLxFlow() == null)
    		return lxData; // This happens when you create data by your self! Not allowed!
        return lxDataRepository.save(lxData);
    }

	public void delete(LxData lxData) {
		lxDataRepository.delete(lxData);
	}

	public List<LxData> findByMultipleValues(String orderNumber, String valveName, String serialNumber, String valvePort, String testNumber, String valveType, Integer maxLog) {
		if(maxLog < 0)
			maxLog = 0;
		return lxDataRepository.findByMultipleValues(orderNumber, valveName, serialNumber, valvePort, testNumber, valveType, maxLog);
	}
}
