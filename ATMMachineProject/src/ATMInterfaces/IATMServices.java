package ATMInterfaces;

import CustomizedExceptions.InsufficientBalanceException;
import CustomizedExceptions.InsufficientMachineBalanceException;
import CustomizedExceptions.InvalidamountException;
import CustomizedExceptions.NotAOperatorException;

public interface IATMServices {

		//to get the user type ,i.e normal user or an operator
		public abstract String getUserType() throws NotAOperatorException;
		
		//to withdraw amount and throwing a exception if the amount is invalid/insufficient/machine has insufficiet balace
		public abstract double WithdrawAmount(double withdrawAmnt) throws 
		InvalidamountException,
		InsufficientBalanceException,
		InsufficientMachineBalanceException;
		
		//to deposit amount
		public abstract double DepositAmount(double depositAmnt) throws InvalidamountException;
		
		//to check balance 
		public abstract double checkBalance();
		
		//to change pin number
		public abstract void changePin(int pinNumber);
		
		//to get pin number
		public abstract int getPinNumber();
		
		//to get user name
		public abstract String getUserName();
		
		//to get the chances of pin number
		public abstract int  getChances();
		
		//pin entering chances i.e decrease the no. of chances whenever the user enters wrong pin
		public abstract void decreaseChances();
		
		//to reset pinchances by operator
		public abstract void resetPin();
		
		//to get mini stmt
		public abstract void getMinistmt();
		
		
	}
