package org.treez.javafxd3.d3.svg;

import org.treez.javafxd3.d3.AbstractTestCase;
import org.treez.javafxd3.d3.svg.datumfunction.ChordRadiusDatumFunction;
import org.treez.javafxd3.d3.svg.datumfunction.ChordSourceDatumFunction;
import org.treez.javafxd3.d3.svg.datumfunction.ChordStartAngleDatumFunction;
import org.treez.javafxd3.d3.svg.datumfunction.ChordTargetDatumFunction;


@SuppressWarnings("javadoc")
public class ChordTest extends AbstractTestCase {

	@Override	
	public void doTest() {
		
		
		
		Chord chord = d3.svg().chord();

		// constants
		chord.startAngle(5);
		chord.endAngle(5);
		chord.radius(6);

		chord.source(new ChordSourceDatumFunction());
		chord.target(new ChordTargetDatumFunction());
		
		// chord
		chord.startAngle(new ChordStartAngleDatumFunction(webEngine)) //
		.endAngle(new ChordStartAngleDatumFunction(webEngine)) //
		.radius(new ChordRadiusDatumFunction(webEngine));

		chord.generate(new Double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0});
	}

	
}
