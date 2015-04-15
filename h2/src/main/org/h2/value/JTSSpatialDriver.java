package org.h2.value;

import org.h2.api.ISpatialDriver;
import org.h2.api.IValueGeometryFactory;


public class JTSSpatialDriver implements ISpatialDriver{

	@Override
	public IValueGeometryFactory<?, ?> createGeometryFactory() {
		return new JTSValueGeometryFactory();
	}
}