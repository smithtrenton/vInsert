package org.vinsert.core.script.stub;

/**
 * An implementation agnostic script stub
 *
 * @param <T> The script type
 * @param <M> The manifest type
 * @author tommo
 */
public interface AgnosticStub<T, M> {

    /**
     * Instantiates this script stub
     *
     * @return The instance
     */
    T instantiate() throws Exception;

    /**
     * Returns the manifest for this script stub
     *
     * @return The manifest
     */
    M manifest();

}
