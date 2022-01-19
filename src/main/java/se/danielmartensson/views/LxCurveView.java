package se.danielmartensson.views;

import java.io.StringWriter;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import se.danielmartensson.entities.LxData;
import se.danielmartensson.service.LxDataService;
import se.danielmartensson.tools.DownloadTools;
import se.danielmartensson.tools.GetSetClassInformation;
import se.danielmartensson.tools.Graf;
import se.danielmartensson.tools.MenuLayout;

/**
 * The main view is a top-level placeholder for other views.
 */
@Route(value = "lxCurve", layout = MenuLayout.class)
public class LxCurveView extends VerticalLayout {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public LxCurveView(LxDataService lxDataService) {

		// Configuration of the grid
		GridCrud<LxData> crud = new GridCrud<>(LxData.class);
		crud.getGrid().setColumns("orderNumber", "valveName", "serialNumber", "valvePort", "testNumber", "valveType", "operator");
		crud.getGrid().setColumnReorderingAllowed(true);
		crud.setAddOperationVisible(false);
		crud.setDeleteOperationVisible(false);

		// Filter
		IntegerField maxLog = new IntegerField();
		maxLog.setPlaceholder("Max log");
		maxLog.setMax(10000);
		maxLog.setMin(0);
		maxLog.setValue(100);
		maxLog.addValueChangeListener(e -> {
			if (e.getValue() == null)
				maxLog.setValue(e.getOldValue());
			crud.refreshGrid();
		});
		TextField orderNumberFilter = createFilterField("Order Number", crud);
		TextField valveNameFilter = createFilterField("Valve Name", crud);
		TextField serialNumberFilter = createFilterField("Serial Number", crud);
		TextField valvePortFilter = createFilterField("Valve Port", crud);
		TextField testNumberFilter = createFilterField("Test Number", crud);
		TextField valveTypeFilter = createFilterField("Valve Type", crud);
		crud.getCrudLayout().addFilterComponents(orderNumberFilter, valveNameFilter, serialNumberFilter,
				valvePortFilter, testNumberFilter, valveTypeFilter);

		// Configuration of the user interface
		crud.getCrudFormFactory().setUseBeanValidation(true);
		crud.getCrudFormFactory().setVisibleProperties("orderNumber", "valveName", "serialNumber", "valvePort", "testNumber", "valveType", "operator"); // We don't want to see the flow values
		crud.getCrudFormFactory().setDisabledProperties("id");
		crud.getCrudFormFactory().setDisabledProperties(CrudOperation.ADD);


		// Download button
		Anchor downloadButton = new Anchor();
		downloadButton.getElement().setAttribute("download", true);
		Button download = new Button("Download curve as .csv", new Icon(VaadinIcon.DOWNLOAD_ALT));
		downloadButton.add(download);

		// User operations
		crud.setCrudListener(new CrudListener<LxData>() {
			private static final long serialVersionUID = 1L;

			@Override
			public LxData update(LxData lxData) {
				return lxDataService.save(lxData);
			}

			@Override
			public Collection<LxData> findAll() {
				return lxDataService.findByMultipleValues(orderNumberFilter.getValue(),
						valveNameFilter.getValue(), serialNumberFilter.getValue(), valvePortFilter.getValue(),
						testNumberFilter.getValue(), valveTypeFilter.getValue(), maxLog.getValue());
			}

			@Override
			public void delete(LxData lxData) {
				lxDataService.delete(lxData);
			}

			@Override
			public LxData add(LxData lxData) {
				return lxDataService.save(lxData);
			}
		});

		// Plot values
		ApexCharts graf = new Graf("Hysteresis curve").getApexChart();
		crud.getGrid().addSelectionListener(e -> {
			try {
				LxData lxData = e.getFirstSelectedItem().get();
				if(lxData == null)
					return;
				updateDownloadButtonForDownloadCurve(lxData, downloadButton);
				updateGrafWithCurve(lxData, graf);
			}catch(NoSuchElementException e2) {
				// Inget
			}catch(Exception e1) {
				e1.printStackTrace();
			}
		});

		// Layout
		VerticalLayout layout = new VerticalLayout(crud, new HorizontalLayout(downloadButton, maxLog), graf);
		layout.setAlignItems(Alignment.START);
		add(layout); //setContent(layout);
	}

	private void updateGrafWithCurve(LxData lxData, ApexCharts graf) {
		// Get data
		Float[] current = GetSetClassInformation.getFieldValuesFloat(lxData.getLxCurrent(), new String[] {"id"});
		Float[] flow = GetSetClassInformation.getFieldValuesFloat(lxData.getLxFlow(), new String[] {"id"});

		// Find max value and its index
		float maxValue = 0;
		int maxIndex = 0;
		for (int i = 0; i < current.length; i++) {
			if (current[i] >= maxValue) {
				maxValue = current[i];
				maxIndex = i;
			}
		}

		// Split flow array into one up and one down
		Float[] flowUp = new Float[current.length];
		Float[] flowDown = new Float[current.length];

		// Fill these arrays - Notice it must be at a special way because we cannot use current as X-axis with this chart library
		for (int i = 0; i < maxIndex; i++)
			flowUp[current.length - 1 - i] = flow[maxIndex - i];
	    for (int i = 0; i < current.length - maxIndex; i++)
			flowDown[maxIndex + i] = flow[current.length - 1 - i];

		// Create series
		Series<?>[] seriesList = new Series[2];
		seriesList[0] = Graf.createSerie(flowUp, "Flow up");
		seriesList[1] = Graf.createSerie(flowDown, "Flow down");
		graf.updateSeries(seriesList);

	}

	private void updateDownloadButtonForDownloadCurve(LxData lxData, Anchor downloadButton) {
		if (lxData == null)
			return;

		// Get all fields names and its values
		String[] dataFieldNames = GetSetClassInformation.getFieldNames(lxData, new String[] {"lxCurrent", "lxFlow"});
		String[] dataFieldValues = GetSetClassInformation.getFieldValuesString(lxData, new String[] {"lxCurrent", "lxFlow"});
		String[] lxCurrentNames = GetSetClassInformation.getFieldNames(lxData.getLxCurrent(), new String[] {"id"});
		Float[] lxCurrentValues = GetSetClassInformation.getFieldValuesFloat(lxData.getLxCurrent(), new String[] {"id"});
		String[] lxFlowNames = GetSetClassInformation.getFieldNames(lxData.getLxFlow(), new String[] {"id"});
		Float[] lxFlowValues = GetSetClassInformation.getFieldValuesFloat(lxData.getLxFlow(), new String[] {"id"});

		// Get all the header and then the values
		StringWriter stringWriter = new StringWriter();
		String row = "";
		for(String s : dataFieldNames)
			row += s + ",";
		for(String s : lxCurrentNames)
			row += s + ",";
		for(String s : lxFlowNames)
			row += s + ",";
		row += "\n";
		stringWriter.write(row);
		row = "";
		for(String s : dataFieldValues)
			row += s + ",";
		for(Float s : lxCurrentValues)
			row += s + ",";
		for(Float s : lxFlowValues)
			row += s + ",";
		stringWriter.write(row);

		// Try to download
		downloadButton.removeHref();
		downloadButton.setHref(DownloadTools.createStreamResource(stringWriter));
	}

	private TextField createFilterField(String placeHolder, GridCrud<LxData> crud) {
		TextField textField = new TextField();
		textField.setPlaceholder(placeHolder);
		textField.setClearButtonVisible(true);
		textField.addValueChangeListener(e -> crud.refreshGrid());
		return textField;
	}

}
