package lab3.GUI;

import java.awt.Dimension;
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
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import lab3.data.GroupOfMaps;
import lab2.Program.MapsException;


public class GroupManagerApp extends JFrame implements ActionListener 
{

	private static final long serialVersionUID = 1L;

	private static final String GREETING_MESSAGE = 
			"Program do zarz�dzania grupami map " + 
	        "- wersja okienkowa\n\n" + 
	        "Autor: Wiktor Syska\n" + 
			"Data:  Grudzie� 2021 r.\n";

	private static final String ALL_GROUPS_FILE = "LISTA_GRUP.BIN"; 
	
	public static void main(String[] args)
	{	
		new GroupManagerApp();
	}

	
	WindowAdapter windowListener = new WindowAdapter() 
	{
		
		@Override
		public void windowClosed(WindowEvent e) 
		{
			
			JOptionPane.showMessageDialog(null, "Program zako�czy� dzia�anie!");
			
		}


		@Override
		public void windowClosing(WindowEvent e)
		{
			// Wywo�ywane gdy okno aplikacji jest  zamykane za pomoc�
			// systemowego menu okna tzn. krzy�yk w naro�niku)
			windowClosed(e);
		}

	};
	
	
	
	// Zbi�r grup os�b, kt�rymi zarz�dza aplikacja
	private List<GroupOfMaps> currentList = new ArrayList<GroupOfMaps>();
	
	// Pasek menu wy�wietlany na panelu w g��wnym oknie aplikacji
	JMenuBar menuBar        = new JMenuBar();
	JMenu menuGroups        = new JMenu("Grupy");
	JMenu menuAbout         = new JMenu("O programie");
	
	// Opcje wy�wietlane na panelu w g��wnym oknie aplikacji
	JMenuItem menuNewGroup           = new JMenuItem("Utw�rz grup�");
	JMenuItem menuEditGroup          = new JMenuItem("Edytuj grup�");
	JMenuItem menuDeleteGroup        = new JMenuItem("Usu� grup�");
	JMenuItem menuLoadGroup          = new JMenuItem("za�aduj grup� z pliku");
	JMenuItem menuSaveGroup          = new JMenuItem("Zapisz grup� do pliku");
	


	JMenuItem menuAuthor             = new JMenuItem("Autor");
	
	// Przyciski wy�wietlane na panelu w g��wnym oknie aplikacji
	JButton buttonNewGroup = new JButton("Utw�rz");
	JButton buttonEditGroup = new JButton("Edytuj");
	JButton buttonDeleteGroup = new JButton(" Unu� ");
	JButton buttonLoadGroup = new JButton("Otw�rz");
	JButton buttonSavegroup = new JButton("Zapisz");


			
	// Widok tabeli z list� grup wy�wietlany 
	// na panelu w oknie g��wnym aplikacji
	ViewGroupList viewList;


	public GroupManagerApp() 
	{
		// Konfiguracja parametr�w g��wnego okna aplikacji
		setTitle("GroupManager - zarz�dzanie grupami map");
		setSize(450, 400);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() 
		{
			// To jest definicja anonimowej klasy (klasy bez nazwy)
			// kt�ra dziedziczy po klasie WindowAdapter i przedefiniowuje
			// metody windowClosed oraz windowClosing.

			@Override
			public void windowClosed(WindowEvent event) 
			{
				// Wywo�ywane gdy okno aplikacji jest zamykane za pomoc�
				// wywo�ania metody dispose()
				try 
				{
					saveGroupListToFile(ALL_GROUPS_FILE);
					JOptionPane.showMessageDialog(null, "Dane zosta�y zapisane do pliku " + ALL_GROUPS_FILE);
				} 
				catch (MapsException e)
				{
					JOptionPane.showMessageDialog(null, e.getMessage(), "B��d", JOptionPane.ERROR_MESSAGE);
				}
			}

			@Override
			public void windowClosing(WindowEvent e) 
			{
				windowClosed(e);
			}

		} // koniec klasy anonimowej
		); // koniec wywo�ania metody addWindowListener

		try 
			{
				loadGroupListFromFile(ALL_GROUPS_FILE);
				JOptionPane.showMessageDialog(null, "Dane zosta�y wczytane z pliku " + ALL_GROUPS_FILE);
			} 
		catch (MapsException e) 
			{
				JOptionPane.showMessageDialog(null, e.getMessage(), "B��d", JOptionPane.ERROR_MESSAGE);
			}		
		
	
		// Utworzenie i konfiguracja menu aplikacji
		setJMenuBar(menuBar);
		menuBar.add(menuGroups);
		menuBar.add(menuAbout);
		
		menuGroups.add(menuNewGroup);
		menuGroups.add(menuEditGroup);
		menuGroups.add(menuDeleteGroup);
		menuGroups.addSeparator();
		menuGroups.add(menuLoadGroup);
		menuGroups.add(menuSaveGroup);
		

		
		menuAbout.add(menuAuthor);
		
		// Dodanie s�uchaczy zdarze� do wszystkich opcji menu.
		// UWAGA: s�uchaczem zdarze� b�dzie metoda actionPerformed
		// zaimplementowana w tej klasie i wywo�ana dla
		// bie��cej instancji okna aplikacji - referencja this
		menuNewGroup.addActionListener(this);
		menuEditGroup.addActionListener(this);
		menuDeleteGroup.addActionListener(this);
		menuLoadGroup.addActionListener(this);
		menuSaveGroup.addActionListener(this);

		menuAuthor.addActionListener(this);
		
		// Dodanie s�uchaczy zdarze� do wszystkich przycisk�w.
		// UWAGA: s�uchaczem zdarze� b�dzie metoda actionPerformed
		// zaimplementowana w tej klasie i wywo�ana dla
		// bie��cej instancji okna aplikacji - referencja this
		buttonNewGroup.addActionListener(this);
		buttonEditGroup.addActionListener(this);
		buttonDeleteGroup.addActionListener(this);
		buttonLoadGroup.addActionListener(this);
		buttonSavegroup.addActionListener(this);

		// Utwotrzenie tabeli z list� os�b nale��cych do grupy
		viewList = new ViewGroupList(currentList, 400, 250);
		viewList.refreshView();
		
		// Utworzenie g��wnego panelu okna aplikacji.
		// Domy�lnym mened�erem rozk�adu dla panelu b�dzie
		// FlowLayout, kt�ry uk�ada wszystkie komponenty jeden za drugim.
		JPanel panel = new JPanel();

		// Dodanie i rozmieszczenie na panelu wszystkich
		// komponent�w GUI.
		panel.add(viewList);
		panel.add(buttonNewGroup);
		panel.add(buttonEditGroup);
		panel.add(buttonDeleteGroup);
		panel.add(buttonLoadGroup);
		panel.add(buttonSavegroup);


		// Umieszczenie Panelu w g��wnym oknie aplikacji.
		setContentPane(panel);
			
		// Pokazanie na ekranie g��wnego okna aplikacji
		// UWAGA: T� instrukcj� nale�y wykona� jako ostatni�
		// po zainicjowaniu i rozmieszczeniu na panelu
		// wszystkich komponent�w GUI.
		// Od tego momentu aplikacja uruchamia g��wn� p�tl� zdarze�
		// kt�ra dzia�a w nowym w�tku niezale�nie od pozosta�ej cz�ci programu.
		setVisible(true);
	}
	
	
	
	@SuppressWarnings("unchecked")
	void loadGroupListFromFile(String file_name) throws MapsException 
	{
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file_name))) 
			{
				currentList = (List<GroupOfMaps>)in.readObject();
			} 
		catch (FileNotFoundException e) 
			{
				throw new MapsException("Nie odnaleziono pliku " + file_name);
			} 
		catch (Exception e) 
			{
				throw new MapsException("Wyst�pi� b��d podczas odczytu danych z pliku.");
			}
	}
	
	
	void saveGroupListToFile(String file_name) throws MapsException 
	{
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file_name)))
		{
			out.writeObject(currentList);
		} catch (FileNotFoundException e) 
		{
			throw new MapsException("Nie odnaleziono pliku " + file_name);
		} catch (IOException e)
		{
			throw new MapsException("Wyst�pi� b��d podczas zapisu danych do pliku.");
		}
	}
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent event) {
		// Odczytanie referencji do obiektu, kt�ry wygenerowa� zdarzenie.
		Object source = event.getSource();
		
		try {
			if (source == menuNewGroup || source == buttonNewGroup) {
				GroupOfMaps group = GroupOfMapsWindowDialog.createNewGroupOfMaps(this);
				if (group != null) {
					currentList.add(group);
				}
			}
			
			if (source == menuEditGroup || source == buttonEditGroup) {
				int index = viewList.getSelectedIndex();
				if (index >= 0) {
					Iterator<GroupOfMaps> iterator = currentList.iterator();
					while (index-- > 0)
						iterator.next();
					new GroupOfMapsWindowDialog(this, iterator.next());
				}
			}
			
			if (source == menuDeleteGroup || source == buttonDeleteGroup) {
				int index = viewList.getSelectedIndex();
				if (index >= 0) {
					Iterator<GroupOfMaps> iterator = currentList.iterator();
					while (index-- >= 0)
						iterator.next();
					iterator.remove();
				}
			}
			
			if (source == menuLoadGroup || source == buttonLoadGroup) {
				JFileChooser chooser = new JFileChooser(".");
				int returnVal = chooser.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					GroupOfMaps group = GroupOfMaps.readFromFile(chooser.getSelectedFile().getName());
					currentList.add(group);
				}
			}
			
			if (source == menuSaveGroup || source == buttonSavegroup) {
				int index = viewList.getSelectedIndex();
				if (index >= 0) {
					Iterator<GroupOfMaps> iterator = currentList.iterator();
					while (index-- > 0)
						iterator.next();
					GroupOfMaps group = iterator.next();

					JFileChooser chooser = new JFileChooser(".");
					int returnVal = chooser.showSaveDialog(this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						GroupOfMaps.printToFile( chooser.getSelectedFile().getName(), group );
					}
				}
			}
			
			
			if (source == menuAuthor) {
				JOptionPane.showMessageDialog(this, GREETING_MESSAGE);
			}
			
		} catch (MapsException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "B��d", JOptionPane.ERROR_MESSAGE);
		}

		// Aktualizacja zawarto�ci tabeli z list� grup.
		viewList.refreshView();
	}

} // koniec klasy GroupManagerApp



/*
 * Pomocnicza klasa do wy�wietlania listy grup
 * w postaci tabeli na panelu okna g��wnego
 */
class ViewGroupList extends JScrollPane {
private static final long serialVersionUID = 1L;
	
	private List<GroupOfMaps> list;
	private JTable table;
	private DefaultTableModel tableModel;

	public ViewGroupList(List<GroupOfMaps> list, int width, int height){
		this.list = list;
		setPreferredSize(new Dimension(width, height));
		setBorder(BorderFactory.createTitledBorder("Lista grup:"));
		
		String[] tableHeader = { "Nazwa grupy", "Typ kolekcji", "Liczba os�b" };
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
		for (GroupOfMaps group : list) {
			if (group != null) {
				String[] row = { group.getName(), group.getType().toString(), "" + group.size() };
				tableModel.addRow(row);
			}
		}
	}
	
	int getSelectedIndex(){
		int index = table.getSelectedRow();
		if (index<0) {
			JOptionPane.showMessageDialog(this, "�adana grupa nie jest zaznaczona.", "B��d", JOptionPane.ERROR_MESSAGE);
		}
		return index;
	}

} // koniec klasy ViewGroupList