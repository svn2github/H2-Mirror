package org.h2.jts;

import java.sql.SQLException;

import org.h2.api.IEnvelope;
import org.h2.api.IGeometry;
import org.h2.message.DbException;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFilter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKBWriter;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * Implementation of the {@link IGeometry} interface which wraps the
 * {@link Geometry}.
 */
public class H2Geometry implements IGeometry {
	private final Geometry geometry;

	private IEnvelope envelope;

	/**
	 * Constructs a new {@link H2Geometry} instance by using the given
	 * parameters.
	 * 
	 * @param geometry
	 *            geometry to wrap
	 */
	public H2Geometry(Geometry geometry) {
		this.geometry = geometry;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * @throws AssertionError
	 *             if the unwrap of {@link Geometry} is not possible.
	 */
	@Override
	public int compareTo(IGeometry g) throws AssertionError {
		if (!g.isWrapperFor(Geometry.class))
			throw new AssertionError(
					"Comparision isn't supported if 'com.vividsolutions.jts.geom.Geometry' can't be unwrapped!");

		return geometry.compareTo(g.unwrap(Geometry.class));
	}

	/**
	 * @see org.h2.value.IGeometry#getString()
	 */
	@Override
	public String getString() {
		return new WKTWriter(3).write(geometry);
	}

	/**
	 * @see org.h2.value.IGeometry#getBytes()
	 */
	@Override
	public byte[] getBytes() {
		boolean includeSRID = geometry.getSRID() != 0;
		int dimensionCount = getDimensionCount(geometry);
		WKBWriter writer = new WKBWriter(dimensionCount, includeSRID);
		return writer.write(geometry);
	}

	private static int getDimensionCount(Geometry geometry) {
		ZVisitor finder = new ZVisitor();
		geometry.apply(finder);
		return finder.isFoundZ() ? 3 : 2;
	}

	/**
	 * @see org.h2.value.IGeometry#clone()
	 */
	@Override
	public IGeometry clone() {
		return new H2Geometry((Geometry) geometry.clone());
	}

	/**
	 * @see org.h2.value.IGeometry#getEnvelope()
	 */
	@Override
	public IEnvelope getEnvelope() {
		if (envelope != null) {
			return envelope;
		}

		return envelope = new H2Envelope(geometry.getEnvelopeInternal());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T unwrap(Class<T> iface) {
		if (isWrapperFor(iface)) {
			return (T) geometry;
		}
		throw DbException.getInvalidValueException("iface", iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) {
		return iface != null && iface.isAssignableFrom(geometry.getClass());
	}
	
	@Override
	public String toString() {
		return getString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((geometry == null) ? 0 : geometry.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		H2Geometry other = (H2Geometry) obj;
		if (geometry == null) {
			if (other.geometry != null)
				return false;
		} else if (!geometry.equals(other.geometry))
			return false;
		return true;
	}
	
	/**
	 * A visitor that checks if there is a Z coordinate.
	 */
	private static class ZVisitor implements CoordinateSequenceFilter {
		boolean foundZ;

		public boolean isFoundZ() {
			return foundZ;
		}

		@Override
		public void filter(CoordinateSequence coordinateSequence, int i) {
			if (!Double.isNaN(coordinateSequence.getOrdinate(i, 2))) {
				foundZ = true;
			}
		}

		@Override
		public boolean isDone() {
			return foundZ;
		}

		@Override
		public boolean isGeometryChanged() {
			return false;
		}
	}
}