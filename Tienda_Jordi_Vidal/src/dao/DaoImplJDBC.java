	package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import model.Employee;
import model.Product;
import model.ProductList;

public class DaoImplJDBC implements Dao{

	private Connection connection;
	
	@Override
	public void connect() {
		String url = "jdbc:mysql://localhost:3306/Shop";
		String user = "root";
		String pass = "";
		try {
			this.connection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		Employee employee = null;
		String query = "SELECT employeeId, name, password FROM employee WHERE employeeId = ? AND password = ?";
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, employeeId);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("employeeId");
                String employeeName = resultSet.getString("name");

                employee = new Employee(employeeName, id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return employee;
	}

	@Override
	public void disconnect(){
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public ArrayList<Product> getInventory() {
		connect();
	    ArrayList<Product> products = new ArrayList<>();
	    String query = "SELECT id, name, wholesalerPrice, available, stock FROM inventory"; 
	    try (PreparedStatement preparedStatement = connection.prepareStatement(query);
	         ResultSet resultSet = preparedStatement.executeQuery()) { 
	        
	        while (resultSet.next()) {
	            int id = resultSet.getInt("id");
	            String name = resultSet.getString("name");
	            double wholesalerPrice = resultSet.getDouble("wholesalerPrice");
	            boolean available = resultSet.getBoolean("available");
	            int stock = resultSet.getInt("stock");
	            
	            Product product = new Product(name, wholesalerPrice, available, stock);
	            products.add(product);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    disconnect();
	    return products;
	}


	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		long currentTime = System.currentTimeMillis();
		Date fechaActual = new Date(currentTime);
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fechaFormateada = formato.format(fechaActual);
	    connect();
	    String insertQuery = "INSERT INTO historical_inventory (id_product, name, wholesalerPrice, available, stock, created_at) "
	                        + "VALUES (?, ?, ?, ?, ?, ?)";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
	        connection.setAutoCommit(false); 

	        for (Product product : inventory) {
	            preparedStatement.setInt(1, product.getId());
	            preparedStatement.setString(2, product.getName());
	            preparedStatement.setDouble(3, product.getWholesalerPrice().getValue());
	            preparedStatement.setBoolean(4, product.isAvailable());
	            preparedStatement.setInt(5, product.getStock());
	            preparedStatement.setString(6, fechaFormateada); 
	            preparedStatement.addBatch(); 
	        }

	        preparedStatement.executeBatch(); 
	        connection.commit(); 
	        return true; 
	    } catch (SQLException e) {
	        e.printStackTrace();
	        try {
	            connection.rollback(); 
	        } catch (SQLException rollbackException) {
	            rollbackException.printStackTrace();
	        }
	        return false;
	    } finally {
	        disconnect(); 
	    }
	}

	@Override
	public void addProduct(Product product) {
		connect();

	    String query = "INSERT INTO inventory (id, name, wholesalerPrice, available, stock) VALUES (?, ?, ?, ?, ?)";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	    	preparedStatement.setInt(1, product.getId());
	    	preparedStatement.setString(2, product.getName());
	        preparedStatement.setDouble(3, product.getWholesalerPrice().getValue());
	        preparedStatement.setBoolean(4, product.isAvailable());
	        preparedStatement.setInt(5, product.getStock());
	        
	        preparedStatement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        disconnect();
	    }
	}

	@Override
	public void deleteProduct(String productName) {
		connect();  

	    String query = "DELETE FROM inventory WHERE name = ?";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        preparedStatement.setString(1, productName);
	        
	        preparedStatement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        disconnect(); 
	    }
	}

	@Override
	public void updateProduct(Product product) {   
		connect();

	    boolean isAvailable = false;
	    if(product.getStock() > 0) {
	    	isAvailable = true;
	    }
	    String query = "UPDATE inventory SET stock = ?, available = ? WHERE name = ?";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        preparedStatement.setInt(1, product.getStock());
	        preparedStatement.setBoolean(2, isAvailable);
	        preparedStatement.setString(3, product.getName());

	        preparedStatement.executeUpdate(); 
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        disconnect();
	    }
	}

}
