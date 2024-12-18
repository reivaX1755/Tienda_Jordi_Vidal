package dao;

import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import dao.xml.SaxReader;
import dao.xml.DomWriter;
import model.Employee;
import model.Product;

public class DaoImplXml implements Dao {

	public ArrayList<Product> inventory;

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
		inventory = new ArrayList<Product>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser;

		try {
			saxParser = factory.newSAXParser();
			SaxReader saxReader = new SaxReader();
			saxParser.parse("xml/inputInventory.xml", saxReader);
			inventory = saxReader.getProducts();
			return inventory;
		} catch (Exception e) {
			e.printStackTrace(); // Metodo para saber que falla exactamente
			return null;
		}
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		DomWriter domWriter = new DomWriter();
		boolean created = domWriter.generateDocument(inventory);
		return created;

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
