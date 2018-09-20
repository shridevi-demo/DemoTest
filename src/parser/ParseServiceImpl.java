package parser;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseServiceImpl implements ParseService {

	@Override
	public PaymentInitWrapper parseRequestMessage(String message)
			throws Exception {
		PaymentInitWrapper paymentInitWrapper = new PaymentInitWrapper();
		
		paymentInitWrapper.setEventID(String.valueOf(UUID.randomUUID()));
		
		String MemberID = getTagValues(message, Pattern.compile("<head:MmbId.*>((.|\\n)*?)</head:MmbId>"));
		if (MemberID != null) 
			paymentInitWrapper.setMmbId(MemberID);
		else
			paymentInitWrapper.addIrrecoverableError(ERRORConstants.MemberID_NULL);
		
		String creationDate = getTagValues(message, Pattern.compile("<head:CreDt.*>((.|\\n)*?)</head:CreDt>"));
		if (isNotEmpty(creationDate))
			paymentInitWrapper.setCreationTime(new Timestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
				.parse(creationDate)
				.getTime()));
		else
			paymentInitWrapper.addIrrecoverableError(ERRORConstants.CREATION_DATE_NULL);
		
		return paymentInitWrapper;
		
		//..
	}
	
	String getTagValues(String message, Pattern regex) {
		Matcher m = regex.matcher(message);
		if (m.find()) {
			String t = m.group(1);
			System.out.println("matched => " + t);
			return t;
		}
		return null;
	}
	
	boolean isNotEmpty(String value) {
		return value != null && !"".equals(value.trim());
	}
}
