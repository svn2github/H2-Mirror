package org.h2.api;

import org.h2.message.DbException;
import org.h2.value.ValueGeometry;

import com.vividsolutions.jts.geom.Geometry;

public interface IValueGeometryFactory<T extends ValueGeometry<S>, S> {

	/**
	 * Get or create a geometry value for the given geometry.
	 *
	 * @param g
	 *            the geometry object
	 * @return the value
	 */
	public T get(Object g);
	
	public T get(byte[] g);
	
	public Class<S> getGeometryType();
	
	public boolean isGeometryTypeSupported(Object g);
	
	public S getGeometry(byte[] bytes) throws DbException;
	
	/**
     * Creates a {@link IGeometry} instance by using the given string
     * representation.
     *
     * @param s the string representation of the geometry
     * @return a new {@link IGeometry} instance
     * @throws GeometryParseException if a parsing problem occurs
     */
	public S getGeometry(String s) throws DbException;
	

    /**
     * Get or create a geometry value for the given geometry.
     *
     * @param s the WKT representation of the geometry
     * @param srid the srid of the object
     * @return the value
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
