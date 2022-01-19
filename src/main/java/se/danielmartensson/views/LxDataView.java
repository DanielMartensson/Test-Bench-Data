package se.danielmartensson.views;

import java.io.StringWriter;
import java.util.Collection;
import java.util.List;

import org.vaadin.crudui.crud.CrudListener;
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
import se.danielmartensson.service.UserSettingsService;
import se.danielmartensson.tools.DownloadTools;
import se.danielmartensson.tools.GetSetClassInformation;
import se.danielmartensson.tools.Graf;
import se.danielmartensson.tools.MenuLayout;

/**
 * The main view is a top-level placeholder for other views.
 */
@Route(value = "", layout = MenuLayout.class)
public class LxDataView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private List<LxData> crudTable;

	public LxDataView(LxDataService lxDataService, UserSettingsService userSettingsService) {

		// Configuration of the grid
		GridCrud<LxData> crud = new GridCrud<>(LxData.class);
		crud.getGrid().setColumns("orderNumber", "valveName", "serialNumber", "valvePort", "testNumber", "valveType", "operator");
		crud.getGrid().setColumnReorderingAllowed(true);
		crud.setAddOperationVisible(false);
		crud.setUpdateOperationVisible(userSettingsService.loggedInUserIsMaster());
		crud.setDeleteOperationVisible(userSettingsService.loggedInUserIsMaster());

		// Filter
		IntegerField maxLog = new IntegerField();
		maxLog.setPlaceholder("Max log");
		maxLog.setMax(10000);
		maxLog.setMin(0);
		maxLog.setValue(100);
		maxLog.addValueChangeListener(e -> {
			if(e.getValue() == null)
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
		crud.getCrudFormFactory().setDisabledProperties("id");

		// Download button
		Anchor downloadButton = new Anchor();
		downloadButton.getElement().setAttribute("download", true);
		Button download = new Button("Download data as .csv", new Icon(VaadinIcon.DOWNLOAD_ALT));
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
				crudTable = lxDataService.findByMultipleValues(orderNumberFilter.getValue(),
						valveNameFilter.getValue(), serialNumberFilter.getValue(), valvePortFilter.getValue(),
						testNumberFilter.getValue(), valveTypeFilter.getValue(), maxLog.getValue());
				updateDownloadButtonForDownloadTable(crudTable, downloadButton);
				return crudTable;
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
		ApexCharts graf = new Graf("Spool leakage & ISO contamination").getApexChart();
		Button updateGraf = new Button("Update graph");
		updateGraf.addClickListener(e -> updateGrafWithData(crudTable, graf));

		// Layout
		VerticalLayout layout = new VerticalLayout(crud, new HorizontalLayout(downloadButton,updateGraf, maxLog), graf);
		layout.setAlignItems(Alignment.START);
		add(layout); //setContent(layout);
	}

	private void updateGrafWithData(List<LxData> crudTable, ApexCharts graf) {
		// Collect all data and create the series for all contamination 6 ISO codes + 1 main spool leakage
		Series<?>[] seriesList = new Series[7];
		int lengthCrudTable = crudTable.size();
		Float[] iso14P = new Float[lengthCrudTable];
		Float[] iso6P = new Float[lengthCrudTable];
		Float[] iso4P = new Float[lengthCrudTable];
		Float[] iso14T = new Float[lengthCrudTable];
		Float[] iso6T = new Float[lengthCrudTable];
		Float[] iso4T = new Float[lengthCrudTable];
		Float[] spoolLeakage = new Float[lengthCrudTable];
		for(int i = 0; i < lengthCrudTable; i++) {
			LxData lxData = crudTable.get(lengthCrudTable - 1 - i); // Reverse is important
			iso14P[i] = (float) lxData.getIso14P();
			iso6P[i] = (float) lxData.getIso6P();
			iso4P[i] = (float) lxData.getIso4P();
			iso14T[i] = (float) lxData.getIso14T();
			iso6T[i] = (float) lxData.getIso6T();
			iso4T[i] = (float) lxData.getIso4T();
			spoolLeakage[i] = lxData.getMainSpoolLeakageValue();
		}
		seriesList[0] = Graf.createSerie(iso14P, "Iso14P");
		seriesList[1] = Graf.createSerie(iso14T, "Iso14T");
		seriesList[2] = Graf.createSerie(iso6P, "Iso6P");
		seriesList[3] = Graf.createSerie(iso6T, "Iso6T");
		seriesList[4] = Graf.createSerie(iso4P, "Iso4P");
		seriesList[5] = Graf.createSerie(iso4T, "Iso4T");
		seriesList[6] = Graf.createSerie(spoolLeakage, "Main spool leakage");
		graf.updateSeries(seriesList);
	}

	private void updateDownloadButtonForDownloadTable(List<LxData> crudTable, Anchor downloadButton) {
		if (crudTable == null)
			return;

		// Use StringWritter
		StringWriter stringWriter = new StringWriter();

		// Create the header
		String[] fieldNames = GetSetClassInformation.getFieldNames(new LxData(), new String[] {"lxCurrent", "lxFlow"});
		String row = "";
		for (String fieldName : fieldNames)
			row += fieldName + ",";
		row += "\n";
		stringWriter.write(row);

		// Create the data
		for (LxData lxData : crudTable) {
			String[] values = GetSetClassInformation.getFieldValuesString(lxData, new String[] {"lxCurrent", "lxFlow"});
			row = "";
			for(String value : values)
				row += value + ",";
			row += "\n";
			stringWriter.write(row);
		}

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