package dao;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;

import model.Amount;
import model.Employee;
import model.Product;

public class DaoImplMongoDB implements Dao {

    private static MongoClient mongoClient;
    private static final String DATABASE_NAME = "shop";
    private static final String COLLECTION_NAME = "inventory";
    private static final String HISTORICAL_COLLECTION_NAME = "historical_inventory";
    private static final String EMPLOYEES_COLLECTION_NAME = "employees";

    @Override
    public void connect() {
        if (mongoClient == null) { // Evita crear múltiples conexiones
            try {
                String uri = "mongodb://localhost:27017/";
                mongoClient = new MongoClient(new MongoClientURI(uri));
                System.out.println("Conexión correcta");
            } catch (Exception e) {
                e.printStackTrace();
                throw new ExceptionInInitializerError("Error al inicializar la conexión de MongoDB: " + e.getMessage());
            }
        }
    }

    @Override
    public void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null; 
        }
    }

    @Override
    public ArrayList<Product> getInventory() {
        ArrayList<Product> inventory = new ArrayList<>();

        connect();

        try {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            FindIterable<Document> resultList = collection.find();

            for (Document document : resultList) {
                String name = document.getString("name");

                Number wholesalerPriceValue = document.getEmbedded(List.of("wholesalerPrice", "value"), Number.class); //Lo hago asi para que convierta cualquier tipo de dato a Double
                double wholesalerPrice = wholesalerPriceValue != null ? wholesalerPriceValue.doubleValue() : 0.0;//Lo hago asi porque en mi base de datos tiene datos integer y mi constructor en
                																								//wholsomePrice pide un double
                Amount wholesalerPriceAmount = new Amount(wholesalerPrice);

                Boolean available = document.getBoolean("available");
                Integer stock = document.getInteger("stock");
                Integer id = document.getInteger("id");

                Product newProduct = new Product(
                    id,
                    name,
                    wholesalerPriceAmount,
                    available,
                    stock
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
    public Employee getEmployee(int employeeId, String password) {
        Employee employee = null;
        connect();

        try {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(EMPLOYEES_COLLECTION_NAME);

            Document query = new Document("employeeId", employeeId)
                                  .append("password", password);

            Document result = collection.find(query).first();

            if (result != null) {
                int id = result.getInteger("employeeId");
                String name = result.getString("name");

                employee = new Employee(name, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }

        return employee;
    }

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		connect();
	    try {
	        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
	        MongoCollection<Document> collection = database.getCollection(HISTORICAL_COLLECTION_NAME);
	        
	        long currentTime = System.currentTimeMillis();
	        Date fechaActual = new Date(currentTime);
	        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
	        String fechaFormateada = formato.format(fechaActual);
	        
	        Document lastProduct = collection.find()
	                .sort(Sorts.descending("id"))
	                .first();
	        int lastId = (lastProduct != null) ? lastProduct.getInteger("id") : 0;

	        ArrayList<Document> batch = new ArrayList<>();
	        
	        for (Product product : inventory) {
	            lastId++;
	            Document wholesalerPrice = new Document("value", product.getWholesalerPrice().getValue())
	                                      .append("currency", "€");

	            Document document = new Document("id", lastId)
	                    .append("name", product.getName())
	                    .append("wholesalerPrice", wholesalerPrice)
	                    .append("available", product.isAvailable())
	                    .append("stock", product.getStock())
	                    .append("created_at", fechaFormateada);
	            
	            batch.add(document);
	        }
	        
	        if (!batch.isEmpty()) {
	            collection.insertMany(batch);
	        }
	        
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

	    try {
	        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
	        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

	        Document lastProduct = collection.find()
	                .sort(Sorts.descending("id"))
	                .first();

	        int newId = (lastProduct != null) ? lastProduct.getInteger("id") + 1 : 1;

	        Document wholesalerPrice = new Document("value", product.getWholesalerPrice().getValue())
	                                  .append("currency", "€");

	        Document document = new Document("name", product.getName())
	                .append("wholesalerPrice", wholesalerPrice)
	                .append("available", product.isAvailable())
	                .append("stock", product.getStock())
	                .append("id", newId);

	        collection.insertOne(document);

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        disconnect();
	    }
	}



	@Override
	public void deleteProduct(String productName) {
	    connect();

	    try {
	        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME); 
	        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

	        Bson filter = Filters.eq("name", productName);

	        collection.deleteOne(filter);

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        disconnect();
	    }
	}

	@Override
	public void updateProduct(Product product) {   
	    connect();

	    try {
	        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
	        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

	        boolean isAvailable = product.getStock() > 0;

	        Bson filter = Filters.eq("name", product.getName());
	        Bson update = Updates.combine(
	            Updates.set("stock", product.getStock()),
	            Updates.set("available", isAvailable)
	        );
	        
	        collection.updateOne(filter, update);

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        disconnect();
	    }
	}
}
