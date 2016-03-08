/*
 * Copyright 2004-2015 H2 Group. Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.api;

import org.h2.message.DbException;
import org.h2.value.Value;
import org.h2.value.ValueGeometry;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Interface of a factory which provides methods for the conversion of a
 * geometry object of a framework like JTS into a {@link ValueGeometry}. 
 *
 * @author Steve Hruda
 * @param <T> the type of your {@link ValueGeometry} implementation
 * @param <S> the type of the frameworks geometry object
 */
public interface ValueGeometryFactory<T extends ValueGeometry<S>, S> {

	/**
	 * Get or create a geometry value for the given geometry.
	 *
	 * @param g
	 *            the geometry object
	 * @return the value
	 */
	public T get(Object g);
	
	/**
	 * Get or create a geometry value for the given byte array.
	 * @param g
	 *            the geometry object as a byte array
	 * @return the value
	 */
	public T get(byte[] g);
	
	/**
	 * Get or create a geometry value for the given {@link Value}.
	 * @param g
	 *            the geometry as {@link Value}
	 * @return the value
	 */
	public T get(Value g);
	
	/**
	 * Returns the type of the used geometry framework.
	 * @return the type of the used geometry framework.
	 */
	public Class<S> getGeometryType();
	
	/**
	 * Returns <code>true</code> if the given object is an instance
	 * of the used geometry framework, <code>false</code> otherwise.
	 * @param g 
	 *            the geometry
	 * @return <code>true</code> if the given object is an instance
	 * of the used geometry framework, <code>false</code> otherwise.
	 */
	public boolean isGeometryTypeSupported(Object g);
	
	/**
	 * Creates a geometry instance by using the given byte array 
	 * representation.
     * 
	 * @param bytes the byte array representation of the geometry
	 * @return a new geometry instance
     * @throws DbException - if an exception occurs during the creation
	 */
	public S getGeometry(byte[] bytes) throws DbException;
	
	/**
     * Creates a geometry instance by using the given string
     * representation.
     *
     * @param s the string representation of the geometry
     * @return a new geometry instance
     * @throws DbException - if an exception occurs during the creation
     */
	public S getGeometry(String s) throws DbException;

    /**
     * Get or create a geometry value for the given geometry.
     *
     * @param s the WKT representation of the geometry
     * @param srid the srid of the object
     * @return the value
     * @throws DbException - if an exception occurs during the creation
     */
	public S getGeometry(String s, int srid) throws DbException;
	
	/**
	 * Get or create a geometry value for the given geometry.
	 *
	 * @param g
	 *            the geometry object
	 * @return the value
	 */
	public T get(Geometry g);

	/**
	 * Get or create a geometry value for the given geometry.
	 *
	 * @param s the WKT representation of the geometry
	 * @return the value
	 */
	public T get(String s);
	
	/**
     * Get or create a geometry value for the given geometry.
     *
     * @param s the WKT representation of the geometry
     * @param srid the srid of the object
     * @return the value
     */
    public T get(String s, int srid);
}
