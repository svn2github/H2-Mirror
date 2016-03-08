package org.h2.value;

import org.h2.api.ISpatialDriver;
import org.h2.api.ValueGeometryFactory;


public class JTSSpatialDriver implements ISpatialDriver{

	@Override
	public ValueGeometryFactory<?, ?> createGeometryFactory() {
		return new JTSValueGeometryFactory();
	}
}