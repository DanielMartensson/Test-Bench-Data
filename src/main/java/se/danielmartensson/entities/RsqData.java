package se.danielmartensson.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class RsqData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	private String orderNumber;
	@NotNull
	private String valveName;
	@NotNull
	private String serialNumber;
	@NotNull
	private String valvePort;
	@NotNull
	private int testNumber;
	@NotNull
	private String operator;
	@NotNull
	private String valveType;
	@NotNull
	private boolean externalLeakageTest;
	@NotNull
	private boolean shuttleSpoolGrippingTest;
	@NotNull
	private boolean checkValveLeakageTest;
	@NotNull
	private boolean mainSpoolLeakageTest;
	@NotNull
	private float shuttleLeakageValue;
	@NotNull
	private String date;
	@NotNull
	private float oilTemperature; // Den heter Oljatemperatur i Codesys
	@NotNull
	private int iso4P;
	@NotNull
	private int iso6P;
	@NotNull
	private int iso14P;
	@NotNull
	private int iso4T;
	@NotNull
	private int iso6T;
	@NotNull
	private int iso14T;
	@NotNull
	private boolean shockPressureTest;
	@NotNull
	private boolean spoolGrippingTest;
	@NotNull
	private boolean headPressureReliefValveTest;
	@NotNull
	private boolean castingTest;
	@NotNull
	private boolean assemblyTest;
	@NotNull
	private boolean electricalTest;
	@NotNull
	private String otherText;
}
