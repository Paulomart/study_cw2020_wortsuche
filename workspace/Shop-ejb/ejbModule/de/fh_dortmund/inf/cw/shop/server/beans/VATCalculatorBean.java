package de.fh_dortmund.inf.cw.shop.server.beans;

import javax.annotation.Resource;
import javax.ejb.Stateless;

import de.fh_dortmund.inf.cw.shop.server.beans.interfaces.VATCalculatorLocal;
import de.fh_dortmund.inf.cw.shop.server.beans.interfaces.VATCalculatorRemote;

@Stateless
public class VATCalculatorBean implements VATCalculatorLocal, VATCalculatorRemote {

	@Resource(name = "vatRate")
	private double vatRate;
	
	@Override
	public double calculateVAT(double amount) {
		double grossAmount = amount * (1. + vatRate);
		double roundedGrossAmount = Math.round(grossAmount * 100.) / 100.;
		
		return roundedGrossAmount;
	}

	@Override
	public double getVATRate() {
		return vatRate;
	}

}
