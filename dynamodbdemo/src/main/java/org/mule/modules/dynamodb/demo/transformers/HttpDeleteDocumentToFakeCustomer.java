package org.mule.modules.dynamodb.demo.transformers;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.modules.dynamodb.demo.models.FakeCustomer;
import org.mule.transformer.AbstractMessageTransformer;

public class HttpDeleteDocumentToFakeCustomer extends AbstractMessageTransformer {

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
		
		String num = (String)message.getInboundProperty("num");
		
		return new FakeCustomer().setNum(num);
	}
}
