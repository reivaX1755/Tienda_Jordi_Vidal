package model;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Sale {
    Client client;
    ArrayList<String> products;
    Amount amount;
    Date fechaActual;
    
    public Sale(Client client, ArrayList<String> products, Amount amount, Date fechaActual) {
        super();
        this.client = client;
        this.products = products;
        this.amount = amount;
        this.fechaActual = fechaActual;
    }

    public Date getFechaActual() {
		return fechaActual;
	}

	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ArrayList<String> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<String> products) {
        this.products = products;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

	@Override
	public String toString() {
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String fechaFormateada = formato.format(fechaActual);
		return "Sale // Cliente = " + client.getName() + ", Productos Vendidos = " + products + " Total Venta = " + amount + ", Fecha venta = "
				+ fechaFormateada;
	}
}
