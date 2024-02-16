package br.com.mvbos.lgj;

import java.awt.Color;


public class Peca {


	public static Color[] Cores = { Color.GREEN, Color.ORANGE, Color.YELLOW, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.WHITE };


	public static final int[][][] PECAS = {
			// Peça 1
			{
					{ 0, 1, 0 },
					{ 0, 1, 0 },
					{ 1, 1, 0 }
			},
			// Peça 2
			{
					{ 0, 1, 0 },
					{ 0, 1, 0 },
					{ 0, 1, 1 }
			},
			// Peça 3
			{
					{ 1, 1, 1 },
					{ 0, 1, 0 },
					{ 0, 0, 0 }
			},
			// Peça 4
			{
					{ 1, 0, 0 },
					{ 1, 1, 0 },
					{ 0, 1, 0 }
			},
			// Peça 5
			{
					{ 0, 0, 1 },
					{ 0, 1, 1 },
					{ 0, 1, 0 }
			},
			// Peça 6
			{
					{ 1, 1 },
					{ 1, 1 }
			},
			// Peça 7
			{
					{ 0, 1, 0, 0 },
					{ 0, 1, 0, 0 },
					{ 0, 1, 0, 0 },
					{ 0, 1, 0, 0 }
			}
	};
}
