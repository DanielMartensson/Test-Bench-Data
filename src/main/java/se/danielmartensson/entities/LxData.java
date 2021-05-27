package se.danielmartensson.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class LxData {
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
	private float flowSetup;
	@NotNull
	private float pressureSetup;
	@NotNull
	private float lsprvPressureSetup;
	@NotNull
	private boolean lsprvPressureSetupTest;
	@NotNull
	private float loadSetup;
	@NotNull
	private String operator;
	@NotNull
	private int maxFlow;
	@NotNull
	private String valveType;
	@NotNull
	private boolean externalLeakageTest;
	@NotNull
	private float lsPressureSetup;
	@NotNull
	private float copyValvePressureSetup;
	@NotNull
	private boolean shuttleSpoolGrippingTest;
	@NotNull
	private float shuttleSpoolGrippingValue;
	@NotNull
	private boolean checkValveLeakageTest;
	@NotNull
	private float checkValveLeakageValue;
	@NotNull
	private boolean mainSpoolLeakageTest;
	@NotNull
	private float mainSpoolLeakageValue;
	@NotNull
	private boolean shuttleLeakageTest;
	@NotNull
	private float shuttleLeakageValue;
	@NotNull
	private String date;
	@NotNull
	private float oilTemperature; 
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
	private float shockPressureValue;
	@NotNull
	private boolean hysteresisMaxTest;
	@NotNull
	private float hysteresisMaxValue;
	@NotNull
	private float pMainPressureReliefValue;
	@NotNull
	private float abPressureReliefValue;
	@NotNull
	private boolean bleedFlowTest;
	@NotNull
	private float bleedFlowValue;
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
	
	@OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
	private LxCurrent lxCurrent;
    
	@OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    private LxFlow lxFlow;
}
