package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Shop;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;

public class ShopView extends JFrame implements ActionListener, KeyListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Shop shop;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ShopView frame = new ShopView();
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
	public ShopView() {
		
		shop = new Shop();
		shop.inventory = shop.dao.getInventory();
		setTitle("MiTienda.com");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		JLabel lblNewLabel = new JLabel("Mi Tienda.com");
		lblNewLabel.setBounds(163, 10, 121, 13);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Pulse o seleccione una opción:");
		lblNewLabel_1.setBounds(109, 33, 190, 13);
		contentPane.add(lblNewLabel_1);
		
		JButton contarcaja = new JButton("1. Contar Caja");
		contarcaja.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opcion = 1;
	            CashView cashView = new CashView(shop);
	            cashView.setVisible(true);
			}
		});
		contarcaja.setBounds(127, 93, 157, 21);
		contentPane.add(contarcaja);
		contarcaja.addKeyListener(this);
		
		JButton añadirproducto = new JButton("2. Añadir producto");
		añadirproducto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opcion = 2;
	            ProductView productView = new ProductView(shop, opcion);
	            productView.setVisible(true);
			}
		});
		añadirproducto.setBounds(127, 124, 157, 21);
		contentPane.add(añadirproducto);
		añadirproducto.addKeyListener(this);
		
		JButton añadirstock = new JButton("3. Añadir Stock");
		añadirstock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opcion = 3;
	            ProductView productView = new ProductView(shop, opcion);
	            productView.setVisible(true);
			}
		});
		añadirstock.setBounds(127, 155, 157, 21);

		contentPane.add(añadirstock);
		añadirstock.addKeyListener(this);
		
		JButton eliminarproducto = new JButton("9. Eliminar Producto");
		eliminarproducto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opcion = 9;
	            ProductView productView = new ProductView(shop, opcion);
	            productView.setVisible(true);
			}
		});

		eliminarproducto.setBounds(127, 217, 157, 21);
		contentPane.add(eliminarproducto);
		
		JButton showInventory = new JButton("5. Mostrar Inventario");
		showInventory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opcion = 5;
				shop.showInventory();
			}
		});

		showInventory.setBounds(127, 186, 157, 21);
		contentPane.add(showInventory);
		
		JButton Exportar = new JButton("0. Exportar Inventario");
		Exportar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				writeInventory(shop);
			}
		});
		Exportar.setBounds(125, 62, 159, 21);
		contentPane.add(Exportar);
		showInventory.addKeyListener(this);
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		char key = e.getKeyChar();
		switch(key) {
		case '0':
			int opcion = 0;
			writeInventory(shop);
            break;
		case '1':
			opcion = 1;
            CashView cashView = new CashView(shop);
            cashView.setVisible(true);
            break;
		case '2':
			opcion = 2;
            ProductView productView = new ProductView(shop, opcion);
            productView.setVisible(true);
            break;
		case '3':
			opcion = 3;
            productView = new ProductView(shop, opcion);
            productView.setVisible(true);
            break;
		case '5':
			opcion = 5;
			shop.showInventory();
            break;
		case '9':
			opcion = 9;
            productView = new ProductView(shop, opcion);
            productView.setVisible(true);
            break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void writeInventory(Shop shop) {
		boolean created = shop.dao.writeInventory(shop.inventory);
		if(created == true) {
			JOptionPane.showMessageDialog(null, "Archivo Creado con exito", "Info", JOptionPane.INFORMATION_MESSAGE);
		}else {
			JOptionPane.showMessageDialog(null, "ERROR al crear el archivo", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
