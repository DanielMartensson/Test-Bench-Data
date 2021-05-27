package se.danielmartensson.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import se.danielmartensson.entities.RsqData;


@Repository
public interface RsqDataRepository extends JpaRepository<RsqData, Long> {
	
	@Transactional
	@Query(value = "SELECT * FROM rsq_data WHERE LOWER(order_number) LIKE LOWER(CONCAT('%', :orderNumber,'%')) AND LOWER(valve_name) LIKE LOWER(CONCAT('%', :valveName,'%')) AND LOWER(serial_number) LIKE LOWER(CONCAT('%', :serialNumber,'%')) AND LOWER(valve_port) LIKE LOWER(CONCAT('%', :valvePort,'%')) AND LOWER(test_number) LIKE LOWER(CONCAT('%', :testNumber,'%')) AND LOWER(valve_type) LIKE LOWER(CONCAT('%', :valveType,'%')) ORDER BY id DESC LIMIT :maxLog", nativeQuery = true) // MySQL
	//@Query(value = "SELECT * FROM rsq_data WHERE LOWER(order_number) LIKE LOWER(CONCAT('%', :orderNumber,'%')) AND LOWER(valve_name) LIKE LOWER(CONCAT('%', :valveName,'%')) AND LOWER(serial_number) LIKE LOWER(CONCAT('%', :serialNumber,'%')) AND LOWER(valve_port) LIKE LOWER(CONCAT('%', :valvePort,'%')) AND LOWER(test_number) LIKE LOWER(CONCAT('%', :testNumber,'%')) AND LOWER(valve_type) LIKE LOWER(CONCAT('%', :valveType,'%')) ORDER BY id DESC OFFSET 0 ROWS FETCH NEXT :maxLog ROWS ONLY", nativeQuery = true) // MSSQL
	List<RsqData> findByMultipleValues(@Param("orderNumber") String orderNumber, @Param("valveName") String valveName, @Param("serialNumber") String serialNumber, @Param("valvePort")  String valvePort, @Param("testNumber") String testNumber, @Param("valveType") String valveType, @Param("maxLog") Integer maxLog);
	
}