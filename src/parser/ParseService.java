package parser;

public interface ParseService {
	PaymentInitWrapper parseRequestMessage(String message) throws Exception;
}
