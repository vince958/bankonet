package com.bankonet.utils;

import org.junit.Assert;
import org.junit.Test;

import com.bankonet.utils.exception.CompteException;

public class CompteTest {
	@Test
	public void testDebitCompteAvecMaximumDebitAZero(){
		Compte compte = new CompteCourant();
		try {
			compte.debiter(10);
			Assert.fail("Test raté");
		} catch (CompteException e) {
			// TODO Auto-generated catch block
			System.out.println("Debit Max atteind");
		}
	}
}
