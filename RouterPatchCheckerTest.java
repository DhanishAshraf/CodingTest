import static org.junit.Assert.*;

import org.junit.Test;

/** 11 tests to ensure that the RouterPatchChecker class works as it should.
 * Care was taken to try and test all the possible boundary cases and normal working cases.
 * The readAndPopulate has been tested here as a method that does not require a command line parameter.
 * The same tests have been done as a command line program informally, however.
 * @author Dhanish Ashraf
 *
 */
public class RouterPatchCheckerTest {

	/**
	 * Checking the RouterPackChecker works as it should with the given sample file.
	 */
	@Test
	public void test1() {
		RouterPatchChecker test = new RouterPatchChecker();
		StringBuffer expected1 = new StringBuffer("b.example.com (1.1.1.2), OS Version 13 [Behind the other routers so no one sees it]"
				+ "\nf.example.com (1.1.1.7), OS Version 12.200");
		StringBuffer actual1 = test.readAndPopulate("sample.csv");
		
		assertEquals("Incorrect result.", expected1.toString(), actual1.toString());
	}
	
	/**
	 * Checking the RouterPackChecker works as it should with the "sample1.csv" file. 
	 * It shows all routers have already been patched so no routers should be returned
	 */
	@Test
	public void test2(){
		RouterPatchChecker test = new RouterPatchChecker();
		StringBuffer expected = new StringBuffer("");
		StringBuffer actual = test.readAndPopulate("sample1.csv");
		
		assertEquals("Incorrect result.", expected.toString(), actual.toString());
	}
	
	/**
	 * Checking the RouterPackChecker works as it should with the "sample2.csv" file. 
	 * It shows all routers have an OS version of 11 so no routers should be returned.
	 */
	@Test
	public void test3(){
		RouterPatchChecker test = new RouterPatchChecker();
		StringBuffer expected = new StringBuffer("");
		StringBuffer actual = test.readAndPopulate("sample2.csv");
		
		assertEquals("Incorrect result.", expected.toString(), actual.toString());
	}
	
	/**
	 * Checking the RouterPackChecker works as it should with the "sample3.csv" file. 
	 * It shows all routers have the same host name so no routers should be returned.
	 */
	@Test
	public void test4(){
		RouterPatchChecker test = new RouterPatchChecker();
		StringBuffer expected = new StringBuffer("");
		StringBuffer actual = test.readAndPopulate("sample3.csv");
		
		assertEquals("Incorrect result.", expected.toString(), actual.toString());
	}

	/**
	 * Checking the RouterPackChecker works as it should with the "sample4.csv" file. 
	 * It shows all routers have the same IP address so no routers should be returned.
	 */
	@Test
	public void test5(){
		RouterPatchChecker test = new RouterPatchChecker();
		StringBuffer expected = new StringBuffer("");
		StringBuffer actual = test.readAndPopulate("sample4.csv");
		
		assertEquals("Incorrect result.", expected.toString(), actual.toString());
	}
	
	/**
	 * Checking the RouterPackChecker works as it should with the "sample5.csv" file. 
	 * It shows all routers require patching so all routers should be returned.
	 */
	@Test
	public void test6(){
		RouterPatchChecker test = new RouterPatchChecker();
		StringBuffer expected = new StringBuffer("A.example.COM (1.1.1.1), OS Version 12 [Faulty fans]"
				+ "\nb.example.com (1.1.1.2), OS Version 13 [Behind the other routers so no one sees it]"
				+ "\nC.EXAMPLE.COM (1.1.1.3), OS Version 12.1"
				+ "\nd.example.com (1.1.1.4), OS Version 14"
				+ "\nh.example.com (1.1.1.5), OS Version 12 [Case a bit loose]"
				+ "\ne.example.com (1.1.1.6), OS Version 12.3"
				+ "\nf.example.com (1.1.1.7), OS Version 12.2"
				+ "\ng.example.com (1.1.1.8), OS Version 15 [Guarded by sharks with lasers on their heads]");
		StringBuffer actual = test.readAndPopulate("sample5.csv");
		
		assertEquals("Incorrect result.", expected.toString(), actual.toString());
	}
	
	/**
	 * Checking the RouterPackChecker works as it should with the "sample6.csv" file. 
	 * It shows only 1 routers should be returned.
	 */
	@Test
	public void test7(){
		RouterPatchChecker test = new RouterPatchChecker();
		StringBuffer expected = new StringBuffer("b.example.com (1.1.1.2), OS Version 13 [Behind the other routers so no one sees it]");
		StringBuffer actual = test.readAndPopulate("sample6.csv");
		
		assertEquals("Incorrect result.", expected.toString(), actual.toString());
	}
	
	/**
	 * Checking the RouterPackChecker works as it should with the "sample7.csv" file. 
	 * It shows that one of the OS versions is in an incorrect format. This should still be added to the list
	 * but a message explaining the issue should also be shown.
	 */
	@Test
	public void test8(){
		RouterPatchChecker test = new RouterPatchChecker();
		StringBuffer expected = new StringBuffer("The server with host name ''b.example.com'' on IP Address ''1.1.1.2'' does not have a valid OS version."
				+ "\nIf the OS version is less than 12, it should not be patched."
				+ "\nb.example.com (1.1.1.2), OS Version hello [Behind the other routers so no one sees it]"
				+ "\nf.example.com (1.1.1.7), OS Version 12.2");
		StringBuffer actual = test.readAndPopulate("sample7.csv");
		
		assertEquals("Incorrect result.", expected.toString(), actual.toString());
	}
	
	/**
	 * Checking the RouterPackChecker works as it should with the "sample8.csv" file. 
	 * It shows that is it unclear if one of the routers has already been patched or not. This should still be added to the list
	 * but a message explaining the issue should also be shown.
	 */
	@Test
	public void test9(){
		RouterPatchChecker test = new RouterPatchChecker();
		StringBuffer expected = new StringBuffer("ERROR: Indefinite answer. Check if the server with host name ''d.example.com'' on IP Address ''1.1.1.4'' has already been patched."
				+ "\nb.example.com (1.1.1.2), OS Version 13 [Behind the other routers so no one sees it]"
				+ "\nd.example.com (1.1.1.4), OS Version 14"
				+ "\nf.example.com (1.1.1.7), OS Version 12.2");
		StringBuffer actual = test.readAndPopulate("sample8.csv");
		
		assertEquals("Incorrect result.", expected.toString(), actual.toString());
	}
	
	/**
	 * Checking the RouterPackChecker works as it should with the "sample9.csv" file. 
	 * It shows only a selection of routers should be returned.
	 */
	@Test
	public void test10(){
		RouterPatchChecker test = new RouterPatchChecker();
		StringBuffer expected = new StringBuffer("f.example.com (1.1.1.7), OS Version 12.2");
		StringBuffer actual = test.readAndPopulate("sample9.csv");
		
		assertEquals("Incorrect result.", expected.toString(), actual.toString());
	}
	
	/**
	 * Checking the RouterPackChecker works as it should with the "sample7.csv" file. 
	 * It shows only a selection of routers should be returned.
	 */
	@Test
	public void test11(){
		RouterPatchChecker test = new RouterPatchChecker();
		StringBuffer expected = new StringBuffer("A.example.COM (1.1.1.1), OS Version 12.9 [Faulty fans]"
				+ "\nb.example.com (1.1.1.2), OS Version 13 [Behind the other routers so no one sees it]"
				+ "\nC.EXAMPLE.COM (1.1.1.3), OS Version 12.1"
				+ "\nc.example.co.uk (1.1.1.5), OS Version 12 [Case a bit loose]"
				+ "\ne.example.com (1.1.1.6), OS Version 12.3"
				+ "\nf.example.com (1.1.1.7), OS Version 12.2"
				+ "\ng.example.com (1.1.1.9), OS Version 15 [Guarded by sharks with lasers on their heads]");
		StringBuffer actual = test.readAndPopulate("sample10.csv");
		
		assertEquals("Incorrect result.", expected.toString(), actual.toString());
	}
	
}
