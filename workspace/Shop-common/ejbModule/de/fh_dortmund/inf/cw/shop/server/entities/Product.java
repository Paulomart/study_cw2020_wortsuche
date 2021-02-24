package de.fh_dortmund.inf.cw.shop.server.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

@NamedQueries({
	@NamedQuery(name = "Product.all", query = "SELECT p FROM Product p")
})
@Entity
public class Product implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long itemNumber;
	
	//@NotNull
	@Basic(optional = false) // Persistenzprovider prüft auf null. Wird von Eclipselink ignoriert! Bug?
	@Column(nullable = false) // Prüfung auf Datenbankebene. 
	private String name;
	private String description;
	
	@Column(nullable = false) // Hier kein @Basic(optional = false) möglich, da Basisdatentypen
	private double netPrice;
	
	@Column(nullable = false)
	private int numberOfItems;
	
	public Product() {
	}

	public long getItemNumber() {
		return itemNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getNetPrice() {
		return netPrice;
	}

	public void setNetPrice(double netPrice) {
		this.netPrice = netPrice;
	}

	public int getNumberOfItems() {
		return numberOfItems;
	}

	public void setNumberOfItems(int numberOfItems) {
		this.numberOfItems = numberOfItems;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	@Override
	public String toString() {
		return "Product [itemNumber=" + itemNumber + ", name=" + name + ", description=" + description + ", netPrice="
				+ netPrice + ", numberOfItems=" + numberOfItems + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (itemNumber ^ (itemNumber >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (itemNumber != other.itemNumber)
			return false;
		return true;
	}

	
}
