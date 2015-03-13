/*
 * Copyright 2004-2014 H2 Group. Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.api;

import java.sql.Wrapper;

/**
 * Interface of a geometry type which provides all necessary information for the
 * H2 database and it's spatial index.
 * 
 * @author Steve Hruda, 2014
 */
public interface IGeometry extends Comparable<IGeometry>, Cloneable, Wrapper{
    /**
     * Returns the string representation of the geometry.
     * 
     * @return the string representation of the geometry
     */
    public String getString();

    /**
     * Returns the binary representation of the geometry.
     * 
     * @return the binary representation of the geometry
     */
    public byte[] getBytes();

    /**
     * Returns a full copy of this {@link IGeometry} object.
     * 
     * @return a full copy of this {@link IGeometry} object
     */
    public IGeometry clone();

    /**
     * Returns the geometries bounding box.
     * 
     * @return the bounding box
     */
    public IEnvelope getEnvelope();
    
    @Override
    public boolean isWrapperFor(Class<?> iface);
    
    @Override
    public <T> T unwrap(Class<T> iface);
}
