package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import model.Amount;
import model.Employee;
import model.Product;


public class DaoImplFile implements Dao{

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
		try {
		    File fichero = new File("src\\Files\\inputInventory.txt");	//Ruta del archivo relativa para que funcione su exportamos a otro pc
		    if (fichero.canRead()) {
		        FileReader newFichero = new FileReader(fichero);
		        BufferedReader newFichero2 = new BufferedReader(newFichero);
		        String linea = newFichero2.readLine();
		        while (linea != null) {
		            linea = linea.replace("Product:", "");
		            String[] partes = linea.split(";");
		            String nombre = partes[0];//Se podria hacer []String nombre = partes[0].split(":") y luego pasar []String a String para guardar el dato
		            double precio = Double.parseDouble(partes[1].replace("Wholesaler Price:", ""));
		            boolean disponible = true;
		            int cantidad = Integer.parseInt(partes[2].replace("Stock:", ""));
		            inventory.add(new Product(nombre, new Amount(precio), disponible, cantidad));
		            linea = newFichero2.readLine();
		        }
		        newFichero2.close();    
		    }
		} catch (java.io.IOException | java.lang.SecurityException e) {
		    System.out.print("Ha habido un problema con el fichero!");
		}
		for (Product product : inventory) {
		    if (product != null) {
		        product.setPublicPrice(product.getWholesalerPrice());
		    }
		}

		return inventory;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		long currentTime = System.currentTimeMillis();
		Date fechaActual = new Date(currentTime);
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        String fechaFormateada = formato.format(fechaActual);
        try (FileWriter escritor = new FileWriter("src\\Files\\inventory_" + fechaFormateada + ".txt")) {
            PrintWriter escribirDatos = new PrintWriter(escritor);
            int i = 1;
            for (Product product : inventory) {
                escribirDatos.println(i + ";Product:" + product.getName() + ";Stock:" + product.getStock() + ";");
                i++;
            }
            escribirDatos.println("Numero total de productos "+(i-1)+";");
            System.out.println("Se ha creado el archivo en la ruta:  src/Files/");
            escribirDatos.close();
        } catch (IOException e) {
            System.out.println("Ha ocurrido un error al crear el archivo.");
            return false;
        }
		return true;
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
	public void updateProduct(Product product) {
		// TODO Auto-generated method stub
		
	}

	
}
