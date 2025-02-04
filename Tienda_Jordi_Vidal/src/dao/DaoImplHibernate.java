package dao;

import java.util.ArrayList;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;
import javax.transaction.Transaction;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import model.Employee;
import model.Product;
import model.ProductHistory;

public class DaoImplHibernate implements Dao{

	private static SessionFactory sessionFactory;

	@Override
	public void connect() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            try {
                sessionFactory = new Configuration()
                        .configure("hibernate.cfg.xml")
                        .addAnnotatedClass(model.Product.class)
                        .addAnnotatedClass(model.ProductHistory.class)
                        .buildSessionFactory();
            } catch (Exception e) {
                e.printStackTrace();
                throw new ExceptionInInitializerError("Error al inicializar la sesión de Hibernate: " + e.getMessage());
            }
        }
    }

	@Override
	public Employee getEmployee(int employeeId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disconnect() {
		if (sessionFactory != null) {
			sessionFactory.close();
        }
	}

	@Override
	public ArrayList<Product> getInventory() {
	    ArrayList<Product> inventory = new ArrayList<>();

	    connect();

	    try {
	        String hql = "FROM Product";
	        Session session = sessionFactory.openSession();
	        
	        Query<Product> query = session.createQuery(hql, Product.class);
	        ArrayList<Product> resultList = (ArrayList<Product>) query.list();

	        for (Product product : resultList) {
	            Product newProduct = new Product(
	                product.getId(),
	                product.getName(),  
	                product.getPrice(),
	                product.isAvailable(), 
	                product.getStock() 
	            );
	            inventory.add(newProduct);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        disconnect();
	    }

	    return inventory;
	}




	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
	    connect();

	    try (Session session = sessionFactory.openSession()) {
	    	session.beginTransaction();

	        for (Product product : inventory) {
	            ProductHistory history = new ProductHistory();
	            history.setAvailable(product.isAvailable());
	            history.setCreatedAt(java.time.LocalDateTime.now());
	            history.setProduct(product);
	            history.setName(product.getName());
	            history.setPrice(product.getPrice());
	            history.setStock(product.getStock());

	            session.save(history);
	        }

	        session.getTransaction().commit();
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    } finally {
	        disconnect();
	    }
	}

	@Override
	public void addProduct(Product product) {
	    connect(); 

	    try (Session session = sessionFactory.openSession()) {
	        session.beginTransaction();

	        Product newProduct = new Product();
	        newProduct.setId(product.getId()); 
	        newProduct.setAvailable(product.isAvailable());
	        newProduct.setName(product.getName());
	        newProduct.setPrice(product.getWholesalerPrice().getValue());
	        newProduct.setStock(product.getStock());

	        session.save(newProduct);

	        session.getTransaction().commit();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        disconnect();
	    }
	}



	@Override
	public void deleteProduct(String productName) {
		connect();
	    try (Session session = sessionFactory.openSession()) {
	        session.beginTransaction();

	        String hql = "DELETE FROM Product WHERE name = :productName";
	        Query query = session.createQuery(hql);
	        query.setParameter("productName", productName);
	        query.executeUpdate();
	        
	        session.getTransaction().commit();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }finally {
	    	disconnect();
	    }
	}


	@Override
	public void updateProduct(Product product) {
	    connect();

	    boolean isAvailable = false;
	    if(product.getStock() > 0) {	//Poner a falso si hay 0 productos
	    	isAvailable = true;
	    }

	    try (Session session = sessionFactory.openSession()) {
	        session.beginTransaction();

	        String hql = "UPDATE Product SET stock = :stock, available = :available WHERE name = :productName";
	        Query query = session.createQuery(hql);
	        query.setParameter("stock", product.getStock());
	        query.setParameter("available", isAvailable);
	        query.setParameter("productName", product.getName());

	        query.executeUpdate();

	        session.getTransaction().commit();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }finally {
	    	disconnect();
	    }
	}

}
