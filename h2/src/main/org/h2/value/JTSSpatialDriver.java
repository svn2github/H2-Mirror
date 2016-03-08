/*
 * Copyright 2004-2016 H2 Group. Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.value;

import org.h2.api.SpatialDriver;
import org.h2.api.ValueGeometryFactory;

/**
 * The {@link SpatialDriver} implementation for the JTS geometry framework.
 * 
 * @author Steve Hruda, 2016
 */
public class JTSSpatialDriver implements SpatialDriver{

	/**
	 * @see org.h2.api.SpatialDriver#createGeometryFactory()
	 */
	@Override
	public ValueGeometryFactory<?, ?> createGeometryFactory() {
		return new JTSValueGeometryFactory();
	}
}