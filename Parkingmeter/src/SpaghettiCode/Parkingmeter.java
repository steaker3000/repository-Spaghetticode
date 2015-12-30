package SpaghettiCode;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class Parkingmeter {
	 
	/**
	 * defines values of coins in the boxes. <br>Example:<br>
	 * <code> 0.50, 1.00 and 2.00 €</code>
	 */
	private static double[] coinsBoxValue = {0.50,1.00,2.00};

	/**
	 * coin boxes contain in the beginning for example 5 coins of each value 
	 * defined by the coinsBoxValue.<br>
	 * Example: <br><code>0.50,1.00 and 2.00 €</code>
	 */
	private static int[] coinsParkingMeter = {5,5,5};
 
	/**
	 * contains the payed date of each of the defined meters. 
	 */
	private static Date[] meterDate = {new Date(),new Date(),new Date(),new Date()};
		
	// Secret Key 
	private final static int[] secretKeys =        {1234,4321,9876,8888,9999,4545};
	
	// Secret Key Text
	private final static String[] secretCommands = {"Transaction Log anzeigen", 
													"Gebühren aller Parkplätze anzeigen", 
													"Alle Informationen abfragen", 
													"Parkuhr testen", 
													"Exit - Programm verlassen",
													"Neuer Münzbestand eingeben"
													};

	/**
	 * defines the payment rules. One single rules consists of 3 numbers: min money value, max money value
	 * and the related time in minutes.<br><code>
	 * MIN_VALUE_INDEX = 0;	Array Index 0 for parkingTimeDef, min money<br>
	 * MAX_VALUE_INDEX = 1;	Array Index 1 for parkingTimeDef, max money<br>
	 * TIME_INDEX = 2;	    Array Index 2 for parkingTimeDef, time in minutes</code><br>
	 */
	private final static double[][] parkingTimeDef={{0.00, 0.00,  0},	// from 0.00€ to 0.00€ -> time  0 minutes
											{0.00, 0.50, 30},   // from 0.00€ to 0.50€ -> time 30 minutes
											{0.50, 1.00, 45},   // from 0.50€ to 1.00€ -> time 45 minutes
											{1.00, 1.50, 55},   // from 1.00€ to 1.50€ -> time 55 minutes
											{1.50, 2.00, 60}};  // from 1.50€ to 2.00€ -> time 60 minutes
	
	private final static int MIN_VALUE_INDEX = 0;	// Array Index 0 for parkingTimeDef
	private final static int MAX_VALUE_INDEX = 1;	// Array Index 1 for parkingTimeDef
	private final static int TIME_INDEX = 2;		// Array Index 2 for parkingTimeDef
	
	/**
	 * currencyString contains the symbol for the used currency like € or $
	 */
	private final static String currencyString = " €";
	
	/**
	 * transactionLog contains the string about all transaction of the parking meter
	 */
	private static String transactionLog="";


	/**
	 * Program entry point. 
	 * @param args not used
	 */
	public static void main (String[] args) {
		
		System.out.println("Start von ParkingMeter");
		System.out.println("======================");
		System.out.println(formatSecretKeysInfo());
		addToTransactionLog ("Start von ParkingMeter");
		addToTransactionLog (formatCoinContent(coinsParkingMeter));
		
		actionController();

		System.out.println("Exit ParkingMeter -> Bye");
	}  
	
	/**
	 * The action controller is taking care about the entry of the parking spot number.
	 * Furthermore there the actionController checks if a secret code was entered. 
	 * The appropriate function call is done. 
	 */
	private static void actionController(){

		boolean exit = false;
		while (!exit) {
			System.out.println ("--------------------------");
			System.out.println ("Parkplatz Nummer eingeben:");

			int parkingSpotNumber  = (int)TastaturRead.readLong();
			if (parkingSpotNumber == secretKeys[0])      printTransactionLog();
			else if (parkingSpotNumber == secretKeys[1]) printMeterInfo();
			else if (parkingSpotNumber == secretKeys[2]) printAllInfo();
			else if (parkingSpotNumber == secretKeys[3]) testAll();
			else if (parkingSpotNumber == secretKeys[4]) exit = true;
			else if (parkingSpotNumber == secretKeys[5]) coinsParkingMeter=insertNewCoinsValueController();
			else  {
				if (parkingSpotNumber > 0 && parkingSpotNumber <=  meterDate.length) {
					insertCoinsController (parkingSpotNumber);
				} // if 
				else {
					System.out.println ("Falsche Parkplatznummer - Die Parkplatznummer muss zwischen [1] und [" + meterDate.length + "] sein");
				}
			}
		}
	}
	
	/**
	 * Taking Care about the [UserStory 1 & 2] "Payment of Parking". 
	 * The user is asked to enter the parking coins. The user input
	 * is verified and processed against the parkingDef table. F
	 * @param parkingSpotNumber Parking Spot Number
	 */
	private static void insertCoinsController (int parkingSpotNumber){
		System.out.println ("Münzen einwerfen für Parkplatz [" + parkingSpotNumber + "]:");
		String inputCoins  = TastaturRead.readString().trim();
		String[] coinsString = inputCoins.split(";|\\s+");
		if (checkAllIsNumeric (coinsString)) {
			double[] insertedCoins = convertStringArrayToDouble(coinsString);
			addToTransactionLog(formatInsertedCoins (insertedCoins)); 
			if (verifyValidCoins(insertedCoins)) {
				parkingMeterPayment(parkingSpotNumber, insertedCoins);
				// TODO Erweiterung mit [UserStory 3] Beleg ausdrucken
			}
			else {
				System.out.println ("Die Eingabe war nicht korrekt - gültige Münzen sind " + formatCoinBoxValueString());
			}
		}
		else {
			System.out.println ("Die Eingabe war nicht korrekt - bitte nur Zahlen eingeben");
		}
	}

	// TODO [UserStory 8] Neuer Münzbetrag eingeben
	/**
	 * 
	 */
	private static int[] insertNewCoinsValueController() {
		// coinsBoxValue
		// coinsParkingMeter
		// Ausgabe neuer Inhalt formatCoinContent(coinsParkingMeter);
		for(int i=0; i<3; i++){
			System.out.println("Neuen Münzbestand für Fach " + (i+1) + " eingeben");
			int newCoinValueTemp  = (int)TastaturRead.readLong();
			coinsParkingMeter[i]=newCoinValueTemp;
		}
		return coinsParkingMeter;
	}
     
    /**
     * Prints the content of all internal variables and definitions
     */
    private static void printAllInfo (){
		System.out.println (formatCoinContent(coinsParkingMeter));
		System.out.println (formatParkingTimeDefinion(parkingTimeDef));
		System.out.println (formatMeterInfo(meterDate));
		System.out.println ("Test von computeTimeInMinutes()");
		System.out.println (formatMoney(0.00, currencyString) + " -> " + formatMinute(computeTimeInMinutes(parkingTimeDef,0.0)));
		System.out.println (formatMoney(0.50, currencyString) + " -> " + formatMinute(computeTimeInMinutes(parkingTimeDef,0.50)));
		System.out.println (formatMoney(1.00, currencyString) + " -> " + formatMinute(computeTimeInMinutes(parkingTimeDef,1.00)));
		System.out.println (formatMoney(1.50, currencyString) + " -> " + formatMinute(computeTimeInMinutes(parkingTimeDef,1.50)));
		System.out.println (formatMoney(2.00, currencyString) + " -> " + formatMinute(computeTimeInMinutes(parkingTimeDef,2.00)));
		System.out.println ("");
		System.out.println ("Max Park Time: " + formatMinute(getMaxParkingTime(parkingTimeDef)));
		System.out.println ("Max Park Value: " + formatMoney(getMaxParkingValue(parkingTimeDef), currencyString));
		System.out.println ("");
		printTransactionLog();
    }
    
    /**
     * Prints the content of the parking meters. The output consists of a title 
     * and of the parkingMeter number, the payed date and time & the time difference.<br>
	 * Example (with two parking meters): <code><br>
	 * Parking Meter Info vom: 2013-11-04 18:15<br>
	 * Parkuhr[1]  |  bezahlt bis 2013-11-04 18:15  |  Differenz 00:00:05<br>
     * Parkuhr[2]  |  bezahlt bis 2013-11-04 18:15  |  Differenz 00:00:05<br>
     * </code>
     */
    private static void printMeterInfo () {
		System.out.println (formatMeterInfo(meterDate));
     }
	
	/**
	 * Adds a string to the transaction log. Each string will be placed as 
	 * an individual line (with \n) together with a transaction date.
	 * @param info the string with a message for the transaction log.
	 */
	private static void addToTransactionLog(String info) {
		Date now = new Date();
		transactionLog += "[" + formatDate (now) + "] " + info + "\n";
	}

	/**
	 * Prints the transaction log to the console. Adds a title with the current date and time.
	 * Example: <br><code>
	 * Transaction Log von 2013-11-05 12:55<br>
	 * [2013-11-05 12:54] Start von ParkingMeter<br>
	 * [2013-11-05 12:54] Inhalt der Münzeinheiten TOTAL:  17.50 € </code><br>
	 */
	private static void printTransactionLog() {
		Date now = new Date();
		System.out.println ("Transaction Log von " + formatDate (now));
		System.out.println (transactionLog);
	}
	
	// TODO Fehler beheben: Buchung der Parkgebühr auf die Münzeinheiten
	private static int[] bookParkingCoins (int[] parkCoins, int[]coinsParkingmeter) {
		for (int i=0; i<coinsBoxValue.length; i++){
			coinsParkingmeter[i]=coinsParkingmeter[i] + parkCoins[i];
		}
		return coinsParkingMeter;
	}
	
	/**
	 * Verifies if the inserted coins are found in the coinsBoxValue array.
	 * @param insertedCoins contains the coins inserted by the customer
	 * @return true if all coins are found.
	 */
	private static boolean verifyValidCoins(double[] insertedCoins) {
		
		boolean valid=false;
		
		for (int i=0; i < insertedCoins.length ; i++) {
			valid=false;
			// Loop through all coinBoxes and check if the value exists
			for (int coinBox = 0 ; coinBox < coinsBoxValue.length ; coinBox++) {
				if (Math.round(insertedCoins[i]*10) == Math.round(coinsBoxValue[coinBox]*10)) {
					valid = true;
					break;
				}
			}
			if (valid == false) break;
		}
		return valid;
	}
	

	/**
	 * Handles the customer payment. Assigns the inserted coins to coin boxes, 
	 * checks it the customer has payed more money than allowed (if yes, returns the left over money to the customer),
	 * adds the payed time the the meter related to the parking spot number and
	 * produces an customer output to the console and a log entry to the transaction log. 
	 * @param parkingSpotNumber the parking spot number
	 * @param insertedCoins the coins inserted by the customer
	 */
	private static void parkingMeterPayment (int parkingSpotNumber, double insertedCoins[]) {
		
		// sort inserted coins into the value order of the coin boxes
		int[] coins = assignCoins(insertedCoins); 

		// Compute usable Coins for max Payment
		int[] parkCoins = computeMaxCoinsForParking(coins); 

		// Produce output to customer and transactionLog
		double parkValue = computeCoinsValue(parkCoins);
		String out = "Zahlung für Parkplatznummer [" + parkingSpotNumber + "]: " + formatMoney(parkValue, currencyString).trim() + " = " + formatCoins(parkCoins);
		System.out.println (out);
		addToTransactionLog(out);

		// Check if we must pay back the left over to the customer
		//if (computeCoinsValue(coins) > getMaxParkingValue( parkingTimeDef)) {
		if (computeCoinsValue(coins) > 0) {
			System.out.println ("-> Rückgabe (zuviel bezahlt) :" + formatCoins(coins));
		}

		// Compute Parking Time
		int minutes = (int)Math.round(computeTimeInMinutes (parkingTimeDef, parkValue));
		meterDate[parkingSpotNumber-1] = addTime(new Date(),minutes); 
		bookParkingCoins(parkCoins, coinsParkingMeter);

		// Produce output to customer and transactionLog
		out = "Parkzeit bis " + formatDate(meterDate[parkingSpotNumber-1]) + " - " + minutes + " Minuten";
		System.out.println (out);
		addToTransactionLog(out);
	}
		
	/**
	 * Computes the max money which can be used for the max parking time, defined by the parking rules.
	 * @param coins array with number of payed coins related to coinsBoxValue
	 * @return the coins array with the the money used for payment
	 */
	private static int[] computeMaxCoinsForParking(int[] coins) {
		int[] parkingCoins =  new int[coinsBoxValue.length];
		int noCoinBoxes = coinsBoxValue.length;
		for (int coinBox = noCoinBoxes-1; coinBox >= 0; coinBox--) {
			
			while (coins[coinBox] > 0) {
				parkingCoins[coinBox]++;
				coins[coinBox]--;
				if (computeCoinsValue(parkingCoins) > getMaxParkingValue( parkingTimeDef)) {
					parkingCoins[coinBox]--;
					coins[coinBox]++;
					break;
				}
			}
		}
		return  parkingCoins;
	}
	
	/**
	 * Assigns the coins from an array of inserted coins to an array of coin boxes.<br>
	 * Example: inserted coins array 1.0, 0.5, 2.0<br>
	 * Assignment:<br> <code>
	 * 1.0 -> coinBox with value 1.00€,<br> 
	 * 0.5 -> coinBox with value 0.50€,<br> 
	 * 2.0 -> coinBox with value 2.00€, </code> 
	 * @param insertedCoins
	 * @return coins array related to coinsBoxValue array
	 */
	private static int[] assignCoins (double insertedCoins[]) {
		int[] coins =  new int[coinsBoxValue.length];
		int noCoinBoxes = coinsBoxValue.length;
		for (int i=0; i < insertedCoins.length ; i++) {
			for (int coinBox = 0; coinBox < noCoinBoxes; coinBox++) {
				if (Math.round(insertedCoins[i]*10) == Math.round (coinsBoxValue[coinBox]*10)) {
			        coins[coinBox] ++;
				}
			}
		}
		return coins;
	}
	
	/**
	 * The algorithm computes the value of the money depending 
	 * on the number of coins in the boxes. The value of each coin depends on the 
	 * definition in the coinsBoxValue array.   
	 * @param coins array containing the amount coins
	 * @return the value of the money
	 */
	private static double computeCoinsValue(int [] coins){
		int noCoinBoxes = coinsBoxValue.length;
		double value = 0;
		for (int coinBox = 0; coinBox < noCoinBoxes; coinBox++) {
			value += coins[coinBox] * coinsBoxValue[coinBox];
		}
		return value;
	}
	
	/**
	 * Computes the parking time in minutes based on a money value defined by the payment rules.
	 * @param parkingTimedef Definition of the payment rules.
	 * @param value the money value.
	 * @return returns the parking time in minutes.
	 */
	private static double computeTimeInMinutes (double parkingTimedef[][], double value) {
		double minutes = 0;
		for (int def = 0; def < parkingTimedef.length; def++) {
			if (value > parkingTimedef[def][MIN_VALUE_INDEX] && value <= parkingTimedef[def][MAX_VALUE_INDEX]) {
				minutes = parkingTimedef[def][TIME_INDEX];
				break;
			}
		}
		return minutes;
	}
	
	/**
	 * Get the max parking time defined by the payment rules.
	 * @param parkingTimedef the definition of the payment rules.
	 * @return the max parking time.
	 */
	private static double getMaxParkingTime(double parkingTimedef[][]) {
		return parkingTimedef[parkingTimedef.length-1][TIME_INDEX];
	}

	/**
	 * Get the max value (money) to reach the max parking time defined by the payment rules.
	 * @param parkingTimedef the definition of the payment rules.
	 * @return the max value to reach the max parking time.
	 */
	private static double getMaxParkingValue(double parkingTimedef[][]) {
		return parkingTimedef[parkingTimedef.length-1][MAX_VALUE_INDEX];
	}
	
	/**
	 * Formats a string of the content of the coin boxes of the parking meter. The value of 
	 * each coin box is defined by the coinsBoxValue array. In the first line appears
	 * a title with the total amount of coins.<br>
	 * Example (with 3 coin boxes):<br>
	 * <code>
	 * Inhalt der Münzeinheiten TOTAL:  17.50 €<br>
	 * Münzeinheit[1]  |  Münze:   0.50 €  |  Anzahl: 5  |  Wert:   2.50 €  |<br>
	 * Münzeinheit[2]  |  Münze:   1.00 €  |  Anzahl: 5  |  Wert:   5.00 €  |<br>
	 * Münzeinheit[3]  |  Münze:   2.00 €  |  Anzahl: 5  |  Wert:  10.00 €  |<br>
	 * </code>
	 * @param coins contains an array with the number of coins (values sorted by coinsBoxValue.
	 * @return the formated string.
	 */
	private static String formatCoinContent(int coins[]){
		String out="Inhalt der Münzeinheiten TOTAL: " + formatMoney(computeCoinsValue(coins), currencyString) + "\n";
		int noCoinBoxes = coinsBoxValue.length;
		double value = 0;
		for (int coinBox = 0; coinBox < noCoinBoxes; coinBox++) {
			value = coins[coinBox] * coinsBoxValue[coinBox];
			out += "Münzeinheit" + "[" + (coinBox + 1) + "]" + "  |  " +
			       "Münze: "  + formatMoney(coinsBoxValue[coinBox], currencyString) + "  |  " +
				   "Anzahl: " + (coins[coinBox]) + "  |  " +
			       "Wert: "   + formatMoney(value, currencyString) +  "  |" + "\n";
		}
		return out;
	}
	
	/**
	 * Creates a table of the parking time payment rules, with rule number, 
	 * money range (from-to) and related parking time.
	 * Example: <br>
	 * <code>
	 * Parkzeit Definition (with 2 definition lines):<br>
	 * Zeitbereich[1]  |  von   0.00 €  |  bis   0.00 €  |  Zeit: 00 min  |<br>
     * Zeitbereich[2]  |  von   0.00 €  |  bis   0.50 €  |  Zeit: 30 min  |<br>
     * </code>
	 * @param parkingTimedef Definition of the payment rules.
	 * @return Formated Definition of the payment rules; one line per rule.
	 */
	private static String formatParkingTimeDefinion(double parkingTimedef[][]) {
		String out = "Parkzeit Definition" + "\n";
		for (int def = 0; def < parkingTimedef.length; def++) {
			out +=  "Zeitbereich" + "[" + (def + 1) + "]" + "  |  " +
					"von " + formatMoney(parkingTimedef[def][MIN_VALUE_INDEX], currencyString) + "  |  " +
					"bis " + formatMoney(parkingTimedef[def][MAX_VALUE_INDEX], currencyString) + "  |  " +
					"Zeit: " + formatMinute(parkingTimedef[def][TIME_INDEX]) +  "  |" + "\n";
		}		
		return out;
	}
	
	/**
	 * Formats the date and time information of all parking meters in the array meterdate. 
	 * The output consists of a title and of: 
	 * the parkingMeter number, the payed date and time & the time difference.
	 * Furthermore if the parking time is over due, a message 
	 * like "** Parkzeit abgelaufen **" is displayed.<br>
	 * Example (with four parking meters): <code><br>
	 * Parking Meter Info vom: 2013-11-05 13:45<br>
	 * Parkuhr[1]  |  bezahlt bis 2013-11-05 14:13  |  Differenz 00:28:42<br>
	 * Parkuhr[2]  |  bezahlt bis 2013-11-05 14:15  |  Differenz 00:29:56<br>
	 * Parkuhr[3]  |  bezahlt bis 2013-11-05 13:43  |  Differenz 00:01:45 ** Parkzeit abgelaufen **<br>
	 * Parkuhr[4]  |  bezahlt bis 2013-11-05 13:43  |  Differenz 00:01:45 ** Parkzeit abgelaufen **<br>
     * </code>
	 * @param meterdate the date array of the parking meters.
	 * @return the formated string.
	 */
	private static String formatMeterInfo(Date[] meterdate){
		Date now = new Date();
		String out="Parking Meter Info vom: " + formatDate(now) + "\n";
		for (int meter = 0; meter < meterdate.length; meter++) {
			String overtime="";
			if (compareTwoDates(now,meterdate[meter])) overtime=" ** Parkzeit abgelaufen **";
			out += "Parkuhr" + "[" + (meter + 1) + "]" + "  |  " +
                   "bezahlt bis " + formatDate(meterdate[meter]) +  "  |  " +
			       "Differenz " + formatTime (computeTimeDifference(now, meterdate[meter])) + overtime + "\n";
		}
		return out;
	}
	
	/**
	 * Creates a list of secret numbers with the dependent meaning.
	 * Example: 
	 * <code><br>
	 * Spezielle Befehle<br>
	 * 1234=Transaction Log<br>
	 * 4321=Meter Info<br>
	 * 9876=Alle Info<br>
	 * 8888=Test<br>
	   9999=Exit<br>
	 * </code>
	 * @return the formated string.
	 */
	private static String formatSecretKeysInfo () {
		
		String out="Spezielle Befehle\n";
		for (int i = 0 ; i < secretKeys.length ; i++) {
			out += secretKeys[i] + "=" + secretCommands[i] + "\n";
		}
		return out;
	}
	
	/**
	 * Formats the values of the inserted coins array to a readable string including currency.<br>
	 * Example <code>0.5, 1, 2, 0.5</code> formats to <code>"0.50 €, 1.00 €, 2.00 €, 0.5 €"</code>
	 * @param insertedCoins the array of coin values.
	 * @return the formated string. 
	 */
	private  static String formatInsertedCoins (double insertedCoins[]) {
		String out ="";
		for (int i=0; i < insertedCoins.length ; i++) {
			out += formatMoney (insertedCoins[i], currencyString) + ", ";
		}
	    return out.substring(MIN_VALUE_INDEX, out.length()-2);
	}
	
	/**
	 * Formats the definition of the coinBoxValue array to the format:<br> 
	 * <code>"0.50 €, 1.00 €, 2.00 €"</code>
	 * @return the string with the coin box values.
	 */
	private  static String formatCoinBoxValueString () {
		String out = "";
	
		for (int coinBox = 0 ; coinBox < coinsBoxValue.length ; coinBox++) {
			out += formatMoney(coinsBoxValue[coinBox], currencyString) + ", ";
		}
        return out.substring(MIN_VALUE_INDEX, out.length()-2);
	}
	
	/**
	 * Formats the number of coins (a, b, c) in the coins array with the value defined in the 
	 * coinBoxValue array to the format <code>"a x 0.50 €, b x 1.00 €, c x 2.00 €"</code>
	 * @param coins the array of coin values based on the coinsBoxValue.
	 * @return the string with the number and values of the coins.
	 */
	private  static String formatCoins (int[] coins) {
		String out="";
		for (int coinBox = 0 ; coinBox < coinsBoxValue.length ; coinBox++) {
			out+= coins[coinBox] + " x " + formatMoney(coinsBoxValue[coinBox], currencyString).trim() + ", ";
		}
	     return out.substring(MIN_VALUE_INDEX, out.length()-2);
	}
	
	/**
	 * Checks if a string array contains only numbers 
	 * @param arr contains the string array to be checked
	 * @return true if all strings in the array containing a number
	 */
	private  static boolean checkAllIsNumeric(String[] arr) {
		boolean numeric = true;
		for (int i = 0; i < arr.length; i++) {
			if (!isNumeric(arr[i].trim())) numeric=false; 
		}
		return numeric;
	}
	
	/**
	 * Checks if a string contains a number or letters.
	 * @param str contains the string to be checked.
	 * @return true if the string str is a number.
	 */
	private  static boolean isNumeric(String str)
	{
		return str.trim().matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	
	/**
	 * Formatting a date and time to <code>"yyyy-MM-dd HH:mm"</code>.<br>
	 * Example: <code>2013-10-11 12:30</code>
	 * @param date to be formated.
	 * @return a string with the formated date.
	 */
	private static String formatDate (Date date) {
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return dt.format(date);
	}
	
	/**
	 * Formats a given time in millisecond to hh:mm:ss. <br>
	 * Example <code>"00:23:55"</code>
	 * @param milliseconds contains the time to be formated.
	 * @return formated time.
	 */
	private static String formatTime (long milliseconds){
		DecimalFormat df = new DecimalFormat("00");
		return  df.format(milliseconds/(60*60*1000)) + ":" + df.format(milliseconds/(60 * 1000) % 60) + ":" + df.format(milliseconds/1000 % 60);
	}
	
	/**
	 * Formats minutes always to 2 numbers and adds the string "min". <br>
	 * Example: <code>"02 min"</code>
	 * @param minutes
	 * @return minutes the string with minutes and " min".
	 */
	private static String formatMinute (double minutes) {
		DecimalFormat df = new DecimalFormat("#00");
		return df.format(minutes) + " min";
	}
	
	/**
	 * Formats the money output depending on the default local region settings. 
	 * The value is 6 characters long, an currency string is added at the end.
	 * Example: <code>"  3.50 €"</code>
	 * @param value the value to be formated.
	 * @param currency the currency string.
	 * @return money the string plus currency.
	 */
	private static String formatMoney(double value, String currency){
		return (String.format ("%,6.2f",value)) + currency;
	}
	
	
	/**
	 * Converts an array of string values to to double numbers. 
	 * Strings with letters are not converted, no error is thrown!
	 * @param stringArray containing strings with values.
	 * @return array of double values.
	 */
	private  static double[] convertStringArrayToDouble(String[] stringArray) {
		double[] doubleArray = new double[stringArray.length];
		for (int i = 0; i < stringArray.length; i++) {
			if (isNumeric(stringArray[i].trim())) {
				doubleArray[i] = Double.parseDouble(stringArray[i].trim());
			}
		}
		return doubleArray;
	}

	/**
	 * Computes from two dates the time difference in milliseconds. The time difference is always positiv, 
	 * independent of date1 is younger or older to date2.
	 * @param date1 date 1.
	 * @param date2 date 2.
	 * @return the time difference in milliseconds.
	 */
	private static long computeTimeDifference (Date date1, Date date2) {
		GregorianCalendar cal1 = new GregorianCalendar();
		cal1.setTime(date1);
		GregorianCalendar cal2 = new GregorianCalendar();
		cal2.setTime(date2);

		long diff= Math.abs(cal1.getTimeInMillis() -  cal2.getTimeInMillis());
		return diff;
	}
	
	/**
	 * Compares date1 with date2. Returns true if date1 is older than date2. <br>
	 * Example date1=2013-10-23 12:10 date2=2013-10-23 11:10 => returns true
	 * @param date1 date 1 to be compared.
	 * @param date2 date 2 to be compared.
	 * @return the time difference in milliseconds
	 */
	private static boolean compareTwoDates (Date date1, Date date2) {
		GregorianCalendar cal1 = new GregorianCalendar();
		cal1.setTime(date1);
		GregorianCalendar cal2 = new GregorianCalendar();
		cal2.setTime(date2);

		return ((cal1.getTimeInMillis() - cal2.getTimeInMillis()) > 0 );	
	}
	
	/**
	 * Adds minutes to a given date.
	 * @param date the given date.
	 * @param minutes the minutes to add.
	 * @return the new date and time.
	 */
	private static Date addTime (Date date, int minutes) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);

		Date addDate = new Date();
		addDate.setTime(cal.getTimeInMillis() +  minutes * 60000);
		return addDate;
	}

	/**
	 * Test function to call all available tests.
	 */
	private static void testAll() {
		testComputeCoinsValue();
		testFormatCoinContent();
		testFormatCoins();
		testFormatParkingTimeDefinition();
		testFormatMeterInfo();
		testComputeTimeInMinutes();
		testGetMaxParkingTimeAndValue();
		testComputeParkingCoins();
		testAddTime();
	}	
	
	private static void testComputeCoinsValue () {
		System.out.println ("Anfangsbestand: " + formatMoney(computeCoinsValue(coinsParkingMeter), currencyString)+ "\n") ;
	}
	
	private static void testFormatCoinContent () {
		System.out.println (formatCoinContent(coinsParkingMeter));
    }
		
	private static void testFormatCoins() {
		System.out.println (formatCoins(coinsParkingMeter));
    }
	private static void testFormatParkingTimeDefinition () {
		System.out.println (formatParkingTimeDefinion(parkingTimeDef));
	}
	
	private static void testFormatMeterInfo () {
		System.out.println (formatMeterInfo(meterDate));
	}
	
    private static void testComputeTimeInMinutes () {
		System.out.println ("Test von computeTimeInMinutes()");
		System.out.println (formatMoney(0.00, currencyString) + " -> " + formatMinute(computeTimeInMinutes(parkingTimeDef,0.0)));
		System.out.println (formatMoney(0.50, currencyString) + " -> " + formatMinute(computeTimeInMinutes(parkingTimeDef,0.50)));
		System.out.println (formatMoney(1.00, currencyString) + " -> " + formatMinute(computeTimeInMinutes(parkingTimeDef,1.00)));
		System.out.println (formatMoney(1.50, currencyString) + " -> " + formatMinute(computeTimeInMinutes(parkingTimeDef,1.50)));
		System.out.println (formatMoney(2.00, currencyString) + " -> " + formatMinute(computeTimeInMinutes(parkingTimeDef,2.00)));
		System.out.println ("");
    }	

    private static void testGetMaxParkingTimeAndValue () {
		System.out.println ("Max Park Time: " + formatMinute(getMaxParkingTime(parkingTimeDef)));
		System.out.println ("Max Park Value: " + formatMoney (getMaxParkingValue(parkingTimeDef), currencyString));
		System.out.println ("");
    }

    private static void testComputeParkingCoins () {
       	System.out.println ("testComputeParkingCoins\n");
           	// Test 1
    	int[] coins1 = {2,1,0};
		System.out.println ("Insert:   " + formatCoins(coins1));
   	    int [] parkingCoins1 = computeMaxCoinsForParking(coins1);
		System.out.println ("Parking:  " + formatCoins(parkingCoins1));
		System.out.println ("Left Over:" + formatCoins(coins1));
		
		// Test 2
    	int[] coins2 = {3,1,0};
		System.out.println ("Insert:   " + formatCoins(coins2));
   	    int [] parkingCoins2 = computeMaxCoinsForParking(coins2);
		System.out.println ("Parking:  " + formatCoins(parkingCoins2));
		System.out.println ("Left Over:" + formatCoins(coins2));
		
		// Test 3
    	int[] coins3 = {3,1,2};
		System.out.println ("Insert:   " + formatCoins(coins3));
   	    int [] parkingCoins3 = computeMaxCoinsForParking(coins3);
		System.out.println ("Parking:  " + formatCoins(parkingCoins3));
		System.out.println ("Left Over:" + formatCoins(coins3));
    }
    
    private static void testAddTime() {
    	Date date = addTime (new Date(),60);
    	System.out.println ("Datum+Zeit: jetzt + 60 Minuten = " + formatDate (date));
    }
}