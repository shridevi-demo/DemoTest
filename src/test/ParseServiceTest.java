package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import parser.ERRORConstants;
import parser.ParseService;
import parser.ParseServiceImpl;
import parser.PaymentInitWrapper;

public class ParseServiceTest {

    ParseService parseService;
    
    static String readFile(String path, Charset encoding) {
    	try {
    		byte[] encoded = Files.readAllBytes(Paths.get("/Users/passport/Documents/Sridevi/DemoTest/src/parser/" + path));
    		return new String(encoded, encoding);
    	} catch (Exception e) {
    		return "";
    	}
    }

    @Before
    public void setup() {
        parseService = new ParseServiceImpl();
        
    }

    @Test
    public void assert_successfull_parsing() {
    	try {
    		String val = readFile("correct.xml", StandardCharsets.UTF_8);
    		System.out.println("=>" + val);
    		PaymentInitWrapper paymentInitWrapper = parseService.parseRequestMessage(val);
            assertEquals(paymentInitWrapper.getMmbId(), "3243");
    	} catch (Exception e) {
    		e.printStackTrace();
    		fail(e.getMessage());
    	}
    }

	@Test
    public void assert_parsing_incorrect_date() {
		try {
    		String val = readFile("incorrect_date.xml", StandardCharsets.UTF_8);
    		System.out.println("=>" + val);
    		PaymentInitWrapper paymentInitWrapper = parseService.parseRequestMessage(val);
            assertTrue(paymentInitWrapper.getIrrecoverableError().contains(ERRORConstants.CREATION_DATE_NULL));
    	} catch (Exception e) {
    		e.printStackTrace();
    		fail(e.getMessage());
    	}
    }
	
	/**
	 * To check the memberid returned contains number only. And no characters.
	 */
	@Test
	public void assert_parsing_incorrect_memberID() {
		try {
    		String val = readFile("incorrect_membid.xml", StandardCharsets.UTF_8);
    		System.out.println("=>" + val);
    		PaymentInitWrapper paymentInitWrapper = parseService.parseRequestMessage(val);
    		boolean contiansChar = paymentInitWrapper.getMmbId().matches("[a-z]");
    		assertFalse(contiansChar); // << throws a error
    	} catch(AssertionError e) {
    		// This was expected
    	} catch (Exception e) {
    		e.printStackTrace();
    		fail(e.getMessage());
    	}
    }
}
