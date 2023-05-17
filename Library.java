package tarea10;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class Library extends JFrame {

	JPanel contentPane = new JPanel();
	JTable tabla;
	DefaultTableModel tablaLibros;
	
	public Library() {
		
		// Creamos el JFrame
		setTitle("Biblioteca");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 445, 511);
		setLocation(400,200);
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Creamos las etiquetas y campos de texto
		JLabel lbNombre = new JLabel("Nombre");
		lbNombre.setBounds(46, 33, 87, 23);
		contentPane.add(lbNombre);
		
		JTextField txNombre = new JTextField();
		txNombre.setBounds(163, 33, 204, 23);
		contentPane.add(txNombre);
		txNombre.setColumns(10);
		
		JLabel lbAutor = new JLabel("Autor");
		lbAutor.setBounds(46, 66, 87, 23);
		contentPane.add(lbAutor);
		
		JTextField txAutor = new JTextField();
		txAutor.setColumns(10);
		txAutor.setBounds(163, 66, 204, 23);
		contentPane.add(txAutor);
		
		JLabel lbEditorial = new JLabel("Editorial");
		lbEditorial.setBounds(46, 99, 87, 23);
		contentPane.add(lbEditorial);
		
		JTextField txEditorial = new JTextField();
		txEditorial.setColumns(10);
		txEditorial.setBounds(163, 99, 204, 23);
		contentPane.add(txEditorial);
		
		JLabel lbFecha = new JLabel("Fecha publicacion");
		lbFecha.setBounds(46, 132, 107, 23);
		contentPane.add(lbFecha);
		
		JTextField txFecha = new JTextField();
		txFecha.setColumns(10);
		txFecha.setBounds(163, 132, 204, 23);
		contentPane.add(txFecha);
		
		JLabel lbIsbn = new JLabel("ISBN");
		lbIsbn.setBounds(46, 165, 87, 23);
		contentPane.add(lbIsbn);
		
		JTextField txIsbn = new JTextField();
		txIsbn.setColumns(10);
		txIsbn.setBounds(163, 165, 204, 23);
		contentPane.add(txIsbn);
		
		
		// Añadimos la tabla
		crearTabla();
		
		
		// Creamos los botones y sus eventos
		JButton btNuevo = new JButton("Nuevo");
		btNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txNombre.setText("");
				txAutor.setText("");
				txEditorial.setText("");
				txFecha.setText("");
				txIsbn.setText("");
			}
		});
		btNuevo.setBounds(46, 443, 85, 21);
		contentPane.add(btNuevo);
		
		JButton btGuardar = new JButton("Guardar");
		btGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Conectamos con la BD e insertamos los datos introducidos
				Connection con;
				try {
					con = DriverManager.getConnection ("jdbc:mysql://localhost:3306/biblioteca", "root", "");
					Statement stmt = con.createStatement();
					stmt.executeUpdate("INSERT INTO LIBRO VALUES ('"+txNombre.getText()+"', '"+txAutor.getText()+"', '"+txEditorial.getText()+"', '"+txFecha.getText()+"', '"+txIsbn.getText()+"');");
					Object[] nuevaFila = {txNombre.getText(),txAutor.getText(), txEditorial.getText(), txFecha.getText(), txIsbn.getText()};
					tablaLibros.addRow(nuevaFila);
					txNombre.setText("");
					txAutor.setText("");
					txEditorial.setText("");
					txFecha.setText("");
					txIsbn.setText("");
					con.close();
					stmt.close();
					
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
			}
			
		});
		btGuardar.setBounds(141, 443, 85, 21);
		contentPane.add(btGuardar);
		
		JButton btSalir = new JButton("Salir");
		btSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(EXIT_ON_CLOSE);
			}
		});
		btSalir.setBounds(316, 443, 85, 21);
		contentPane.add(btSalir);
		
		setVisible(true);
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Library a = new Library();
	}
	
	
	public void crearTabla() {
			// Creamos y añadimos la tabla
			Connection con;		
			Object[] nombrecolumnas = {"NOMBRE", "AUTOR", "EDITORIAL", "FECHA_PUBLICACION", "ISBN"};
			tablaLibros = new DefaultTableModel(nombrecolumnas, 0);
			tabla = new JTable(tablaLibros);
			tabla.setBounds(10, 226, 411, 167);
			try {
				con = DriverManager.getConnection ("jdbc:mysql://localhost:3306/biblioteca", "root", "");
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery ("SELECT NOMBRE, AUTOR, EDITORIAL, FECHA_PUBLICACION, ISBN FROM LIBRO");
				while (rs.next()) {
					
					String[] datos = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5) } ;
					tablaLibros.addRow(datos);
				}
				
				
				// Creamos el menú de modificar/borrar que se active cuando seleccionamos una fila y le damos al boton derecho
				tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			        public void valueChanged(ListSelectionEvent event) {

			        	   tabla.addMouseListener(new MouseAdapter() {
			        		    public void mouseClicked(MouseEvent e) {	
			        		     if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1){
			        		    	// Menú modificar/borrar
			        		        JPopupMenu menu = new JPopupMenu("Menu"); 
			   		        		JMenuItem Modificar = new JMenuItem("Modificar");
			   		        		JMenuItem Borrar = new JMenuItem("Borrar");
			   		        		menu.add(Modificar); 
			   		        		menu.add(Borrar); 
			   		        		menu.show(tabla , e.getX(), e.getY());
			   		        		
			   		        		// Evento borrar
			   		        		Borrar.addActionListener(new ActionListener() {
			   		   	            public void actionPerformed(ActionEvent e) {
			   		   	            	try {
			   								Connection con = DriverManager.getConnection ("jdbc:mysql://localhost:3306/biblioteca", "root", "");
			   								Statement stmt = con.createStatement();
			   								String a = tabla.getValueAt(tabla.getSelectedRow(), tabla.getSelectedColumn()).toString();
			   								String b = tabla.getColumnName(tabla.getSelectedColumn());
			   								stmt.executeUpdate("DELETE FROM LIBRO WHERE "+b+" = '"+a+"' ");
			   								tablaLibros.removeRow(tabla.getSelectedRow());
			   							} catch (SQLException e1) {
			   								// TODO Auto-generated catch block
			   								e1.printStackTrace();
			   							}
			   		   	            	
			   		   	            	}
			   		        	   });
			   		        		
			   		        		// Evento modificar
			   		        		Modificar.addActionListener(new ActionListener() {
				   		   	            public void actionPerformed(ActionEvent e) {
				   		   	            	//*No me ha dado tiempo a completar esta parte pero mi intencion era mostrar otra ventana 
				   		   	            	//donde rellenar los campos y despues un boton (similar al de guardar) que mandase
				   		   	            	//una consulta UPDATE a la BD y otro boton "cancelar" que cerrase la ventana.
				   		   	            }
			   		        		});
			   		        		
			        		     }        
			        		    }               
			        		   });
			        }
			    });

				JScrollPane scrollPane= new  JScrollPane(tabla);
				scrollPane.setBounds(10, 226, 411, 167);
				contentPane.add(scrollPane);
				
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
	}	

}
