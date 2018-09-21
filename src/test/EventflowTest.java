package test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import parser.EnrichmentService;
import parser.EventFlowProcessor;
import parser.EventManager;
import parser.EventStage;
import parser.MessagePublisher;
import parser.PaymentInitWrapper;
import parser.TransformService;
import parser.ValidationServices;
import parser.iPublish;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class EventflowTest {
	
	ValidationServices validationServices;

	TransformService transformService;
	
	EnrichmentService enrichmentService;
	
	EventManager eventManager;
	
	iPublish publishManager;
	
	MessagePublisher messagePublisher;
	
	@Before
	public void setup() {
		validationServices = mock(ValidationServices.class);
		transformService = mock(TransformService.class);
		enrichmentService = mock(EnrichmentService.class);
		eventManager = mock(EventManager.class);
		publishManager = mock(iPublish.class);
		messagePublisher = mock(MessagePublisher.class);
	}
	
	/**
	 * Successful flow. If you go check method definition of requestResponseFlow,
	 * you will see for a successful execution, each of the methods shall be executed
	 * in the nested if structure.
	 */
	@Test
	public void assert_successfull_requestResponseFlow() {
		PaymentInitWrapper correct = new PaymentInitWrapper();		
		when(validationServices.validateRequest(any())).thenReturn(correct);
		//when(validationServices.validateRequest(Matchers.any(PaymentInitWrapper.class))).thenReturn(correct);
		when(transformService.transformMessage(any())).thenReturn(correct);
		when(enrichmentService.enrichRequest(any())).thenReturn(correct);

		// .. other conditions
		
		EventFlowProcessor processor = new EventFlowProcessor(validationServices, transformService, enrichmentService, eventManager, publishManager, messagePublisher);
		try {
			processor.requestResponseFlow(correct);
		} catch (Exception e) {
			fail(e.getMessage());
		}
//		verify(validationServices, Mockito.atLeastOnce()).validateRequest(correct);
//		verify(validationServices, Mockito.times(20)).validateRequest(correct);
//		verify(validationServices, Mockito.atLeast(5)).validateRequest(correct);
//		verify(validationServices, Mockito.atMost(5)).validateRequest(correct);

		verify(validationServices, Mockito.times(1)).validateRequest(correct);
		verify(enrichmentService, Mockito.times(1)).enrichRequest(correct);
		verify(transformService, Mockito.times(1)).transformMessage(correct);
	}
	
	/**
	 * Unsuccessful condition. If enrichRequest fails, it should not go into next step.
	 * i.e. transform should not be called.
	 */
	@Test
	public void assert_incorrect_situation_1() {
		
		PaymentInitWrapper correct = new PaymentInitWrapper();
		PaymentInitWrapper incorrect = new PaymentInitWrapper();
		incorrect.addIrrecoverableError(EventStage.FWD_FLOW_VALIDATION_IRRECOVERABLE_ERROR.toString());
		
		when(validationServices.validateRequest(any())).thenReturn(correct);
		when(enrichmentService.enrichRequest(any())).thenReturn(incorrect);
		when(transformService.transformMessage(any())).thenReturn(incorrect);
		
		EventFlowProcessor processor = new EventFlowProcessor(validationServices, transformService, enrichmentService, eventManager, publishManager, messagePublisher);
		try {
			processor.requestResponseFlow(correct);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		verify(validationServices, Mockito.times(1)).validateRequest(any());
		verify(enrichmentService, Mockito.times(1)).enrichRequest(any()); // fails here
		verify(transformService, Mockito.times(0)).transformMessage(any()); // so this should not be executed
	}
	
	/**
	 * Unsuccessful condition. If fails at very first step,
	 * nothing within that if block can be executed.
	 * 
	 * Hope it helps ;)
	 */
	@Test
	public void assert_incorrect_situation_2() {
		
		PaymentInitWrapper correct = new PaymentInitWrapper();
		PaymentInitWrapper incorrect = new PaymentInitWrapper();
		incorrect.addIrrecoverableError(EventStage.FWD_FLOW_VALIDATION_IRRECOVERABLE_ERROR.toString());
		
		when(validationServices.validateRequest(any())).thenReturn(incorrect);
//		-- doesn't matter, as it will be failed after first execution only.
//		when(enrichmentService.enrichRequest(any())).thenReturn(incorrect);
//		when(transformService.transformMessage(any())).thenReturn(incorrect);
		
		EventFlowProcessor processor = new EventFlowProcessor(validationServices, transformService, enrichmentService, eventManager, publishManager, messagePublisher);
		try {
			processor.requestResponseFlow(correct);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		verify(validationServices, Mockito.times(1)).validateRequest(any()); // failure induced here, none of the block shall be executed within the if statement
		verify(enrichmentService, Mockito.times(0)).enrichRequest(any());
		verify(transformService, Mockito.times(0)).transformMessage(any());
	}

}
