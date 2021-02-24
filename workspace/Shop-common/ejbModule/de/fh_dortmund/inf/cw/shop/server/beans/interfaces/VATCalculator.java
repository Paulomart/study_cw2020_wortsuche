package de.fh_dortmund.inf.cw.shop.server.beans.interfaces;

public interface VATCalculator {
	public double calculateVAT(double amount);
	public double getVATRate();
}
	