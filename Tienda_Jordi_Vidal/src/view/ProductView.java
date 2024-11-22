package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Shop;
import model.Amount;
import model.Product;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ProductView extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField nombreproducto;
	private JTextField stockproducto;
	private JTextField precioproducto;
	private Shop shop;
	private static int opcion;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Shop shop = new Shop();
			ProductView dialog = new ProductView(shop, opcion);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ProductView(Shop shop, int opcion) {
		setTitle("Añadir Producto");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Nombre del producto: ");
		lblNewLabel.setBounds(73, 57, 143, 13);
		contentPanel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Stock del producto:");
		lblNewLabel_1.setBounds(73, 97, 112, 13);
		contentPanel.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Precio del producto:");
		lblNewLabel_2.setBounds(73, 139, 123, 13);
		contentPanel.add(lblNewLabel_2);

		nombreproducto = new JTextField();
		nombreproducto.setBounds(226, 54, 96, 19);
		contentPanel.add(nombreproducto);
		nombreproducto.setColumns(10);

		stockproducto = new JTextField();
		stockproducto.setBounds(226, 94, 96, 19);
		contentPanel.add(stockproducto);
		stockproducto.setColumns(10);

		precioproducto = new JTextField();
		precioproducto.setBounds(226, 136, 96, 19);
		contentPanel.add(precioproducto);
		precioproducto.setColumns(10);
		switch(opcion) {
		case 3:
			setTitle("Añadir Stock");
			lblNewLabel_2.setVisible(false);
			precioproducto.setVisible(false);
			break;
		case 9:
			setTitle("Eliminar Producto");
			lblNewLabel_2.setVisible(false);
			precioproducto.setVisible(false);
			lblNewLabel_1.setVisible(false);
			stockproducto.setVisible(false);
			break;
		}		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						switch(opcion) {
						case 2:
							String name = nombreproducto.getText();
							String stocktext = stockproducto.getText();
							String wholesalerPricetext = precioproducto.getText();
							double wholesalerPrice = Double.parseDouble(wholesalerPricetext);
							int stock = Integer.parseInt(stocktext);
							boolean productoExistente = false;
							for (Product product : shop.inventory) {
							    if (product.getName().equalsIgnoreCase(name)) {
							    	JOptionPane.showMessageDialog(null, "ERROR: Este producto ya existe", "Error", JOptionPane.ERROR_MESSAGE);
							    	productoExistente = true;
							        break; 
							    }
							}
							if (!productoExistente) {
								shop.inventory.add(new Product(name, new Amount(wholesalerPrice), true, stock));
							    JOptionPane.showMessageDialog(null, "Producto añadido con éxito!", "Añadir Producto", JOptionPane.INFORMATION_MESSAGE);
							    for (Product product : shop.inventory) {
									if (product != null) {
										System.out.println("Nombre: "+product.getName()+" // Id: "+product.getId()+" // Precio Proveedor Unidad: "+product.getWholesalerPrice()
										+" // Precio Venta Cliente Unidad: "+product.getPublicPrice()+" // Stock: "+product.getStock()+" // isAvailable: "+product.isAvailable());
									}
								}
							    ProductView.this.setVisible(false);
							}
							break;
						case 3:
							name = nombreproducto.getText();
							stocktext = stockproducto.getText();
							stock = Integer.parseInt(stocktext);
							Product product = shop.findProduct(name);

							if (product != null) {
								product.setStock((product.getStock() + stock));
								if (product.getStock() > 0) {
					                product.setAvailable(true);
					            }
								JOptionPane.showMessageDialog(null, "El stock del producto " + name + " ha sido actualizado a " + product.getStock(), "Añadir Stock", JOptionPane.INFORMATION_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(null, "ERROR: No se ha encontrado el producto con nombre " + name, "Error", JOptionPane.ERROR_MESSAGE);
							}
							break;
						case 9:
							productoExistente = false;
							name = nombreproducto.getText();
							boolean existe = false;
							for (int i = 0; i < shop.inventory.size(); i++) {
								Product product2 = shop.inventory.get(i);
								if (product2 != null) {
									if (product2.getName().equalsIgnoreCase(name)) {
										shop.inventory.remove(i);
										existe = true;
										JOptionPane.showMessageDialog(null, "El producto llamado: " + name + " ha sido eliminado con exito! ", "Eliminar producto", JOptionPane.INFORMATION_MESSAGE);
										for (Product product3 : shop.inventory) {
									    	System.out.println("Nombre: "+product3.getName()+" // Id: "+product3.getId()+" // Precio Proveedor Unidad: "+product3.getWholesalerPrice()
											+" // Precio Venta Cliente Unidad: "+product3.getPublicPrice()+" // Stock: "+product3.getStock()+" // isAvailable: "+product3.isAvailable());
									    }
										break;
									}
								}
							}
							if (!existe) {
								JOptionPane.showMessageDialog(null, "ERROR: No se ha encontrado el producto con nombre " + name, "Error", JOptionPane.ERROR_MESSAGE);
							}
							break;
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ProductView.this.setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
