package AtmCards;

import java.util.ArrayList;
import java.util.Collections;

import ATMInterfaces.IATMServices;
import CustomizedExceptions.InsufficientBalanceException;
import CustomizedExceptions.InsufficientMachineBalanceException;
import CustomizedExceptions.InvalidamountException;
import CustomizedExceptions.NotAOperatorException;

public class SBIDebitCard implements IATMServices{
	
	String name;
	long debitCardNumber;
	double accountBalance;
	int pinNumber;
	ArrayList<String> statement;
	final String type = "user";
	int chances;
	
	public SBIDebitCard(long debitCardNumber,String name, double accountBalance, int pinNumber) {
		chances = 3;
		this.name = name;
		this.accountBalance= accountBalance;
		this.pinNumber= pinNumber;
		statement = new ArrayList<>();
		
		
	}
	
	@Override
	public String getUserType() throws NotAOperatorException {
	
		return type;
	}

	@Override
	public double WithdrawAmount(double withdrawAmnt)
			throws InvalidamountException, InsufficientBalanceException, InsufficientMachineBalanceException {
		if(withdrawAmnt<=0) {
			throw new InvalidamountException("You entered less than Zero rupee... \n so enter valid amount");
		}else if(withdrawAmnt%100!=0) {
			throw new InvalidamountException("please enter multiple of Hundreds");
		}else if(withdrawAmnt<500) {
			throw new InvalidamountException("Please enter more than 500 rupees to withdraw");
		}else if(withdrawAmnt>accountBalance) {
			throw new InsufficientBalanceException("You don't have sufficient funds in your account");
		}else {
			accountBalance = accountBalance-withdrawAmnt;
			statement.add("Amount withdrawn :"+withdrawAmnt);
			
		}
		return withdrawAmnt;
	}

	@Override
	public double DepositAmount(double depositAmnt) throws InvalidamountException {

		 if(depositAmnt%100!=0) {
			throw new InvalidamountException("please deposit the amount in the multiples of Hundred");
		}else if(depositAmnt<500) {
			throw new InvalidamountException("Please deposit more than 500 rupees");
		}else {
			accountBalance = accountBalance+depositAmnt;
			statement.add("Amount Deposited :"+depositAmnt);
		
		}
		return depositAmnt;
	}

	@Override
	public double checkBalance() {
		
		
		return accountBalance;
	}

	@Override
	public void changePin(int pinNumber) {
		this.pinNumber=pinNumber;
		
	}
	
	public int getPinNumber() {
		return pinNumber;
	}

	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public int getChances() {
		return chances;
		
	}

	@Override
	public void decreaseChances() {
		// TODO Auto-generated method stub
		--chances;
	}

	@Override
	public void resetPin() {
		chances = 3;
		
	}

	@Override
	public void getMinistmt() {
		int count = 5;
		if(statement.size()==0) {
			System.out.println("there are no transactions to show..");
			return;
			
		}
		System.out.println("=============Last five transactions==================");
		Collections.reverse(statement);
		for(String transactions:statement) {
			if(count==0) {
				break;
			}
			System.out.println(transactions);
		}
		Collections.reverse(statement);
		
	}

}
