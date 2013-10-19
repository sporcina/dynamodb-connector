package org.mule.modules.tools;

import org.mule.transport.NullPayload;

/**
 * Generic class for responses from a Mule flow
 *
 * @param <T>
 *          the type of the response (e.g. SomeClass or List<SomeClass>)
 */
public class FlowResponse<T> {

    private T response;

    public T getResponse() {
        // TODO: I would rather return EmptyResponse instead of null! - sporcina (Oct.12, 2013)
        // return null instead of NullPayload to ensure the caller (e.g. acceptance tests) is not exposed to Mule constructs
        return (response instanceof NullPayload) ? null : response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
