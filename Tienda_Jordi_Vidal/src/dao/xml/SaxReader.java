package dao.xml;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import model.Amount;
import model.Product;

public class SaxReader extends DefaultHandler {
	ArrayList<Product> products;
	Product product;
	String value;
	String parsedElement;

	public ArrayList<Product> getProducts() {
		return products;
	}

	@Override
	public void startDocument() throws SAXException {
		this.products = new ArrayList<>();
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		switch (qName) {
		case "product":
			this.product = new Product(attributes.getValue("name") != null ? attributes.getValue("name") : "empty",
					new Amount(0.0), true, 0);
			break;
		case "wholesalerPrice":
			break;
		case "stock":
			break;
		}
		this.parsedElement = qName;
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		value = new String(ch, start, length);
		switch (parsedElement) {
		case "product":
			break;
		case "wholesalerPrice":
			this.product.setWholesalerPrice(new Amount(Double.valueOf(value)));
			this.product.setPublicPrice(new Amount(this.product.getWholesalerPrice().getValue() * 2));
			break;
		case "stock":
			this.product.setStock(Integer.valueOf(value));
			break;
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equals("product")) {
			this.products.add(product);
		}
		this.parsedElement = "";
	}
}
