package model;

import dao.Dao;
import dao.DaoImplJDBC;
import main.Logable;

public class Employee extends Person implements Logable{
	
	int employeeID;
	Dao dao;
	//final static int USER = 123;
	//final static String PASSWORD = "test";
	
	public Employee(String name, int employeeID) {
		super(name);
		this.employeeID = employeeID;
		this.dao = new DaoImplJDBC();
	}
	@Override
	public boolean login(int user, String password) {
        boolean isLogged = false;
        dao.connect();
        Employee employeeConsulta = dao.getEmployee(employeeID, password);
        if(employeeConsulta != null) {
        	isLogged = true;
        	System.out.println("Inicio de sesión correcto!");
        }
        dao.disconnect();
    	return isLogged;
        //if (user == USER && password.equals(PASSWORD)) { isLogged = true; System.out.println("Inicio de sesión correcto!"); }
    }
}
