package se.danielmartensson.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.danielmartensson.entities.LxCurrent;


@Repository
public interface LxCurrentRepository extends JpaRepository<LxCurrent, Long> {

}