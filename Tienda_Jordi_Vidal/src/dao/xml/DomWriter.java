package dao.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import model.Product;

public class DomWriter {
private Document document;
	
	public DomWriter() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			document = builder.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.out.println("ERROR generating document");
		}
	}
	public boolean generateDocument(ArrayList<Product> inventory) {
	    Element products = document.createElement("products");
	    products.setAttribute("total", String.valueOf(Product.getTotalProducts())); 
	    document.appendChild(products);
	    int i = 1;
	    for (Product producto : inventory) {
	    	
	        // Crear el nodo "product" con el atributo del contador
	        Element product = document.createElement("product");
	        product.setAttribute("id", String.valueOf(producto.getId()));
	        products.appendChild(product);

	        // Nodo "name" para el nombre del producto
	        Element name = document.createElement("name");
	        name.setTextContent(producto.getName()); 
	        product.appendChild(name);

	        // Nodo "price" para el precio del producto
	        Element price = document.createElement("price");
	        price.setAttribute("currency", "€");  
	        price.setTextContent(String.format("%.2f", producto.getWholesalerPrice())); 
	        product.appendChild(price);

	        // Nodo "stock" para la cantidad disponible del producto
	        Element stock = document.createElement("stock");
	        stock.setTextContent(String.valueOf(producto.getStock()));  
	        product.appendChild(stock);
	        
	        i++;
	    }

	    return generateXml();
	}
	
	private boolean generateXml() {
		try {			
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			
			Source source = new DOMSource(document);
			long currentTime = System.currentTimeMillis();
			Date fechaActual = new Date(currentTime);
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
	        String fechaFormateada = formato.format(fechaActual);
			File file = new File("xml/inventory_" + fechaFormateada + ".xml");
			FileWriter fw = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fw);
			Result result = new StreamResult(pw);
			
			transformer.transform(source, result);
			return true;
		} catch (IOException e) {
			System.out.println("Error when creating writter file");
			e.printStackTrace();
			return false;
		} catch (TransformerException e) {
			System.out.println("Error transforming the document");
			e.printStackTrace();
			return false;
		}
	}

}
