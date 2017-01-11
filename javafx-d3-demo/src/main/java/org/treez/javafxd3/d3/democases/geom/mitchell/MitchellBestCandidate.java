
package org.treez.javafxd3.d3.democases.geom.mitchell;

import org.treez.javafxd3.d3.geom.Quadtree.RootNode;
import org.treez.javafxd3.d3.D3;
import org.treez.javafxd3.d3.core.Selection;
import org.treez.javafxd3.d3.demo.AbstractDemoCase;
import org.treez.javafxd3.d3.demo.DemoCase;
import org.treez.javafxd3.d3.demo.DemoFactory;
import org.treez.javafxd3.d3.demo.Margin;
import org.treez.javafxd3.d3.functions.TimerFunction;

import javafx.scene.layout.VBox;

@SuppressWarnings("javadoc")
public class MitchellBestCandidate extends AbstractDemoCase {

	//#region ATTRIBUTES

	private boolean done = false;

	private int maxRadius = 22; // maximum radius of circle
	private int padding = 1; // padding between circles; also minimum radius
	private Margin margin = new Margin(-maxRadius, -maxRadius, -maxRadius, -maxRadius);
	private int width = 700 - margin.left - margin.right;
	private int height = 400 - margin.top - margin.bottom;
	private Selection svg;

	private TimerFunction timerFunction;

	//#end region

	//#region CONSTRUTORS

	/**
	 * Constructor
	 * 
	 * @param d3
	 * @param demoPreferenceBox
	 */
	public MitchellBestCandidate(D3 d3, VBox demoPreferenceBox) {
		super(d3, demoPreferenceBox);
	}

	//#end region

	//#region METHODS

	/**
	 * Factory provider
	 * 
	 * @param d3
	 * @param demoPreferenceBox
	 * @return
	 */
	public static DemoFactory factory(D3 d3, VBox demoPreferenceBox) {
		return new DemoFactory() {
			@Override
			public DemoCase newInstance() {
				return new MitchellBestCandidate(d3, demoPreferenceBox);
			}
		};
	}

	@Override
	public void start() {

		final CircleGenerator circleGenerator = createBestCircleGenerator(maxRadius, padding);

		svg = d3.select("svg") //				
				.attr("width", width) //
				.attr("height", height) //
				.append("g") //
				.attr("transform", "translate(" + margin.left + "," + margin.top + ")");

		timerFunction = new MitchellTimerFunction(circleGenerator, svg, this);
		done = false;
		d3.timer(timerFunction);

	}

	@Override
	public void stop() {
		done = true;
	}

	private CircleGenerator createBestCircleGenerator(final double maxRadius, final double padding) {

		final RootNode<Circle> quadtree = d3.geom() //
				.quadtree() //
				.x(new XDataFunction(webEngine)) //
				.y(new YDataFunction(webEngine)) //
				.extent(0, 0, width, height) //
				.apply(new Circle[1]);

		return new MitchellCircleGenerator(webEngine, quadtree, maxRadius, width, height, padding) ;
	}
	
	//#end region
	
	//#region ACCESSORS
	
	public boolean getDone() {
		return done;
	}
	
	//#end region

}
