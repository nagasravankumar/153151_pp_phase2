package com.cg.mypaymentapp.test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Wallet;
import com.cg.mypaymentapp.exception.InsufficientBalanceException;
import com.cg.mypaymentapp.exception.InvalidInputException;
import com.cg.mypaymentapp.repo.DBUtil;
import com.cg.mypaymentapp.service.WalletService;
import com.cg.mypaymentapp.service.WalletServiceImpl;

public class TestClass {

	private static WalletService service;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		service= new WalletServiceImpl();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception 
	{
	}

	@Before
	public void setUp() throws Exception 
	{
		
		 Customer cust1=new Customer("Amit", "9900112212",new Wallet(new BigDecimal(9000)));
		 Customer cust2=new Customer("Ajay", "9963242422",new Wallet(new BigDecimal(6000)));
		 Customer cust3=new Customer("Yogini", "9922950519",new Wallet(new BigDecimal(7000)));
				
		try(Connection connection = DBUtil.getConnection())
		{
			
			PreparedStatement pre=connection.prepareStatement("Insert into paytm_account_details values(?,?,?)");
			pre.setString(1, cust1.getMobileNo());
			pre.setString(2, cust1.getName());
			pre.setBigDecimal(3, cust1.getWallet().getBalance());
			pre.execute();
			PreparedStatement pre1=connection.prepareStatement("Insert into paytm_account_details values(?,?,?)");
			pre1.setString(1, cust2.getMobileNo());
			pre1.setString(2, cust2.getName());
			pre1.setBigDecimal(3, cust2.getWallet().getBalance());
			pre1.execute();
			PreparedStatement pre2=connection.prepareStatement("Insert into paytm_account_details values(?,?,?)");
			pre2.setString(1, cust3.getMobileNo());
			pre2.setString(2, cust3.getName());
			pre2.setBigDecimal(3, cust3.getWallet().getBalance());
			pre2.execute();
		}
		catch(ClassNotFoundException | SQLException e)
		{
			
		}
		
		
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected=InvalidInputException.class)
	public void testCreateAccount1() 
	{
		service.createAccount(null, "9856895600", new BigDecimal(1500));
	}
	
	
	@Test(expected=InvalidInputException.class)
	public void testCreateAccount2() 
	{
		service.createAccount("", "9090504066", new BigDecimal(1500));
	}
	
	
	@Test(expected=InvalidInputException.class)
	public void testCreateAccount3() 
	{
		service.createAccount("john", "999", new BigDecimal(1500));
	}
	
	
	@Test(expected=InvalidInputException.class)
	public void testCreateAccount4() 
	{
		service.createAccount("john", "", new BigDecimal(1500));
	}
	
	
	@Test(expected=InvalidInputException.class)
	public void testCreateAccount5() 
	{
		service.createAccount("", "", new BigDecimal(1500));
	}
	
	
	
	@Test(expected=InvalidInputException.class)
	public void testCreateAccount6() 
	{
		service.createAccount("Amit", "9900112212", new BigDecimal(9000));
	}
	
	
	@Test(expected=InvalidInputException.class)
	public void testCreateAccount7() 
	{
		service.createAccount("Eric", "6549871230", new BigDecimal(-100));
	}
	
	
	@Test
	public void testCreateAccount8() 
	{
		Customer actual=service.createAccount("Johan", "9942221102", new BigDecimal(5000));
		Customer expected=new Customer("Johan", "9942221102", new Wallet(new BigDecimal(5000)));
		
		assertEquals(expected, actual);
	}
	
	
	@Test
	public void testCreateAccount9() 
	{
		Customer actual=service.createAccount("Shilpa", "9876543210", new BigDecimal(0));
		Customer expected=new Customer("Shilpa", "9876543210", new Wallet(new BigDecimal(0)));
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCreateAccount10() 
	{
		Customer actual=service.createAccount("Aarthika", "9965521102", new BigDecimal(5000.75));
		Customer expected=new Customer("Aarthika", "9965521102", new Wallet(new BigDecimal(5000.75)));
		
		assertEquals(expected, actual);
	}


	
	@Test(expected=InvalidInputException.class)
	public void testShowBalance11() 
	{
		service.showBalance(null);		
	}
	
	
	@Test(expected=InvalidInputException.class)
	public void testShowBalance12() 
	{
		service.showBalance("");		
	}
	
	
	@Test(expected=InvalidInputException.class)
	public void testShowBalance13() 
	{
		service.showBalance("12345");		
	}
	
	
	@Test(expected=InvalidInputException.class)
	public void testShowBalance14() 
	{
		service.showBalance("9876345210");		
	}
	
	
	@Test(expected=InvalidInputException.class)
	public void testShowBalance15() 
	{
		service.showBalance("99001122122");		
	}
	
	
	@Test
	public void testShowBalance16() 
	{
		Customer customer=service.showBalance("9900112212");
		BigDecimal expectedResult=new BigDecimal(8449.5);
		BigDecimal obtainedResult=customer.getWallet().getBalance();
		
		assertEquals(expectedResult, obtainedResult);
		
	}

	
	@Test(expected=InvalidInputException.class)
	public void testFundTransfer17() 
	{
		service.fundTransfer("9948484810", "9922950519", new BigDecimal(5000));		
	}
	
	
	
	@Test(expected=InvalidInputException.class)
	public void testFundTransfer18() 
	{
		service.fundTransfer("9922950519", "9922950519", new BigDecimal(5000));		
	}

	
	@Test(expected=InsufficientBalanceException.class)
	public void testFundTransfer19() 
	{
		service.fundTransfer("9900112212", "9963242422", new BigDecimal(12000));		
	}
	
	
	@Test(expected=InvalidInputException.class)
	public void testFundTransfer20() 
	{
		service.fundTransfer("9900112212", "9922950519", new BigDecimal(0));		
	}
	
	
	@Test(expected=InvalidInputException.class)
	public void testFundTransfer21() 
	{
		service.fundTransfer("9900112212", "", new BigDecimal(0));		
	}
	
	
	@Test(expected=InvalidInputException.class)
	public void testFundTransfer22() 
	{
		service.fundTransfer("", "9922950519", new BigDecimal(500));		
	}
	
	
	@Test
	public void testFundTransfer23() 
	{
		Customer customer=service.fundTransfer("9922950519", "9963242422", new BigDecimal(500));
		BigDecimal expected=customer.getWallet().getBalance();
		BigDecimal actual=new BigDecimal(6500);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testFundTransfer24() 
	{
		Customer customer=service.fundTransfer("9900112212", "9922950519", new BigDecimal(550.50));
		BigDecimal expected=customer.getWallet().getBalance();
		BigDecimal actual=new BigDecimal(8449.50);
		
		assertEquals(expected, actual);
	}
	
	
	@Test(expected=InsufficientBalanceException.class)
	public void testFundTransfer25() 
	{
		Customer customer=service.fundTransfer("9900112212", "9922950519", new BigDecimal(15000));	
	}
	
	
	@Test(expected=InvalidInputException.class)
	public void testFundTransfer26() 
	{
		service.fundTransfer("", "9922950519", new BigDecimal(-100));		
	}
	
	
	@Test(expected=InvalidInputException.class)
	public void testFundTransfer27() 
	{
		service.fundTransfer("", "", new BigDecimal(500));		
	}
	
	
	@Test(expected=InvalidInputException.class)
	public void testFundTransfer28() 
	{
		service.fundTransfer(null, null, new BigDecimal(0));		
	}
	
	
	

}
