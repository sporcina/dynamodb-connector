package org.mule.modules.dynamodb.demo.transformers;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import org.mule.modules.dynamodb.demo.models.FakeCustomer;

public class HttpCreateDocumentToFakeCustomer extends AbstractMessageTransformer {

	/**
	 * Http query parameters will be stored as in-bound properties
	 * 
	 * For example, the http request "http://localhost:8081/saveDoc2?name=Joe" will result in an in-bound property titled
	 * "name" with a value of "Joe".
	 * 
	 * @see
	 * 	http://www.mulesoft.org/documentation-3.2/display/32X/HTTP+Transport+Reference#HTTPTransportReference-ProcessingGETQueryParameters
	 */ 
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		
		String name = (String)message.getInboundProperty("name");
		String phone = (String)message.getInboundProperty("phone");
		
		return new FakeCustomer().setName(name).setPhone(phone);
	}
}
