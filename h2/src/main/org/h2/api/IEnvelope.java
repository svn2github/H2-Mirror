/*
 * Copyright 2004-2014 H2 Group. Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.api;

import java.sql.Wrapper;

/**
 * Interface of a envelope type which defines a rectangular region of the 2D
 * coordinate plane.
 */
public interface IEnvelope extends Wrapper {
    /**
     * Returns the Envelopes minimum x-value.
     * 
     * @return the Envelopes minimum x-value
     */
    public double getMinX();

    /**
     * Returns the Envelopes minimum y-value.
     * 
     * @return the Envelopes minimum y-value
     */
    public double getMinY();

    /**
     * Returns the Envelopes maximum x-value.
     * 
     * @return the Envelopes maximum x-value
     */
    public double getMaxX();

    /**
     * Returns the Envelopes maximum y-value.
     * 
     * @return the Envelopes maximum y-value
     */
    public double getMaxY();

    /**
     * Check if the region defined by other overlaps (intersects) the region of
     * this Envelope.
     * 
     * @param envelope
     * @return {@code true} if the given envelope overlaps the region of this
     *         envelope, {@code false} otherwise
     */
    public boolean intersects(IEnvelope envelope);

    /**
     * Creates the union of this and the given envelope.
     *
     * @param other the other envelope
     * @return the union of this envelope and another envelope
     */
    public IEnvelope getUnion(IEnvelope other);
    
    @Override
    public boolean isWrapperFor(Class<?> iface);
    
    @Override
    public <T> T unwrap(Class<T> iface);
}
