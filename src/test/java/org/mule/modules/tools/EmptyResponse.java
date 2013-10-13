package org.mule.modules.tools;

/**
 * This class is in place to ensure that platform specific entities are not returned to higher level entities.
 *
 * For example, a test that deletes a document from a repository through mule should not receive a
 * org.mule.transport.NullPayload response.  The test should receive an instance of this class instead.
 */
public class EmptyResponse {
}
