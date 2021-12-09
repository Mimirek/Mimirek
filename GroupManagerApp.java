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
			"Program do zarz¹dzania grupami map " + 
	        "- wersja okienkowa\n\n" + 
	        "Autor: Wiktor Syska\n" + 
			"Data:  Grudzieñ 2021 r.\n";

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
	
	
	
	// Zbiór grup osób, którymi zarz¹dza aplikacja
	private List<GroupOfMaps> currentList = new ArrayList<GroupOfMaps>();
	
	// Pasek menu wyœwietlany na panelu w g³ównym oknie aplikacji
	JMenuBar menuBar        = new JMenuBar();
	JMenu menuGroups        = new JMenu("Grupy");
	JMenu menuAbout         = new JMenu("O programie");
	
	// Opcje wyœwietlane na panelu w g³ównym oknie aplikacji
	JMenuItem menuNewGroup           = new JMenuItem("Utwórz grupê");
	JMenuItem menuEditGroup          = new JMenuItem("Edytuj grupê");
	JMenuItem menuDeleteGroup        = new JMenuItem("Usuñ grupê");
	JMenuItem menuLoadGroup          = new JMenuItem("za³aduj grupê z pliku");
	JMenuItem menuSaveGroup          = new JMenuItem("Zapisz grupê do pliku");
	


	JMenuItem menuAuthor             = new JMenuItem("Autor");
	
	// Przyciski wyœwietlane na panelu w g³ównym oknie aplikacji
	JButton buttonNewGroup = new JButton("Utwórz");
	JButton buttonEditGroup = new JButton("Edytuj");
	JButton buttonDeleteGroup = new JButton(" Unuñ ");
	JButton buttonLoadGroup = new JButton("Otwórz");
	JButton buttonSavegroup = new JButton("Zapisz");


			
	// Widok tabeli z list¹ grup wyœwietlany 
	// na panelu w oknie g³ównym aplikacji
	ViewGroupList viewList;


	public GroupManagerApp() 
	{
		// Konfiguracja parametrów g³ównego okna aplikacji
		setTitle("GroupManager - zarz¹dzanie grupami map");
		setSize(450, 400);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() 
		{
			// To jest definicja anonimowej klasy (klasy bez nazwy)
			// która dziedziczy po klasie WindowAdapter i przedefiniowuje
			// metody windowClosed oraz windowClosing.

			@Override
			public void windowClosed(WindowEvent event) 
			{
				// Wywo³ywane gdy okno aplikacji jest zamykane za pomoc¹
				// wywo³ania metody dispose()
				try 
				{
					saveGroupListToFile(ALL_GROUPS_FILE);
					JOptionPane.showMessageDialog(null, "Dane zosta³y zapisane do pliku " + ALL_GROUPS_FILE);
				} 
				catch (MapsException e)
				{
					JOptionPane.showMessageDialog(null, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
				}
			}

			@Override
			public void windowClosing(WindowEvent e) 
			{
				windowClosed(e);
			}

		} // koniec klasy anonimowej
		); // koniec wywo³ania metody addWindowListener

		try 
			{
				loadGroupListFromFile(ALL_GROUPS_FILE);
				JOptionPane.showMessageDialog(null, "Dane zosta³y wczytane z pliku " + ALL_GROUPS_FILE);
			} 
		catch (MapsException e) 
			{
				JOptionPane.showMessageDialog(null, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
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
		
		// Dodanie s³uchaczy zdarzeñ do wszystkich opcji menu.
		// UWAGA: s³uchaczem zdarzeñ bêdzie metoda actionPerformed
		// zaimplementowana w tej klasie i wywo³ana dla
		// bie¿¹cej instancji okna aplikacji - referencja this
		menuNewGroup.addActionListener(this);
		menuEditGroup.addActionListener(this);
		menuDeleteGroup.addActionListener(this);
		menuLoadGroup.addActionListener(this);
		menuSaveGroup.addActionListener(this);

		menuAuthor.addActionListener(this);
		
		// Dodanie s³uchaczy zdarzeñ do wszystkich przycisków.
		// UWAGA: s³uchaczem zdarzeñ bêdzie metoda actionPerformed
		// zaimplementowana w tej klasie i wywo³ana dla
		// bie¿¹cej instancji okna aplikacji - referencja this
		buttonNewGroup.addActionListener(this);
		buttonEditGroup.addActionListener(this);
		buttonDeleteGroup.addActionListener(this);
		buttonLoadGroup.addActionListener(this);
		buttonSavegroup.addActionListener(this);

		// Utwotrzenie tabeli z list¹ osób nale¿¹cych do grupy
		viewList = new ViewGroupList(currentList, 400, 250);
		viewList.refreshView();
		
		// Utworzenie g³ównego panelu okna aplikacji.
		// Domyœlnym mened¿erem rozk³adu dla panelu bêdzie
		// FlowLayout, który uk³ada wszystkie komponenty jeden za drugim.
		JPanel panel = new JPanel();

		// Dodanie i rozmieszczenie na panelu wszystkich
		// komponentów GUI.
		panel.add(viewList);
		panel.add(buttonNewGroup);
		panel.add(buttonEditGroup);
		panel.add(buttonDeleteGroup);
		panel.add(buttonLoadGroup);
		panel.add(buttonSavegroup);


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
				throw new MapsException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
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
			throw new MapsException("Wyst¹pi³ b³¹d podczas zapisu danych do pliku.");
		}
	}
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent event) {
		// Odczytanie referencji do obiektu, który wygenerowa³ zdarzenie.
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
			JOptionPane.showMessageDialog(this, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
		}

		// Aktualizacja zawartoœci tabeli z list¹ grup.
		viewList.refreshView();
	}

} // koniec klasy GroupManagerApp



/*
 * Pomocnicza klasa do wyœwietlania listy grup
 * w postaci tabeli na panelu okna g³ównego
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
		
		String[] tableHeader = { "Nazwa grupy", "Typ kolekcji", "Liczba osób" };
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
			JOptionPane.showMessageDialog(this, "¯adana grupa nie jest zaznaczona.", "B³¹d", JOptionPane.ERROR_MESSAGE);
		}
		return index;
	}

} // koniec klasy ViewGroupList