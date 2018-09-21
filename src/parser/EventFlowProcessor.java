package parser;

public class EventFlowProcessor {

	ValidationServices validationServices;

	TransformService transformService;
	
	EnrichmentService enrichmentService;
	
	EventManager eventManager;
	
	iPublish publishManager;
	
	MessagePublisher messagePublisher;
	
	private String errorQueue;
	
	public EventFlowProcessor(ValidationServices v, TransformService t, EnrichmentService e, EventManager m,
			iPublish p, MessagePublisher pub) {
		this.validationServices = v;
		this.transformService = t;
		this.enrichmentService = e;
		this.eventManager = m;
		this.publishManager = p;
		this.messagePublisher = pub;
	}
	
	public PaymentInitWrapper requestResponseFlow(PaymentInitWrapper paymentInitWrapper) throws Exception {
		
//		paymentInitWrapper.resetRecoverableErrors();
		paymentInitWrapper.resetIrrecoverableErrors();
		
		paymentInitWrapper = validationServices.validateRequest(paymentInitWrapper);
			
		if (checkForErrors(paymentInitWrapper, EventStage.FWD_FLOW_VALIDATION_IRRECOVERABLE_ERROR,
				EventStage.FWD_FLOW_ENRICH_RECOVERABLE_ERROR, EventStage.FWD_FLOW_VALIDATION_SUCCESS)) {
			
			paymentInitWrapper = enrichmentService.enrichRequest(paymentInitWrapper);
			
			if (checkForErrors(paymentInitWrapper, EventStage.FWD_FLOW_ENRICH_IRRECOVERABLE_ERROR,
					EventStage.FWD_FLOW_ENRICH_RECOVERABLE_ERROR, EventStage.FWD_FLOW_ENRICH_SUCCESS)) {
				
				paymentInitWrapper = transformService.transformMessage(paymentInitWrapper);
				
				//... omore code here
			}
		}
		
		return paymentInitWrapper;
	}
	
 	private boolean checkForErrors(PaymentInitWrapper paymentInitWrapper, EventStage irrecoverable, EventStage recoverable, EventStage success) {
 		try {
 			if (!paymentInitWrapper.getIrrecoverableError().isEmpty()) {
 				eventManager.pushEvent(EventManager.Type.IRRECOVERABLE_ERROR.toString(), paymentInitWrapper);
 				return false;
 			}
// 			.. more code
 		} catch (Exception e) {
 			
 		}
 		return true;
 	}
	
}
