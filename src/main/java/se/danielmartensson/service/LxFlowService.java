package se.danielmartensson.service;

import java.util.List;

import org.springframework.stereotype.Service;

import se.danielmartensson.entities.LxFlow;
import se.danielmartensson.repositories.LxFlowRepository;

@Service
public class LxFlowService {
	
	private final LxFlowRepository lxFlowRepository;
	
	public LxFlowService(LxFlowRepository lxFlowRepository) {
        this.lxFlowRepository = lxFlowRepository;
    }

    public List<LxFlow> findAll() {
        return lxFlowRepository.findAll();
    }

    public long count() {
        return lxFlowRepository.count();
    }

    public LxFlow save(LxFlow lxFlow) {
        return lxFlowRepository.save(lxFlow);
    }

	public void delete(LxFlow lxFlow) {
		lxFlowRepository.delete(lxFlow);
	}
}
