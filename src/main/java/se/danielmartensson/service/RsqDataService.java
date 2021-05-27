package se.danielmartensson.service;

import java.util.List;

import org.springframework.stereotype.Service;

import se.danielmartensson.components.Schedule;
import se.danielmartensson.entities.RsqData;
import se.danielmartensson.repositories.RsqDataRepository;

@Service
public class RsqDataService {
	
	private final RsqDataRepository rsqDataRepository;
	
	public RsqDataService(RsqDataRepository rsqDataRepository) {
        this.rsqDataRepository = rsqDataRepository;
    }

    public List<RsqData> findAll() {
        return rsqDataRepository.findAll();
    }

    public long count() {
        return rsqDataRepository.count();
    }

    public RsqData save(RsqData rsqData) {
    	if(!rsqData.getValveType().equals(Schedule.VALVE_TYPE_RSQ))
    		rsqData.setValveType(Schedule.VALVE_TYPE_RSQ); // We need to have this entity on the RSQ crud
        return rsqDataRepository.save(rsqData);
    }

	public void delete(RsqData rsqData) {
		rsqDataRepository.delete(rsqData);
	}
	
	public List<RsqData> findByMultipleValues(String orderNumber, String valveName, String serialNumber, String valvePort, String testNumber, String valveType, Integer maxLog) {
		if(maxLog < 0)
			maxLog = 0;
		return rsqDataRepository.findByMultipleValues(orderNumber, valveName, serialNumber, valvePort, testNumber, valveType, maxLog);
	}
}
