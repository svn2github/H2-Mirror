/*
 * Copyright 2004-2014 H2 Group. Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.value;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ServiceLoader;
import org.h2.api.GeometryParseException;
import org.h2.api.IEnvelope;
import org.h2.api.IGeometry;
import org.h2.api.IGeometryFactory;
import org.h2.message.DbException;
import org.h2.util.StringUtils;

/**
 * Implementation of the GEOMETRY data type.
 *
 * @author Thomas Mueller
 * @author Noel Grandin
 * @author Nicolas Fortin, Atelier SIG, IRSTV FR CNRS 24888
 */
public class ValueGeometry extends Value {

    /**
     * Factory which provides a couple of methods to create a {@link IGeometry}
     * instance.
     */
    private static final IGeometryFactory GEOMETRY_FACTORY;

    static {
        ServiceLoader<IGeometryFactory> geometryFactories = ServiceLoader.load(IGeometryFactory.class);
        Iterator<IGeometryFactory> geometryFactoryIterator = geometryFactories.iterator();
        GEOMETRY_FACTORY = geometryFactoryIterator.hasNext() ? geometryFactories.iterator().next() : null;
    }
    
    /**
     * As conversion from/to byte array cost a significant amount of CPU cycles,
     * byte array are kept in ValueGeometry instance.
     *
     * We always calculate the byte array, because not all geometry string 
     * representation values can be represented in byte array, but since we 
     * persist it in binary format, it has to be valid in byte array
     */
    private final byte[] bytes;

    private final int hashCode;

    /**
     * The value. Converted from byte array only on request as conversion 
     * from/to byte array cost a significant amount of CPU cycles.
     */
    private IGeometry geometry;

    /**
     * Create a new geometry objects.
     *
     * @param bytes the bytes (always known)
     * @param geometry the geometry object (may be null)
     */
    private ValueGeometry(byte[] bytes, IGeometry geometry) {
        this.bytes = bytes;
        this.geometry = geometry;
        this.hashCode = Arrays.hashCode(bytes);
    }

    /**
     * Get or create a geometry value for the given geometry.
     *
     * @param g the geometry object
     * @return the value
     */
    public static ValueGeometry get(IGeometry g) {
        byte[] bytes = g.getBytes();
        return (ValueGeometry) Value.cache(new ValueGeometry(bytes, g));
    }

    /**
     * Get or create a geometry value for the given geometry.
     *
     * @param s the string representation of the geometry
     * @return the value
     */
    public static ValueGeometry get(String s) {
        try {
            IGeometry g = GEOMETRY_FACTORY.toGeometry(s);
            return get(g);
        } catch (GeometryParseException ex) {
            throw DbException.convert(ex);
        }
    }

    /**
     * Get or create a geometry value for the given geometry.
     *
     * @param s the string representation of the geometry
     * @param srid the srid of the object
     * @return the value
     */
    public static ValueGeometry get(String s, int srid) {
        try {
            IGeometry g = GEOMETRY_FACTORY.toGeometry(s, srid);
            return get(g);
        } catch (GeometryParseException ex) {
            throw DbException.convert(ex);
        }
    }

    /**
     * Get or create a geometry value for the given geometry.
     *
     * @param bytes the WKB representation of the geometry
     * @return the value
     */
    public static ValueGeometry get(byte[] bytes) {
        return (ValueGeometry) Value.cache(new ValueGeometry(bytes, null));
    }

    /**
     * Get a copy of geometry object. Geometry object is mutable. The returned
     * object is therefore copied before returning.
     *
     * @return a copy of the geometry object
     */
    public IGeometry getGeometry() {
        return getGeometryNoCopy().clone();
    }

    public IGeometry getGeometryNoCopy() {
        if (geometry == null) {
            try {
                geometry = GEOMETRY_FACTORY.toGeometry(bytes);
            } catch (GeometryParseException ex) {
                throw DbException.convert(ex);
            }
        }
        return geometry;
    }

    /**
     * Test if this geometry envelope intersects with the other geometry
     * envelope.
     *
     * @param r the other geometry
     * @return true if the two overlap
     */
    public boolean intersectsBoundingBox(ValueGeometry r) {
        // the Geometry object caches the envelope
        return getGeometryNoCopy().getEnvelope().intersects(
                r.getGeometryNoCopy().getEnvelope());
    }

    /**
     * Get the union.
     *
     * @param r the other geometry
     * @return the union of this geometry envelope and another geometry envelope
     */
    public Value getEnvelopeUnion(ValueGeometry r) {
        IEnvelope mergedEnvelope = getGeometryNoCopy().getEnvelope().getUnion(
                r.getGeometryNoCopy().getEnvelope());
        return get(GEOMETRY_FACTORY.toGeometry(mergedEnvelope));
    }

    @Override
    public int getType() {
        return Value.GEOMETRY;
    }

    @Override
    public String getSQL() {
        // WKT does not hold Z or SRID with JTS 1.13. As getSQL is used to
        // export database, it should contains all object attributes. Moreover
        // using bytes is faster than converting WKB to Geometry then to WKT.
        return "X'" + StringUtils.convertBytesToHex(getBytesNoCopy()) + "'::Geometry";
    }

    @Override
    protected int compareSecure(Value v, CompareMode mode) {
        IGeometry g = ((ValueGeometry) v).getGeometryNoCopy();
        return getGeometryNoCopy().compareTo(g);
    }

    @Override
    public String getString() {
        return getGeometryNoCopy().getString();
    }

    @Override
    public long getPrecision() {
        return 0;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public Object getObject() {
        return getGeometry();
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public byte[] getBytesNoCopy() {
        return bytes;
    }

    @Override
    public void set(PreparedStatement prep, int parameterIndex)
            throws SQLException {
        prep.setObject(parameterIndex, getGeometryNoCopy());
    }

    @Override
    public int getDisplaySize() {
        return getGeometryNoCopy().getString().length();
    }

    @Override
    public int getMemory() {
        return getBytes().length * 20 + 24;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ValueGeometry &&
                Arrays.equals(getBytes(), ((ValueGeometry) other).getBytes());
    }

    @Override
    public Value convertTo(int targetType) {
        if (targetType == Value.JAVA_OBJECT) {
            return this;
        }
        return super.convertTo(targetType);
    }
    
    /**
     * Returns <code>true</code> if a IGeometryFactory is available and initialized.
     * @return
     */
    public static boolean isInitialized()
    {
    	return GEOMETRY_FACTORY!=null;
    }
}
