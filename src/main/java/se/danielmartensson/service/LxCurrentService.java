package se.danielmartensson.service;

import java.util.List;

import org.springframework.stereotype.Service;

import se.danielmartensson.entities.LxCurrent;
import se.danielmartensson.repositories.LxCurrentRepository;

@Service
public class LxCurrentService {
	
	private final LxCurrentRepository lxCurrentRepository;
	
	public LxCurrentService(LxCurrentRepository lxCurrentRepository) {
        this.lxCurrentRepository = lxCurrentRepository;
    }

    public List<LxCurrent> findAll() {
        return lxCurrentRepository.findAll();
    }

    public long count() {
        return lxCurrentRepository.count();
    }

    public LxCurrent save(LxCurrent lxCurrent) {
        return lxCurrentRepository.save(lxCurrent);
    }

	public void delete(LxCurrent lxCurrent) {
		lxCurrentRepository.delete(lxCurrent);
	}
}
