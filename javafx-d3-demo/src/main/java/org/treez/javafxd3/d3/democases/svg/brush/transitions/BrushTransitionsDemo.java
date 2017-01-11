package org.treez.javafxd3.d3.democases.svg.brush.transitions;

import java.util.ArrayList;
import java.util.List;

import org.treez.javafxd3.d3.D3;
import org.treez.javafxd3.d3.arrays.Array;
import org.treez.javafxd3.d3.coords.Coords;
import org.treez.javafxd3.d3.core.ConversionUtil;
import org.treez.javafxd3.d3.core.Selection;
import org.treez.javafxd3.d3.demo.AbstractDemoCase;
import org.treez.javafxd3.d3.demo.DemoCase;
import org.treez.javafxd3.d3.demo.DemoFactory;
import org.treez.javafxd3.d3.functions.DataFunction;
import org.treez.javafxd3.d3.functions.data.wrapper.CompleteDataFunctionWrapper;
import org.treez.javafxd3.d3.functions.data.wrapper.DataFunctionWrapper;
import org.treez.javafxd3.d3.geom.Quadtree.RootNode;
import org.treez.javafxd3.d3.scales.IdentityScale;
import org.treez.javafxd3.d3.svg.Brush;
import org.treez.javafxd3.d3.svg.Brush.BrushEvent;
import org.treez.javafxd3.d3.wrapper.Element;

import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

public class BrushTransitionsDemo extends AbstractDemoCase {

	//#region ATTRIBUTES

	private Brush brush;

	private Selection svg;

	private Selection point;

	//#end region

	//#region CONSTRUCTORS

	public BrushTransitionsDemo(D3 d3, VBox demoPreferenceBox) {
		super(d3, demoPreferenceBox);
	}

	//#end region

	//#region METHODS

	public static DemoFactory factory(D3 d3, VBox demoPreferenceBox) {
		return new DemoFactory() {
			@Override
			public DemoCase newInstance() {
				return new BrushTransitionsDemo(d3, demoPreferenceBox);
			}
		};
	}

	@Override
	public void start() {
		final int width = 960;
		final int height = 500;

		final Double[][] defaultExtent = new Double[][] { { 100.0, 100.0 }, { 300.0, 300.0 } };

		
		Array<Double> pointSeed = d3.range(50.0);
		
		List<Point> points = new ArrayList<>();
		
		pointSeed.forEach((value)->{
			Point point = new Point(webEngine, Math.random() * width, Math.random() *
					  width);
			points.add(point);
		});		 
		 
		Point[] data =  points.toArray(new Point[points.size()]); 

		final RootNode<Point> quadtree = d3.geom() //
				.quadtree() //
				.extent(-1, -1, width + 1, height + 1).x(Coords.getXAccessor(webEngine)) //
				.y(Coords.getYAccessor(webEngine)) //
				.apply(data);

		IdentityScale x = d3.scale() //
				.identity() //
				.domain(0, width);

		IdentityScale y = d3.scale() //
				.identity() //
				.domain(0, height);

		DataFunction<Void> brushFunction = new CompleteDataFunctionWrapper<>(new DataFunction<Void>() {
			@Override
			public Void apply(final Object context, final Object d, final int index) {
				Array<Double> extent = brush.extent();

				DataFunction<Void> unselectFunction = new DataFunctionWrapper<>(Point.class, webEngine, (point) -> {
					point.setSelected(false);
					return null;
				});

				point.each(unselectFunction);
				search(quadtree, extent.get(0, 0, Double.class), extent.get(0, 1, Double.class),
						extent.get(1, 0, Double.class), extent.get(1, 1, Double.class));

				DataFunction<Boolean> isSelectedFunction = new DataFunctionWrapper<>(Point.class, webEngine,
						(point) -> {
							return point.isSelected();
						});

				point.classed("selected", isSelectedFunction);
				return null;
			}
		});

		DataFunction<Void> brushEndFunction = new CompleteDataFunctionWrapper<>(new DataFunction<Void>() {
			@Override
			public Void apply(final Object context, final Object d, final int index) {
				if (d3.event().sourceEvent() == null) {
					return null; // only transition after input
				}

				Element element = ConversionUtil.convertObjectTo(context, Element.class, webEngine);

				d3.select(element) //
						.transition() //
						.duration(brush.empty() ? 0 : 750) //
						.call(brush.extent(defaultExtent)) //
						.call(brush.event());

				return null;

			}
		});

		brush = d3.svg() //
				.brush() //
				.x(x) //
				.y(y) //
				.extent(defaultExtent) //
				.on(BrushEvent.BRUSH, brushFunction) //
				.on(BrushEvent.BRUSH_END, brushEndFunction);

		svg = d3.select("#svg") //
				.attr("width", width) //
				.attr("height", height);

		DataFunction<Double> cxFunction = new DataFunctionWrapper<>(Point.class, webEngine, (point) -> {
			return point.x();
		});

		DataFunction<Double> cyFunction = new DataFunctionWrapper<>(Point.class, webEngine, (point) -> {
			return point.y();
		});

		point = svg.selectAll(".point") //
				.data(data) //
				.enter() //
				.append("circle") //
				.attr("class", "point") //
				.attr("cx", cxFunction) //
				.attr("cy", cyFunction) //
				.attr("r", 4);

		svg.append("g") //
				.attr("class", "brush") //
				.call(brush) //
				.call(brush.event());

	}

	/**
	 * Finds the nodes within the specified rectangle.
	 */
	private void search(final RootNode<Point> quadtreeRoot, final double x0, final double y0, final double x3,
			final double y3) {

		quadtreeRoot.visit(new BrushTransitionsDemoCallback(webEngine, x0, y0, x3, y3));
	}

	@Override
	public void stop() {

	}

	//#end region

	//#region CLASSES

	/**
	 * Represents a point that can be selected
	 */
	public static class Point extends Coords {

		//#region CONSTRUCTORS

		public Point(WebEngine webEngine, JSObject wrappedJsObject) {
			super(webEngine, wrappedJsObject);
		}

		public Point(WebEngine webEngine, double x, double y) {
			super(webEngine, x, y);
		}

		//#end region

		//#region METHODS

		public void setSelected(boolean isSelected) {
			String command = "this.selected = " + isSelected + ";";
			eval(command);
		}

		public boolean isSelected() {
			Boolean result = getMemberForBoolean("selected");
			return result;
		}

		//#end region

	}

	//#end region

}
