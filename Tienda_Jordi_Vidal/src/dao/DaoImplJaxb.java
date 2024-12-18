package dao;

import java.util.ArrayList;

import dao.jaxb.JaxbMarshaller;
import dao.jaxb.JaxbUnMarshaller;
import model.Employee;
import model.Product;

public class DaoImplJaxb implements Dao{

	@Override
	public void connect() {
		
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		return null;
	}

	@Override
	public void disconnect() {
		
	}

	@Override
	public ArrayList<Product> getInventory() {
	    return (new JaxbUnMarshaller()).init();
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
	    return (new JaxbMarshaller()).init(inventory);
	}

	@Override
	public void deleteProduct(String productName) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addProduct(Product product) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addStock(Product product) {
		// TODO Auto-generated method stub
		
	}


}
