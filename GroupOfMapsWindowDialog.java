package lab3.GUI;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import lab2.GUI.MapWindowDialog;
import lab2.Program.Maps;
import lab2.Program.MapsException;
import lab3.data.GroupOfMaps;
import lab3.GUI.GroupManagerApp;

public class GroupOfMapsWindowDialog extends JFrame implements ActionListener {
	
	private static final String GREETING_MESSAGE = 
			"Program do zarz¹dzania grupami map " + 
	        "- wersja okienkowa\n\n" + 
	        "Autor: Wiktor Syska\n" + 
			"Data:  Grudzieñ 2021 r.\n";


	
	
	public static GroupOfMaps createNewGroupOfMaps(Window parent)
	{	
		GroupOfMapsWindowDialog dialog = new GroupOfMapsWindowDialog();
		return dialog.createNewGroupOfMaps(parent);
	}
	
	private Maps currentMap;
	
	private static final long serialVersionUID = 1L;
	
	WindowAdapter windowListener = new WindowAdapter() 
	{
		
		@Override
		public void windowClosed(WindowEvent e) 
		{
			
			JOptionPane.showMessageDialog(null, "Program zakoñczy³ dzia³anie!");
			
		}


		@Override
		public void windowClosing(WindowEvent e)
		{
			// Wywo³ywane gdy okno aplikacji jest  zamykane za pomoc¹
			// systemowego menu okna tzn. krzy¿yk w naro¿niku)
			windowClosed(e);
		}

	};
	
	// Pasek menu wyœwietlany na panelu w g³ównym oknie aplikacji
	JMenuBar menuBar        = new JMenuBar();
	JMenu menuLists         = new JMenu("Lista map");
	JMenu menuSort          = new JMenu("Sortowanie");
	JMenu menuProperties    = new JMenu("W³aœciwoœci");
	JMenu menuAbout         = new JMenu("O programie");

	JMenuItem menuNewMap               = new JMenuItem("Dodaj now¹ mapê");
	JMenuItem menuEditMap              = new JMenuItem("Edytuj mapê");
	JMenuItem menuDeleteMap            = new JMenuItem("Usuñ mapê");
	JMenuItem menuLoadMap              = new JMenuItem("Wczytaj mapê z pliku");
	JMenuItem menuSaveMap              = new JMenuItem("Zapisz mapê do pliku");
	
	
	JMenuItem menuSortAlphabetically   = new JMenuItem("Sortuj alfabetycznie");
	JMenuItem menuSortHeight           = new JMenuItem("Sortuj wg. d³ugoœci");
	JMenuItem menuSortWidth            = new JMenuItem("Sortuj wg. szerokoœci");
	JMenuItem menuSortScale            = new JMenuItem("Sortuj wg. skali");
	JMenuItem menuSortType             = new JMenuItem("Sortuj wg. typu mapy");

	JMenuItem menuChangeName           = new JMenuItem("Zmieñ nazwê");
	JMenuItem menuChangeTypeCollection = new JMenuItem("Sortuj wg. typu mapy");
	
	JMenuItem menuAuthor               = new JMenuItem("Autor");
	
	// Przyciski wyœwietlane na panelu w g³ównym oknie aplikacji
	JButton buttonNewMap = new JButton("Dodaj now¹ mapê");
	JButton buttonEditMap = new JButton("Edytuj mapê");
	JButton buttonDeleteMap = new JButton("Usuñ mapê");
	JButton buttonLoadMap = new JButton("Wczytaj mapê z pliku");
	JButton buttonSaveMap = new JButton("Zapisz mapê do pliku");
	
	
	JTextField nameField = new JTextField(10);
	JTextField widthField = new JTextField(10);
	JTextField heightField = new JTextField(10);
	JTextField scaleField = new JTextField(10);
	JTextField typeField     = new JTextField(10);
	
	 ViewCurrentMap viewList;
	
	public GroupOfMapsWindowDialog() {
		setTitle("Modyfikacja grupy map");
		setSize(450, 400);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
	
		
		
		// Utworzenie i konfiguracja menu aplikacji
		setJMenuBar(menuBar);
		menuBar.add(menuLists);
		menuBar.add(menuSort);
		menuBar.add(menuProperties);
		menuBar.add(menuAbout);
		
		menuLists.add(menuNewMap);
		menuLists.add(menuEditMap);
		menuLists.add(menuDeleteMap);
		menuLists.addSeparator();
		menuLists.add(menuLoadMap);
		menuLists.add(menuSaveMap);
		
		menuSort.add(menuSortAlphabetically);
		menuSort.add(menuSortHeight);
		menuSort.add(menuSortWidth);
		menuSort.add(menuSortScale);
		menuSort.add(menuSortType);
		
		menuProperties.add(menuChangeName);
		menuProperties.add(menuChangeTypeCollection);
		
		menuAbout.add(menuAuthor);
		
		// Dodanie s³uchaczy zdarzeñ do wszystkich opcji menu.
		// UWAGA: s³uchaczem zdarzeñ bêdzie metoda actionPerformed
		// zaimplementowana w tej klasie i wywo³ana dla
		// bie¿¹cej instancji okna aplikacji - referencja this
		menuNewMap.addActionListener(this);
		menuEditMap.addActionListener(this);
		menuDeleteMap.addActionListener(this);
		menuLoadMap.addActionListener(this);
		menuSaveMap.addActionListener(this);
		
		menuAuthor.addActionListener(this);
		
		// Dodanie s³uchaczy zdarzeñ do wszystkich przycisków.
		// UWAGA: s³uchaczem zdarzeñ bêdzie metoda actionPerformed
		// zaimplementowana w tej klasie i wywo³ana dla
		// bie¿¹cej instancji okna aplikacji - referencja this
		buttonNewMap.addActionListener(this);
		buttonEditMap.addActionListener(this);
		buttonDeleteMap.addActionListener(this);
		buttonLoadMap.addActionListener(this);
		buttonSaveMap.addActionListener(this);
		
		

		// Utwotrzenie tabeli z list¹ osób nale¿¹cych do grupy
		viewList = new ViewCurrentMap(currentMap, 400, 250);
		viewList.refreshView();
		
		// Utworzenie g³ównego panelu okna aplikacji.
		// Domyœlnym mened¿erem rozk³adu dla panelu bêdzie
		// FlowLayout, który uk³ada wszystkie komponenty jeden za drugim.
		JPanel panel = new JPanel();

		// Dodanie i rozmieszczenie na panelu wszystkich
		// komponentów GUI.
		panel.add(viewList);
		panel.add(buttonNewMap);
		panel.add(buttonEditMap);
		panel.add(buttonDeleteMap);
		panel.add(buttonLoadMap);
		panel.add(buttonSaveMap);


		
		// Umieszczenie Panelu w g³ównym oknie aplikacji.
		setContentPane(panel);
			
		// Pokazanie na ekranie g³ównego okna aplikacji
		// UWAGA: T¹ instrukcjê nale¿y wykonaæ jako ostatni¹
		// po zainicjowaniu i rozmieszczeniu na panelu
		// wszystkich komponentów GUI.
		// Od tego momentu aplikacja uruchamia g³ówn¹ pêtlê zdarzeñ
		// która dzia³a w nowym w¹tku niezale¿nie od pozosta³ej czêœci programu.
		setVisible(true);
	}
	
	public GroupOfMapsWindowDialog(GroupManagerApp groupManagerApp, GroupOfMaps next) {
		// TODO Auto-generated constructor stub
	}

	
	
	
	void showCurrentMap() {
		if (currentMap == null) {
			nameField.setText("");
			widthField.setText("");
			heightField.setText("");
			scaleField.setText("");
			typeField.setText("");
		} else {
			nameField.setText(currentMap.getName());
			widthField.setText(""+currentMap.getWidth());
			heightField.setText("" + currentMap.getHeight());
			scaleField.setText("" + currentMap.getScale());
			typeField.setText(""+ currentMap.getType());
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		// Odczytanie referencji do obiektu, który wygenerowa³ zdarzenie.
		Object source = event.getSource();
		
		
		try {
			if (source == menuNewMap || source == buttonNewMap) {
			
				currentMap = MapWindowDialog.createNewMap(this);
				//currentList.add(currentMap);
			}
			
			if (source == menuEditMap || source == buttonEditMap) {
			
				int index = viewList.getSelectedIndex();
				if(index >= 0  ) {
				if (currentMap == null) throw new MapsException("¯adna mapa nie zosta³a utworzona.");
				MapWindowDialog.changeMapData(this, currentMap);
				}
				
			}
			
			if (source == menuDeleteMap || source == buttonDeleteMap) {

				currentMap = null;
			}
			
			if (source == menuLoadMap || source == buttonLoadMap) {
				
				String fileName = JOptionPane.showInputDialog("Podaj nazwê pliku");
				if (fileName == null || fileName.equals("")) return;  
				currentMap = Maps.readFromFile(fileName);
			}
			
			if (source == menuSaveMap || source == buttonSaveMap) {
				String fileName = JOptionPane.showInputDialog("Podaj nazwê pliku");
				if (fileName == null || fileName.equals("")) return;  
				Maps.printToFile(fileName, currentMap);
				
				}
			
			
			
			if (source == menuAuthor) {
				JOptionPane.showMessageDialog(this, GREETING_MESSAGE);
			}
			
		} catch (MapsException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
		}

		showCurrentMap();
		viewList.refreshView();
	}
}














class ViewCurrentMap extends JScrollPane {
private static final long serialVersionUID = 1L;
	
	private Collection<Maps> list;
	private JTable table;
	private DefaultTableModel tableModel;

	public ViewCurrentMap(Collection<Maps> list, int width, int height){
		this.list = list;
		setPreferredSize(new Dimension(width, height));
		setBorder(BorderFactory.createTitledBorder("Lista map:"));
		
		String[] tableHeader = { "Nazwa mapy", "Wysokoœæ", "Szerokoœæ", "Skala", "Typ mapy" };
		tableModel = new DefaultTableModel(tableHeader, 0);
		table = new JTable(tableModel) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false;
			}
		};
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		setViewportView(table);
	}
	
	void refreshView(){
		tableModel.setRowCount(0);
		for (Maps map : list) {
			if (map != null) {
				String[] row = { map.getName(),  "" + map.getHeight(), "" + map.getWidth(), "" + map.getScale(), map.getType().toString()};
				tableModel.addRow(row);
			}
		}
	}
	
	int getSelectedIndex(){
		int index = table.getSelectedRow();
		if (index<0) {
			JOptionPane.showMessageDialog(this, "¯adana mapa nie jest zaznaczona.", "B³¹d", JOptionPane.ERROR_MESSAGE);
		}
		return index;
	}

}
