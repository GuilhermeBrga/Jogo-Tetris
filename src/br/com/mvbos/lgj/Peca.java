package br.com.mvbos.lgj;

import java.awt.Color;


public class Peca {


	public static Color[] Cores = { Color.GREEN, Color.ORANGE, Color.YELLOW, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.WHITE };


	public static final int[][][] PECAS = {
			// Pe�a 1
			{
					{ 0, 1, 0 },
					{ 0, 1, 0 },
					{ 1, 1, 0 }
			},
			// Pe�a 2
			{
					{ 0, 1, 0 },
					{ 0, 1, 0 },
					{ 0, 1, 1 }
			},
			// Pe�a 3
			{
					{ 1, 1, 1 },
					{ 0, 1, 0 },
					{ 0, 0, 0 }
			},
			// Pe�a 4
			{
					{ 1, 0, 0 },
					{ 1, 1, 0 },
					{ 0, 1, 0 }
			},
			// Pe�a 5
			{
					{ 0, 0, 1 },
					{ 0, 1, 1 },
					{ 0, 1, 0 }
			},
			// Pe�a 6
			{
					{ 1, 1 },
					{ 1, 1 }
			},
			// Pe�a 7
			{
					{ 0, 1, 0, 0 },
					{ 0, 1, 0, 0 },
					{ 0, 1, 0, 0 },
					{ 0, 1, 0, 0 }
			}
	};
}
