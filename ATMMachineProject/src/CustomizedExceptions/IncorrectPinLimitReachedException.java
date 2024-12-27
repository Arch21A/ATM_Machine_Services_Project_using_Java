package CustomizedExceptions;

public class IncorrectPinLimitReachedException extends Exception{
	
	public IncorrectPinLimitReachedException(String errormsg) {
		super(errormsg);
	}

}
