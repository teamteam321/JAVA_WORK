import java.awt.Color;
import java.util.HashMap;

import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

class nnn{
	public boolean equals(Object o){
		if (o == this) { 
            return true; 
        } 
        if (!(o instanceof nnn)) { 
            return false; 
        } 
        return false;
	}
}

public class graph_or {
	boolean enter_scrp = false;
	int[]wei;
	Graph<String, pickedge> g;
	
	private mxGraph adapter;
	private mxGraphComponent component;
	private mxCompactTreeLayout layout;
	private bnb bbb;
	public boolean ISFIN = false;
	
	public double gx = 0,gy = 0;
	private Object current_node;
	private Object current_best;
	private Object[] temp3;
	private Object[] temp2;
	private Object[] temp;
	String temp_value;
	String  []temp_split;
	private Thread step;
	/*public void init_bnb_graph(double []wei,double[]val,int capa) {
		bbb = new bnb(wei,val,10);
		g = bbb.graph;
		Thread s = new Thread() {
			public void run() {
				bbb.add_root();
				bbb.knap(0,bbb.pickl,"  ROOT  ",new pickedge("-"));
				//g = bbb.graph;
				//bbb.pickl[0] = false;
				//bbb.knap(0,bbb.pickl,"  ROOT  ",new pickedge("not_pick"));
			}
		};
		s.start();
	}*/
	graph_or(double[]wei,double[]val,double capa){
		 bbb = new bnb(wei,val,capa);
		 g = bbb.graph;
		 
		
		/*Thread s = new Thread() {
			public void run() {
				
				bbb.add_root();
				bbb.knap(0,bbb.pickl,"  START  ",new pickedge(""));
			//Natthawat khuwijitjaru 07600458
				ISFIN = true;
				//prevent last node appeared purpled after finished 
				//cause: next button disable after ISFIN set to true 
				//System.out.println("DEAD");
			}
		};
		s.start();*/
	}
	public double getBounds() {
		return bbb.upper_bounds;
	}
	public HashMap<Integer,node_info> getInfoMap() {
		return bbb.info_map;
	}
	public HashMap<Integer,node_info> draw_start() {
		bbb.knap_start();
		this.ISFIN = true;
		this.update_graph_view();

		return bbb.info_map;
	}
	public void nextstep() {
		bbb.NEXT = 1;
	}
	
	void init_step() {
		step = new Thread() {
			public void run() {
				
				bbb.add_root();
				bbb.knap(0,bbb.pickl,"  START  ",new pickedge(""));
				
				
			
				ISFIN = true;
			}
		};
		step.start();
	}
	public HashMap<Integer,node_info> step_add() {
		nextstep();
		/*System.out.println(step.getState().toString());
		step.notify();*/
		do{
			try {
				//System.out.println("WAITING"+ISFIN);
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}while(!bbb.step_fin&&!this.ISFIN);
		
		this.update_graph_view();
		
		return bbb.info_map;
		
		/*adapter = new JGraphXAdapter<>(g);
		component = new mxGraphComponent(adapter);
		component.setConnectable(false);
		component.getGraph().setAllowDanglingEdges(false);
		component.setSize(500, 500);
		component.setBounds(0, 0, 500, 500);
		component.setBackground(Color.WHITE);
		layout = new mxCompactTreeLayout(adapter);
		layout.setHorizontal(false);
		layout.setLevelDistance(50);
		layout.setNodeDistance(50);
		layout.setMoveTree(false);
		layout.execute(adapter.getDefaultParent());
		adapter.clearSelection();
		adapter.selectAll();
		Object[]temp = adapter.getSelectionCells();
		
		mxGraph MG = component.getGraph();
		MG.setCellStyle("fillColor=#d3d3d3"); // passed node
		
		
		adapter.clearSelection();
		Object[]temp2 = new Object[temp.length];
		Object[]temp3 = new Object[temp.length];
		Object current_node = null;
		Object current_best = null;
		int no = 0;
		int no2 = 0;
		for(Object s:temp) {
			
			mxCell cell = (mxCell)s;
			if(cell.isEdge())
				continue;
			
			if(cell.getValue().toString().contains("Solution")) {
				temp2[no++] = cell;
			}else if(cell.getValue().toString().contains("profit")) {
				temp3[no2++] = cell;
			}else if(cell.getValue().toString().split("\n")[0].contains(bbb.current_best+"")) {
				current_best = cell;
			}
			if(cell.getValue().toString().split("\n")[0].contains(bbb.nodename-1+"")) {
				current_node = cell;
			}
			if(cell.getValue().toString().split("\n")[0].contains(bbb.nodename-1+"")) {
				this.gx = cell.getGeometry().getX();
				this.gy = cell.getGeometry().getY();System.out.println(gx+" XXX "+gy);
			}
			
		}
		
		adapter.clearSelection();
		adapter.setSelectionCells(temp2);
		MG.setCellStyle("fillColor=#ffd700");
		//MG.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#ffd700");
		adapter.clearSelection();
		adapter.setSelectionCells(temp3);
		MG.setCellStyle("fillColor=#ffb6c1");
		adapter.clearSelection();
		
		adapter.setSelectionCell(current_best);
		MG.setCellStyle("fillColor=#00ff00");
		adapter.clearSelection();
		
		adapter.setSelectionCell(current_node);
		MG.setCellStyle("fillColor=#ff00ff");
		adapter.clearSelection();
		adapter.selectAll();
		
		MG.setResetEdgesOnResize(true);
		
		
		MG.setCellStyles(mxConstants.STYLE_FONTSIZE, "18");
		MG.setCellStyles(mxConstants.STYLE_MOVABLE, "0");
		MG.setCellStyles(mxConstants.STYLE_RESIZABLE, "0");
		MG.setCellStyles(mxConstants.STYLE_EDITABLE, "0");
		MG.setCellStyles(mxConstants.W3C_SHADOWCOLOR, "1");
		for(Object s:temp) {
			mxCell cell = (mxCell)s;
			MG.updateCellSize(cell,true);
		}
		adapter.clearSelection();*/
	}
	
	
	public mxGraphComponent getgc() {
		
		return this.component;
	}
	
	private void update_graph_view() {
		//long st = System.currentTimeMillis();
		//System.out.println("Start:"+(System.currentTimeMillis()-st)/1000);
		
		adapter = new JGraphXAdapter<>(g);
		component = new mxGraphComponent(adapter);
		component.setConnectable(false);
		component.getGraph().setAllowDanglingEdges(false);
		component.setSize(500, 500);
		component.setBounds(0, 0, 500, 500);
		component.setBackground(Color.WHITE);
		component.setDragEnabled(false);
		
		layout = new mxCompactTreeLayout(adapter);
		layout.setHorizontal(false);
		layout.setLevelDistance(37);
		layout.setNodeDistance(37);
		layout.setMoveTree(false);
		layout.execute(adapter.getDefaultParent());
		adapter.clearSelection();
		adapter.selectAll();
		temp = adapter.getSelectionCells();
		
		mxGraph MG = component.getGraph();
		MG.setCellStyle("fillColor=#d3d3d3"); // passed node
		//MG.setCellStyles(mxConstants.STYLE_FONTSIZE, "14");
		MG.setCellStyleFlags(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD, true);
		MG.setCellStyles(mxConstants.STYLE_MOVABLE, "0");
		MG.setCellStyles(mxConstants.STYLE_RESIZABLE, "0");
		MG.setCellStyles(mxConstants.STYLE_EDITABLE, "0");
		//MG.setCellStyles(mxConstants.W3C_SHADOWCOLOR, "1");
		MG.setCellStyles(mxConstants.STYLE_AUTOSIZE, "1");
		//MG.setCellStyles(mxConstants.ed)
		//System.out.println("Start2:"+(System.currentTimeMillis()-st)/1000);
		
		adapter.clearSelection();
		temp2 = new Object[temp.length];
		temp3 = new Object[temp.length];
		current_node = null;
		current_best = null;
		int no = 0;
		int no2 = 0;
		
		for(Object s:temp) {
			
			mxCell cell = (mxCell)s;
			if(cell.isEdge())
				continue;
			
			temp_value = cell.getValue().toString();
			temp_split = temp_value.split("\n");
			if(temp_value.contains("Infeasible!")) {
				temp2[no++] = cell;
			}else if(temp_value.contains("Ignored!")) {
				temp3[no2++] = cell;
			}else if(temp_split[0].contains(bbb.current_best+"")) {
				current_best = cell;
			}
			if(temp_split[0].contains(bbb.nodename-1+"")) {
				current_node = cell;
			}
			if(temp_split[0].contains(bbb.nodename-1+"")) {
				this.gx = cell.getGeometry().getX();
				this.gy = cell.getGeometry().getY();
				//System.out.println(gx+" XXX "+gy);
			}
			
		}
		//System.out.println("Start loop color:"+(System.currentTimeMillis()-st)/1000);
		adapter.clearSelection();
		adapter.setSelectionCells(temp2);
		MG.setCellStyles("fillColor","ffd700");
		//MG.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#ffd700");
		adapter.clearSelection();
		adapter.setSelectionCells(temp3);
		MG.setCellStyles("fillColor","ffb6c1");
		adapter.clearSelection();
		
		adapter.setSelectionCell(current_best);
		//System.out.println(bbb.current_best+"current best"+(current_best == null));
		MG.setCellStyles("fillColor","00ff00");
		adapter.clearSelection();
		
		if(!this.ISFIN) {
			adapter.setSelectionCell(current_node);
			MG.setCellStyles("fillColor","ff00ff");
			adapter.clearSelection();
		}
		
		adapter.selectAll();
		
		//MG.setResetEdgesOnResize(true);
		
		adapter.clearSelection();
		//System.out.println("after color:"+(System.currentTimeMillis()-st)/1000);
		//Natthawat khuwijitjaru 07600458
		/*MG.setCellStyles(mxConstants.STYLE_FONTSIZE, "18");
		MG.setCellStyles(mxConstants.STYLE_MOVABLE, "0");
		MG.setCellStyles(mxConstants.STYLE_RESIZABLE, "0");
		MG.setCellStyles(mxConstants.STYLE_EDITABLE, "0");
		MG.setCellStyles(mxConstants.W3C_SHADOWCOLOR, "1");
		for(Object s:temp) {
			mxCell cell = (mxCell)s;
			MG.updateCellSize(cell,true);
		}*/
		/*for(Object s:temp) {
			mxCell cell = (mxCell)s;
			MG.updateCellSize(cell,true);
		}*/
		
		//System.out.println("fin:    "+(System.currentTimeMillis()-st)/1000);
	}
	
	/*public void add_tree(int level,String prt,Graph <String, pickedge>g) {
		
		if(level == wei.length)
			return;
		String name1 = prt+"+"+wei[level];
		String name2 = prt+"+0";
		
		g.addVertex(name1);
		g.addVertex(name2);
		g.addEdge(prt,name1,new pickedge("Pick"));
		g.addEdge(prt,name2,new pickedge("Not_pick"));
		
		add_tree(level+1,name1,g);
		add_tree(level+1,name2,g);
	}*/
}
