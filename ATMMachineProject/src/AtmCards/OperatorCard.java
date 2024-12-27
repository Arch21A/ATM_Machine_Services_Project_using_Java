package AtmCards;

import ATMInterfaces.IATMServices;
import CustomizedExceptions.InsufficientBalanceException;
import CustomizedExceptions.InsufficientMachineBalanceException;
import CustomizedExceptions.InvalidamountException;
import CustomizedExceptions.NotAOperatorException;

public class OperatorCard implements IATMServices{
	
	private int pinNumber;
	private long id;
	private String name;
	private final String type = "operator";
	
	public OperatorCard(long id,int pin, String name) {
		id=id;
		pinNumber=pin;
		this.name=name;
	}

	@Override
	public String getUserType() throws NotAOperatorException {
	
		return type;
	}

	@Override
	public double WithdrawAmount(double withdrawAmnt)
			throws InvalidamountException, InsufficientBalanceException, InsufficientMachineBalanceException {
		
		return 0;
	}

	@Override
	public double DepositAmount(double depositAmnt) throws InvalidamountException {
		
		return 0;
	}

	@Override
	public double checkBalance() {
		
		return 0;
	}

	@Override
	public void changePin(int pinNumber) {
		
		
	}

	@Override
	public int getPinNumber() {
		
		return pinNumber;
	}

	@Override
	public String getUserName() {
		
		return name;
	}

	@Override
	public int getChances() {
	
		return 0;
	}

	@Override
	public void decreaseChances() {
		
		
	}

	@Override
	public void resetPin() {
	
		
	}

	@Override
	public void getMinistmt() {
	
		
	}
	
	

}
