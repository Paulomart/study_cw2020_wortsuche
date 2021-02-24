package de.fh_dortmund.inf.cw.shop.server.beans.interfaces;

import javax.ejb.Local;

import de.fh_dortmund.inf.cw.shop.server.entities.Product;

@Local
public interface ProductManagementLocal extends ProductManagement {
	public Product create(Product product);
	public Product update(Product product);
	public void delete(Product product);
	public Product removeItemFromInventory(Product product, int quantity) throws Exception;
	public Product addItemToInventory(Product product, int quantity);
}
