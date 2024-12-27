package Operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import ATMInterfaces.IATMServices;
import AtmCards.AxisDebitCard;
import AtmCards.HDFCDebitCard;
import AtmCards.ICICIDebitCard;
import AtmCards.OperatorCard;
import AtmCards.SBIDebitCard;
import CustomizedExceptions.IncorrectPinLimitReachedException;
import CustomizedExceptions.InsufficientBalanceException;
import CustomizedExceptions.InsufficientMachineBalanceException;
import CustomizedExceptions.InvalidCardException;
import CustomizedExceptions.InvalidPinException;
import CustomizedExceptions.InvalidamountException;
import CustomizedExceptions.NotAOperatorException;

public class ATMOperations {

	// initial atm machine balance
	public static double ATM_MACHINE_BALANCE = 100000.00;

	// activities performed by atm
	public static ArrayList<String> ACTIVITY = new ArrayList<>();

	// database to map card numbers to card object
	public static HashMap<Long, IATMServices> database = new HashMap<>();

	// flag to indicate if the atn is on or off
	public static boolean MACHINE_ON = true;

	// reference to the current card
	public static IATMServices card;

	// validating the inserted card by checking in the database
	public static IATMServices validateCard(long cardNumber) throws InvalidCardException {
		if (database.containsKey(cardNumber)) {
			return database.get(cardNumber);
		} else {
			ACTIVITY.add("Accessed bt " + cardNumber + " is not compatable ");
			throw new InvalidCardException("This is Not A Calid Card");
		}
	}

	// Displaying the activities performed on the atm
	public static void checkAtmActivities() {
		System.out.println("=========================Activities Performed===========================================");
		for (String activity : ACTIVITY) {
			System.out.println("====================================================================================");
			System.out.println(activity);
			System.out.println("=====================================================================================");
		}
	}

	// reseting the no. of pin attempts
	public static void resetUserAttempts(IATMServices operatorCard) {
		IATMServices card = null;
		long number;
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter your card Number : ");
		number = sc.nextLong();
		try {
			card = validateCard(number);
			card.resetPin();// reseting pin attempts for a specified user
			ACTIVITY.add("Accessed by " + operatorCard.getUserName() + " to reset number of attempts for user");

		} catch (InvalidCardException ive) {
			ive.printStackTrace();
		}

	}

	// validating user credentials including pin number
	public static IATMServices validateCredentials(long cardNumber, int pinNumber)
			throws InvalidCardException, InvalidPinException, IncorrectPinLimitReachedException {
		if (database.containsKey(cardNumber)) {
			card = database.get(cardNumber);
		} else {
			throw new InvalidCardException("This is not a valid card");
		}
		try {
			if (card.getUserType().equals("operator")) {

				// operator have a different pin validation
				if (card.getPinNumber() != pinNumber) {
					throw new InvalidPinException("Dear Operator... Please enter correct pin number");
				} else {
					return card;

				}
			}
		} catch (NotAOperatorException noe) {
			noe.printStackTrace();
		}

		// validating pin and handling incorrect attempts
		if (card.getChances() <= 0) {
			throw new IncorrectPinLimitReachedException("you have entered incorrect pin enter limit");

		}
		if (card.getPinNumber() != pinNumber) {
			card.decreaseChances();// reducing the no. of chances
			throw new InvalidPinException("You have entered wrong pin number");

		} else {
			return card;
		}

	}

	// validating the amount for withdrawl to ensure sufficient machine balance
	public static void validateAmount(double amount) throws InsufficientMachineBalanceException {
		if (amount > ATM_MACHINE_BALANCE) {
			throw new InsufficientMachineBalanceException("Insufficient funds in the Atm");
		}
	}

	// validating deposit amount to ensure it meets the requirements
	public static void validDepositAmount(double amount)
			throws InvalidamountException, InsufficientMachineBalanceException, InsufficientBalanceException {
		if (amount % 100 != 0) {
			throw new InvalidamountException("Please Deposit multiples of hundred");

		}if (amount < 500) {
			throw new InvalidamountException("Please Deposit more Than 500 Rupees");
		}
		if (amount + ATM_MACHINE_BALANCE > 200000.00) {
			ACTIVITY.add("Unable to deposit the cash in the atm...");
			throw new InsufficientBalanceException(
					"The limit of cash in the Atm has reached it's Limit...  unable to deposit the cash into the Atm");

		}
	}

	public static void operatorMode(IATMServices card)
			throws InsufficientMachineBalanceException, InsufficientBalanceException {
		Scanner sc = new Scanner(System.in);
		double amount;
		boolean flag = true;
		while (flag) {
			System.out.println("operator mode : operator name " + card.getUserName());
			System.out.println(
					"===========================================================================================");
			System.out.println();
			System.out.println(
					"||                           0. Switch-Off The ATM Machine                               ||");
			System.out.println();
			System.out.println(
					"||                           1. Check the ATM Balance                                    ||");
			System.out.println();
			System.out.println(
					"||                           2. Deposit Cash                                             ||");
			System.out.println();
			System.out.println(
					"||                           3. Reset the User Pin Attempts                              ||");
			System.out.println();
			System.out.println(
					"||                           4. Activities Performed in the ATM                          ||");
			System.out.println();
			System.out.println(
					"||                           5. Exit Operator Mode                                       ||");
			System.out.println();
			System.out.println(
					"========================================================================================== ");
			System.out.println();
			System.out.println("Enter Your Choice........");
			int option = sc.nextInt();
			switch (option) {
			case 0:
				MACHINE_ON = false;
				ACTIVITY.add(
						"Accessed by " + card.getUserName() + " Activity Performed : Switching-Off the ATM Machine");
				flag = false;
				break;

			case 1:
				ACTIVITY.add("Accessed by " + card.getUserName() + "Activity Performed :  Checking the ATM Balance");
				System.out.println("The Balance in the ATM : " + ATM_MACHINE_BALANCE + "Rupees");
				break;

			case 2:
				System.out.println("Enter the Amount To Deposit");
				amount = sc.nextDouble();
				try {
					validDepositAmount(amount);// to validate the deposit amount
					ATM_MACHINE_BALANCE += amount;// updating machine balance;
					ACTIVITY.add("Accessed by " + card.getUserName() + " Activity Performed : Cash Deposit");
					System.out.println(
							"===================================================================================");
					System.out.println(
							"===========================Cash Deposited Successfully=============================");
					System.out.println(
							"===================================================================================");

				} catch (InvalidamountException | InsufficientMachineBalanceException e) {
					System.out.println(e.getMessage());
				}
				break;

			case 3:
				resetUserAttempts(card);
				System.out.println(
						"=======================================================================================");
				System.out.println(
						"===========================No. of User Attempts Reseted Successfully===================");
				System.out.println(
						"=======================================================================================");
				ACTIVITY.add("Accessed by " + card.getUserName() + "Activity Performed :  Reset of  User Pin Attempts");
				break;

			case 4:
				checkAtmActivities();// to display activities performed in the atm
				break;

			case 5:
				flag = false;
				break;

			default:
				System.out.println("You have entered Invalid choice... Please enter a valid choice...");

			}
		}
	}

	public static void main(String[] args) throws InsufficientMachineBalanceException, InsufficientBalanceException, NotAOperatorException  {
		
		database.put(12345678910l,new AxisDebitCard(12345678910l,"Arch",55000.0,1234));
		database.put(99999999999l,new HDFCDebitCard(99987654321l,"Arif",75000.0,1222));
		database.put(88888888888l,new SBIDebitCard(88887654321l,"Pasha",53000.0,1333));
		database.put(77777777777l,new ICICIDebitCard(77777654321l,"Monarch",125000.0,1444));
		database.put(00000000000l,new OperatorCard(00000000000l,0000,"Operator"));
		Scanner sc = new Scanner(System.in);
		long cardNumber = 0;
		double depositAmount = 0.0;
		double withdrawAmount =0.0;
		int pin=0;
		
		while(MACHINE_ON) {
			System.out.println("Please Enter the Debit_Card Number : ");
			cardNumber=sc.nextLong();
			
			try {
				System.out.println("Please Enter Your PIN Number");
				pin = sc.nextInt();
				
				card = validateCredentials(cardNumber,pin);//validating card number and pin
				
				if(card==null) {
					System.out.println("Card validation failed...");
					continue;
					
				}
				ACTIVITY.add("Accessed by "+card.getUserName()+" Status : Access Approved");
				
				if(card.getUserType().equals("operator")) {
					operatorMode(card);
					continue;
				}
				
				while(true) {
					System.out.println("User mode : User name "+card.getUserName());
					System.out.println("===========================================================================================");
					System.out.println("||                           1. WITHDRAW CASH                                            ||");
					System.out.println("||---------------------------------------------------------------------------------------||");
					System.out.println("||                           2. DEPOSIT CASH                                             ||");
					System.out.println("||---------------------------------------------------------------------------------------||");
					System.out.println("||                           3. CHECK BALANCE                                            ||");
					System.out.println("||---------------------------------------------------------------------------------------||");
					System.out.println("||                           4. CHANGE PIN                                               ||");
					System.out.println("||---------------------------------------------------------------------------------------||");
					System.out.println("||                           5. GET MINI STATEMENT                                       ||");
					System.out.println("||---------------------------------------------------------------------------------------||");
					System.out.println("===========================================================================================");
					System.out.println();
					System.out.println("Enter Your Choice........");
					int option = sc.nextInt();
					try {
						switch(option) {
						case 1 :
							System.out.println("Please Enter the amount to Withdraw :");
							withdrawAmount = sc.nextDouble();
							validateAmount(withdrawAmount);
							card.WithdrawAmount(withdrawAmount);
							ATM_MACHINE_BALANCE -= withdrawAmount;
							ACTIVITY.add("Accessed by "+card.getUserName()+"Activity : Amount Withdraw of "+withdrawAmount+"From ATM");
							break;
							
						case 2 :
							System.out.println("Enter the Amount To Deposit");
							    depositAmount=sc.nextDouble();
								validDepositAmount(depositAmount);//to validate the deposit amount
								card.DepositAmount(depositAmount);
								ATM_MACHINE_BALANCE+=depositAmount;//updating machine balance
								ACTIVITY.add("Accessed by "+card.getUserName()+" Activity Performed : Cash Deposit of "+depositAmount);
								System.out.println("===================================================================================");
								System.out.println("===========================Cash Deposited Successfully=============================");
								System.out.println("===================================================================================");
							    break;
							    
						case 3 :
							System.out.println("Account Balance : "+card.checkBalance());
							ACTIVITY.add("Accessed by "+card.getUserName()+" Activity Performed : check balance");
							break;
							
						case 4 :
							System.out.println("Enter New PIN Number");
							ACTIVITY.add("Accessed by "+card.getUserName()+" Activity Performed : PIN Change");
							break;
							
						case 5 :
							ACTIVITY.add("Accessed by "+card.getUserName()+" Activity Performed : Generation of Mini Statement");
							card.getMinistmt();
							break;
							
						default :
							System.out.println("You have entered Invalid choice... Please enter a valid choice...");
						
					}
						System.out.println("Do You Want To Continue......");
						String nextOption = sc.next();
						if(nextOption.equalsIgnoreCase("N")) {
							break;//to exit the mode
						}

					}catch(InvalidamountException | InsufficientBalanceException | InsufficientMachineBalanceException e) {
						e.printStackTrace();
					}
				
			}
		}catch(InvalidPinException | InvalidCardException | IncorrectPinLimitReachedException e) {
			ACTIVITY.add("Accessed by "+card.getUserName()+" Activity Status : Access Denied ");
			e.printStackTrace();
		}
		
		
	}
		System.out.println("===========================================================================================================");
		System.out.println("=====================Thanks For Using  Arch ATM Services===================================================");
		System.out.println("===========================================================================================================");
}
}
