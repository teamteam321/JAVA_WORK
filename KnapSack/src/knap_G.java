import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;

import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;

import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class knap_G {
	private double []wei_in;
	private double[]val_in;
	private double capacity;
	
	HashMap<Integer,node_info> dict;
	
	private JFrame frame;
	boolean enter_scrp = false;
	protected mxGraphComponent temp;
	private graph_or x_graph;
	private JCheckBox autos;
	private JLabel show_bounds;
	private JEditorPane op;
	
	private JFileChooser fchoose = new JFileChooser("");
	private JLabel errorlbl;
	protected BufferedReader temp2;
	protected FileReader temp1;
	private JEditorPane ep;
	private JScrollPane graphpanel;
	private JButton refresh;
	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					knap_G window = new knap_G();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public knap_G() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("0/1 Knapsack Problem Solver (Group 1)");
		frame.setResizable(false);
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 14));
		frame.setBounds(200, 50, 1280, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		JPanel output_panel = new JPanel();
		output_panel.setBorder(new TitledBorder(null, "Output", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		output_panel.setBounds(10, 251,1008, 709);
		frame.getContentPane().add(output_panel);
		output_panel.setLayout(null);
	/*	panel.addMouseListener(new MouseAdapter() {
			@Override
			
			public void mouseEntered(MouseEvent e) {
				enter_scrp = true;
				Thread s = new Thread() {
					public void run() {
						while(enter_scrp) {
							
							System.out.println(e.getX());
						}
						
					}
				};
				s.start();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				enter_scrp = false;
			}
		});*/
		
		graphpanel = new JScrollPane();
		graphpanel.getViewport().setBackground(Color.white);
		//update_op.start();
		
		
		graphpanel.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		//graphpanel.setWheelScrollingEnabled(true);
		//graphpanel.removeMouseWheelListener(graphpanel.getMouseWheelListeners()[0]);
		
		
		graphpanel.setBounds(10, 25, 988, 673);
		
		//int[] a = {1,5,3,9,4};;
		// x = new graph_or() ;
		output_panel.add(graphpanel);
		//graphpanel.setViewportView(x.draw_start());
		//System.out.println(((mxGraphComponent)x.getgc()).getGraph().getModel().getEdgeCount("-8"));
		
		
		
		
		
		
	
		
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1274, 21);
		frame.getContentPane().add(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpenFile = new JMenuItem("Open file");
		mntmOpenFile.addActionListener(new ActionListener() {
			 

			public void actionPerformed(ActionEvent arg0) {
				fchoose.addChoosableFileFilter(new FileNameExtensionFilter("Text file","txt"));
				int a = fchoose.showOpenDialog(frame);
				if(a == JFileChooser.APPROVE_OPTION) {
					try {
						temp1 = new FileReader(fchoose.getSelectedFile());
						temp2 = new BufferedReader(temp1);
						String inp = "";
						String ttt;
						while((ttt = temp2.readLine())!= null) {
							inp += ttt+"\r\n";
						}
						//System.out.print(inp);
						ep.setText(inp);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
		});
		mnFile.add(mntmOpenFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(-1);
			}
		});
		mnFile.add(mntmExit);
		
		JPanel input_panel = new JPanel();
		input_panel.setBorder(new TitledBorder(null, "Input", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		input_panel.setBounds(10, 24, 1254, 179);
		frame.getContentPane().add(input_panel);
		input_panel.setLayout(null);
		
		JScrollPane inputedit = new JScrollPane();
		inputedit.setBounds(10, 20, 1066, 143);
		input_panel.add(inputedit);
		
		 ep = new JEditorPane();
		ep.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		inputedit.setViewportView(ep);
		ep.setText("Enter capacity, weight and Value list in order, line by line; Both Weight and Value list must be separated by either \",\" or \" \"\r\n ie. \r\n     10.0\r\n     11, 21, 13, 4, 5\r\n     2, 4, 6, 7, 4\r\n or\r\n     10.0\r\n     11 21 13 4 5\r\n     2 4 6 7 4");
		
		inputedit.getViewport().setViewPosition(new Point(0,0));
		 autos = new JCheckBox("AutoStep");
		autos.setBounds(1099, 140, 97, 23);
		input_panel.add(autos);
		
		JButton thou = new JButton("Confirm input");
		thou.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String a = ep.getText();
				if(parse_input(a)) {
					errorlbl.setText(wei_in.length+" Item(s) entered.");
					op.setText("");
					errorlbl.setForeground(Color.BLUE);
					x_graph = new graph_or(wei_in,val_in,capacity);
					dict = x_graph.getInfoMap();
					
					refresh.setText("Next Step");
					
					if(autos.isSelected()) {
						frame.setCursor(new Cursor(3));
						thou.setEnabled(false);
						refresh.setEnabled(false);
						Thread t = new Thread() {
							public void run() {
								//System.out.println("1");
								x_graph.draw_start();
								update_look();
								frame.setCursor(new Cursor(0));
								thou.setEnabled(true);
								
								show_bounds.setText(String.format("%.2f", x_graph.getBounds()));
								JOptionPane.showMessageDialog(frame, "Click on a green node to see result.", "Finished!", JOptionPane.INFORMATION_MESSAGE);
							}
							
						};
						t.start();
						
					}else {
						x_graph.init_step();
						refresh.setEnabled(true);
					}
						
				}
				
				
				
				//step mode
				//Natthawat khuwijitjaru 07600458
				
			}
		});
		thou.setFont(new Font("Tahoma", Font.PLAIN, 14));
		thou.setBounds(1099, 85, 145, 48);
		input_panel.add(thou);
		
		errorlbl = new JLabel("...");
		errorlbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		errorlbl.setBounds(20, 214, 257, 22);
		frame.getContentPane().add(errorlbl);
		
		refresh = new JButton("Next Step");
		refresh.setForeground(new Color(0, 139, 139));
		refresh.setBounds(1033, 740, 231, 56);
		refresh.setEnabled(false);
		frame.getContentPane().add(refresh);
		refresh.setFont(new Font("Dialog", Font.PLAIN, 16));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Status", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(1033, 251, 231, 478);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel status_bounds = new JLabel("Upper Bounds:");
		status_bounds.setFont(new Font("Tahoma", Font.PLAIN, 18));
		status_bounds.setBounds(10, 27, 177, 32);
		panel.add(status_bounds);
		
		 show_bounds = new JLabel("-1.0");
		show_bounds.setHorizontalAlignment(SwingConstants.RIGHT);
		show_bounds.setFont(new Font("Tahoma", Font.PLAIN, 18));
		show_bounds.setBounds(10, 56, 177, 32);
		panel.add(show_bounds);
		
		
		JScrollPane outputedit = new JScrollPane();
		outputedit.setBounds(10, 99, 211, 369);
		panel.add(outputedit);
		
		 op = new JEditorPane();
		 op.setEditable(false);
		 op.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		 //op.setText("Node number:0\r\nValue: 0.0\r\nWeight: 0.0\r\nBoiunds: 0.0\r\nItem list: {0,0,0,0}");
		 
		 outputedit.setViewportView(op);
		 
		 
		// frame.getContentPane().add(scrollPane);
		refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refresh.setEnabled(false);
				thou.setEnabled(false);
				frame.setCursor(new Cursor(3));
				Thread d = new Thread() {
					public void run() {
						x_graph.step_add();
						show_bounds.setText(String.format("%.2f", x_graph.getBounds()));
						update_look();
						refresh.setEnabled(true);
						thou.setEnabled(true);
						frame.setCursor(new Cursor(0));
						if(x_graph.ISFIN) {
							refresh.setEnabled(false);
							refresh.setText("Finished!");
							JOptionPane.showMessageDialog(frame, "Click on a green node to see result.", "Finished!", JOptionPane.INFORMATION_MESSAGE);
						}
							
					}
				};
				d.start();
				
			}
		});
		
		
		
	}
	
	private boolean parse_input(String inp) {
		String[]input_split = inp.trim().split("\r\n");
		String capac;
		String[] wei_temp;
		String[] val_temp;
		try {
			if(input_split.length >3)
				throw new Exception();
			capac = input_split[0].trim().split(" ")[0];
			if(input_split[1].contains(","))
				wei_temp = input_split[1].trim().split(",");
			else
				wei_temp = input_split[1].trim().split(" ");
			
			if(input_split[2].contains(","))
				val_temp = input_split[2].trim().split(",");
			else
				val_temp = input_split[2].trim().split(" ");
			
			if(wei_temp.length != val_temp.length)
				throw new Exception();
			capacity = Double.parseDouble(capac);
			
			
			wei_in = new double[wei_temp.length];
			val_in = new double[wei_temp.length];
			for(int e = 0;e<wei_temp.length;e++) {
				val_in[e] = Double.parseDouble(val_temp[e].trim());
				wei_in[e] = Double.parseDouble(wei_temp[e].trim());
			}
		}catch(Exception any) {
			errorlbl.setText("Invalid Input");
			errorlbl.setForeground(Color.RED);
			return false;
		}
		return true;
	}
	
	private void update_look() {
		/*if(auto) {
		//	x_graph.pass_add();
		}else {
			x_graph.step_add();
		}*/
		
		 temp = x_graph.getgc();
		 
		 temp.getGraphControl().addMouseListener(new MouseAdapter() {
			 public void mouseClicked(MouseEvent E) {
				 	
				 	
				 
					mxCell ttt = (mxCell)temp.getCellAt(E.getX(),E.getY());
					if(ttt == null ||!ttt.isVertex())
						return;
					String value = (String)ttt.getValue();
					String out = "";
					if(value.trim().equals("START")) {
						out = "Start node.";
						op.setText(out);
						return;
					}
					node_info xxx = dict.get(Integer.parseInt(value.split("\n")[0]));
					
					   /*  out = "Node Number:\n   "+value.split("\n")[0];
						out += "\r\nDecision:\n   "+xxx.reason;
					    out += "\r\nValue:\n   "+xxx.value;
					    out += "\r\nWeight:\n   "+xxx.weight;
					    out += "\r\nPossible Value:\n   "+String.format("%.2f", xxx.bounds);
					    out += "\r\nItem List:\n   {"+xxx.pick_item+"}";*/
					
					    out = "Node Number:\n   "+value.split("\n")[0];
						out += "\r\nDecision:\n   "+xxx.reason;
					    out += "\r\nValue:\n   "+String.format("%.2f", xxx.value);
					    out += "\r\nWeight:\n   "+String.format("%.2f", xxx.weight);
					    out += "\r\nBounds (Possible Value):\n   "+String.format("%.2f", xxx.bounds);
					    out += "\r\nItem List:\n   {"+xxx.pick_item+"}";
					    
					op.setText(out);
				}
		 });
		 int x = (int)x_graph.gx;
		 int y = (int)x_graph.gy;
		 temp.getViewport().setOpaque(true);
		 temp.getViewport().setBackground(Color.WHITE);
		graphpanel.setViewportView(temp);
		//graphpanel.getViewport().setBackground(Color.white);
		graphpanel.getViewport().revalidate();
		
		int sx,sy;
		
		sx = x-150;
		sy = y-150;
		///save old position
		/*if(sx + graphpanel.getWidth() > temp.getWidth())
			sx = temp.getWidth() - graphpanel.getWidth();
		if(sy + graphpanel.getHeight() > temp.getHeight())
			sy = temp.getHeight() - graphpanel.getHeight();*/
		//if(sx < 0)sx = 0;
		//if(sy < 0)sy = 0;
		
		graphpanel.getViewport().setViewPosition(new Point(sx,sy));
	}
	
	//Natthawat khuwijitjaru 07600458
}


