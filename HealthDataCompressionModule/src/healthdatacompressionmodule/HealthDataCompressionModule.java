/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package healthdatacompressionmodule;

import java.io.File;
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
		if (args.length == 3) {
			// compress inputFilePath outputFilePath
			if (args[0].equals("compression")) {
				compress(args[1], args[2]);
			} else
			// decompress inputFilePath outputFilePath
			if (args[0].equals("decompress")) {
				decompress(args[1], args[2]);
			} else
			// A test portion to test the compression and decompression classes
			if (args[0].equals("test")) {
				test(args[1], args[2]);
			} else
			// show the help menu
			if (args[0].equals("help")) {
				showHelp();
			} else {
				// command wasn't recognized by the system
				System.out.println("Command not recognized");
				showHelp();
			}

		} else {
			// Not enough arguments
			System.out.println("Not enough arguments");
			showHelp();
		}

	}

	public static void compress(String input, String output) {
		Compression session = new Compression();
		session.startCompression(input);
		try {
			session.generateCompressedData(output);
		} catch (Exception e) {
			System.out
					.println("Wierd Stuff Happened. Contact someone who knows what's going on...");
		}
	}

	public static void decompress(String input, String output) {
		Decompression session = new Decompression(input, output);
		String data = session.decompress();
		System.out.println(data);
	}

	public static void showHelp() {
		System.out.println("Here is a list of available commands:");
		System.out
				.printf("compress inputFile outputPath %109s\n",  "---->  inputFile is the file to be compressed and outputPath is the destination to send the compressed file");
		System.out
				.printf("decompress inputZip outputPath  %53s\n","---->  inputZip is the zip archive that contains the compressed data and outputPath is the location of the archived files");
		System.out.printf("help  %53s\n","---->  shows this help menu");
		System.out
				.printf("test inputFile outputPath  %42s\n","---->  see the compress command above");
		System.out
				.println("The above test command runs the test program that is attacted to this module");
	}

	public static void test(String input, String output) {
		String testFile = "input.txt";
		Compression comSession = new Compression();
		comSession.startCompression(input);
		try {
			comSession.generateCompressedData(output);
		} catch (Exception e) {
			System.err
					.println("Weird stuff happened. You'll need to debug this...");
		}
		Decompression decSession = new Decompression(output + File.separator
				+ "compressed.zip", input.substring(0, input.indexOf(testFile)));
		String data = decSession.decompress();
		System.out.println(data);
	}

}
