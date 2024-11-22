package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "product")
@XmlType(propOrder = {"id", "name", "available", "wholesalerPrice", "publicPrice", "stock"})
public class Product {
	private int id;
	private String name;
    private Amount publicPrice;
    private Amount wholesalerPrice;
    private boolean available;
    private int stock;
    private static int totalProducts;

    static final double EXPIRATION_RATE = 0.60;

    public Product() {
        this.id = totalProducts + 1;
        totalProducts++;
        available = true;
    }

    public Product(String name, Amount wholesalerPrice, boolean available, int stock) {
        this.id = totalProducts + 1;
        this.name = name;
        this.wholesalerPrice = wholesalerPrice;
        this.available = available;
        this.stock = stock;
        this.publicPrice = new Amount(wholesalerPrice.getValue() * 2); 
        totalProducts++;
    }
    @XmlAttribute(name = "id")
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @XmlElement(name = "publicPrice")
    public Amount getPublicPrice() {
        return publicPrice;
    }

    public void setPublicPrice(Amount publicPrice) {
        this.publicPrice = publicPrice;
    }

    @XmlElement(name = "wholesalerPrice")
    public Amount getWholesalerPrice() {
        return wholesalerPrice;
    }

    public void setWholesalerPrice(Amount wholesalerPrice) {
        this.wholesalerPrice = wholesalerPrice;
        this.publicPrice = new Amount(wholesalerPrice.getValue()* 2); 
    }
    
    @XmlElement(name = "available")
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @XmlElement(name = "stock")
    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public static int getTotalProducts() {
        return totalProducts;
    }

    public static void setTotalProducts(int totalProducts) {
        Product.totalProducts = totalProducts;
    }

    public void expire() {
        this.publicPrice = new Amount(this.publicPrice.getValue() * EXPIRATION_RATE);
    }
}
