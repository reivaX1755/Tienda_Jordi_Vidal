package view;

import java.awt.EventQueue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dao.Dao;
import exception.LimitLoginException;
import model.Employee;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

public class LoginView extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField password;
	private JTextField numEmpleado;
	private JLabel lblNewLabel_1;
	private int loginErrorCount = 0;
	private static final int MAX_LOGIN_ERRORS = 3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginView frame = new LoginView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginView() {
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Número de empleado");
		lblNewLabel.setBounds(62, 38, 158, 13);
		contentPane.add(lblNewLabel);
		
		numEmpleado = new JTextField();
		numEmpleado.setBounds(72, 61, 158, 19);
		contentPane.add(numEmpleado);
		numEmpleado.setColumns(10);
		
		lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setBounds(62, 131, 158, 13);
		contentPane.add(lblNewLabel_1);
		
		password = new JTextField();
		password.setBounds(72, 154, 158, 19);
		contentPane.add(password);
		password.setColumns(10);
		
		
		JButton btnNewButton = new JButton("Acceder");
		btnNewButton.setBounds(311, 193, 81, 32);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    String pass;
			    int employeeID = 0;

			    pass = password.getText();
			    String employeeIDText = numEmpleado.getText();
			    try {
			        employeeID = Integer.parseInt(employeeIDText);
			        Employee employee = new Employee("Paco", employeeID);
			        if (employee.login(employeeID, pass)) {
			            LoginView.this.setVisible(false);
			            ShopView shopView = new ShopView();
			            shopView.setVisible(true);
			        } else {
			            loginErrorCount++;
			            if (loginErrorCount >= MAX_LOGIN_ERRORS) {
			                throw new LimitLoginException("Se han superado los intentos de inicio de sesión.");
			            } else {
			                JOptionPane.showMessageDialog(LoginView.this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
			                password.setText("");
			                numEmpleado.setText("");}
			        }
			    } catch (NumberFormatException ex) {
			        JOptionPane.showMessageDialog(LoginView.this, "Por favor, ingrese un ID de empleado válido", "Error", JOptionPane.ERROR_MESSAGE);
			    } catch (LimitLoginException ex) {
			        JOptionPane.showMessageDialog(LoginView.this, "Se han superado los intentos login cerrando sesión... ", "Error", JOptionPane.ERROR_MESSAGE);
			        System.exit(0);
			    }   
			}
		});
		contentPane.add(btnNewButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}

