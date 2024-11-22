package dao.jaxb;
import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import model.Product;
import model.ProductList;

public class JaxbUnMarshaller {

	public ArrayList<Product> init() {
	    ProductList products = null;
	    try {
	        JAXBContext context = JAXBContext.newInstance(ProductList.class);
	        Unmarshaller unmarshaller = context.createUnmarshaller();
	        products = (ProductList) unmarshaller.unmarshal(new File("jaxb/inputInventory.xml"));
	    } catch (JAXBException e) {
	        e.printStackTrace();
	    }

	    if (products == null) {
	        System.out.println("Error unmarshalling");
	        return new ArrayList<>(); 
	    } else {
	        return new ArrayList<>(products.getProducts()); 
	    }
	}
}
