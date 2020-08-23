
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.*;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
//import javax.swing.event.ChangeListener;
//import javax.swing.event.ChangeEvent;
import java.awt.Color;

public class MainGui {

	private JFrame frame;
	private JTabbedPane graphtab;
	private JButton delGraph;
	private JComboBox<String> lista;
	private JComboBox<String> listb;
	private JButton union;
	private JButton intersect;
	
	private JFileChooser js = new JFileChooser();

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					
					MainGui window = new MainGui();
					
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public MainGui() {
		js.setMultiSelectionEnabled(false);
		
		/*try {
			Class test = Class.forName("com.mxgraph.view.mxGraph");
			Object o = test.newInstance();
			//com.mxgraph.view.mxGraph sss = new com.mxgraph.view.mxGraph();
		}catch(Exception s) {
			JOptionPane.showMessageDialog(frame, "Library not found!", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}*/
		
		initialize();
	}

	private void initialize() {
		frame = new JFrame("Automata Drawing stable");
		frame.setBounds(100, 100, 1400, 810);
		frame.setMinimumSize(new Dimension(1400,910));
		frame.setMaximumSize(new Dimension(1920,1080));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		//cofirm on closing
		
		
		WindowListener listenE = new WindowAdapter( ){
			public void windowClosing(WindowEvent ee) {
				if(JOptionPane.showConfirmDialog(null, "Confirmation","Are you sure?",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					frame.dispose();
			}
		};
		frame.addWindowListener(listenE);
		
		
		graphtab = new JTabbedPane(JTabbedPane.TOP);
		/*graphtab.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {

				//if (e.getSource() instanceof JTabbedPane) {
					//JTabbedPane pane = (JTabbedPane) e.getSource();
					//System.out.println("Selected paneNo : " + graphtab.getSelectedIndex());
					//if(graphtab.getSelectedIndex() != -1)
					//((Automata_gc)graphtab.getSelectedComponent()).getGc().setSize(1000, graphtab.getSize().height-80);
				//}

			}
		});*/
		graphtab.setBounds(0, 104, 1400, 730);
		frame.getContentPane().add(graphtab);
		
		
		frame.getRootPane().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				updateSize();
			}
		});
		
		
		JButton addGraph = new JButton("Create new Automata");
		addGraph.setBackground(Color.GREEN);
		addGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = JOptionPane.showInputDialog(frame, "Enter automata name:", "Input", JOptionPane.QUESTION_MESSAGE);
				//for(int er = 0;er<10;er++)
				
				if(name!=null && name.trim().length()>0) {
					addGc(name,new AutomataPanel());
				}else if(name != null)
					showError("Automata name can't be blank!");
				
				graphtab.setSelectedIndex(graphtab.getTabCount()-1);
				updateSize();
			}
		});
		addGraph.setFont(new Font("Tahoma", Font.BOLD, 14));
		addGraph.setBounds(10, 35, 185, 58);
		frame.getContentPane().add(addGraph);
		
		delGraph = new JButton("Remove Automata");
		
		delGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!(JOptionPane.showConfirmDialog(null, "Confirmation","Are you sure?",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)) 
					return;
					
				
				if(graphtab.getSelectedComponent()!=null) {
					graphtab.remove(graphtab.getSelectedComponent());
					updateBox();
				}else {
					updateBox();
					showError("Please create or load automata first!");
				}
			}
		});
		delGraph.setBackground(Color.RED);
		delGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		delGraph.setFont(new Font("Tahoma", Font.BOLD, 14));
		delGraph.setBounds(400, 35, 185, 58);
		frame.getContentPane().add(delGraph);
		
		lista = new JComboBox<String>();
		lista.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lista.setBounds(620, 35, 150, 26);
		
		frame.getContentPane().add(lista);
		
		listb = new JComboBox<String>();
		listb.setFont(new Font("Tahoma", Font.PLAIN, 14));
		listb.setBounds(620, 67, 150, 26);
		frame.getContentPane().add(listb);
		
		JLabel lblA = new JLabel("A:");
		lblA.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblA.setBounds(596, 35, 46, 26);
		frame.getContentPane().add(lblA);
		
		JLabel lblB = new JLabel("B:");
		lblB.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblB.setBounds(596, 67, 46, 26);
		frame.getContentPane().add(lblB);
		
		union = new JButton("Union");
		union.setBackground(Color.GREEN);
		union.setFont(new Font("Tahoma", Font.BOLD, 14));
		union.setBounds(780, 35, 104, 58);
		union.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//for(int e = 0;e<graphtab.getTabCount();e++)
				if(lista.getSelectedIndex() < 0) {
					showError("Please create or load automata first!");
					return;
				}
					
				System.out.println(graphtab.getComponentAt(lista.getSelectedIndex()));
				AutomataPanel A = (AutomataPanel)graphtab.getComponentAt(lista.getSelectedIndex());
				System.out.println(graphtab.getComponentAt(listb.getSelectedIndex()));
				AutomataPanel B = (AutomataPanel)graphtab.getComponentAt(listb.getSelectedIndex());
				
				if(A.getReady() && B.getReady()) {
					if(js.showSaveDialog(frame)==JFileChooser.APPROVE_OPTION) {
						Parser un = new Parser(A.getCharSet(),A.getSeen(),A.getAccept(),A.getTable(),js.getSelectedFile());
						un.addB(B.getCharSet(), B.getSeen(), B.getAccept(), B.getTable());
						
						un.merge(true);
						try {
							un.dump_res(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						try {
							AutomataPanel K = Parser.load(new AutomataPanel(), js.getSelectedFile());
							addGc(js.getSelectedFile().getName(),K);
							graphtab.setSelectedIndex(graphtab.getTabCount()-1);
						} catch (Exception e) {
							e.printStackTrace();
						}
						updateSize();
					}
				}else {
					showError("Either A or B is invalid!");
					return;
				}
				
				
			}
		});
		frame.getContentPane().add(union);
		
		intersect = new JButton("Intersection");
		intersect.setBackground(Color.MAGENTA);
		intersect.setFont(new Font("Tahoma", Font.BOLD, 14));
		intersect.setBounds(894, 35, 132, 58);
		intersect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(lista.getSelectedIndex() < 0) {
					showError("Please create or load automata first!");
					return;
				}
				
				System.out.println(graphtab.getComponentAt(lista.getSelectedIndex()));
				AutomataPanel A = (AutomataPanel)graphtab.getComponentAt(lista.getSelectedIndex());
				System.out.println(graphtab.getComponentAt(listb.getSelectedIndex()));
				AutomataPanel B = (AutomataPanel)graphtab.getComponentAt(listb.getSelectedIndex());
				
				if(A.getReady() && B.getReady()) {
					if(js.showSaveDialog(frame)==JFileChooser.APPROVE_OPTION) {
						Parser un = new Parser(A.getCharSet(),A.getSeen(),A.getAccept(),A.getTable(),js.getSelectedFile());
						un.addB( B.getCharSet(), B.getSeen(), B.getAccept(), B.getTable());
						//false for intersection mode
						un.merge(false);
						try {
							un.dump_res(false);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						try {
							AutomataPanel K = Parser.load(new AutomataPanel(), js.getSelectedFile());
							addGc(js.getSelectedFile().getName(),K);
							graphtab.setSelectedIndex(graphtab.getTabCount()-1);
						} catch (Exception e) {
							e.printStackTrace();
						}
						updateSize();
						
					}
				}else {
					showError("Invalid automata!");
					return;
				}
				
			}
		});
		frame.getContentPane().add(intersect);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1930, 21);
		frame.getContentPane().add(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem load = new JMenuItem("Load from File");
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(js.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION) {
					
					try {
						AutomataPanel K = Parser.load(new AutomataPanel(), js.getSelectedFile());
						addGc(js.getSelectedFile().getName(),K);
					} catch (Exception e) {
						e.printStackTrace();
					}
					graphtab.setSelectedIndex(graphtab.getTabCount()-1);
					updateSize();
				}
				//graphtab.setSelectedIndex(graphtab.getTabCount()-1);
			}
		});
		
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frame, 
						"Java Program: Union and Intersection(DFA) by cartesian product.\n"
						+ "archived by JGraphT library package\n",
						"About",JOptionPane.PLAIN_MESSAGE);
			}
		});
		mnFile.add(load);
		mnFile.add(about);
		mnFile.add(exit);
		
		JButton frontload = new JButton("Load Automata");
		frontload.setFont(new Font("Tahoma", Font.BOLD, 14));
		frontload.setBackground(Color.ORANGE);
		frontload.setBounds(205, 35, 185, 58);
		frontload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(js.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION) {
					
					try {
						AutomataPanel K = Parser.load(new AutomataPanel(), js.getSelectedFile());
						addGc(js.getSelectedFile().getName(),K);
					} catch (Exception e) {
						e.printStackTrace();
					}
					graphtab.setSelectedIndex(graphtab.getTabCount()-1);
					updateSize();
				}
				
			}
		});
		frame.getContentPane().add(frontload);
		//graphtab.addTab("", null);
		//init A:B
		updateBox();
		
		
	}
	
	public void addGc(String title,JPanel gc)  {
		
		for(int e = 0;e<graphtab.getTabCount();e++)
			if(graphtab.getTitleAt(e).equals(title)) {
				showError("Automata with the same name is already Exists!");
				return;
				
			}
				
		
		JLabel lab = new JLabel(title);
		//tab title length grow as title 
		lab.setMinimumSize(new Dimension(50,25));
		
		lab.setPreferredSize(new Dimension(25+(int)((title.length())*7.5), 25));
		lab.setHorizontalAlignment(0);
		lab.setFont(new Font("Tahoma", Font.PLAIN, 14));
		graphtab.addTab(title, gc);
		
		
		graphtab.setTabComponentAt(graphtab.getTabCount()-1, lab);
		
		this.updateBox();
		
	}
	private void showError(String s){
		JOptionPane.showMessageDialog(frame, s, "Error", JOptionPane.ERROR_MESSAGE);
	}
	private void updateSize() {
		graphtab.setSize(frame.getSize().width, frame.getSize().height-110);
		
		for(int e = 0;e<graphtab.getTabCount();e++) {
			//System.out.println("sdsd");
			//((ttt)graphtab.getComponent(e)).getGc().setSize(1000, graphtab.getSize().height-38);
			AutomataPanel K = (AutomataPanel) graphtab.getComponentAt(e);
			//K.getGc().setSize(graphtab.getSize().width-400, graphtab.getSize().height-80);
			K.getGc().setBounds(5, 5, graphtab.getSize().width-400, graphtab.getSize().height-80);
			K.getGm().setBounds(graphtab.getSize().width-380, 20, 350, 190);
			K.getText().setBounds(graphtab.getSize().width-380, 220, 350, 250);
			K.getTestText().setBounds(graphtab.getSize().width-380, 480, 350, 100);
			K.getTestButton().setBounds(graphtab.getSize().width-380, 590, 100, 30);
			K.getTestResult().setBounds(graphtab.getSize().width-200, 590, 180, 30);
			K.getToggleColor().setBounds(graphtab.getSize().width-380, 630, 130, 30);
			
			
		}
	}
	
	private void updateBox() {
		lista.removeAllItems();
		listb.removeAllItems();
		if(graphtab.getTabCount() == 0) {
			lista.setEnabled(false);
			listb.setEnabled(false);
			return;
		}
		lista.setEnabled(true);
		listb.setEnabled(true);
		for(int e = 0;e<graphtab.getTabCount();e++) {
			listb.addItem(((JLabel)graphtab.getTabComponentAt(e)).getText());
			lista.addItem(((JLabel)graphtab.getTabComponentAt(e)).getText());
		}
	}
}
//Natthawat khuwijitjaru on 1/5/2020 10:06 PM updated