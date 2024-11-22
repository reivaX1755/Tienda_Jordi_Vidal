package dao.jaxb;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import model.Product;
import model.ProductList;

public class JaxbMarshaller {
    public boolean init(ArrayList<Product> inventory) {
        try {
            JAXBContext context = JAXBContext.newInstance(ProductList.class);
            Marshaller marshaller = context.createMarshaller();
            ProductList productList = new ProductList();
            productList.setProducts(inventory);

            long currentTime = System.currentTimeMillis();
            Date fechaActual = new Date(currentTime);
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            String fechaFormateada = formato.format(fechaActual);

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(productList, new File("jaxb/inventory_" + fechaFormateada + ".xml"));
            
            System.out.println("Archivo XML generado correctamente.");
            return true; 
        } catch (JAXBException e) {
            e.printStackTrace();
            return false; 
        }
    }
}

