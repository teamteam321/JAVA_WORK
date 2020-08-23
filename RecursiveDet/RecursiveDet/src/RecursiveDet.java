import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.Scanner;
import java.util.Stack;
public class RecursiveDet {
	private int[][] matrix;
	private boolean[] visited;
	
	public static void main(String[] args)throws Exception {
		
		
		File test = null;
		BufferedReader r = null;
		try {
			test = new File(args[0]);
			 r = new BufferedReader(new FileReader(test));
		}catch(Exception s) {
			System.err.println("File not found.");
			System.exit(-1);
		}
		
		int dim = Integer.parseInt(r.readLine());
		if(dim <= 0 || !test.exists())
		{
			System.err.println("Invalid input.");
			System.exit(-1);
		}
		
		int[][] tap = new int[dim][dim];
		int[][] tapT = new int[dim][dim];
		int[] zeroV = new int[dim];
		int[] zeroH = new int[dim];
		int zero = 0;
		int inp;
		boolean swap_row = false;
		for (int e = 0; e < dim; e++) {
			
			String[] temp = r.readLine().split(" ");
			
			for (int ro = 0; ro < dim; ro++) {
				inp =  Integer.parseInt(temp[ro]);
				tap[e][ro] = inp;
				tapT[ro][e] = inp;
				if(inp == 0) {
					zero++;
					zeroV[ro]++;
					zeroH[e]++;
				}
			}
		}
		
		if(zero != 0) {
			int max = 0;
			int HVmax = -1;
			for(int e = 0;e<dim;e++) {
				if(zeroH[e] > max) {
					HVmax = 1;
					max = zeroH[e];
				}
				if(zeroV[e] > max) {
					HVmax = 2;
					max = zeroV[e];
				}
			}
			if(HVmax == 2) {
				tap = tapT;
				zeroH = zeroV;
			}
			int sw;
			int[]swap = new int[dim];
			
			for(int p = 0;p<dim-1;p++) {
				for(int c = p+1;c<dim;c++) {
					if(zeroH[p] < zeroH[c]) {
						swap_row = !swap_row;
						sw = zeroH[p];
						zeroH[p] = zeroH[c];
						zeroH[c] = sw;
						
						swap = tap[p];
						tap[p] = tap[c];
						tap[c] = swap;
					}
				}
			}
		}
		RecursiveDet x = new RecursiveDet(tap);
		long st_time = System.currentTimeMillis();
		long ans = x.det(0);
		System.out.println(""+((swap_row)?ans*-1:ans));
		
	}

	public RecursiveDet(int[][] matrix) {
		this.matrix = matrix;
		this.visited = new boolean[matrix.length];
		Array.setBoolean(visited, 0, false);
	}
	
	private final long det(int depth) {

		long[] subans = new long[matrix.length - depth];
		long rowans = 0;
		int ch = 0;

		for (int e = 0; e < matrix.length; e++) {

			if (visited[e])
				continue;
			if (depth + 1 >= matrix.length) {
				return matrix[depth][e];
			} 
			else if(matrix[depth][e] == 0) {
				ch++;
				continue;
			}
			else {
				subans[ch] = matrix[depth][e];
				visited[e] = true;
				subans[ch] *= det(depth + 1);
				visited[e] = false;
			}
			
			if (ch % 2 == 0) {
				rowans += subans[ch];
			}
			else {
				rowans -= subans[ch];
			}
				
			ch++;
		}
		depth++;
		return rowans;
	}
}
