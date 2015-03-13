/*
 * Copyright 2004-2014 H2 Group. Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.api;


/**
 * Interface of a factory which provides a couple of methods to create a
 * {@link IGeometry} instance.
 */
public interface IGeometryFactory {
    /**
     * Creates a {@link IGeometry} instance by using the given string
     * representation.
     *
     * @param s the string representation of the geometry
     * @return a new {@link IGeometry} instance
     * @throws GeometryParseException if a parsing problem occurs
     */
    public IGeometry toGeometry(String s) throws GeometryParseException;

    /**
     * Creates a {@link IGeometry} instance by using the given parameters.
     *
     * @param s the string representation of the geometry
     * @param srid the srid of the object
     * @return a new {@link IGeometry} instance
     * @throws GeometryParseException if a parsing problem occurs
     */
    public IGeometry toGeometry(String s, int srid) throws GeometryParseException;

    /**
     * Creates a {@link IGeometry} instance by using the given binary
     * representation.
     *
     * @param bytes the binary representation of the geometry
     * @return a new {@link IGeometry} instance
     * @throws GeometryParseException if a parsing problem occurs
     */
    public IGeometry toGeometry(byte[] bytes) throws GeometryParseException;

    /**
     * Creates a {@link IGeometry} instance by using the given parameters.
     * 
     * @param envelope the envelope
     * @return a new {@link IGeometry} instance
     */
    public IGeometry toGeometry(IEnvelope envelope);
}
