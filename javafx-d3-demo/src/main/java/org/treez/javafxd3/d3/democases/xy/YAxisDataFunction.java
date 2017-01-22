package org.treez.javafxd3.d3.democases.xy;

import org.treez.javafxd3.d3.core.Value;
import org.treez.javafxd3.d3.functions.DataFunction;
import org.treez.javafxd3.d3.scales.LinearScale;

import org.treez.javafxd3.d3.core.JsEngine;
import org.treez.javafxd3.d3.core.JsObject;

public class YAxisDataFunction implements DataFunction<Double> {
	
	//#region ATTRIBUTES
	
	private JsEngine engine;
	
	private LinearScale yScale;
	
	//#end region
	
	//#region CONSTRUCTORS
	
	public YAxisDataFunction(JsEngine engine, LinearScale yScale ){
		this.engine=engine;
		this.yScale = yScale;
	}
	
	//#end region

	//#region METHODS
	
	@Override
	public Double apply(final Object context, final Object d, final int index) {
		
		JsObject datum = (JsObject) engine.toJsObjectIfNotSimpleType(d);
		Value inputValue = new Value(engine, datum);
		
		Double input = inputValue.asDouble();
		Value value = yScale.apply(input);
		Double result = value.asDouble();
		return result;
		
		
		
		
		
		
	}
	
	//#end region
}
