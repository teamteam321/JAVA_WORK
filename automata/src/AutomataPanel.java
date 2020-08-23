
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.mxCell;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseMotionAdapter;

public class AutomataPanel extends JPanel {
	
	private boolean isReady = false;
	public boolean getReady() {
		return this.isReady;
	}
	
	private mxGraph g = null;
	public mxGraph getGraph() {
		return this.g;
	}
	private mxGraphComponent gc = null;
	private Object gp = null;
	
	private mxCell[] pair = new mxCell[2];
	private mxParallelEdgeLayout layout;
	
	//sets of accept state
	private LinkedList<Object> accept_node  = new LinkedList<>();
	public LinkedList<Object> getAccept(){
		return this.accept_node;
	}
	//start node
	private Object START = null;
	public void setStart(Object s) {
		this.START = s;
	}
	//what did start_node point to
	private Object from_start = null;
	public void setFromStart(Object s) {
		this.from_start = s;
	}
	//private boolean start_node = false;
	//automata menu panel
	private JPanel gm;
	public JPanel getGm() {
		return this.gm;
	}
	private JTextPane a_out = new JTextPane();
	
	private JTextPane test_text = new JTextPane();
	
	TreeSet<String> ch1 = null;
	public TreeSet<String>getCharSet(){
		return this.ch1;
	}
	private HashMap<mxCell, HashMap<String, mxCell>> table = null;
	public HashMap<mxCell, HashMap<String, mxCell>> getTable(){
		return this.table;
	}
	private LinkedList <mxCell> seen = null;
	public LinkedList <mxCell> getSeen(){
		return this.seen;
	}
	
	
	MouseMotionAdapter m_drag = null;
	MouseAdapter m_release = null;
	
	
	private JScrollPane vw;
	public JScrollPane getText() {
		return this.vw;
	}
	private JScrollPane tt;
	public JScrollPane getTestText() {
		return this.tt;
	}
	private JButton testb;
	public JButton getTestButton() {
		return this.testb;
	}
	private JLabel result;
	public JLabel getTestResult() {
		return this.result;
	}
	
	int font_size = 21;
	int state_size = 65; //vertex size

	private String[]Scheme = new String[]{ //edge color scheme
		"6e6e6e",
		"f35e5a",
		"b68a06",
		"46a903",
		"18b682",
		"15a7e6",
		"9370ff",
		"f740ce"
	};
	public JToggleButton getToggleColor() {
		return this.toggle;
	}
	private JToggleButton toggle = new JToggleButton();
	
	
	
	AutomataPanel(){
		
		setLayout(null);
		init();
	}
	private void paint_g() {
		//g.setDefaultLoopStyle(new jtest.LoopEdge());
		Object []sel = g.getSelectionCells();
		g.selectAll();
		Object []MG = g.getSelectionCells();
		
		int scc = 0;
		
		for(Object sss : MG) {
			//is it a vertex?
			if(((mxCell)sss).isVertex()) {
				
				if(sss.equals(START)) {
					((mxCell)START).getGeometry().setHeight(0);
					((mxCell)START).getGeometry().setWidth(0);;
					continue;
				}
					
				
				((mxCell)sss).getGeometry().setHeight(state_size);
				((mxCell)sss).getGeometry().setWidth(state_size);
				//easy fix by init setting
				/*if(((mxCell)sss).getGeometry().getX() < 0)
					((mxCell)sss).getGeometry().setX(10);
				if(((mxCell)sss).getGeometry().getY() < 0)
					((mxCell)sss).getGeometry().setY(10);*/
				
				if(accept_node.contains(sss))
					((mxCell)sss).setStyle("fillColor=#ffffff;strokeColor=#000000;"
							+ "fontColor=#000000;shape=doubleEllipse;strokeWidth=1.75");
				else
					((mxCell)sss).setStyle("fillColor=#ffffff;strokeColor=#000000;"
							+ "fontColor=#000000;shape=ellipse;strokeWidth=1.75");
				
				if(((mxCell)sss).equals(pair[0])) 
					((mxCell)sss).setStyle(((mxCell)sss).getStyle()+";fillColor=#ffff00");
				
					
				
			}
			//in case it is edge
			else {
				if(toggle.isSelected()) {
					//Object pe [] = g.getEdgesBetween(((mxCell)sss).getSource(), ((mxCell)sss).getTarget());
					((mxCell)sss).setStyle("fillColor=#ffffff;strokeColor=#"+Scheme[scc%7]+";"
							+ "fontColor=#"+Scheme[scc++%7]+";strokeWidth=1.75;align=left");
				}else
				((mxCell)sss).setStyle("fillColor=#000000;strokeColor=#000000;"
						+ "fontColor=#000000;strokeWidth=1.75;align=left");
			}	
		}
		//repaint selected vertices
		for(Object s : sel) {
			if(((mxCell)s).isVertex())
				((mxCell)s).setStyle(((mxCell)s).getStyle()+";fillColor=#00ff00");
		}
		
		g.selectAll();
		/*g.setCellsEditable(false);
		g.setVertexLabelsMovable(false);
		g.setEdgeLabelsMovable(false);
		//g.setResetEdgesOnMove(true);
		g.setKeepEdgesInBackground(true);
		g.setBorder(50);
		g.setGridEnabled(true);
		g.setGridSize(15);
		g.setAllowNegativeCoordinates(false);
		g.setAllowDanglingEdges(false);
		g.setCellsCloneable(false);*/
		
		g.setCellStyles(mxConstants.STYLE_FONTSIZE,font_size+"");
		//g.setCellStyleFlags(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD, true);
		
		g.setCellStyles(mxConstants.STYLE_PERIMETER,mxConstants.PERIMETER_ELLIPSE );
		
		Map<String, Object> style = g.getStylesheet().getDefaultEdgeStyle();
		    style.put(mxConstants.STYLE_ROUNDED, true);
		    style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
		    //style.put(mxConstants.STYLE_FONTSIZE, "18");
		    
		    //style.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ELBOW);
		    
		gc.refresh();
		g.clearSelection();
		g.setSelectionCells(sel);
		
	}
	private void init() {
		
		
		
		setSize(1350,900);
		setVisible(true);
		g = new mxGraph();
		gc = new mxGraphComponent(g);
		gc.setPageBackgroundColor(new Color(255,255,255));
		gc.getViewport().setOpaque(true);
		gc.getViewport().setBackground(Color.white);
		gc.getVerticalScrollBar().setUnitIncrement(16);
		gc.getHorizontalScrollBar().setUnitIncrement(16);
		
		
		g.setCellsEditable(false);//custom edit context implemented
		g.setVertexLabelsMovable(false);
		g.setEdgeLabelsMovable(false);
		//g.setResetEdgesOnMove(true);
		g.setKeepEdgesInBackground(true);
		g.setBorder(50);
		g.setGridEnabled(true);
		g.setGridSize(15);	//cliping size
		g.setAllowNegativeCoordinates(false);//fixed
		g.setAllowDanglingEdges(false);
		g.setCellsCloneable(false);
		g.setConnectableEdges(false);
		//g.setCellsSelectable(false);
		
		
		
		
		/*m_drag = new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				
				updateLayout();
				
			}
		};*/
		//gc.getGraphControl().addMouseMotionListener(m_drag);
		
		//graph component listening to cells move event
		//perform start node arrow update and style-color update
		g.addListener(mxEvent.CELLS_MOVED, new mxIEventListener() {
			public void invoke(Object arg0, mxEventObject arg1) {
				System.out.println("invoke!!");
				Object []k = g.getSelectionCells();
				if(from_start!=null) {
					((mxCell)START).getGeometry().setX(((mxCell)from_start).getGeometry().getCenterX()-100);
					((mxCell)START).getGeometry().setY(((mxCell)from_start).getGeometry().getCenterY());
				}
				g.addSelectionCells(k);
				updateLayout();
			}
		});
		//event listeners are instantanous, causing the start arrow 
		//to originated from the former point
		/*g.addListener(mxEvent.CELLS_ADDED, new mxIEventListener() {
			public void invoke(Object arg0, mxEventObject arg1) {
				System.out.println("added!!");
				Object []k = g.getSelectionCells();
				if(from_start!=null) {
					((mxCell)START).getGeometry().setX(((mxCell)from_start).getGeometry().getCenterX()-100);
					((mxCell)START).getGeometry().setY(((mxCell)from_start).getGeometry().getCenterY());
				}
				g.addSelectionCells(k);
				updateLayout();
			}
		});*/
		m_release = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent ee) {
				//updateLayout();
				//ee.consume();
				//System.out.print("release");
				//update start edge
				Object []k = g.getSelectionCells();
				//System.out.print("klen"+k.length);
				g.selectAll();
				Object []sss = edgeFilter(g.getSelectionCells());
				g.clearSelection();
				String d;
				int ins = 0;
				for(Object edges : sss) {
					if(((mxCell)edges).getSource() == START||((mxCell)edges).getTarget() == START)
						continue;
						ins = 0;
						if(((mxCell)edges).getValue().toString().equalsIgnoreCase("")) {
							g.removeCells(new Object[] {edges});
							d = JOptionPane.showInputDialog(gc,"Enter Character...","" );
							String[]spd = null;
							if(d != null) {
								d = d.trim();
								spd = d.split(",");
							}
							if(spd!=null&&spd.length>0) {
								
								Object []vt;
								
								for(int e1 = spd.length-1;e1>=0;e1--) {
									if(spd[e1].trim().length() != 1)
										continue;
									boolean have = false;
									vt = g.getEdgesBetween(((mxCell)edges).getSource(),((mxCell)edges).getTarget(),true);
									for(Object s:vt)
										if(((mxCell)s).getValue().toString().equalsIgnoreCase(spd[e1]))
											have = true;
									if(!have) {
										g.getModel().beginUpdate();
										g.insertEdge(gp, null, spd[e1] ,((mxCell)edges).getSource(),
												((mxCell)edges).getTarget());
										g.getModel().endUpdate();
										ins++;
									}
										
								}
								
							} 
							//cant insert with string len >1
							if(ins == 0&&d != null) {
								showError("Invalid/duplicated Character!");
								//g.removeCells(new Object[] {edges});
							}else if(ins == 0){
								//g.removeCells(new Object[] {edges}); //cancel
							}
						}
				}
				
				try {construct();}
				catch(Exception e) {}
				
				g.addSelectionCells(k);
				//updateLayout();
			}
		};
		
		gc.getGraphControl().addMouseListener(m_release);
		//graph control
		
		layout = new mxParallelEdgeLayout(g);
		
		layout.execute(g.getDefaultParent());
		//layout2 = new mxHierarchicalLayout(g,SwingConstants.WEST);
		
		//layout2.setIntraCellSpacing(50);
		//layout2.setInterRankCellSpacing(85);
		//layout2.setInterHierarchySpacing(10);
		//layout2.setMoveParent(true);
		//layout2.setParallelEdgeSpacing(30);
		//layout2.execute(g.getDefaultParent());
		g.setAllowDanglingEdges(false);
		g.setDisconnectOnMove(false);
		
		gc.setBounds(5, 5, 1000,720);
		
		gp = g.getDefaultParent();
		
		//this is default automata. being created every time
		//first call beginupdate()
		g.getModel().beginUpdate();
		
		//add node
		mxCell testrem1 = (mxCell) g.insertVertex(gp, null, "q0", 200, 100, 60, 60);
		mxCell testrem2 = (mxCell) g.insertVertex(gp, null, "q1", 200, 400, 60, 60);
		//mxCell testrem3 = (mxCell) g.insertVertex(gp, null, "q2", 0, 200, 60, 60);
		mxCell sttr = (mxCell) g.insertVertex(gp, null, "", 130, 130, 0, 0);
		
		//add edge
		g.insertEdge(gp, null, "0",testrem1, testrem1);
		g.insertEdge(gp, null, "1",testrem1, testrem2);
		g.insertEdge(gp, null, "0",testrem2, testrem2);
		g.insertEdge(gp, null, "1",testrem2, testrem1);
		g.insertEdge(gp, null, "",sttr, testrem1);
		
		//mandatory! to set start node and what its pointed to
		this.START = sttr;
		this.from_start = testrem1;
		
		//mandatory! to add accept state only after all node
		//accept_node.add(testrem1);
		
		//call construct() once all resources are added
		//refresh charset and various fields
		try {
			this.construct();
		} catch (Exception e2) {
			//e2.printStackTrace();
		}
		
		//finished by call endUpdate() and refresh style and layout
		updateLayout();
		g.getModel().endUpdate();
		
		
		
		
		setLayout(null);
		
		
		
		gc.setPreferredSize(new Dimension(500,500));
		add(gc);
		
		
		
		
		gc.getGraphControl().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int X = e.getX();
				int Y = e.getY();
				
				if(e.getClickCount()==2){
					
					//System.out.println("double click");
					mxCell cell = (mxCell) gc.getCellAt(X, Y);
					
					String d = null;
					if(cell!=null)
						d = JOptionPane.showInputDialog(gc, "Edit Element Name",
								"Edit State",JOptionPane.QUESTION_MESSAGE );
					else {
						//suggesting next name
						g.selectAll();
						int next_q = vertexFilter(g.getSelectionCells()).length-1;
						if(next_q < 0)
							next_q = 0;
						g.clearSelection();
						
						d = JOptionPane.showInputDialog(gc,"Enter State...",
			                  "q"+next_q );
					}
					try {
						d = d.trim();
					}catch(Exception dhuip) {}
					
					
					g.getModel().beginUpdate();
					
					if(d != null&&d.length()==0) {
						if(d!= null)
						showError("State name mustn't contains only whitespace");
						return;
					}
						
					
					if(cell!=null&&d != null&&d.length()>0) {
						//d = d.trim();
						
						if(cell.isVertex() && cell != START)
							cell.setValue(d);
						else if(cell.isVertex())
							JOptionPane.showMessageDialog(gc, "Invalid node!", "Error!", JOptionPane.ERROR_MESSAGE);
						
						if(cell.isEdge() && cell.getSource() != START && d.length()==1)
							cell.setValue(d);
						else if(cell.isEdge())
							showError("Character is too long!");
						
						
						
					}else if(d != null&&d.length()>0) {
						//d = d.trim();
						g.selectAll();
						ArrayList<Object> r = vertexNameFilter(g.getSelectionCells(),d);
						g.clearSelection();
						if(r.size() == 0)
							g.insertVertex(gp, null, d, e.getX(), e.getY(), state_size, state_size,"shape=ellipse");
						else
							showError("State already existed!");
		
						/*if(d.equalsIgnoreCase("")){
							addStart(st);
						}*/
					}
						
					
					try {
						construct();
					} catch (Exception e1) {
						//e1.printStackTrace();
					}
					
					
					updateLayout();
					
					g.getModel().endUpdate();
					
					
				}
				else if(SwingUtilities.isRightMouseButton(e)) {
					g.getModel().beginUpdate();
					//System.out.println("Right click");
					
					
					mxCell cell = (mxCell) gc.getCellAt(X, Y);
					if(cell!=null&&cell.isVertex()) {
						pair[1] = pair[0];
						pair[0] = cell;
						
						
						if(pair[1]!=null) {
							mxCell testb = (mxCell)pair[0];
							if(testb.getValue().toString().equalsIgnoreCase("")) {
								//JOptionPane.showMessageDialog(gc, "START node can't have in coming edge.", "Error!", JOptionPane.WARNING_MESSAGE);
							}else {
								testb = (mxCell)pair[1];
								//start as source
								if(testb.getValue().toString().equalsIgnoreCase("")) {
									//JOptionPane.showMessageDialog(gc, "START node can't have in coming edge.", "Error!", JOptionPane.WARNING_MESSAGE);
									
									g.insertEdge(gp, null, "" ,pair[1], pair[0]);
									addStart(new Object[] {pair[0]});
								}else {
									int ins = 0;
									//not source; edge char is requiredhj
									String d = JOptionPane.showInputDialog(gc,"Enter Character...","" );
									String[]spd = null;
									if(d != null) {
										d = d.trim();
										spd = d.split(",");
									}
										
									if(spd!=null &&spd.length>0) {
										
											
										Object []vt;
										ins = 0;
										
										for(int e1 = spd.length-1;e1>=0;e1--) {
											if(spd[e1].trim().length() != 1)
												continue;
											
											
											vt = g.getEdgesBetween(pair[1], pair[0],true);
											boolean have = false;
											for(Object s:vt)
												if(((mxCell)s).getValue().toString().equalsIgnoreCase(spd[e1]))
													have = true;
											if(!have) {
												g.insertEdge(gp, null, spd[e1] ,pair[1], pair[0]);
												ins ++;
											}
												
										}
										
									}
									if(d != null&&ins == 0)
										showError("Invalid Character!");
										
									
								}
								
							}
								
							pair[0] = null;
							pair[1] = null;
						}
						
						try {
							construct();
						} catch (Exception e1) {
							//e1.printStackTrace();
						}
						
					}else if(cell==null) {
						try {
							construct();
						} catch (Exception e1) {
							//e1.printStackTrace();
						}
						pair[0] = null;
						pair[1] = null;
					}
					updateLayout();
					g.getModel().endUpdate();
					
				}else if(SwingUtilities.isLeftMouseButton(e)) {
					pair[0] = null;
					pair[1] = null;
					
					Object []k = g.getSelectionCells();
					System.out.println("klen:"+k.length);
					mxCell cell = (mxCell) gc.getCellAt(X, Y);
					if(cell == null)
						try {
							construct();
						} catch (Exception e1) {}
					
					g.addSelectionCells(k);
					paint_g();
					//click = paint green
					//updateLayout();
					//System.out.println("Left click");
					
				}
				
			}
			
			
		});
		
		// menu from panel
		gm = new JPanel();
		gm.setBounds(1020, 20, 350, 190);
		
		gm.setLayout(null);
		gm.setBorder(BorderFactory.createTitledBorder("Automata menu"));
		gm.setFont(new Font("Tahoma", Font.PLAIN, 13));
		add(gm);
		
		//out automata
		vw = new JScrollPane();
		vw.setBounds(1020, 220, 350, 250);
		vw.setBorder(BorderFactory.createTitledBorder("Automata Properties:"));
		//a_out = new JTextPane();
		a_out.setEditable(false);
		a_out.setFont(new Font("Tahoma", Font.PLAIN, 14));
		a_out.setBounds(0, 0, 320, 250);
		
		tt = new JScrollPane();
		tt.setBounds(1020, 480, 350, 100);
		
		tt.setBorder(BorderFactory.createTitledBorder("Test String:"));
		//test_text.setBackground(Color.WHITE);
		test_text.setFont(new Font("Tahoma", Font.PLAIN, 14));
		test_text.setText("");
		test_text.setBounds(0, 0, 350, 100);
		
		//first output 
		/*try {
			construct();
		} catch (Exception e2) {
			//e2.printStackTrace();
		}*/
		
		vw.setViewportView(a_out);;
		add(vw);
		tt.setViewportView(test_text);
		add(tt);
		
		result = new JLabel("");
		result.setHorizontalAlignment(SwingConstants.CENTER);
		result.setBounds(1200, 590, 180, 30);
		result.setFont(new Font("Tahoma", Font.BOLD, 14));
		add(result);
	
		testb = new JButton();
		testb.setText("Test");
		testb.setFont(new Font("Tahoma", Font.PLAIN, 14));
		testb.setBounds(1020, 590, 100, 30);
		testb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(test_text.getText().length()>=4096) {
					result.setText("String is too long");
					result.setForeground(Color.red);
					//test_text.setForeground(Color.red);
					return;
				}
				if(!isReady) {
					result.setText("Invalid Automata!");
					result.setForeground(Color.red);
					return;
				}
				updateLayout();
				//paint_g();
				
				//g.selectAll();
				//if(vertexNameFilter(g.getSelectionCells(),"").size() ==0){
				if(START == null){
					System.out.println("Please set start state.");
					g.clearSelection();
				} else {
					g.clearSelection();
				
					try {
						if(test_text.getText().trim().length()==0&&accept_node.contains(from_start)) {
							paintTest((mxCell)from_start,true);
							result.setText("Accept!");
							result.setForeground(Color.blue);
							
						}else if(test_text.getText().trim().length()==0) {
							paintTest((mxCell)from_start,false);
							result.setText("Not accept!");
							result.setForeground(Color.red);
							
						}else if(testString(test_text.getText().trim(),table.get(from_start))) {
							result.setText("Accept");
							result.setForeground(Color.blue);
								
						}else {
							result.setText("Not accept!");
							result.setForeground(Color.red);
						}
								
					} catch (Exception e) {
						//e.printStackTrace();
					}
				}
				
			}
		});
		add(testb);
		
		
		toggle.setText("Edge: Black");
		toggle.setFont(new Font("Tahoma", Font.PLAIN, 14));
		toggle.setBounds(1020, 630, 130, 30);
		toggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				paint_g();
				if(toggle.isSelected()) {
					toggle.setText("Edge: Colored");
					toggle.setForeground(Color.red);
				}
					
				else {
					toggle.setText("Edge: Black");
					toggle.setForeground(Color.black);
				}
					
			}
		});
		add(toggle);
		//out.setText("...");
		
		JButton del = new JButton("Delete");
		del.setBackground(Color.RED);
		del.setFont(new Font("Tahoma", Font.PLAIN, 13));
		//del.addMouseListener(new MouseAdapter() {
		//	public void mouseClicked(MouseEvent e) {
				
		del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {		
				g.getModel().beginUpdate();
				Object[] k2 = g.getSelectionCells();
				
				if(k2.length == 0) {
					JOptionPane.showMessageDialog(gc, "Please select state.", "Info", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				Object []ed = edgeFilter(k2);
				Object []vt = vertexFilter(k2);
				vt = g.removeCells(vt);
				if(vertexNameFilter(vt,"").size()!=0)
					resetStart();
				for(Object sss:vt) {
					accept_node.remove(sss);
				}
				
				//remove edges after
				for(Object sss : ed) {
					g.removeCells(new Object[] {sss});
				}
				for(Object sss : ed) {
					vt = null;
					System.out.println("deleted: "+((mxCell)sss).getValue().toString());
					mxCell k = (mxCell) sss;
					//g.removeCells(new Object[] {sss});
					
					if(g.getModel().contains(k.getSource())||g.getModel().contains(k.getTarget()));
						vt = g.getEdgesBetween(k.getSource(), k.getTarget());
					
					
					//in case double edge reinsert the remaining edge to regain linear edge
					//else the remaining one is divided in to two part 
					//cause : double-edge layout
					if(vt != null&&vt.length == 1) {
						mxCell temp = (mxCell) vt[0];
						g.removeCells(vt);
						//System.out.println("one-edge added: "+temp.getValue().toString());
						g.insertEdge(gp, null, temp.getValue(), temp.getSource(), temp.getTarget());
					}
					if(((mxCell)k.getSource()).getValue().toString().equalsIgnoreCase("")) {
						from_start = null;
					}
				}
				
				try {
					construct();
				} catch (Exception e1) { }
				
				g.getModel().endUpdate();
				
			}
		});
		del.setSize(100,100);
		del.setBounds(15,146,140, 32);
		gm.add(del);
		
		
		JButton setacc = new JButton("Set accept");
		setacc.setBackground(Color.BLUE);
		setacc.setFont(new Font("Tahoma", Font.PLAIN, 13));
		setacc.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				g.getModel().beginUpdate();
				Object[] k = vertexFilter(g.getSelectionCells());
				g.clearSelection();
				if(k.length == 0) {
					JOptionPane.showMessageDialog(gc, "Please select state.", "Info", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				for(Object sss:k)
					if(!accept_node.contains(sss))
						accept_node.add(sss);
				
				
				try {
					construct();
				} catch (Exception e1) { }
				g.getModel().endUpdate();
				
			}
		});
		setacc.setSize(100,100);
		setacc.setBounds(15,62, 140, 32);
		gm.add(setacc);
		
		
		JButton cls = new JButton("Clear accept");
		cls.setBackground(Color.ORANGE);
		cls.setFont(new Font("Tahoma", Font.PLAIN, 13));
		cls.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				g.getModel().beginUpdate();
				Object[] k = vertexFilter(g.getSelectionCells());
				g.clearSelection();
				if(k.length == 0) {
					JOptionPane.showMessageDialog(gc, "Please select state.", "Info", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				for(Object sss:k)
					accept_node.remove(sss);
				
				try {
					construct();
				} catch (Exception e1) {}
				g.getModel().endUpdate();
				
			}
		});
		cls.setSize(100,100);
		cls.setBounds(15,104, 140, 32);
		
		gm.add(cls);
		
		JButton setstart = new JButton("Set start");
		setstart.setBackground(Color.GREEN);
		setstart.setFont(new Font("Tahoma", Font.PLAIN, 13));
		setstart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Object[]k =vertexFilter(g.getSelectionCells());
				g.clearSelection();
				if(k.length == 0) {
					JOptionPane.showMessageDialog(gc, "Please select state.", "Info", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				addStart(k);
				try {
					construct();
				} catch (Exception e1) {
					//e1.printStackTrace();
				}
				
				updateLayout();
			}
		});
		setstart.setSize(100,100);
		setstart.setBounds(15,20, 140, 32);
		gm.add(setstart);
		
		
		JButton save = new JButton();
		save.setBackground(Color.CYAN);
		save.setText("Save automata");
		save.setFont(new Font("Tahoma", Font.BOLD, 13));
		save.setBounds(170,20,140, 32);
		
		save.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					construct();
					//JOptionPane.showInputDialog(gc, "Enter automata file name e.g. auto1.txt","Input", JOptionPane.);
					//JOptionPane.showInputDialog(parentComponent, message, title, messageType)
					JFileChooser js = new JFileChooser();
					
					js.setMultiSelectionEnabled(false);
					//int op = js.showSaveDialog(gc);
					
					if(js.showSaveDialog(gc) == JFileChooser.APPROVE_OPTION) {
						Parser ps = new Parser(ch1, seen, accept_node, table, js.getSelectedFile());
						ps.dump();
					}
					
				} catch (Exception e2) {
					showError("Invalid automata!");
					//e2.printStackTrace();
				}
				
			}
		});
		gm.add(save);
		
		JButton reset = new JButton();
		reset.setBackground(Color.RED);
		reset.setText("Reset");
		reset.setFont(new Font("Tahoma", Font.BOLD, 13));
		reset.setBounds(170,146,140, 32);
		reset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(JOptionPane.showConfirmDialog(gc, "Are you sure?","Clear all elements",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					reset();
			}
		});
		gm.add(reset);
		
		/*JLabel current_state = new JLabel("State size : 60");
		current_state.setFont(new Font("Tahoma", Font.PLAIN, 14));
		current_state.setBounds(1020, 631, 115, 26);
		add(current_state);
		
		JLabel current_font = new JLabel("Font size : 18");
		current_font.setFont(new Font("Tahoma", Font.PLAIN, 14));
		current_font.setBounds(1020, 681, 138, 26);
		add(current_font);
		
		JSlider slider = new JSlider();
		slider.setMaximum(120);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				//state_size = slider.getValue();
				current_state.setText("State size: "+state_size);
				updateLayout();
			}
		});
		slider.setMinimum(60);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setSnapToTicks(true);
		slider.setValue(60);
		slider.setBounds(1030, 659, 300, 26);
		add(slider);
		
		JSlider fontsizeslider = new JSlider();
		fontsizeslider.setMaximum(40);
		fontsizeslider.setValue(18);
		fontsizeslider.setSnapToTicks(true);
		fontsizeslider.setPaintTicks(true);
		fontsizeslider.setPaintLabels(true);
		fontsizeslider.setMinimum(18);
		fontsizeslider.setBounds(1030, 709, 300, 26);
		fontsizeslider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				//font_size = fontsizeslider.getValue();
				current_font.setText("Font size: "+font_size);
				updateLayout();
			}
		});
		add(fontsizeslider);*/
		
		
		//gc.addMouseListener(l);
	}
	
	public void reset() {
		
		g.selectAll();
		g.removeCells(g.getSelectionCells());
		START = null;
		from_start = null;
		accept_node = new LinkedList<Object>();
		table = null;
		seen = null;
		
		resetStart();
		
		gc.getGraphControl().removeMouseMotionListener(m_drag);
		/*gc.getGraphControl().removeMouseListener(m_release);
		m_drag =  new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				updateLayout();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		};*/
		m_release = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				Object []k = g.getSelectionCells();
				updateLayout();
				g.addSelectionCells(k);
			}
		};
		gc.getGraphControl().addMouseMotionListener(m_drag);
		gc.getGraphControl().addMouseListener(m_release);
		
		//g = new mxGraph();
		
		updateLayout();
		try {
			construct();
		}catch(Exception er) {}
	}
	public void construct() throws Exception {
		this.isReady = false;
		g.selectAll();
		Object []k = edgeFilter(g.getSelectionCells());
		//new
		ArrayList <Object>vtx = vertexNameFilter(vertexFilter(g.getSelectionCells()),"");
		mxCell start_que = null;
		g.clearSelection();
		if(vtx.size() == 0||((mxCell)vtx.get(0)).getEdgeCount() == 0) {
			a_out.setText("No start state.");
			a_out.setForeground(Color.RED);
			throw new Exception("Nulll start");
		}
		//start_que = (mxCell)vtx.get(0);
		start_que = (mxCell)from_start;
		//System.out.println("start: "+((mxCell)from_start).getValue().toString());
		g.clearSelection();
		//transition func table
		 table = new HashMap<mxCell,HashMap<String,mxCell>>();
		//char
		ch1 = new TreeSet<String>();
		mxCell kkk;
		
		Queue<mxCell> que = new LinkedList<mxCell>();
		que.add(start_que);
		seen = new LinkedList<mxCell>();
		while(!que.isEmpty()) {
			mxCell tr = que.poll();
			//dont re-exploring
			if(!seen.contains(tr)) {
				seen.add(tr);
				for(int e = 0;e<tr.getEdgeCount();e++) {
					
					if(!seen.contains(((mxCell)tr.getEdgeAt(e)).getTarget()))
						que.add((mxCell)((mxCell)tr.getEdgeAt(e)).getTarget());
					
				}
			}
		}
		//System.out.println("From start has: "+seen.size());
		
		for(Object sss : k) {
			kkk = (mxCell)sss;
			//filtering out what isn't connecting w/ start node
			if(!seen.contains((mxCell)kkk.getSource()))
				continue;
			if(table.containsKey((mxCell)kkk.getSource())) {
				if(table.get((mxCell)kkk.getSource()).containsKey(kkk.getValue().toString())) {
					a_out.setText("Transition function is invalid");
					a_out.setForeground(Color.RED);
					throw new Exception("1 -> more than 1 state");
				}
				table.get((mxCell)kkk.getSource()).put(kkk.getValue().toString(), (mxCell)kkk.getTarget());
			}else {
				table.put((mxCell)kkk.getSource(), new HashMap<String,mxCell>());
				table.get((mxCell)kkk.getSource()).put(kkk.getValue().toString(), (mxCell)kkk.getTarget());
			}
		}
		
		
		//Map.Entry<String, Integer> it: s
		ArrayList <String> temp = new ArrayList<String>();
		String trans;
		for(Map.Entry<mxCell,HashMap<String,mxCell>> i : table.entrySet()) {
			if (i.getKey().getValue().toString().equalsIgnoreCase(""))
				continue;
			trans = "";
			//1System.out.print(i.getKey().getValue().toString()+" -> ");
			
			trans = "    {"+i.getKey().getValue().toString()+"} -> ";
			
			for(Map.Entry<String,mxCell> j : i.getValue().entrySet()) {
				//2System.out.print(j.getKey()+":"+j.getValue().getValue()+" ");
				//no enclosure{ :) }
				//trans += "<"+j.getKey()+":"+j.getValue().getValue()+">, ";
				trans += j.getKey()+":{"+j.getValue().getValue()+"}, ";
				if(j.getKey().length()>1) {
					a_out.setText("Some character is too long.");
					a_out.setForeground(Color.RED);
					throw new Exception("character is too long");
				}
				ch1.add(j.getKey());
			}
			
			temp.add(trans);
			//System.out.println(temp.get(temp.size()-1));
		}
		String fff = "";
		String t_out ="State(s): {";
		//fff += t_out;
		
		//g.selectAll();
		//k = vertexFilter(g.getSelectionCells());
		//g.clearSelection();
		System.out.println("size: "+seen.size());
		fff = "";
		for(int e=0;e<seen.size();e++) {
			if(!seen.get(e).getValue().toString().equalsIgnoreCase(""))
			//fff += ((mxCell)f).getValue().toString()+", ";
			fff += "{"+seen.get(e).getValue()+"}, ";
		}
		if(fff.indexOf(',') > 0)
			fff = fff.substring(0, fff.length()-2);
		
		
		t_out+= fff+"}\n";
		
		//4System.out.println("char size: "+ch1.size());
		t_out += "Character set: {";
		fff = "";
		for(String s:ch1)
			fff += s+", ";
		if(fff.indexOf(',') > 0)
			fff = fff.substring(0, fff.length()-2);
		
		t_out+=fff+"}\nTransition:\n";
		
		
		for(String f:temp) {
			//clean excessive , 
			t_out += f;
			t_out = t_out.substring(0, t_out.length()-2);
			t_out +="\n";
		}
		
		t_out+="Start state: ";
		
		//5System.out.print("Start State: ");
		if(from_start != null) {
			//6System.out.println(((mxCell)from_start).getValue().toString());
			t_out += "{"+((mxCell)from_start).getValue().toString()+"}";
		}
		else {
			//7System.out.println("-");
			t_out += "-";
		}
		
		t_out+="\n";
		t_out+="Accept state(s): {";	
		
		//8System.out.print("Accept State: {");
		fff = "";
		for(int e = 0;e<accept_node.size();e++) {
			if(!seen.contains(((mxCell)accept_node.get(e))))
				continue;
			//9System.out.print(((mxCell)accept_node.get(e)).getValue().toString()+" ");
			fff += "{"+((mxCell)accept_node.get(e)).getValue().toString()+"}, ";
		}
		if(fff.indexOf(',') > 0)
			fff = fff.substring(0, fff.length()-2);
		
		t_out += fff + "}";
		a_out.setText(t_out);
		
		//10System.out.println("\n------------------------");
		
		this.isReady = true;
		
		a_out.setForeground(Color.BLACK);
		this.updateLayout();
		
	}
	
	
	public mxGraphComponent getGc() {
		return this.gc;
	}
	private void addStart(Object[] k) {
		g.getModel().beginUpdate();
		if(k.length == 1) {
			
			if(START  != null&&((mxCell)START).getValue().toString().equalsIgnoreCase("")) {
				g.removeCells(new Object[] {START});
			}
			//start
			
			START = g.insertVertex(gp, null, "",
					((mxCell)k[0]).getGeometry().getCenterX()-100, ((mxCell)k[0]).getGeometry().getCenterY(),0,0);
			
			g.insertEdge(gp, null, "", START, k[0]);
			from_start = k[0];
			
		}else
			showError("Please select only one node.");
		updateLayout();
		g.getModel().endUpdate();
	}
	public void updateLayout() {
		
		paint_g();
		layout.execute(gp);
		
	}
	/*private void addStart(Object k) {
		
		g.getModel().beginUpdate();
		g.selectAll();
		Object[] ttt = g.getSelectionCells();
		for(Object sss : ttt) {
			if(((mxCell)sss).getValue().toString().equalsIgnoreCase(""))
				g.removeCells(new Object[] {sss});
		}
		
			if(START  != null) {
				g.removeCells(new Object[] {START});
				start_node = false;
			}
			
			START = g.insertVertex(gp, null, "",
					((mxCell)k).getGeometry().getCenterX()-100, ((mxCell)k).getGeometry().getY(),0,0);
			//g.insertEdge(gp, null, "", START, k);
			start_node = true;
			from_start = null;
		g.clearSelection();
		updateLayout();
		g.getModel().endUpdate();
	}*/
	
	private static Object[] vertexFilter(Object[] obj) {
		ArrayList<Object> temp = new ArrayList<>();
		for(Object sss:obj) {
			if(((mxCell)sss).isVertex())
				temp.add(sss);
		}
		//System.out.println("Vertex : "+temp.size());
		return temp.toArray();
	}
	private static Object[] edgeFilter(Object[] obj) {
		ArrayList<Object> temp = new ArrayList<>();
		for(Object sss:obj) {
			if(((mxCell)sss).isEdge())
				temp.add(sss);
		}
		//System.out.println("Edge : "+temp.size());
		return temp.toArray();
	}
	private static ArrayList<Object> vertexNameFilter(Object[] obj, String data) {
		ArrayList<Object> temp = new ArrayList<>();
		for(Object sss:obj) {
			if(((mxCell)sss).isVertex()&&((mxCell)sss).getValue().toString().equalsIgnoreCase(data))
				temp.add(sss);
		}
		//debug
		//System.out.println(data+" : "+temp.size());
		return temp;
	}
	private void resetStart() {
		START = null;
		from_start = null;
	}
	
	
	private boolean testString(String s,HashMap<String,mxCell> hsmx) throws Exception {
		//int inx = 0;
		
		mxCell res = null;
		if(hsmx == null) {
			return false;
			
		}
		
		
		res = hsmx.get(s.charAt(0)+"");
			
		
		if(s.length() == 1 && accept_node.contains(res)) {
			//System.out.print("Passed!");
			paintTest(res,true);
			return true;
		}else if(s.length() == 1) {
			paintTest(res,false);
			return false;
		}
		
		if(res != null)
			return testString(s.substring(1, s.length()),table.get(res));
		else
			//return testString(s.substring(1, s.length()),hsmx);
		{
			//dead state is invisible so final result isn't last feasible state
			return false;
		}
		
		
	}
	private void showError(String err) {
		JOptionPane.showMessageDialog(gc, err, "Error!", JOptionPane.ERROR_MESSAGE);
	}
	
	private void paintTest(mxCell res,boolean t) {
		if(res == null)
			return;
		g.getModel().beginUpdate();
		if(t)
			res.setStyle("fontSize="+font_size+";fillColor=#47e0ff;strokeColor=#000000;"
					+ "fontColor=#000000;shape=doubleEllipse;strokeWidth=1.75");
		else
			res.setStyle("fontSize="+font_size+";fillColor=#fc0303;strokeColor=#000000;"
					+ "fontColor=#000000;shape=ellipse;strokeWidth=1.75");
		
		gc.refresh();
		g.getModel().endUpdate();
	}
	
	//for test
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		
		JFrame test = new JFrame("FOR_TESTING");
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		AutomataPanel k = new AutomataPanel();
		test.getContentPane().add(k);
		test.setSize(1400, 900);
		test.setMinimumSize(new Dimension(1200, 900));
		test.setMaximumSize(new Dimension(1800,1080));
		test.getRootPane().addComponentListener(new ComponentAdapter() {
		      @Override
		      public void componentResized(ComponentEvent e) {
		        System.out.println("Resized to " + e.getComponent().getSize());
		       k.setSize(k.getSize().width, e.getComponent().getSize().height-10);
		       int t = test.getMaximumSize().height;
		       if(t >= e.getComponent().getSize().height)
		    	   t = e.getComponent().getSize().height;
		       k.getGc().setSize(k.getGc().getSize().width,t-10);
		       System.out.println("resize"+k.getGc().getHeight());
		       
		       test.validate();
		       test.repaint();
		       test.setVisible(true);
		      }
		});
		test.setVisible(true);
		test.repaint();
		
	}
}

//Natthawat khuwijitjaru on 1/5/2020 10:06 PM updated