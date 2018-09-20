package parser;

import java.sql.Timestamp;

public class PaymentInitWrapper {

	String eventID;
	String mmbId;
	Timestamp creationTime;
	String messageFormat;
	
	String irrecoverableError;
	
	public String getEventID() {
		return eventID;
	}
	public void setEventID(String eventID) {
		this.eventID = eventID;
	}
	public String getMmbId() {
		return mmbId;
	}
	public void setMmbId(String mmbId) {
		this.mmbId = mmbId;
	}
	
	public Timestamp getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}
	public String getIrrecoverableError() {
		return irrecoverableError;
	}
	public void setIrrecoverableError(String irrecoverableError) {
		this.irrecoverableError = irrecoverableError;
	}
	public String getMessageFormat() {
		return messageFormat;
	}
	public void setMessageFormat(String messageFormat) {
		this.messageFormat = messageFormat;
	}
	
	public void addIrrecoverableError(String error) {
		irrecoverableError = irrecoverableError == null ? error : irrecoverableError.concat(", ").concat(error);
	}
	
	
}
