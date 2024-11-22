package main;

import model.Amount;
import model.Client;
import model.Employee;
import model.PremiumClient;
import model.Product;
import model.Sale;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

import dao.Dao;
import dao.DaoImplFile;
import dao.DaoImplJaxb;
import dao.DaoImplXml;

public class Shop {
    private Amount cash;
    public static ArrayList<Product> inventory;
    private ArrayList<Sale> sales;
    public Dao dao;
    
	final static double TAX_RATE = 1.04;

	public Shop() {
        cash = new Amount(0.0);
        inventory = new ArrayList<>();
        sales = new ArrayList<>();
        dao = new DaoImplJaxb();
    }
	public static void main(String[] args){
		Shop shop = new Shop();
		shop.initSession();
		inventory = shop.dao.getInventory();
		Scanner scanner = new Scanner(System.in);
		int opcion = 0;
		boolean exit = false;
		do {
			System.out.println("\n");
			System.out.println("===========================");
			System.out.println("Menu principal miTienda.com");
			System.out.println("===========================");
			System.out.println("1) Contar caja");
			System.out.println("2) Añadir producto");
			System.out.println("3) Añadir stock");
			System.out.println("4) Marcar producto proxima caducidad");
			System.out.println("5) Ver inventario");
			System.out.println("6) Venta");
			System.out.println("7) Ver ventas");
			System.out.println("8) Ver precio ventas totales");
			System.out.println("9) Eliminar un producto");
			System.out.println("10) Salir programa");
			System.out.print("Seleccione una opción: ");
			opcion = scanner.nextInt();

			switch (opcion) {
			case 1:
				shop.showCash();
				break;
			case 2:
				shop.addProduct();
				break;
			case 3:
				shop.addStock();
				break;
			case 4:
				shop.setExpired();
				break;
			case 5:
				shop.showInventory();
				break;
			case 6:
				shop.sale();
				break;
			case 7:
				shop.showSales();
				break;
			case 8:
				shop.countAmountSales();
				break;
			case 9:
				shop.deleteProduct();
				break;
			case 10:
				exit = true;
				break;
			}

		} while (!exit);
	}
	private void initSession() {
	    boolean logged = false;
	    Scanner scanner = new Scanner(System.in);
	    do {
	        System.out.print("Introduzca numero de empleado: ");
	        int user = scanner.nextInt();
	        scanner.nextLine(); 

	        System.out.print("Introduzca contraseña: ");
	        String password = scanner.nextLine();

	        Logable employee = new Employee(password, user); 
	        logged = employee.login(user, password);

	        if (!logged) {
	            System.out.println("Credenciales incorrectas. Por favor, intente nuevamente.");
	        }
	    } while (!logged);
	}

	public double showCash() {
	    double valorCash = Math.round(cash.getValue() * 100.00) / 100.00;
	    System.out.print("\nDinero actual: " + valorCash + "€"); 
	    return valorCash;
	}
	private void countAmountSales() {
		System.out.println("\nSuma de dinero durante las ventas:");
		boolean hayVentas = false;
		double amount = 0.0;
		for (Sale sale : sales) {
			if (sale != null) {
				hayVentas = true;
				break;
			}
		}
		if(hayVentas == true) {
			for (Sale sale : sales) {
				if (sale != null) {
					amount += sale.getAmount().getValue();
				}
			}
		}else {
			System.out.println("Precio total de las ventas : 0, No hay ninguna venta echa");
		}
		if(hayVentas == true) {
			System.out.println("Precio total de las ventas : "+amount+"€");
		}
	}
	public void addProduct() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Añada el nombre del producto a añadir: ");
		String name = scanner.nextLine();
		System.out.print("Precio producto mayorista: ");
		Double wholesalerPrice = scanner.nextDouble();
		System.out.print("Stock: ");
		int stock = scanner.nextInt();

		inventory.add(new Product(name, new Amount(wholesalerPrice), true, stock));
		Product product = findProduct(name);
		product.setPublicPrice(product.getWholesalerPrice());
		System.out.print("\n"+stock+" unidades de "+name+" han sido añadidos");
	}
	public void addStock() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Seleccione un nombre de producto: ");
		String name = scanner.next();
		Product product = findProduct(name);

		if (product != null) {
			System.out.print("Seleccione la cantidad de "+name+" para añadir al stock actual de este producto:");
			int stock = scanner.nextInt();
			product.setStock((product.getStock() + stock));
			if (product.getStock() > 0) {
                product.setAvailable(true);
            }
			System.out.println("\nEl stock del producto " + name + " ha sido actualizado a " + product.getStock());
		} else {
			System.out.println("\nNo se ha encontrado el producto con nombre " + name);
		}
	}
	private void setExpired() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Seleccione el nombre del producto que quieres añadir el descuento por caducidad: ");
		String name = scanner.next();
		Product product = findProduct(name);
		product.expire();
		if (product != null) {
			System.out.println("\nEl precio del producto " + name + " ha sido actualizado a " + Math.round(product.getPublicPrice().getValue() * 100.0) / 100.0 + "€");
		}
	}
	public void showInventory() {
	    System.out.println("Contenido actual de la tienda:");
	    for (Product product : inventory) {
	        if (product != null) {
	            double wholesalerPriceValue = product.getWholesalerPrice() != null ? product.getWholesalerPrice().getValue() : 0.0;
	            double publicPriceValue = product.getPublicPrice() != null ? product.getPublicPrice().getValue() : 0.0;
	            

	            System.out.println("Nombre: " + product.getName() +
	                               " // Id: " + product.getId() +
	                               " // Precio Proveedor Unidad: " + String.format("%.2f", wholesalerPriceValue) +" " +(product.getWholesalerPrice().getCurrency()) +
	                               " // Precio Venta Cliente Unidad: " + String.format("%.2f", publicPriceValue) +" " +(product.getWholesalerPrice().getCurrency()) +
	                               " // Stock: " + product.getStock() +
	                               " // isAvailable: " + product.isAvailable());
	        }
	    }
	}
	public void deleteProduct() {
		Scanner scanner = new Scanner(System.in);
	    System.out.print("Introduce el nombre del producto que quieres eliminar del inventario: ");
	    String deleteProduct = scanner.nextLine();
	    boolean existe = false;
	    for (int i = 0; i < inventory.size(); i++) {
	    	Product product = inventory.get(i);
	    	if (product != null) {
				if(product.getName().equalsIgnoreCase(deleteProduct)) {
					inventory.remove(i);
					System.out.println("Producto con nombre "+deleteProduct+" ha sido eliminado con exito!");
					existe = true;
					break;
				}
			}
	    }
	    if(!existe) {
	    	System.out.println("No se puede eliminar el producto "+deleteProduct+" porque no existe");
	    }
	}
	public void sale() {
	    Scanner scanner = new Scanner(System.in);
	    System.out.println("El cliente es premium?	[Y / Else for No]");
	    String isPremium = scanner.nextLine();
	    if(isPremium.equalsIgnoreCase("Y")) {
			System.out.println("Realizar venta, escribir nombre cliente premium");
		    String namePremiumClient = scanner.nextLine();
		    PremiumClient clientePremium = new PremiumClient(namePremiumClient, 0);
		    Amount totalAmount = new Amount(0.0);
		    String name = "";
		    ArrayList<String> productosVendidos = new ArrayList<>();
		    int numVentas = 0;
		    while (!name.equals("0")) {
		        System.out.println("Introduce el nombre del producto, escribir 0 para terminar:");
		        name = scanner.nextLine();
		        if (name.equalsIgnoreCase("0")) {
		            System.out.println("Venta finalizada");
		            break;
		        }
		        Product product = findProduct(name);
		        boolean productAvailable = false;
		        if (product != null && product.isAvailable()) {
		            productAvailable = true;
		            totalAmount.setValue(totalAmount.getValue() + product.getPublicPrice().getValue()); 
		            product.setStock(product.getStock() - 1);
		            if (product.getStock() == 0) {
		                product.setAvailable(false);
		            }
		            System.out.println("Se ha vendido 1 unidad de " + name);
		        }
		        if (productAvailable) {
		            productosVendidos.add(name);
		            numVentas = (numVentas +1);
		        } else {
		            System.out.println("Producto no encontrado o sin stock");
		        }
		    }
		    totalAmount.setValue(totalAmount.getValue() / 2);
		    totalAmount.setValue(totalAmount.getValue() * TAX_RATE);
		    totalAmount.setValue(Math.round(totalAmount.getValue() * 100.00) / 100.00);
		    boolean Payable = false;
		    double points = (totalAmount.getValue() /10);
		    int finalPoints = (int) points;
		    Payable = clientePremium.pay(totalAmount);
		    long currentTime = System.currentTimeMillis();
		    Date fechaHoraActual = new Date(currentTime);
			Sale sale = new Sale(clientePremium, productosVendidos, totalAmount, fechaHoraActual); 
		    cash.setValue(cash.getValue() + totalAmount.getValue());        
		    System.out.println("Venta final: " + sale.toString());
		    if(Payable == true) {		
		    	double newBalance = (clientePremium.getBalance().getValue() - totalAmount.getValue());
		    	double roundedBalance = Math.round(newBalance * 100.00) / 100.00;
		    	System.out.println("El cliente premium llamado: "+namePremiumClient+ ", puede pagar y le sobra: "+roundedBalance+"€");
		    	System.out.println("El cliente premium llamado: "+namePremiumClient+ " obtiene "+finalPoints+" puntos!");
		    }else {
		    	double newBalance = (clientePremium.getBalance().getValue() - totalAmount.getValue());
		    	double roundedBalance = Math.round(newBalance * 100.00) / 100.00;
		    	System.out.println("El cliente premium llamado: "+namePremiumClient+ ", tiene un balance de "+roundedBalance+"€");
		    	System.out.println("El cliente premium llamado: "+namePremiumClient+ " obtiene "+finalPoints+" puntos!");
		    }
		    sales.add(sale);
	    }else {
	    	System.out.println("Realizar venta, escribir nombre cliente");
		    String client = scanner.nextLine();
		    Client cliente = new Client(client);
		    Amount totalAmount = new Amount(0.0);
		    String name = "";
		    ArrayList<String> productosVendidos = new ArrayList<>();
		    int numVentas = 0;
		    while (!name.equals("0")) {
		        System.out.println("Introduce el nombre del producto, escribir 0 para terminar:");
		        name = scanner.nextLine();
		        if (name.equalsIgnoreCase("0")) {
		            System.out.println("Venta finalizada");
		            break;
		        }
		        Product product = findProduct(name);
		        boolean productAvailable = false;
		        if (product != null && product.isAvailable()) {
		            productAvailable = true;
		            totalAmount.setValue(totalAmount.getValue() + product.getPublicPrice().getValue());  
		            product.setStock(product.getStock() - 1);
		            if (product.getStock() == 0) {
		                product.setAvailable(false);
		            }
		            System.out.println("Se ha vendido 1 unidad de " + name);
		        }
		        if (productAvailable) {
		            productosVendidos.add(name);
		            numVentas = (numVentas +1);	
		        } else {
		            System.out.println("Producto no encontrado o sin stock");
		        }
		    }
		    totalAmount.setValue(totalAmount.getValue() / 2);
		    totalAmount.setValue(totalAmount.getValue() * TAX_RATE);
		    totalAmount.setValue(Math.round(totalAmount.getValue() * 100.00) / 100.00);
		    boolean Payable = false;
		    Payable = cliente.pay(totalAmount);
		    long currentTime = System.currentTimeMillis();
		    Date fechaHoraActual = new Date(currentTime);
			Sale sale = new Sale(cliente, productosVendidos, totalAmount, fechaHoraActual); 
		    cash.setValue(cash.getValue() + totalAmount.getValue());        
		    System.out.println("Venta final: " + sale.toString());
		    if(Payable == true) {		
		    	double newBalance = (cliente.getBalance().getValue() - totalAmount.getValue());
		    	double roundedBalance = Math.round(newBalance * 100.00) / 100.00;
		    	System.out.println("El cliente llamado: "+client+ ", puede pagar y le sobra: "+roundedBalance+"€");
		    }else {
		    	double newBalance = (cliente.getBalance().getValue() - totalAmount.getValue());
		    	double roundedBalance = Math.round(newBalance * 100.00) / 100.00;
		    	System.out.println("El cliente llamado: "+client+ ", tiene un balance de "+roundedBalance+"€");
		    }
		    sales.add(sale);
	    }
	}
	private void showSales() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("\nLista de ventas:");
		if (sales.isEmpty()) {
			System.out.println("No hay ventas que mostrar");
		}else {
			for (Sale sale : sales) {
				if (sale != null) {
					System.out.println(sale);
				}
			}
			boolean salir = false;
			boolean guardarArchivo = false;
			while(!salir) {
				System.out.print("\nDesea escribir todas las ventas en un fichero? [S/N]: ");
				String escribirVentas = scanner.nextLine();
				if(escribirVentas.equalsIgnoreCase("S") || escribirVentas.equalsIgnoreCase("N")){
					if(escribirVentas.equalsIgnoreCase("S")) {
						guardarArchivo = true;
					}
					salir = true;
				}else {
					System.out.println("Seleccione la opción S para crear el archivo o N para salir");
				}
			}
			if(guardarArchivo == true) {
				long currentTime = System.currentTimeMillis();
				Date fechaActual = new Date(currentTime);
				SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		        String fechaFormateada = formato.format(fechaActual);
		        try (FileWriter escritor = new FileWriter("src\\Files\\sales_" + fechaFormateada + ".txt")) {
		            PrintWriter escribirDatos = new PrintWriter(escritor);
		            SimpleDateFormat formato2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		            int numeroVenta = 1;
		            for (Sale sale : sales) {
		                escribirDatos.println(numeroVenta + ";Client=" + sale.getClient() + ";Date=" + formato2.format(sale.getFechaActual()) + ";");
		                escribirDatos.print(numeroVenta + ";Products=");
		                ArrayList<String> nombresProductos = sale.getProducts();
		                for (int i = 0; i < nombresProductos.size(); i++) {
		                    String nombreProducto = nombresProductos.get(i);
		                    for (Product producto : inventory) {
		                        if (producto.getName().equalsIgnoreCase(nombreProducto)) {
		                            escribirDatos.print(producto.getName() + "," + producto.getWholesalerPrice()+ "€");
		                            break;
		                        }
		                    }
		                    if (i < nombresProductos.size() - 1) {
		                        escribirDatos.print(";");
		                    }
		                }
		                escribirDatos.println("\n"+numeroVenta + ";Amount= "+sale.getAmount()+";");
		                numeroVenta++;
		            }
		            System.out.println("Se ha creado el archivo en la ruta:  src/Files/");
		        } catch (IOException e) {
		            System.out.println("Ha ocurrido un error al crear el archivo.");
		        }
			}
		}
	}
	public Product findProduct(String name) {
		for (Product product : inventory) {
		    if (product != null && product.getName().equalsIgnoreCase(name)) {
		        return product;
		    }
		}
		return null;	
	}
}