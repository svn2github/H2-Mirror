/*
 * Copyright 2004-2016 H2 Group. Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.api;

/** 
 * Interface which will be used by h2 to create the geometry factory.
 * 
 * @author Steve Hruda
 */
public interface SpatialDriver {

	/**
	 * A new {@link ValueGeometryFactory} instance.
	 * 
	 * @return a new {@link ValueGeometryFactory} instance
	 */
	public ValueGeometryFactory<?,?> createGeometryFactory();
}
