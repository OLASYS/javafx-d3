package org.treez.javafxd3.d3.democases.axis;

import org.treez.javafxd3.d3.core.Value;
import org.treez.javafxd3.d3.demo.Data;
import org.treez.javafxd3.d3.functions.DataFunction;
import org.treez.javafxd3.d3.scales.LinearScale;

import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

public class YAxisDataFunction implements DataFunction<Double> {
	
	//#region ATTRIBUTES
	
	private WebEngine webEngine;
	
	private LinearScale yScale;
	
	//#end region
	
	//#region CONSTRUCTORS
	
	public YAxisDataFunction(WebEngine webEngine, LinearScale yScale ){
		this.webEngine=webEngine;
		this.yScale = yScale;
	}
	
	//#end region

	//#region METHODS
	
	@Override
	public Double apply(final Object context, final Object d, final int index) {
		
		JSObject datum = (JSObject) d;
		Value value = new Value(webEngine, datum);
		Data data = value.as();
		Double price = data.getPrice();		
		Value outputValue = yScale.apply(price);
		Double result = outputValue.asDouble();	
		return result;
	}
	
	//#end region
}
