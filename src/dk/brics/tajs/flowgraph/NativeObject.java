package dk.brics.tajs.flowgraph;

import dk.brics.tajs.analysis.NativeAPIs;

/**
 * A native objects is an object implemented directly in the analyzer.
 * Native objects have to belong to an API.
 */
public interface NativeObject {

    /**
     * Returns the API descriptor that this native object belongs to.
     */
    public NativeAPIs getAPI();

    public boolean hasGetter(String p);

    public boolean hasSetter(String p);

}
