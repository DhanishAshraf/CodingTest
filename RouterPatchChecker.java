import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/** This class will check all the routers given in a .csv file and determine which ones can be patched based on a
 * number of criteria:
 * - The router has not already been patched
 * - The current version of the router OS is 12 or above
 * - There are no other routers which share the same IP address
 * - There are no other routers which share the same host name
 * @author Dhanish Ashraf
 *
 */
public class RouterPatchChecker {
	//an ArrayList of String arrays, where each array within the ArrayList contains information about a different router.
	private List<String[]> routerList;
	//a TreeSet that will store all the rows to be deleted in the RouterList. The rows will be deleted once all the conditions have been checked so that
	//we do not have to worry about keeping track of changing indexes as elements in the routerList get deleted in the array after each check.
	//Furthermore it ensures that situations such as the following do not occur: Let us say that we have three routers, A, B and C where A and B have the same 
	//hostname and B and C have the same IP address. Clearly by the conditions none of these can be patched. However if we check for and delete the routers that 
	//have the same hostname (A and B) and then when we check for routers with the same IP addresses, then according to the ArrayList no router has the same IP address
	//as C and so it will get patched when in actual fact it has the same IP address as B and so should not get patched.
	private TreeSet<Integer> rowsToDelete;
	//a StringBuffer variable to store the list of routers to be pacthed.
	private StringBuffer result;
	
	/**
	 * The constructor. Initialises the routerList ArrayList, the rowsToDelete TreeSet and the result StringBuffer.
	 */
	public RouterPatchChecker(){
		this.routerList = new ArrayList<String[]>();
		this.rowsToDelete = new TreeSet<Integer>();
		this.result = new StringBuffer();
	}
	
	/** This method will read in the file passed in and store it in the routerList ArrayList in order to make the data easier to work with.
	 * The method will then call filterRouters() method to check which of the routers in the ArrayList can be patched.
	 * @param file, the file to be read in (contains the router information)
	 * @return StringBuffer, the routers that can be patched
	 */
	public StringBuffer readAndPopulate(String file){
	   	 
       	String line;
       	String splitter=",";
       	String[] token;
       	try {
     	/*
           	to read data from file
           	*/
        	BufferedReader br=new BufferedReader(new FileReader(file));
        //this while loop will copy the data in the file into the routerList ArrayList
       	while((line = br.readLine()) != null){
       			if(!line.matches(",,,,")){ //getting rid of any blank lines
       				token = line.split(splitter);   
                	routerList.add(token);
       			}
       	} 	 
          	br.close();
       	}

       	catch (FileNotFoundException e){
           	System.out.println("File not found");
       	}
       	catch (IOException ex) {
          	System.out.println(ex.getMessage()+"Error reading file");
        	}
       	
      	return filterRouters();
    }

	/** this method filters the RouterList ArrayList by deleting the entry of any router that cannot be patched 
	 * according to the criteria stated above.
	 * @return StringBuffer, the routers that can be patched
	 */
	public StringBuffer filterRouters(){ 
				
		//here we check that no routers have the same hostname; if they do, we add its index to the rowsToDelete TreeSet.
		int z = routerList.size()-1;
		while(z > 0){
			String[] temp = routerList.get(z);
			int deletions = 0;
			for(int j = z-1; j >0; j--){
				if(temp[0].equalsIgnoreCase(routerList.get(j)[0])){
					rowsToDelete.add(j);
					deletions++;
				}	
			}
			if(deletions > 0) {
				rowsToDelete.add(z);
				deletions++;
			}
				z--;	
					
		}
		
		//here we check that no routers have the same IP address; if they do, we add its index to the rowsToDelete TreeSet.
		z = routerList.size() - 1;
		while(z > 0){
			String[] temp = routerList.get(z);
			int deletions = 0;
			for(int j = z-1; j >0; j--){
				if(temp[1].equalsIgnoreCase(routerList.get(j)[1])){
					rowsToDelete.add(j);
					deletions++;
				}	
			}
			if(deletions > 0) {
				rowsToDelete.add(z);
				deletions++;
			}
				z--;	
		}
		
		//here we iterate backwards through the the TreeSet and delete the entries at the indexes listed in the TreeSet.
		//we iterate backwards so that we delete the entries in the ArrayList from bottom up. This way we do no have to worry about the indexes of entries changing.
		Iterator<Integer> it = rowsToDelete.descendingIterator();
		while(it.hasNext()){
			int tempIndex = (int) it.next();
			routerList.remove(tempIndex);
		}

		
		//here we check if either the router has already been patched or if the OS version is less than 12
		//if either of the conditions are true, we remove that router from the list
		for(int i = routerList.size() - 1; i >= 1; i--){
			String[] temp = routerList.get(i);
			if(temp[2].equalsIgnoreCase("yes")){
				routerList.remove(i);
			} else if(!temp[2].equalsIgnoreCase("No")){ //the case where the data is in an incorrect format
				result.append("ERROR: Indefinite answer. "
						+ "Check if the server with host name ''" + temp[0] + "'' on IP Address ''" + temp[1] + "'' has already been patched.\n");
			}
			else {
				try {
					if(Double.parseDouble((temp[3])) < 12.0){
						routerList.remove(i);
				}
				
			} catch(NumberFormatException e){ //the case where the data is in an incorrect format
				result.append("The server with host name ''" + temp[0] + "'' on IP Address ''" + temp[1] + "'' does not have a valid OS version."
						+ "\nIf the OS version is less than 12, it should not be patched.\n");
			}
				
			}		
		}
		
		//calling the arrToString method to display the router in the correct format.
		for(int i = 1; i < routerList.size(); i++){
			if(i == routerList.size() - 1)
				result.append(arrToString(routerList.get(i)));
			else
				result.append(arrToString(routerList.get(i)) + "\n");
		}
		
		return result;
	}
	
	/** This method simply creates a String to display each router that needs to be patched in the correct format.
	 * @param arr, the array that needs to be printed
	 * @return String, with the router displayed in the correct format
	 */
	public static String arrToString(String[] arr){
		if(arr.length == 5)
			return arr[0] + " (" + arr[1] + "), OS Version " + arr[3] + " [" + arr[4] + "]"; 
		else
			return arr[0] + " (" + arr[1] + "), OS Version " + arr[3];
	}


	public static void main(String[] args) {
		RouterPatchChecker test = new RouterPatchChecker();
		System.out.println(test.readAndPopulate(args[0]));
	}

}
