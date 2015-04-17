/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package healthdatacompressionmodule;

import java.io.IOException;
import java.io.ObjectInputStream.GetField;

/**
 * Health Data Compression Module (Group 2)
 * 
 * @author Chase Parks, Tyler Parker, Logan Lindon, Sarah Haman, Tim Strutz.
 * @author d35kumar Source:
 *         https://github.com/dharam3/DS/blob/master/src/com/dk/
 *         greedy/HuffmanCoding.java
 */

/*
 * TODO Add in decompression packet: reading both files and reconstructing the
 * compressed data
 */
public class HealthDataCompressionModule {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		// order of parameters: command inputFilePath outputFilePath
		// compress inputFilePath outputFilePath
		if (args.length != 3) {
			// not enough arguments

		} else if (args[0].equals("compression")) {
			Compression session = new Compression();
			session.startCompression(args[1]);
			try {
				session.generateCompressedData(args[2]);
			} catch (Exception e) {
				System.out.println("Wierd Stuff Happened. Contact someone who knows what's going on...");
			}
		} else
		// decompress inputFilePath outputFilePath
		if (args[0].equals("decompress")) {

		} else {
			// command wasn't recognized by the system
			showHelp();
		}
	}

	public static void compress() {

	}

	public static void decompress() {

	}

	public static void showHelp() {

	}

}
