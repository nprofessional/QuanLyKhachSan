package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.toedter.calendar.JDateChooser;

import database.MySqlDB;
import database.Sql;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.MaterialLiteTheme;

public class FrmEmployeeController extends JFrame {

	private JPanel contentPane;
	private JTextField txtCode;
	private JTextField txtFullName;
	private JTextField txtIdentityCard;
	private JTextField txtAddress;
	private JTextField txtPhone;
	private JDateChooser dateBirth;
	private JComboBox cboStatus;
	private JTextField txtEmail;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmEmployeeController frame = new FrmEmployeeController();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * loadData
	 */
	public void loadData() {
		txtCode.setText("");
		txtFullName.setText("");
		dateBirth.setDate(null);
		txtEmail.setText("");
		cboStatus.setSelectedIndex(0);
		txtPhone.setText("");
		txtIdentityCard.setText("");
		txtAddress.setText("");
		DefaultTableModel dataModel = new DefaultTableModel();
		dataModel.setColumnIdentifiers(new String[] { "Mã nhân viên", "Họ tên", "Ngày sinh", "Email", "Trạng thái",
				"Số điện thoại", "CMND", "Địa chỉ" });
		table.setModel(dataModel);
		try {
			Connection conn = new MySqlDB().getConnection();
			ResultSet rows = MySqlDB.executeQuery(conn, Sql.selectAllEmployee());
			while (rows.next()) {
				dataModel.addRow(new Object[] { rows.getString("code"), rows.getString("fullname"),
						rows.getString("birth"), rows.getString("email"), rows.getString("status"),
						rows.getString("phone"), rows.getString("identity_card"), rows.getString("address") });
			}
			conn.close();
		} catch (Exception e) {

		}
	}

	/**
	 * formWindowOpened
	 * 
	 * @param event
	 */
	public void formWindowOpened(WindowEvent event) {
		loadData();
	}

	/**
	 * rowMouseClicked
	 * 
	 * @param event
	 * @throws ParseException
	 */
	public void rowMouseClicked(MouseEvent event) throws ParseException {
		DefaultTableModel dataModel = (DefaultTableModel) table.getModel();
		txtCode.setText(dataModel.getValueAt(table.getSelectedRow(), 0).toString());
		txtFullName.setText(dataModel.getValueAt(table.getSelectedRow(), 1).toString());
		dateBirth.setDate(
				new SimpleDateFormat("yyyy-MM-dd").parse(dataModel.getValueAt(table.getSelectedRow(), 2).toString()));
		txtEmail.setText(dataModel.getValueAt(table.getSelectedRow(), 3).toString());
		if (dataModel.getValueAt(table.getSelectedRow(), 4).toString().compareTo("Inactive") == 0) {
			cboStatus.setSelectedIndex(0);
		} else {
			cboStatus.setSelectedIndex(1);
		}
		txtPhone.setText(dataModel.getValueAt(table.getSelectedRow(), 5).toString());
		txtIdentityCard.setText(dataModel.getValueAt(table.getSelectedRow(), 6).toString());
		txtAddress.setText(dataModel.getValueAt(table.getSelectedRow(), 7).toString());
	}

	/**
	 * btnReloadClick
	 * 
	 * @param actionEvent
	 */
	public void btnReloadClick(ActionEvent actionEvent) {
		loadData();
	}

	/**
	 * btnAddClick
	 * 
	 * @param actionEvent
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void btnAddClick(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
		String[] params = new String[] { txtCode.getText(), String.valueOf(cboStatus.getSelectedIndex()),
				txtFullName.getText(), txtIdentityCard.getText(), txtAddress.getText(), txtPhone.getText(),
				txtEmail.getText(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateBirth.getDate()) };
		Connection conn = new MySqlDB().getConnection();
		MySqlDB.executeUpdate(conn, Sql.insertEmployee(), params);
		MySqlDB.executeUpdate(conn, Sql.insertAccount(), new String[] {txtCode.getText(), txtCode.getText()});
		conn.close();
		loadData();
	}

	/**
	 * btnUpdateClick
	 * 
	 * @param actionEvent
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void btnUpdateClick(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
		String[] params = new String[] { String.valueOf(cboStatus.getSelectedIndex()), txtFullName.getText(),
				txtIdentityCard.getText(), txtAddress.getText(), txtPhone.getText(), txtEmail.getText(),
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateBirth.getDate()), txtCode.getText() };
		Connection conn = new MySqlDB().getConnection();
		MySqlDB.executeUpdate(conn, Sql.updateEmployee(), params);
		conn.close();
		loadData();
	}

	/**
	 * btnRemoveClick
	 * 
	 * @param actionEvent
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void btnRemoveClick(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
		Connection conn = new MySqlDB().getConnection();
		MySqlDB.executeUpdate(conn, Sql.deleteEmployee(), new String[] { txtCode.getText() });
		conn.close();
		loadData();
	}

	/**
	 * Create the frame.
	 */
	public FrmEmployeeController() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Quản lý khách sạn | Cài đặt thông tin nhân viên");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				formWindowOpened(arg0);
			}

			@Override
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				FrmDashBoard frmDashBoard = new FrmDashBoard();
				frmDashBoard.setVisible(true);
			}
		});
		try {
			UIManager.setLookAndFeel(new MaterialLookAndFeel());
			if (UIManager.getLookAndFeel() instanceof MaterialLookAndFeel) {
				MaterialLookAndFeel.changeTheme(new MaterialLiteTheme());
			}
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		setBounds(0, 0, 800, 600);
		setMinimumSize(getSize());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JPanel panel = new JPanel();

		JPanel panel_1 = new JPanel();

		JPanel panel_2 = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
				.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
				.addComponent(panel_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE));
		gl_contentPane
				.setVerticalGroup(
						gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(Alignment.LEADING,
										gl_contentPane.createSequentialGroup()
												.addComponent(panel, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(panel_1, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(panel_2,
														GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)));
		panel_2.setLayout(new BorderLayout(0, 0));
		table = new JTable() {
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					rowMouseClicked(arg0);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		});
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] {}));
		panel_2.add(table, BorderLayout.CENTER);
		panel_2.add(table.getTableHeader(), BorderLayout.NORTH);
		panel_1.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					btnAddClick(arg0);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			}
		});
		panel_1.add(btnAdd, "2, 2");

		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					btnUpdateClick(arg0);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			}
		});
		panel_1.add(btnUpdate, "4, 2");

		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					btnRemoveClick(arg0);
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			}
		});
		panel_1.add(btnRemove, "6, 2");

		JButton btnReload = new JButton("Reload");
		btnReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnReloadClick(arg0);
			}
		});
		panel_1.add(btnReload, "8, 2");
		panel.setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, }));

		JLabel lblNewLabel = new JLabel("Mã nhân viên");
		panel.add(lblNewLabel, "2, 2, right, default");

		txtCode = new JTextField();
		txtCode.setFont(new Font("Arial", Font.PLAIN, 16));
		panel.add(txtCode, "4, 2, fill, default");
		txtCode.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Họ tên");
		panel.add(lblNewLabel_1, "2, 4, right, default");

		txtFullName = new JTextField();
		txtFullName.setFont(new Font("Arial", Font.PLAIN, 16));
		panel.add(txtFullName, "4, 4, fill, default");
		txtFullName.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Ngày sinh");
		panel.add(lblNewLabel_2, "2, 6, right, default");

		dateBirth = new JDateChooser();
		dateBirth.setFont(new Font("Arial", Font.PLAIN, 16));
		panel.add(dateBirth, "4, 6, fill, default");

		JLabel lblNewLabel_3 = new JLabel("Email");
		panel.add(lblNewLabel_3, "2, 8, right, default");

		txtEmail = new JTextField();
		txtEmail.setFont(new Font("Arial", Font.PLAIN, 16));
		panel.add(txtEmail, "4, 8, fill, default");
		txtEmail.setColumns(10);

		JLabel lblNewLabel_4 = new JLabel("Trạng thái");
		panel.add(lblNewLabel_4, "2, 10, right, default");

		cboStatus = new JComboBox(new String[] { "Inactive", "Active" });
		cboStatus.setFont(new Font("Arial", Font.PLAIN, 16));
		panel.add(cboStatus, "4, 10, fill, default");

		JLabel lblNewLabel_5 = new JLabel("Số điện thoại");
		panel.add(lblNewLabel_5, "2, 12, right, default");

		txtPhone = new JTextField();
		txtPhone.setFont(new Font("Arial", Font.PLAIN, 16));
		panel.add(txtPhone, "4, 12, fill, default");
		txtPhone.setColumns(10);

		JLabel lblNewLabel_6 = new JLabel("CMND");
		panel.add(lblNewLabel_6, "2, 14, right, default");

		txtIdentityCard = new JTextField();
		txtIdentityCard.setFont(new Font("Arial", Font.PLAIN, 16));
		panel.add(txtIdentityCard, "4, 14, fill, default");
		txtIdentityCard.setColumns(10);

		JLabel lblNewLabel_7 = new JLabel("Địa chỉ");
		panel.add(lblNewLabel_7, "2, 16, right, default");

		txtAddress = new JTextField();
		txtAddress.setFont(new Font("Arial", Font.PLAIN, 16));
		panel.add(txtAddress, "4, 16, fill, default");
		txtAddress.setColumns(10);

		contentPane.setLayout(gl_contentPane);
	}

}
