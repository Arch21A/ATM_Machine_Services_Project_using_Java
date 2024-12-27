package CustomizedExceptions;

public class InsufficientMachineBalanceException extends Exception {
	
	public InsufficientMachineBalanceException(String errormsg) {
		super(errormsg);
	}

}
