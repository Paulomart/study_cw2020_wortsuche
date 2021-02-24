package de.fh_dortmund.inf.cw.shop.server.beans.interfaces;

import java.util.List;

import de.fh_dortmund.inf.cw.shop.server.entities.Product;

public interface ProductManagement {
	public List<Product> readProducts();
}
