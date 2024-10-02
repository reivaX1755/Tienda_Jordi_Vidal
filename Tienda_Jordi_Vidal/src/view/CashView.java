package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Shop;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CashView extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField dineroencajanoeditable;
	private Shop shop;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Shop shop = new Shop();
			CashView dialog = new CashView(shop);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public CashView(Shop shop) {
		this.shop = shop;
		setTitle("Caja");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Dinero en caja:");
			lblNewLabel.setBounds(117, 82, 156, 13);
			contentPanel.add(lblNewLabel);
		}
		dineroencajanoeditable = new JTextField();
		dineroencajanoeditable.setBounds(177, 105, 96, 19);
		dineroencajanoeditable.setEditable(false);
		contentPanel.add(dineroencajanoeditable);
		dineroencajanoeditable.setColumns(10);
		double valorCash = shop.showCash();
		String valorCashStr = valorCash + "€";
		dineroencajanoeditable.setText(valorCashStr);
		{
			JButton okButton = new JButton("OK");
			okButton.setBounds(285, 154, 55, 27);
			contentPanel.add(okButton);
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					CashView.this.setVisible(false);
				}
			});
			okButton.setActionCommand("OK");
			getRootPane().setDefaultButton(okButton);
		}
	}
}
