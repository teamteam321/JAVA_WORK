import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class maximum_is extends Thread {
	private static volatile long state = 0xCAFEBABE;
	private int mexit;
	private String sexit;

	private int calc = 0;
	private ArrayList<String> member ;
	private ArrayList<String> maxstr ;
	private long start, stop;
	private int max = -2;

	private int[][] th_copy;

	private int min_edge2;
	private int[][] ref_test2;
	private int maxim;
	private String e11;
	private String save_n;
	private int start_pos, stop_pos, step_n;
	Random rrnd;
	String th_name;

	public int get_start() {
		return this.start_pos;

	}

	public int get_stop() {
		return this.stop_pos;
	}

	public String res_String() {
		return this.maxstr.get(this.maxstr.size() - 1);
	}

	public int res_total() {
		return this.calc;
	}

	public String thread_name() {
		return this.th_name;
	}

	public long time_taken() {
		return this.stop - this.start;
	}

	public int res_mis() {
		return this.max;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		if (args.length <= 0) {
			wel_S();
			args = new String[1];
			args[0] = "graph.mis";
			File testgh = new File("graph.mis");

			if (testgh.canRead()) {
				System.out.print(" >graph.mis is present, continue? (Y/N) :");
				char ync = sc.next().trim().charAt(0);
				if (ync == 'N' || ync == 'n') {
					System.out.println(" >Exiting...");
					System.exit(2);
				}
			} else {
				System.out.println(" >graph.mis is not present, Exiting...\n===========================");
				System.exit(2);
			}
		}

		File readgh = new File(args[0]);
		if (readgh.canRead() == false) {
			System.err.println("File (" + readgh.getAbsolutePath() + ") is not found or can't be read!\nExiting...");
			System.exit(-1);
		}
		System.out.println(" >Starting...");
		String line = "", lline = "", splitline[];
		int graph_dim = 0;
		int tvv = 0, tuu = 0;
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(readgh));

			while ((line = bfr.readLine()).charAt(0) != 'p' && (line.charAt(1) != ' '))
				;

			splitline = line.split(" ");
			graph_dim = Integer.parseInt(splitline[2]);
			int edge_no = 0;

			int[][] original = new int[graph_dim + 1][graph_dim + 1];
			while ((line = bfr.readLine()) != null && line.length() > 1) {
				if (line.charAt(0) != 'e' && line.charAt(1) != ' ')
					continue;
				edge_no++;
				splitline = line.split(" ");

				tvv = Integer.parseInt(splitline[1]);
				tuu = Integer.parseInt(splitline[2]);
				original[tvv][tuu] = 1;
				original[tuu][tvv] = 1;

			}
			String res_name = args[0];
			res_name = res_name.substring(0, res_name.lastIndexOf("."));
			res_name += "_Result.txt";
			File bef = new File(res_name);
			bef.createNewFile();
			BufferedWriter ans = new BufferedWriter(new FileWriter(bef));

			String date = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(new Date());

			ans.write("// " + date);
			ans.newLine();
			ans.write("The Graph has: " + graph_dim + " vertices and " + edge_no + " edges");
			ans.newLine();
			ans.flush();
			ans.close();
			int max = -2;
			String res = "";

			int n_time = 1;

			// main _ thread
			int loopcount = 1;
			boolean contin = true;
			ThreadGroup tg;
			int proc_no;
			ArrayList<maximum_is> running;
			int st, op;

			System.out.println(" >The Graph has: " + graph_dim + " vertices and " + edge_no + " edges...");
			char ch3, ch4;
			/// here

			while (contin) {

				ans = new BufferedWriter(new FileWriter(bef, true));

				tg = new ThreadGroup("main" + loopcount);
				proc_no = Runtime.getRuntime().availableProcessors();
				running = new ArrayList<maximum_is>();

				st = 0;
				op = original.length / proc_no;
				if (original.length >= 12) {

					int cv = 1;
					while (cv < proc_no) {
						running.add(
								new maximum_is(original, st, st += op - 1, n_time, res_name, "MIS_0" + (cv - 1), tg));
						cv++;
					}
					if (st < original.length)
						running.add(new maximum_is(original, st, original.length - 1, n_time, res_name,
								"MIS_0" + (cv - 1), tg));

				} else {
					running.add(new maximum_is(original, 0, original.length - 1, n_time, res_name, "MIS_00", tg));
				}

				int th_left = 0;

				while (th_left < running.size()) {
					if (tg.activeCount() < proc_no) {
						maximum_is tober = running.get(th_left);
						tober.start();
						th_left++;
					} else {
						try {
							Thread.sleep(50);
						} catch (InterruptedException es) {
							es.printStackTrace();
						}
					}
				}
				while (tg.activeCount() > 0) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException es) {
						es.printStackTrace();
					}
				}

				for (int e = 0; e < running.size(); e++) {
					if (running.get(e).res_mis() > max) {
						max = running.get(e).res_mis();
						res = running.get(e).res_String();
					}
				}
				System.out.println(" .\n ..\n ...\n >Success! Time taken: " + running.get(0).time_taken() + " ms ("
						+ running.get(0).time_taken() / 1000 + " s)\n===============================\n > MIS = " + max
						+ "\n >" + " {" + res.substring(res.indexOf(":") + 1, res.length()) + " }");

				ans = new BufferedWriter(new FileWriter(bef, true));
				ans.newLine();
				ans.write(loopcount + ". MIS size = " + max);
				ans.newLine();
				ans.write("\n{" + res.substring(res.indexOf(":") + 1, res.length()) + " }");
				ans.newLine();
				ans.write("==============================");

				ans.flush();
				ans.close();

				loopcount++;
				n_time++;
				if (n_time > graph_dim + 2)
					break;
				System.out.print("===============================\n >Is the result satisfy? (Y:exit / N:continue)? : ");
				ch3 = sc.next().charAt(0);
				if (ch3 == 'y' || ch3 == 'Y')
					break;

				System.out.println(" >Re-calculating...");
			}
			System.out.println(" >Result saved as: " + res_name);

		} catch (NumberFormatException n) {
			System.err.println(n);
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}

	}

	private static void wel_S() {
		for (int e = 0; e < 5; e++)
			System.out.println();
		System.out.println(
				"==========================================================\n >Use this Program in command line mode.\n >The first argument is ASCII Dimac graph format (.mis)");
		System.out.println("  Example \n          :java MIS_program <graph_name>.mis");
		System.out.println("\n  Or copy this class to the same directory of graph file \n          :java MIS_program");
		System.out.println(
				"\n  and Program will automatically search for \"graph.mis\"\n==========================================================");
	}

	maximum_is(int[][] send, int start, int stop, int step, String save_name, String name, ThreadGroup tg) {
		super(tg, name);
		th_name = name;

		this.start_pos = start;
		this.stop_pos = stop;
		this.step_n = step;
		this.save_n = save_name;
		th_copy = new int[send.length][send.length];
		for (int cp1 = 0; cp1 < th_copy.length; cp1++) {
			for (int cp2 = 0; cp2 < th_copy.length; cp2++) {
				th_copy[cp1][cp2] = send[cp1][cp2];
			}
		}
		rrnd = new Random();
	}

	public void run() {
		try {
			this.maxstr  = new ArrayList<String>();
			this.member  = new ArrayList<String>();
			compute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void compute() throws IOException {

		int[][] temp_sn = new int[th_copy.length][th_copy.length];
		int[][] ref_test;
		int min_edge;

		start = System.currentTimeMillis();

		for (int e = start_pos; e < stop_pos; e++) {
			temp_sn = new int[th_copy.length][th_copy.length];

			for (int cp1 = 0; cp1 < th_copy.length; cp1++) {
				for (int cp2 = 0; cp2 < th_copy.length; cp2++) {
					temp_sn[cp1][cp2] = th_copy[cp1][cp2];
				}
			}
			remove_startn(temp_sn, e);
			ref_test = ref_gen(temp_sn);
			min_edge = ref_sort2(ref_test);

			if (ref_test.length == 0) {
				mexit = indep_f(temp_sn);
				calc++;
				sexit = indep_chk(temp_sn);
				if (mexit >= max) {
					maxstr.add(sexit);
					max = mexit;
				}
				continue;
			}
			recursive_n(temp_sn, ref_test, min_edge, step_n, rrnd);
		}

		this.stop = System.currentTimeMillis();

	}

	private void recursive_n(int[][] temp_sn, int[][] ref_test, int min_edge, int n_time, Random rrnd)
			throws IOException {
		int[][] minedge1 = new int[temp_sn.length][temp_sn.length];
		for (int me = 0; me < min_edge; me++) {
			// copy graph n rem
			for (int cp1 = 1; cp1 < temp_sn.length; cp1++) {
				for (int cp2 = 0; cp2 < temp_sn.length; cp2++) {
					minedge1[cp1][cp2] = temp_sn[cp1][cp2];
				}
			}

			if (n_time == 0) {

				remove_com(minedge1, ref_test[me][1], rrnd);

				e11 = indep_chk(minedge1);
				maxim = indep_f(minedge1);

				calc++;

				if (max <= maxim) {
					max = maxim;
					maxstr.add(e11);
				}
			} else {

				remove_startn(minedge1, ref_test[me][1]);
				// remove the following least edges node
				ref_test2 = ref_gen(minedge1);

				min_edge2 = ref_sort2(ref_test2);

				if (ref_test2.length == 0) {
					mexit = indep_f(minedge1);

					calc++;
					sexit = indep_chk(minedge1);

					if (mexit >= max) {
						maxstr.add(sexit);
						max = mexit;
					}
					continue;
					// no next recur
				}

				recursive_n(minedge1, ref_test2, min_edge2, n_time - 1, rrnd);
			}
		}
	}

	static int ref_sort2(int[][] ex_ref) {
		int[] tempr = new int[2];
		int less = 0;
		for (int e = 0; e < ex_ref.length - 1; e++) {
			tempr[0] = ex_ref[e][0];
			tempr[1] = ex_ref[e][1];
			less = e;
			for (int ee = e + 1; ee < ex_ref.length; ee++) {
				if (tempr[0] > ex_ref[ee][0]) {
					tempr[0] = ex_ref[ee][0];
					tempr[1] = ex_ref[ee][1];
					less = ee;
				}
			}
			ex_ref[less][0] = ex_ref[e][0];
			ex_ref[less][1] = ex_ref[e][1];
			ex_ref[e][0] = tempr[0];
			ex_ref[e][1] = tempr[1];
		}
		int min = 0;
		for (int w = 0; w < ex_ref.length; w++) {
			if (ex_ref[w][0] == ex_ref[0][0])
				min++;
			else
				break;
		}
		return min;
	}

	public static int[] remove_com(int[][] graph, int node, Random rrn) {
		int[][] ref_test = new int[graph.length][2];
		ref_test = remove_startn(graph, node);
		ref_sort(ref_test, rrn);
		while (ref_test.length != 0) {
			ref_test = remove_startn(graph, ref_test[0][1]);
			ref_sort(ref_test, rrn);
		}
		int[] first_r = new int[graph.length];

		for (int e = 0; e < graph.length; e++)
			first_r[e] = graph[e][0];

		return first_r;
	}

	static int[][] remove_startn(int[][] graph, int node) {
		// return ref table
		// as well as call ref_gen
		graph[node][0] = 1;
		for (int r = 1; r < graph.length; r++) {
			if (graph[node][r] == 1) {
				graph[r][0] = -1;
				for (int c = 1; c < graph.length; c++) {
					graph[c][r] = 0;

				}
			}
		}
		// node w/o connection must be independent

		int all_zero;
		for (int w2 = 1; w2 < graph.length; w2++) {
			if (graph[w2][0] == 0) {
				all_zero = 1;
				for (int w22 = 1; w22 < graph.length; w22++) {
					if (graph[w2][w22] == 1) {
						all_zero = 0;
						break;
					}
				}
				if (all_zero == 1)
					graph[w2][0] = 1;
			}
		}

		return ref_gen(graph);

	}

	static int[][] ref_gen(int[][] graph) {
		int nodel = 0;
		int[][] table_r;
		for (int e = 1; e < graph.length; e++) {
			if (graph[e][0] == 0)
				nodel++;
		}

		table_r = new int[nodel][2];
		int ct = 0, nl;
		for (int e = 1; e < graph.length; e++) {
			nl = 0;
			if (graph[e][0] == 0) {

				for (int tr = 1; tr < graph.length; tr++) {
					if (graph[e][tr] == 1)
						nl++;
				}

				table_r[ct][0] = nl;
				table_r[ct][1] = e;
				ct++;
			}

		}

		return table_r;
	}

	static int ref_sort(int[][] ex_ref, Random rr2) {
		int[] tempr = new int[2];
		int less;
		for (int e = 0; e < ex_ref.length; e++) {
			tempr[0] = ex_ref[e][0];
			tempr[1] = ex_ref[e][1];
			less = e;
			for (int ee = e + 1; ee < ex_ref.length; ee++) {
				if (tempr[0] > ex_ref[ee][0]) {
					tempr[0] = ex_ref[ee][0];
					tempr[1] = ex_ref[ee][1];
					less = ee;
				}
			}
			ex_ref[less][0] = ex_ref[e][0];
			ex_ref[less][1] = ex_ref[e][1];
			ex_ref[e][0] = tempr[0];
			ex_ref[e][1] = tempr[1];
		}
		int min = 0;
		for (int w = 0; w < ex_ref.length; w++) {
			if (ex_ref[w][0] == ex_ref[0][0])
				min++;
		}

		if (min > 0) {

			int sw = -1;

			sw = randomx(min);

			ex_ref[0][0] = ex_ref[sw][0];
			ex_ref[0][1] = ex_ref[sw][1];
		}
		return min;
	}

	static int indep_f(int[][] graph) {
		int total_ind = 0;
		for (int r = 1; r < graph.length; r++)
			if (graph[r][0] == 1)
				total_ind++;
		return total_ind;
	}

	static String indep_chk(int[][] graph) {
		String rep = "";
		int total_ind = 0;
		for (int r = 1; r < graph.length; r++) {
			if (graph[r][0] == 1) {
				total_ind++;
				rep += " " + r;
			}
		}
		return total_ind + ":" + rep;
	}

	public static final long nextLong() {
		long a = state;
		state = xorShift64(a);
		return a;
	}

	public static final long xorShift64(long a) {
		a ^= (a << 21);
		a ^= (a >>> 35);
		a ^= (a << 4);
		return a;
	}

	public static final int randomx(int n) {
		if (n < 0)
			throw new IllegalArgumentException();
		long result = ((nextLong() >>> 32) * n) >> 32;
		return (int) result;
	}

}
