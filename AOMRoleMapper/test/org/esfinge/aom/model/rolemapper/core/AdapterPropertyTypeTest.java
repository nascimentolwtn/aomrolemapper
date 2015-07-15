package org.esfinge.aom.model.rolemapper.core;

import junit.framework.Assert;

import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.rolemapper.core.testclasses.propertytypetest.Account;
import org.esfinge.aom.rolemapper.core.testclasses.propertytypetest.AccountPropertyType;
import org.esfinge.aom.rolemapper.core.testclasses.propertytypetest.AccountType;
import org.junit.Test;

public class AdapterPropertyTypeTest {

	private static final String accountPropertyTypeClass = "org.esfinge.aom.rolemapper.core.testclasses.propertytypetest.AccountPropertyType";

	@Test
	public void testConstructor() throws Exception 
	{	
		AdapterPropertyType propertyType = new AdapterPropertyType(accountPropertyTypeClass);
		Assert.assertTrue(propertyType.getAssociatedObject() instanceof AccountPropertyType);
	}	
	
	@Test
	public void testConstructor_FromDsObject() throws Exception 
	{	
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		AdapterPropertyType propertyType = AdapterPropertyType.getAdapter(accountPropertyType);
		Assert.assertEquals(accountPropertyType, propertyType.getAssociatedObject());
	}
	
	@Test
	public void testGetName() throws Exception 
	{
		String propertyName = "Owner";
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		AdapterPropertyType adapterPropertyType = AdapterPropertyType.getAdapter(accountPropertyType);
		accountPropertyType.setName(propertyName);
		Assert.assertEquals(propertyName, adapterPropertyType.getName());
	}		
	
	@Test
	public void testSetName() throws Exception 
	{
		String propertyName = "Owner";
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		AdapterPropertyType adapterPropertyType = AdapterPropertyType.getAdapter(accountPropertyType);
		adapterPropertyType.setName(propertyName);
		Assert.assertEquals(propertyName, accountPropertyType.getName());
	}	
	
	@Test
	public void testGetType() throws Exception 
	{
		Class<?> propertyType = String.class;
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		AdapterPropertyType adapterPropertyType = AdapterPropertyType.getAdapter(accountPropertyType);
		accountPropertyType.setPropertyType(propertyType);
		Assert.assertEquals(propertyType, adapterPropertyType.getType());
	}
	
	@Test
	public void testGetType_relationship() throws Exception 
	{
		AccountType accountType = new AccountType();
		AdapterEntityType relationshipType = AdapterEntityType.getAdapter(accountType);
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		
		AdapterPropertyType adapterPropertyType = AdapterPropertyType.getAdapter(accountPropertyType);
		adapterPropertyType.setType(relationshipType);
		
		Assert.assertEquals(relationshipType, adapterPropertyType.getType());
	}
	
	@Test
	public void testGetType_invalidType() throws Exception 
	{
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		accountPropertyType.setPropertyType("invalidType");
		
		AdapterPropertyType adapterPropertyType = AdapterPropertyType.getAdapter(accountPropertyType);
				
		try
		{
			adapterPropertyType.getType();
			Assert.fail();
		}
		catch (EsfingeAOMException e)
		{
			Assert.assertEquals("Invalid type", e.getMessage());
		}
	}
	
	@Test
	public void testGetTypeAsString() throws Exception 
	{
		Class<?> propertyType = String.class;
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		AdapterPropertyType adapterPropertyType = AdapterPropertyType.getAdapter(accountPropertyType);
		accountPropertyType.setPropertyType(propertyType);
		Assert.assertEquals("java.lang.String", adapterPropertyType.getTypeAsString());
	}
	
	@Test
	public void testGetTypeAsString_relationship() throws Exception 
	{
		AccountType accountType = new AccountType();
		accountType.setName("accountType");
		
		AdapterEntityType relationshipType = AdapterEntityType.getAdapter(accountType);
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		
		AdapterPropertyType adapterPropertyType = AdapterPropertyType.getAdapter(accountPropertyType);
		adapterPropertyType.setType(relationshipType);
		
		Assert.assertEquals("accountType", adapterPropertyType.getTypeAsString());
	}
	
	@Test
	public void testSetType() throws Exception 
	{
		Class<?> propertyType = String.class;
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		AdapterPropertyType adapterPropertyType = AdapterPropertyType.getAdapter(accountPropertyType);
		adapterPropertyType.setType(propertyType);
		Assert.assertEquals(propertyType, accountPropertyType.getPropertyType());
	}
	
	@Test
	public void testSetType_relationship() throws Exception 
	{
		AccountType accountType = new AccountType();
		AdapterEntityType relationshipType = AdapterEntityType.getAdapter(accountType);
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		
		AdapterPropertyType adapterPropertyType = AdapterPropertyType.getAdapter(accountPropertyType);
		adapterPropertyType.setType(relationshipType);
		Assert.assertEquals(accountType, accountPropertyType.getPropertyType());
	}
	
	@Test
	public void testSetType_invalidType() throws Exception 
	{
		AccountPropertyType accountPropertyType = new AccountPropertyType();			
		AdapterPropertyType adapterPropertyType = AdapterPropertyType.getAdapter(accountPropertyType);
		try
		{
			adapterPropertyType.setType("invalidType");
			Assert.fail();
		}
		catch (EsfingeAOMException e)
		{
			Assert.assertEquals("Invalid type", e.getMessage());
		}
	}
	
	@Test
	public void testIsValidValue_validValuePrimitiveType() throws Exception 
	{
		Class<?> propertyType = double.class;
		double value = 100;
		Double notPrimitiveValue = new Double(100);
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		AdapterPropertyType adapterPropertyType = AdapterPropertyType.getAdapter(accountPropertyType);
		adapterPropertyType.setType(propertyType);
		Assert.assertTrue(adapterPropertyType.isValidValue(100.0));
		Assert.assertTrue(adapterPropertyType.isValidValue(value));
		Assert.assertTrue(adapterPropertyType.isValidValue(notPrimitiveValue));
	}
	
	@Test
	public void testIsValidValue_validValueNotPrimitiveType() throws Exception 
	{
		Class<?> propertyType = Double.class;
		double value = 100;
		Double notPrimitiveValue = new Double(100);
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		AdapterPropertyType adapterPropertyType = AdapterPropertyType.getAdapter(accountPropertyType);
		adapterPropertyType.setType(propertyType);
		Assert.assertTrue(adapterPropertyType.isValidValue(100.0));
		Assert.assertTrue(adapterPropertyType.isValidValue(value));
		Assert.assertTrue(adapterPropertyType.isValidValue(notPrimitiveValue));
	}
	
	@Test
	public void testIsValidValue_validValueNullValue() throws Exception 
	{
		Class<?> propertyType = Double.class;
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		AdapterPropertyType adapterPropertyType = AdapterPropertyType.getAdapter(accountPropertyType);
		adapterPropertyType.setType(propertyType);
		Assert.assertTrue(adapterPropertyType.isValidValue(null));
	}
	
	@Test
	public void testIsValidValue_validValueNumberAssign() throws Exception 
	{
		Class<?> propertyType = double.class;
		int value = 100;
		Integer notPrimitiveValue = new Integer(100);
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		AdapterPropertyType adapterPropertyType = AdapterPropertyType.getAdapter(accountPropertyType);
		adapterPropertyType.setType(propertyType);
		Assert.assertTrue(adapterPropertyType.isValidValue(100));
		Assert.assertTrue(adapterPropertyType.isValidValue(value));
		Assert.assertTrue(adapterPropertyType.isValidValue(notPrimitiveValue));
	}
	
	@Test
	public void testIsValidValue_relationship() throws Exception 
	{
		AccountType accountType = new AccountType();
		AdapterEntityType relationshipType = AdapterEntityType.getAdapter(accountType);
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		
		AdapterPropertyType adapterPropertyType = AdapterPropertyType.getAdapter(accountPropertyType);
		adapterPropertyType.setType(relationshipType);
		
		Account account = new Account();
		account.setAccountType(accountType);
		AdapterEntity relationshipEntity = AdapterEntity.getAdapter(relationshipType, account);
		
		Assert.assertTrue(adapterPropertyType.isValidValue(relationshipEntity));
	}
	
	@Test
	public void testIsValidValue_relationshipInvalidValue() throws Exception 
	{
		AccountType accountType = new AccountType();
		AdapterEntityType relationshipType = AdapterEntityType.getAdapter(accountType);
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		
		AdapterPropertyType adapterPropertyType = AdapterPropertyType.getAdapter(accountPropertyType);
		adapterPropertyType.setType(relationshipType);
		
		Account account = new Account();
		AccountType anotherAccountType = new AccountType();
		account.setAccountType(anotherAccountType);
		AdapterEntityType anotherEntityType = AdapterEntityType.getAdapter(anotherAccountType);
		AdapterEntity relationshipEntity = AdapterEntity.getAdapter(anotherEntityType, account);
		
		Assert.assertFalse(adapterPropertyType.isValidValue(relationshipEntity));
	}
	
	@Test
	public void testIsRelationshipProperty() throws Exception 
	{
		AccountType accountType = new AccountType();
		AdapterEntityType relationshipType = AdapterEntityType.getAdapter(accountType);
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		
		AdapterPropertyType adapterPropertyType = AdapterPropertyType.getAdapter(accountPropertyType);
		adapterPropertyType.setType(relationshipType);
		
		Assert.assertTrue(adapterPropertyType.isRelationshipProperty());
	}
	
	@Test
	public void testIsRelationshipProperty_notARelationship() throws Exception 
	{
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		
		AdapterPropertyType adapterPropertyType = AdapterPropertyType.getAdapter(accountPropertyType);
		adapterPropertyType.setType(String.class);
		
		Assert.assertFalse(adapterPropertyType.isRelationshipProperty());
	}
	
}