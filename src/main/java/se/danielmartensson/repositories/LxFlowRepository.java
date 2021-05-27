package se.danielmartensson.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.danielmartensson.entities.LxFlow;


@Repository
public interface LxFlowRepository extends JpaRepository<LxFlow, Long> {

}