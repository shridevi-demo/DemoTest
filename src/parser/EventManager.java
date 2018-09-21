package parser;

import java.util.ArrayList;
import java.util.List;

import parser.EventManager.Type;

public class EventManager {

	public enum Type {
		IRRECOVERABLE_ERROR,
		RECOVERABLE_ERROR
	}
	
	List<PaymentInitWrapper> irrecoverable = new ArrayList<>();

	public void pushEvent(String type,
			PaymentInitWrapper paymentInitWrapper) {
		// TODO Auto-generated method stub
		irrecoverable.add(paymentInitWrapper);
	}
}
