package model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Entity
@Table(name = "inventory")
@XmlRootElement(name = "product")
@XmlType(propOrder = {"id", "name", "available", "wholesalerPrice", "publicPrice", "stock"})
public class Product {

    @Id
    @Column(name = "id")
    @XmlAttribute(name = "id")
    private int id;

    @Column(name = "name")
    @XmlAttribute(name = "name")
    private String name;

    @Transient
    @XmlElement(name = "publicPrice")
    private Amount publicPrice;

    @Transient
    @XmlElement(name = "wholesalerPrice")
    private Amount wholesalerPrice;

    @Column(name = "price")
    private double price;

    @Column(name = "available")
    @XmlElement(name = "available")
    private boolean available;

    @Column(name = "stock")
    @XmlElement(name = "stock")
    private int stock;

    @Transient
    private static int totalProducts;

    @Transient
    static final double EXPIRATION_RATE = 0.60;

    public Product() {
        // Constructor vacío para Hibernate
    }
    // Constructor con precios de tipo Amount
    public Product(int id, String name, Amount wholesalerPrice, boolean available, int stock) {
        this.id = id;
        this.name = name;
        this.wholesalerPrice = wholesalerPrice;
        this.available = available;
        this.stock = stock;
        this.publicPrice = new Amount(wholesalerPrice.getValue() * 2);
        this.price = wholesalerPrice.getValue();
    }

    // Constructor con precios de tipo double
    public Product(int id, String name, double price, boolean available, int stock) {
        this.id = id;
        totalProducts++;
        this.name = name;
        this.wholesalerPrice = new Amount(price); 
        this.available = available;
        this.stock = stock;
        this.publicPrice = new Amount(wholesalerPrice.getValue() * 2);
    }

    public Product(boolean isAvailable, String name, double price, int stock) { 
    	this.id = totalProducts + 1;
        totalProducts++;
        this.available = isAvailable;
        this.name = name;
        this.wholesalerPrice = new Amount(price);
        this.stock = stock;
        this.publicPrice = new Amount(wholesalerPrice.getValue() * 2);
	}

	public Product(Object object, Amount amount, boolean b, int i) {
		// TODO Auto-generated constructor stub
	}
	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Amount getPublicPrice() {
        return publicPrice;
    }

    public void setPublicPrice(Amount publicPrice) {
        this.publicPrice = publicPrice;
    }

    public Amount getWholesalerPrice() {
        return wholesalerPrice;
    }

    public void setWholesalerPrice(Amount wholesalerPrice) {
        this.wholesalerPrice = wholesalerPrice;
        this.publicPrice = new Amount(wholesalerPrice.getValue() * 2);  // Actualiza precio público
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
        if (this.stock > 0) {
            this.available = true;
        }
    }

    public static int getTotalProducts() {
        return totalProducts;
    }

    public static void setTotalProducts(int totalProducts) {
        Product.totalProducts = totalProducts;
    }

    public void expire() {
        this.publicPrice = new Amount(this.publicPrice.getValue() * EXPIRATION_RATE);  // Aplica tasa de expiración
    }
}
