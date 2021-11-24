package jpabook.jpashop.exception;

public class NotEnoughStockException extends RuntimeException{

	public NotEnoughStockException() {
		super();
	}
	
	public NotEnoughStockException(String message) {
		super(message);
	}
	
	public NotEnoughStockException(String message, Throwable cause) {	
		//메세지 + 예외가 발생한 근원적인 exception
		super(message, cause);
	}
	
	public NotEnoughStockException(Throwable cause) {
		super(cause);
	}
	
}
