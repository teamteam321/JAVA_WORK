
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeSet;

import com.mxgraph.model.mxCell;
//import com.mxgraph.view.mxGraph;

public class Parser {
	
	//private BufferedReader read;
	private LinkedHashMap<String,mxab> dict;
	
	//private mxGraph g;
	private LinkedList<mxCell>seen;
	private HashMap<mxCell, HashMap<String, mxCell>> table;
	private BufferedWriter wrt;
	private LinkedList<Object> accept;
	private TreeSet<String> charset;
	
	//private mxGraph gB;
	private LinkedList<mxCell>seenB;
	private HashMap<mxCell, HashMap<String, mxCell>> tableB;
	//private BufferedWriter wrtB;
	private LinkedList<Object> acceptB;
	private TreeSet<String> charsetB;
	
	private HashMap<mxab, HashMap<String, mxab>> table_result = null;
	private TreeSet <String> charset_result = null;
	//private LinkedList<Object> accept_result = null;
	private LinkedList<mxab> seen_result = null;
	
	Parser(TreeSet<String> ch1,
			LinkedList<mxCell>seen,
			LinkedList<Object>accept, 
			HashMap<mxCell,HashMap<String, mxCell>> table,
			File file){
		
		
		this.seen = seen;
		this.table = table;
		this.accept = accept;
		this.charset = ch1;
		
		//this.accept_result = new LinkedList<Object>(this.accept);
		File text_out = file;
		try {
			wrt = new BufferedWriter(new FileWriter(text_out));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void addB(TreeSet<String> ch1b,
			LinkedList<mxCell>seenb,
			LinkedList<Object>acceptb, 
			HashMap<mxCell, HashMap<String, mxCell>> tableb) {
		
		//for second operand
		
		this.seenB = seenb;
		this.tableB = tableb;
		this.acceptB = acceptb;
		this.charsetB = ch1b;
		
	}
	
	public void merge(boolean mode) {
		Queue <String> que = new LinkedList<String>();
		dict = new LinkedHashMap<String,mxab>();
		String startk = seen.get(0).getValue()+","+seenB.get(0).getValue();
		
		dict.put(startk, new mxab(seen.get(0),seenB.get(0)));
		que.add(startk);
		//new table
		table_result = new HashMap<mxab,HashMap<String,mxab>>();
		//new charset
		charset_result = new TreeSet<String>(charset);
		//combind accept state 
		//start state = startk(string) || seen,seenB.get(1)
		
		for(String s:charsetB)
			if(!charset_result.contains(s))
				charset_result.add(s);
		
		mxCell from_a = null;
		mxCell from_b = null;
		
		while (!que.isEmpty()) {
			mxab temp = dict.get(que.poll());
			// first 1,1
				//test a or b by charset
			for (String sss :charset_result) {
				from_a = null;
				from_b = null;
				
				if(temp.a!=null && table.get(temp.a) != null)
					from_a = table.get(temp.a).get(sss);
				if(temp.b!=null && tableB.get(temp.b) != null)
					from_b = tableB.get(temp.b).get(sss);
				
				
				
				/*if(from_a != null&&from_b == null) {
					
					if(!dict.containsKey(from_a.getValue().toString())) {
						dict.put(from_a.getValue().toString(), new mxab(from_a,null));
						que.add(from_a.getValue().toString());
					}
					if(!table_result.containsKey(temp)) {
						table_result.put(temp, new HashMap<String,mxab>());
						table_result.get(temp).put(sss, dict.get(from_a.getValue().toString()));
					}
					else {
						table_result.get(temp).put(sss, dict.get(from_a.getValue().toString()));
					}
						
					System.out.print(temp.a.getValue().toString()+"->");
					System.out.println(from_a.getValue().toString());
				}
					
				else if(from_a == null&&from_b != null) {
					if(!dict.containsKey(from_b.getValue().toString())) {
						dict.put(from_b.getValue().toString(), new mxab(null,from_b));
						que.add(from_b.getValue().toString());
					}
					if(!table_result.containsKey(temp)) {
						table_result.put(temp, new HashMap<String,mxab>());
						table_result.get(temp).put(sss, dict.get(from_b.getValue().toString()));
					}
					else {
						table_result.get(temp).put(sss, dict.get(from_b.getValue().toString()));
					}
					System.out.print(temp.b.getValue().toString()+"->");
					System.out.println(from_b.getValue().toString());
				}
					
				else */
					
				if(from_a != null||from_b != null){
				
					String key = (from_a != null)? from_a.getValue().toString() : "-";
						key += ",";
						key += 	(from_b != null)? from_b.getValue().toString() : "-";
						
					if(!dict.containsKey(key)) {
						
						dict.put(key,
								new mxab(from_a,from_b));
						que.add(key);
					}
					
					if(!table_result.containsKey(temp)) {
						table_result.put(temp, new HashMap<String,mxab>());
						table_result.get(temp).put(sss, 
								dict.get(key));
					}
					else {
						//System.out.println("put!!");
						table_result.get(temp).put(sss, 
								dict.get(key));
					}
					
					/*System.out.print(temp.a.getValue().toString()
							+","+temp.b.getValue().toString()+"->");
					System.out.println(from_a.getValue().toString()
								+","+from_b.getValue().toString());*/
					
					
				}
					
			}

		}
		
		this.seen_result = new LinkedList<mxab>();
		
		for(Map.Entry<String, mxab> show:dict.entrySet()) {
			//push result to seen?
			seen_result.add(show.getValue());
			System.out.println("["+show.getKey()+"]"+((show.getKey().equalsIgnoreCase(startk))?"Start":""));
		}
		for(Map.Entry<mxab, HashMap<String,mxab>> show:table_result.entrySet()) {
			System.out.print(show.getKey().getValue()+":= "+show.getValue().size());
			if(mode) {
					System.out.println(" "+
							((accept.contains(show.getKey().a) ||
							acceptB.contains(show.getKey().b))?"(Accept)":""));
					
			}
			else {
				System.out.println(" "+
						((accept.contains(show.getKey().a) &&
						acceptB.contains(show.getKey().b))?"(Accept)":""));
				
			}
			for(Map.Entry<String,mxab> j : show.getValue().entrySet()) {
				System.out.println(j.getKey()+"->"+j.getValue().getValue());
			}
		}
		
	}
	//ONLY FOR UNION AND INTERSECT
	public void dump_res(boolean mode) throws Exception{
		String s = "";
		for(String a:charset_result) {
			s += a+",";
		}
		if(s.length()!=0)
			s = s.substring(0, s.length()-1);
		
			
		wrt.write(s);
		wrt.newLine();
		
		wrt.write(seen_result.size()+"");
		wrt.newLine();
		int t_count = 0;
		//state
		for(mxab x:seen_result) {
		//for(Map.Entry<String, mxab> K : dict.entrySet()) {
			//mxab x = K.getValue();
			double xcoor = (x.a != null)? x.a.getGeometry().getX():0.0;
			xcoor += (x.b != null)? x.b.getGeometry().getX() : 0.0;
			double ycoor = (x.a != null)? x.a.getGeometry().getY():0.0;
			xcoor += (x.b != null)? x.b.getGeometry().getY() : 0.0;
			
			wrt.write((x.getValue()+" "+ 
			//wrt.write((K.getKey()+" "+ 
					(xcoor)+" "+
					(ycoor)).trim());
			wrt.newLine();
			if(table_result.get(x) != null)
				t_count+=table_result.get(x).size();
		}
		//edge
		wrt.write(t_count+"");
		wrt.newLine();
		for(mxab x:seen_result) {
		//for(Map.Entry<String, mxab> K : dict.entrySet()) {
			//mxab x = K.getValue();
			if(table_result.get(x) != null) {
				for(Map.Entry<String, mxab> et : table_result.get(x).entrySet()) {
					wrt.write((x.getValue()+" "+et.getKey()+" "+et.getValue().getValue()).trim());
					//wrt.write((K.getKey()+" "+et.getKey()+" "+et.getValue().getValue()).trim());
					wrt.newLine();
				}
			}
		}
		//edge
		
		for(mxab x:seen_result) {
			//mxCell k = (mxCell)x;
			if(mode) {
				if(accept.contains(x.a) || acceptB.contains(x.b))
					wrt.write(x.getValue());
				else
					continue;
			}else {
				if(accept.contains(x.a) && acceptB.contains(x.b))
					wrt.write(x.getValue());
				else
					continue;
			}
			
			wrt.newLine();
		}
		
		wrt.flush();
		wrt.close();
		
	}
	
	public void dump() throws Exception{
		String s = "";
		for(String a:charset) {
			s += a+",";
		}
		if(s.length()!=0)
			s = s.substring(0, s.length()-1);
		
			
		wrt.write(s);
		wrt.newLine();
		
		wrt.write((seen.size())+"");
		wrt.newLine();
		int t_count = 0;
		//state
		for(mxCell x:seen) {
			if(x.getValue().toString().length() == 0)
				continue;
			wrt.write((x.getValue()+" "+ x.getGeometry().getX()+" "+x.getGeometry().getY()).trim());
			wrt.newLine();
			if(table.get(x) != null)
				t_count+=table.get(x).size();
		}
		//edge
		wrt.write(t_count+"");
		wrt.newLine();
		for(mxCell x:seen) {
			if(table.get(x) != null) {
				for(Map.Entry<String, mxCell> et : table.get(x).entrySet()) {
					if(x.getValue().toString().length() == 0)
						continue;
					wrt.write((x.getValue()+" "+et.getKey()+" "+et.getValue().getValue()).trim());
					wrt.newLine();
				}
			}
		}
		//edge
		
		for(Object x:accept) {
			mxCell k = (mxCell)x;
			if(!seen.contains(k))
				continue;
			wrt.write(k.getValue()+"");
			wrt.newLine();
		}
		
		wrt.flush();
		wrt.close();
		
	}

	public static AutomataPanel load(AutomataPanel X,File open)throws Exception {
		X.reset();
		HashMap<String,mxCell> dict = new HashMap<String,mxCell>();
		try {
			BufferedReader read = new BufferedReader(new FileReader(open));
			
			String [] chl = read.readLine().split(",");
			int total_state = Integer.parseInt(read.readLine());
			
			for(int e = 0;e<total_state;e++) {
				
				chl = read.readLine().split(" ");
				//System.out.println("line len: "+chl.length);
				mxCell f = (mxCell) X.getGraph().insertVertex(X.getGraph().getDefaultParent(),
						null, chl[0], Double.parseDouble(chl[1]), Double.parseDouble(chl[2]), 60,60);
				
				dict.put(chl[0], f);
				
				if(e == 0) {
					mxCell START = (mxCell) X.getGraph().insertVertex(X.getGraph().getDefaultParent(),
							null, "",
							((mxCell)f).getGeometry().getCenterX()-100, ((mxCell)f).getGeometry().getCenterY(),0,0);
					X.getGraph().insertEdge(X.getGraph().getDefaultParent(), null, "", START, f);
					X.setStart(START);
					X.setFromStart(f);
				}
				System.out.println(chl[0]+" "+Double.parseDouble(chl[1])+" "+Double.parseDouble(chl[2]));
			}
			
			int total_trans = Integer.parseInt(read.readLine());
			
			for(int e = 0;e<total_trans;e++) {
				chl = read.readLine().split(" ");
				
				X.getGraph().insertEdge(X.getGraph().getDefaultParent(),
						null,chl[1], dict.get(chl[0]), dict.get(chl[2]));
				
				System.out.println(chl[0]+" "+chl[1]+" "+chl[2]);
			}
			String acc = null;
			while((acc = read.readLine())!= null) {
				System.out.println(acc);
				X.getAccept().add(dict.get(acc));
			}
			
			
			//X.updateLayout();
			X.construct();
			read.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return X;
	}
	
}

class mxab{
	mxCell a = null,b = null;
	mxab(mxCell a,mxCell b){
		this.a = a;
		this.b = b;
	}
	public String getValue() {
		if(a!=null && b!=null)
			return a.getValue()+","+b.getValue();
		else if(a != null)
			return a.getValue().toString()+",-";
		else if(b != null)
			return "-,"+b.getValue().toString();
		else
			return "-,-";
			
	}
}
//Natthawat khuwijitjaru on 1/5/2020 10:06 PM updated