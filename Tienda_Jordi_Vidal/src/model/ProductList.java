package model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="products")
public class ProductList {
	@XmlAttribute(name="total")
	private int total;
	
	private ArrayList<Product> products = new ArrayList<>(); 
	  
	
	public void setProducts(ArrayList<Product> products) {
		this.products = products;
		this.total = products.size();;
	}

	public ProductList() {};	
	
	public ProductList(ArrayList<Product> products) {
		this.products = products;
		this.total = products.size();;
	}

	@XmlElement(name="product")
	public ArrayList<Product> getProducts() {
		return products;
	}
	
}
