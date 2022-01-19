package se.danielmartensson.tools;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder;
import com.github.appreciated.apexcharts.config.builder.GridBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.StrokeBuilder;
import com.github.appreciated.apexcharts.config.builder.TitleSubtitleBuilder;
import com.github.appreciated.apexcharts.config.builder.TooltipBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.AnimationsBuilder;
import com.github.appreciated.apexcharts.config.chart.builder.ToolbarBuilder;
import com.github.appreciated.apexcharts.config.chart.builder.ZoomBuilder;
import com.github.appreciated.apexcharts.config.grid.builder.RowBuilder;
import com.github.appreciated.apexcharts.config.stroke.Curve;
import com.github.appreciated.apexcharts.config.subtitle.Align;
import com.github.appreciated.apexcharts.config.yaxis.builder.TitleBuilder;
import com.github.appreciated.apexcharts.helper.Series;

import lombok.Data;

@Data
public class Graf {

	private ApexCharts apexChart;

	public Graf(String title) {
		apexChart = ApexChartsBuilder.get()
				.withChart(ChartBuilder.get()
						.withType(Type.line)
						.withZoom(ZoomBuilder.get()
								.withEnabled(true)
								.build())
						.withToolbar(ToolbarBuilder.get()
								.withShow(true)
								.build())
						.withAnimations(AnimationsBuilder.get()
								.withEnabled(false)
								.build())
						.build())
				.withLegend(LegendBuilder.get()
						.withShow(true)
						.build())
				.withDataLabels(DataLabelsBuilder.get()
						.withEnabled(false)
						.build())
				.withColors("#48912c", "#13ebd5", "#215ed9", "#e6c222", "#a524e0", "#633326", "#BC7050")
				.withTooltip(TooltipBuilder.get()
						.withEnabled(false)
						.build())
				.withStroke(StrokeBuilder.get()
						.withCurve(Curve.straight)
						.build())
				.withTitle(TitleSubtitleBuilder.get()
						.withText(title)
						.withAlign(Align.left)
						.build())
				.withGrid(GridBuilder.get()
						.withRow(RowBuilder.get()
								.withColors("#7E93C1", "transparent")
								.withOpacity(0.5)
								.build())
						.build())
				.withYaxis(YAxisBuilder.get()
						.withOpposite(true)
						.withTitle(TitleBuilder.get()
								.withText("Measurements")
								.build())
						.build())
				.withXaxis(XAxisBuilder.get()
						.withTitle(com.github.appreciated.apexcharts.config.xaxis.builder.TitleBuilder.get()
								.withText("Samples")
								.build())
						.withCategories("")
						.build())
				.withTooltip(TooltipBuilder.get()
						.withEnabled(false)
						.withShared(false)
						.build())
				.withSeries(
						createSerie(new Float[] { 0f }, "analog"))
				.build();
		apexChart.setWidthFull();
	}

	static public Series<Float> createSerie(Float[] data, String legend) {
		return new Series<>(legend, data);
	}
}