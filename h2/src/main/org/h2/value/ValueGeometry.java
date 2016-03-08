/*
 * Copyright 2004-2014 H2 Group. Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.value;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import org.h2.mvstore.rtree.SpatialKey;
import org.h2.util.StringUtils;

/**
 * Implementation of the GEOMETRY data type.
 *
 * @author Thomas Mueller
 * @author Noel Grandin
 * @author Nicolas Fortin, Atelier SIG, IRSTV FR CNRS 24888
 * @param <T> the type of the used framework geometry
 */
public abstract class ValueGeometry<T> extends Value{

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
    private T geometry;

    /**
     * Create a new geometry objects.
     *
     * @param bytes the bytes (always known)
     * @param geometry the geometry object (may be null)
     */
    protected ValueGeometry(byte[] bytes, T geometry) {
        this.bytes = bytes;
        this.geometry = geometry;
        this.hashCode = Arrays.hashCode(bytes);
    }

    /**
     * Get a copy of geometry object. Geometry object is mutable. The returned
     * object is therefore copied before returning.
     *
     * @return a copy of the geometry object
     */
    public abstract T getGeometry();

	/**
	 * Returns the internal geometry instance which should be immutable.
	 * @return the internal geometry instance which should be immutable
	 */
	@SuppressWarnings("unchecked")
	public T getGeometryNoCopy() {
		if (geometry == null) {
			geometry = (T)getGeometryFactory().getGeometry(bytes);
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
	protected abstract boolean _intersectsBoundingBox(ValueGeometry<T> r);

	 /**
     * Test if this geometry envelope intersects with the other geometry
     * envelope.
     *
     * @param r the other geometry
     * @return true if the two overlap
     */
	@SuppressWarnings("unchecked")
	public final boolean intersectsBoundingBox(ValueGeometry<?> r)
	{
		if(!getClass().isInstance(r)){
			return false; // not supported and should never happen
		}
		
		return _intersectsBoundingBox((ValueGeometry<T>) r);
	}
	
    /**
     * Get the union.
     *
     * @param r the other geometry
     * @return the union of this geometry envelope and another geometry envelope
     */
    protected abstract Value _getEnvelopeUnion(ValueGeometry<T> r);
    
    /**
     * Get the union.
     *
     * @param r the other geometry
     * @return the union of this geometry envelope and another geometry envelope
     */
    @SuppressWarnings("unchecked")
	public final Value getEnvelopeUnion(ValueGeometry<?> r)
    {
    	if(!getClass().isInstance(r)){
			return ValueNull.INSTANCE; // not supported and should never happen
		}
    	
    	return _getEnvelopeUnion((ValueGeometry<T>) r);
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
        return getString().length();
    }

    @Override
    public int getMemory() {
        return getBytes().length * 20 + 24;
    }
    
    @Override
	public abstract boolean equals(Object other);


    @Override
    public Value convertTo(int targetType) {
        if (targetType == Value.JAVA_OBJECT) {
            return this;
        }
        return super.convertTo(targetType);
    }

    /**
     * Returns the {@link SpatialKey} for the given row key. 
     * @param id the row key
     * @return the {@link SpatialKey} for the given row key
     */
	public abstract SpatialKey getSpatialKey(long id);
}
