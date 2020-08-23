import java.util.Arrays;
import java.util.HashMap;

import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;


class node_info{
	node_info(String p,String reason,double value,double weight,double bounds){
		this.pick_item = p;
		this.reason = reason;
		this.value = value;
		this.weight = weight;
		this.bounds = bounds;
	}
	String pick_item = "";
	String reason = "";
	double bounds = 0.0;
	double value;
	double weight;
}

public class bnb {
	Graph<String, pickedge> graph;
	int NEXT = 0;
	boolean infcal = false;
	double upper_bounds = -1;
	//double bounds = Double.POSITIVE_INFINITY;
	boolean[]finalpick;
	
	int[]former_order;
	
	int current_best = -1;
	//String current_best = "";
	
	boolean[]pickl;
	double[]wei;
	double[]val;
	double[]ratio;
	
	int[]temp_sort;
	
	double capacity;
	
	HashMap<Integer,node_info> info_map = new HashMap<Integer,node_info>();
	//Stack<steps> node_st = new Stack();
	public static void main(String[] args) {
		//double[]wei = {2,4,6,9};
		//double[]val = {10,10,12,18};
		/*double[] val= {360, 83, 59, 130, 431, 67, 230, 52, 93, 125, 670, 892, 600, 38, 48, 147,
	    	    78, 256, 63, 17, 120, 164, 432, 35, 92, 110, 22, 42, 50, 323, 514, 28, 87, 73, 78, 15, 26,
	    	    78, 210, 36, 85, 189, 274, 43, 33, 10, 19, 389, 276, 312};
	    double[] wei = {7, 0, 30, 22, 80, 94, 11, 81, 70, 64, 59, 18, 0, 36, 3, 8, 15, 42, 9,
	    	    0, 42, 47, 52, 32, 26, 48, 55, 6, 29, 84, 2, 4, 18, 56, 7, 29, 93, 44, 71, 3, 86, 66, 31,
	    	    65, 0, 79, 20, 65, 52, 13};*/
		double[]wei = {2,3.14,1.98,5,3};
		double[]val = {40,50,100,95,30};
		bnb x = new bnb(wei,val,10);
		x.add_root();
		x.knap_inf(0,x.pickl,"  START  ",new pickedge("pick"));
		//x.pickl[0] = false;
		//x.knap(0,x.pickl,"  START  ",new pickedge("not_pick"));
		double validate = 0.0;
		for(int e = 0;e<x.finalpick.length;e++) {
			if(x.finalpick[e]) {
				validate+= val[e];
				System.out.print(x.former_order[e]+" ");
			}
		}
		//System.out.println("\n:"+validate+" ans:"+x.mcost);
		//Natthawat khuwijitjaru 07600458
	}
	
	public bnb (double[]weigth,double []value,double capac) {
		//System.out.println("EEEEEEEEEEEEEEE");
		this.wei = weigth;
		this.val = value;
		this.capacity = capac;
		
		pickl = new boolean[wei.length];
		finalpick = new boolean[wei.length];
		Arrays.fill(pickl, true);
		Arrays.fill(finalpick,true);
		
		
		this.former_order = new int[wei.length];
		
		this.ratio = new double[wei.length];
		for(int e = 0;e<wei.length;e++) {
			ratio[e] =  val[e]/wei[e];
			former_order[e] = e+1;
		}
		double temp;
		int tempo;
		for(int e = 0;e<wei.length-1;e++) {
			for(int r = e+1;r<wei.length;r++) {
				if(ratio[e] < ratio[r]) {
					temp = ratio[e];
					ratio[e] = ratio[r];
					ratio[r] = temp;
					temp = wei[e];
					wei[e] = wei[r];
					wei[r] = temp;
					temp = val[e];
					val[e] = val[r];
					val[r] = temp;
					
					tempo = former_order[e];
					former_order[e] = former_order[r];
					former_order[r] = tempo;
					
				}
			}
		}
		temp_sort = new int[this.wei.length];
		
		graph = new SimpleGraph<String, pickedge>(pickedge.class);
		//	mxGraph mxgraph= new mxGraph();
			//mxGraphComponent graphComponent= new mxGraphComponent(mxgraph);
		//	graph.addVertex("  ROOT  ");
		/*for(int e = 0;e<ratio.length;e++) {
			System.out.println("W:"+wei[e]+" V:"+val[e]+" R:"+ratio[e] +" former: "+former_order[e]);
		}*/
	}
	int nodename = 1;
	
	public void add_root() {
		//graph = new SimpleGraph(pickedge.class);
		graph.addVertex("  START  ");
		
	}
	boolean step_fin = false;
	public void knap(int pos,boolean[]picklist,String root,pickedge pon) {
		//System.out.println("call");
		step_fin = true;
		while(NEXT<=0) {
			try {
				//System.out.println("BBB");
				Thread.sleep(33);
			} catch (InterruptedException e1) {
			}
		}
		/*try {
			System.out.println("Sleeping UwU");
			Thread.currentThread().wait();;
		} catch (InterruptedException e1) {
		}*/
		step_fin = false;
		NEXT = 0;
		
		double bounds = 0.0;
		double cost = 0.0;
		double cwei = 0.0;
		int e;
		for(e = 0;e<picklist.length;e++) {
			if(picklist[e]) {
				if(cwei+wei[e] < this.capacity) {
					cwei += wei[e];
					bounds += val[e];
				}else {
					bounds += (this.capacity-cwei)*ratio[e];
					break;
				}
			}	
		}
		cwei = 0;
		//double temp_e = 0.0;
		for(e = 0;e<pos;e++) {
			if(picklist[e]) {
				if(cwei+wei[e] <= this.capacity) {
					cwei += wei[e];
					cost += val[e];
				}else {
					String bls = "";
					temp_count = 0;
					for(int bg = 0;bg<pos;bg++) {
						//bls+= (picklist[bg])?former_order[bg]+", ":"";
						if(picklist[bg]) {
							temp_sort[temp_count] = former_order[bg];
							temp_count++;
						}
					}
					sort_int(temp_sort,temp_count);
					for(int er = 0;er<temp_count;er++) {
						bls+=temp_sort[er]+", ";
					}
					if(bls.length()>2)
						bls = bls.substring(0,bls.length()-2);
					String name = ""+nodename+"\n"+
							bls+"\nWeight: "+(String.format("%.2f",cwei+wei[e])+"\nInfeasible Solution");
					//temp_e = cwei+wei[e];
					name = ""+nodename+"\nWeight: "+(String.format("%.2f",cwei+wei[e])+"\nInfeasible!");
					//insert map
						info_map.put(nodename, 
								new node_info(bls, "Infeasible Solution!", cost+val[e], cwei+wei[e], bounds));
					graph.addVertex(name);
					graph.addEdge(root, name,pon);
					nodename++;
					return;
				}
			}	
		}
	
		String bls = "";
		temp_count = 0;
		for(int bg = 0;bg<pos;bg++) {
			//bls+= (picklist[bg])?former_order[bg]+", ":"";
			if(picklist[bg]) {
				temp_sort[temp_count] = former_order[bg];
				temp_count++;
			}
		}
		sort_int(temp_sort,temp_count);
		for(int er = 0;er<temp_count;er++) {
			bls+=temp_sort[er]+", ";
		}
		if(bls.length()>2)
			bls = bls.substring(0,bls.length()-2);
		//if(bls.length()>1)
		//	bls = bls.substring(0,bls.length()-1);
			//bls += "}";
		//String name = nodename+"\nValue: "+cost+"\nBounds: "+String.format("%.2f",bounds)+"\nWeight: "+cwei;
		String name = nodename+"\nValue: "+String.format("%.2f",cost)+"\nBounds: "+String.format("%.2f",bounds)+"\nWeight: "+String.format("%.2f",cwei);
		
		
		if(cost > this.upper_bounds&& pos == wei.length) {
			this.upper_bounds = cost;
			//System.out.println(""+cost);
			//this.current_best = "";
		}
			
		
		if(bounds == this.upper_bounds&& pos == wei.length) {
			this.current_best = nodename;
			//this.current_best +=" "+nodename;
			//System.out.println(this.current_best);
			Arrays.fill(finalpick, false);
			System.arraycopy(pickl, 0, finalpick, 0, pos);
			info_map.put(nodename, 
					new node_info(new String(bls), "Update Answer.", cost, cwei, bounds));
			graph.addVertex(name);
			graph.addEdge(root, name,pon);
			nodename++;
			
		}else if(bounds >= this.upper_bounds) {
			info_map.put(nodename, 
					new node_info(new String(bls), "Continue.", cost, cwei, bounds));
			graph.addVertex(name);
			graph.addEdge(root, name,pon);
			nodename++;
		}else if(bounds < this.upper_bounds){
			name +="\nIgnored!";
			info_map.put(nodename, 
					new node_info(new String(bls), "Ignored by bounds.", cost, cwei, bounds));
			graph.addVertex(name);
			graph.addEdge(root, name,pon);
			nodename++;
			return;
		}
		
		if(pos==picklist.length) {
			return;
		}
		knap(pos+1,picklist,name,new pickedge("pick "+former_order[pos]));
		picklist[pos] = false;
		knap(pos+1,picklist,name,new pickedge("Not pick "+former_order[pos]));
		picklist[pos] = true;
		
	}
	public void knap_start() {
		this.add_root();
		this.knap_inf(0,pickl,"  START  ",new pickedge(""));
		//this.nodename++;
		ifin = true;
		//System.out.println("bnb_finnn");
	}
	boolean ifin = false;
	public void knap_inf(int pos,boolean[]picklist,String root,pickedge pon) {
		
		double bounds = 0.0;
		double cost = 0.0;
		double cwei = 0.0;
		int e;
		for(e = 0;e<picklist.length;e++) {
			if(picklist[e]) {
				if(cwei+wei[e] < this.capacity) {
					cwei += wei[e];
					bounds += val[e];
				}else {
					bounds += (this.capacity-cwei)*ratio[e];
					break;
				}
			}	
		}
		cwei = 0;
		//double temp_e = 0.0;
		for(e = 0;e<pos;e++) {
			if(picklist[e]) {
				if(cwei+wei[e] <= this.capacity) {
					cwei += wei[e];
					cost += val[e];
				}else {
					String bls = "";
					temp_count = 0;
					for(int bg = 0;bg<pos;bg++) {
						//bls+= (picklist[bg])?former_order[bg]+", ":"";
						if(picklist[bg]) {
							temp_sort[temp_count] = former_order[bg];
							temp_count++;
						}
					}
					sort_int(temp_sort,temp_count);
					for(int er = 0;er<temp_count;er++) {
						bls+=temp_sort[er]+", ";
					}
					if(bls.length()>2)
						bls = bls.substring(0,bls.length()-2);
					
					String name = ""+nodename+"\n"+
							bls+"\nWeight: "+(String.format("%.2f",cwei+wei[e])+"\nInfeasible Solution");
					//temp_e = cwei+wei[e];
					name = ""+nodename+"\nWeight: "+(String.format("%.2f",cwei+wei[e])+"\nInfeasible!");
					//insert map
						info_map.put(nodename, 
								new node_info(bls, "Infeasible Solution!", cost+val[e], cwei+wei[e], bounds));
					graph.addVertex(name);
					graph.addEdge(root, name,pon);
					nodename++;
					return;
				}
			}	
		}
	
		String bls = "";
		temp_count = 0;
		for(int bg = 0;bg<pos;bg++) {
			//bls+= (picklist[bg])?former_order[bg]+", ":"";
			if(picklist[bg]) {
				temp_sort[temp_count] = former_order[bg];
				temp_count++;
			}
		}
		sort_int(temp_sort,temp_count);
		for(int er = 0;er<temp_count;er++) {
			bls+=temp_sort[er]+", ";
		}
		if(bls.length()>2)
			bls = bls.substring(0,bls.length()-2);
			//bls += "}";
		String name = nodename+"\nValue: "+String.format("%.2f",cost)+"\nBounds: "+String.format("%.2f",bounds)+"\nWeight: "+String.format("%.2f",cwei);
		
		
		if(cost > this.upper_bounds&& pos == wei.length) {
			this.upper_bounds = cost;
			//this.current_best = "";
			//System.out.println("ddd"+cost);
			//Natthawat khuwijitjaru 07600458
		}
			
		
		if(bounds == this.upper_bounds&& pos == wei.length) {
			this.current_best = nodename;
			//this.current_best +=" "+nodename;
			//System.out.println(this.current_best);
			Arrays.fill(finalpick, false);
			System.arraycopy(pickl, 0, finalpick, 0, pos);
			info_map.put(nodename, 
					new node_info(new String(bls), "Update Answer.", cost, cwei, bounds));
			graph.addVertex(name);
			graph.addEdge(root, name,pon);
			nodename++;
			
		}else if(bounds >= this.upper_bounds) {
			info_map.put(nodename, 
					new node_info(new String(bls), "Continue.", cost, cwei, bounds));
			graph.addVertex(name);
			graph.addEdge(root, name,pon);
			nodename++;
		}else if(bounds < this.upper_bounds){
			name +="\nIgnored!";
			info_map.put(nodename, 
					new node_info(new String(bls), "Ignored by bounds.", cost, cwei, bounds));
			graph.addVertex(name);
			graph.addEdge(root, name,pon);
			nodename++;
			return;
		}
		
		if(pos==picklist.length) {
			return;
		}
		/*knap_inf(pos+1,picklist,name,new pickedge("pick "+(pos+1)));
		picklist[pos] = false;
		knap_inf(pos+1,picklist,name,new pickedge("Not pick "+(pos+1)));
		picklist[pos] = true;*/
		//Natthawat khuwijitjaru 07600458
		knap_inf(pos+1,picklist,name,new pickedge("pick "+former_order[pos]));
		picklist[pos] = false;
		knap_inf(pos+1,picklist,name,new pickedge("Not pick "+former_order[pos]));
		picklist[pos] = true;
	}
	int temp_count;
	
	//Natthawat khuwijitjaru 07600458
	private void sort_int(int[]arr,int len) {
		int temp;
		for(int e = 0;e<len-1;e++) {
			for(int r = e+1;r<len;r++) {
				if(arr[e] > arr[r]) {
					temp = arr[e];
					arr[e] = arr[r];
					arr[r] = temp;
				}
			}
		}
	}
}



