/*
 * Copyright 2004-2014 H2 Group. Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.value;

import java.util.Arrays;

import org.h2.mvstore.rtree.SpatialKey;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * The {@link ValueGeometry} implementation for the JTS geometry framework.
 * 
 * @author Steve Hruda
 */
public class JTSValueGeometry extends ValueGeometry<Geometry> {

	JTSValueGeometry(byte[] bytes, Geometry geometry) {
		super(bytes, geometry);
	}

	@Override
	public Geometry getGeometry() {
		return (Geometry) getGeometryNoCopy().clone();
	}

	@Override
	public boolean _intersectsBoundingBox(ValueGeometry<Geometry> r) {
		// the Geometry object caches the envelope
		return getGeometryNoCopy().getEnvelopeInternal().intersects(
				r.getGeometryNoCopy().getEnvelopeInternal());
	}

	@Override
	public Value _getEnvelopeUnion(ValueGeometry<Geometry> r) {
		GeometryFactory gf = new GeometryFactory();
		Envelope mergedEnvelope = new Envelope(getGeometryNoCopy().getEnvelopeInternal());
		mergedEnvelope.expandToInclude(r.getGeometryNoCopy().getEnvelopeInternal());
		return getGeometryFactory().get(gf.toGeometry(mergedEnvelope));
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof ValueGeometry
				&& Arrays.equals(getBytes(), ((JTSValueGeometry) other).getBytes());
	}
	
	@Override
    protected int compareSecure(Value v, CompareMode mode) {
        Geometry g = ((JTSValueGeometry) v).getGeometryNoCopy();
        return getGeometryNoCopy().compareTo(g);
    }
	
	@Override
    public String getString() {
        return new WKTWriter(3).write(getGeometryNoCopy());
    }
	
	@Override
	public SpatialKey getSpatialKey(long id) {
		Envelope env = getGeometryNoCopy().getEnvelopeInternal();
        return new SpatialKey(id,
                (float) env.getMinX(), (float) env.getMaxX(),
                (float) env.getMinY(), (float) env.getMaxY());
	}
}
