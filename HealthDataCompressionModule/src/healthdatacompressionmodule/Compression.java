/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package healthdatacompressionmodule;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Health Data Compression Module (Group 2)
 * 
 * @author Chase Parks, Tyler Parker, Logan Lindon, Sarah Haman, Tim Strutz.
 * @author d35kumar Source:
 *         https://github.com/dharam3/DS/blob/master/src/com/dk/
 *         greedy/HuffmanCoding.java
 */
public class Compression {
	private HashMap<Character, Integer> characterFrequencies;
	private ArrayList<CharFreq> charFreqs;
	private String data;

	public Compression() {
		charFreqs = new ArrayList<>();
		characterFrequencies = new HashMap<>();
		data = "";
	}

	/**
	 * Find the count of the characters in the incoming string.
	 * 
	 * @param dataLine
	 *            The incoming string from a data source
	 */
	public void countCharacters(String dataLine) {
		for (int i = 0; i < dataLine.length(); i++) {
			if (characterFrequencies.containsKey(dataLine.charAt(i))) {
				characterFrequencies.put(dataLine.charAt(i),
						characterFrequencies.get(dataLine.charAt(i)) + 1);
			} else {
				characterFrequencies.put(dataLine.charAt(i), 1);
			}
		}
		data += dataLine;
	}

	/**
	 * Find the count of the characters in the file pointed to by the incoming
	 * file name Uses method countCharacters(String dataLine);
	 * 
	 * @param fileName
	 */
	public void countCharactersFile(String fileName) {
		try {
			Scanner fileReader = new Scanner(new File(fileName));
			while (fileReader.hasNextLine()) {
				countCharacters(fileReader.nextLine() + "\n");
			}
		} catch (Exception e) {
			System.err.println("Found not found...");
		}
	}

	/**
	 * Construct the CharFreq nodes for the construction of a encoding tree
	 */
	public void constructCharFreqs() {
		ArrayList<Character> charKeys = new ArrayList<>(
				characterFrequencies.keySet());
		Collections.sort(charKeys);
		for (Character charKey : charKeys) {
			charFreqs.add(new CharFreq(charKey, characterFrequencies
					.get(charKey)));
		}
	}

	/**
	 * @param rootNode
	 * @param code
	 *            Starting with 0
	 * @return It is used for just calculating the huffrecursion value further.
	 *         It's is not returning any final value Source:
	 *         https://github.com/dharam3/DS/blob/master/src/com/dk/greedy/HuffmanCoding.java
	 */
	// DON'T USE
	public int huffManEncoding(CharFreq rootNode, int code) {
		if (!rootNode.isLeafNode()) {
			// While going left left shift one bit, so that 0 will be appended
			// at last
			code = code << 1;
			code = huffManEncoding(rootNode.left, code);
			// while going right, left shift 1 bit and Or with 1, so that 1 will
			// be appended at last
			code = (code << 1) | 1;
			code = huffManEncoding(rootNode.right, code);
		} else {
			//rootNode.huffMan = code;
		}
		// While going back right shift by one bit, to discard it
		return code >> 1;
	}

	/**
	 * Same as above function, but using StringBuilder instead of int. This is
	 * only for readability purpose. <br>
	 * This is for traversing the tree and assigning the huffman code when leaf
	 * node is encountered
	 *
	 * @param rootNode
	 *            Root node of the tree
	 * @param code
	 *            Empty stringBuilder, which Source:
	 *            https://github.com/dharam3/DS
	 *            /blob/master/src/com/dk/greedy/HuffmanCoding.java
	 */
	public void huffManEncoding(CharFreq rootNode, StringBuilder code) {
		if (!rootNode.isLeafNode()) {
			// While going left append 0
			code.append(0);
			huffManEncoding(rootNode.left, code);
			// while going right, append 1
			code.append(1);
			huffManEncoding(rootNode.right, code);
		} else {
			rootNode.huffMan = code.toString();
		}
		// remove the last character while going back
		if (code.length() > 0) {
			code.deleteCharAt(code.length() - 1);
		}
	}

	/**
	 * Create the Huffman tree and start the encoding on the root node of the
	 * tree, and then calculates the encoding for all of the characters from the
	 * data source. Source:
	 * https://github.com/dharam3/DS/blob/master/src/com/dk/
	 * greedy/HuffmanCoding.java
	 */
	public void calculateHuffManEncoding() {
		Queue<CharFreq> pQueue = new PriorityQueue<>();
		// List<CharFreq> chF = Arrays.asList(charFreq);
		pQueue.addAll(charFreqs);
		// Create Huffman tree
		while (pQueue.size() > 1) {
			CharFreq left = pQueue.poll();
			CharFreq right = pQueue.poll();
			CharFreq internal = new CharFreq(left.freq + right.freq, left,
					right);
			pQueue.offer(internal);
		}

		huffManEncoding(pQueue.remove(), new StringBuilder());
		//huffManEncoding(pQueue.remove(), 0);
	}

	public void printHuffmanEncoding() {
		System.out.println("\n====================");
		System.out.println("Huffman codes for each character");
		for (CharFreq charFreq : charFreqs) {
			System.out.printf("%-5c%s\n", charFreq.ch,
					charFreq.huffMan);
		}
	}

	public void printHuffmanEncoding(ArrayList<String> binaryStrings) {
		System.out.println("======================");
		System.out.println("Huffman codes for each character");
		for (int i = 0; i < binaryStrings.size(); i++) {
			System.out.printf("%-5c%s\n", charFreqs.get(i).ch,
					binaryStrings.get(i));
		}
	}

	/**
	 * A quick start method to start up a compression session.
	 * 
	 * @param s
	 *            Can either be a fileName or a line of words.
	 */
	public void startCompression(String s) {
		if (s.endsWith(".txt")) {
			countCharactersFile(s);
		} else {
			countCharacters(s);
		}
		constructCharFreqs();
		calculateHuffManEncoding();
	}

	public ArrayList<String> getBinaryHuffmanStrings() {
		ArrayList<String> binaryStrings = new ArrayList<String>();

		// Get all of the Strings from the huffman tree
		for (CharFreq charFreq : charFreqs) {
			binaryStrings.add(charFreq.huffMan);
		}
//		DEBUG Show the binary encoding off all of the characters
//		System.out.println(binaryStrings);

		return binaryStrings;
	}


	public void generateCompressedData(String outputDest) throws IOException,
			NumberFormatException {
		File outputFile = new File("output.txt");
		PrintWriter tableWriter = new PrintWriter("table.txt");
		FileOutputStream fos = new FileOutputStream(outputDest + File.separator
				+ "compressed.zip");
		ZipOutputStream zos = new ZipOutputStream(fos);

		ArrayList<String> binaryStrings = getBinaryHuffmanStrings();

		String comData = "";
		// Write the data for the compressed data
		for (int i = 0; i < data.length(); i++) {
			// Get the character from the string
			char c = data.charAt(i);

			// Get the node to which the character corresponds to
			int targetString = 0;
			for (CharFreq charFreq : charFreqs) {
				if (charFreq.ch == c) {
					// target found go ahead an end the loop
					break;
				}
				targetString++;
			}

			// Write the huffman code to the file.
			comData += binaryStrings.get(targetString);

		}
		// Convert the String to a BitSet
		BitSet bs = convertToBitSet(comData);
		// Write the BitSet to the file
		try {
			writeToFile(bs, outputFile);
		} catch (FileNotFoundException e) {
			System.err
					.println("COMPRESSION [OUT WRITE]: The file could not be opened or created");
		} catch (IOException e) {
			System.err
					.println("COMPRESSION [OUT WRITE]: The file could not be written to");
		}
		// send the original length of the data to the file
		tableWriter.println(comData.length());
		// Write the table file and close the tableWriter object afterward.
		for (int i = 0; i < binaryStrings.size(); i++) {
			Character ch = charFreqs.get(i).ch;
			if (ch.equals('\n')) {
				tableWriter.printf("%-5s%s\n", "\\n", binaryStrings.get(i));
			} else if (ch.equals('\t')) {
				tableWriter.printf("%-5s%s\n", "\\t", binaryStrings.get(i));
			} else if (Character.isWhitespace(ch)) {
				tableWriter.printf("%-5s%s\n", "\\s", binaryStrings.get(i));
			} else {
				tableWriter.printf("%-5c%s\n", charFreqs.get(i).ch,
						binaryStrings.get(i));
			}
		}
		tableWriter.close(); // Closing the tableWriter object

		System.out.println("Writing new files to zip...\n");
		addToZipFile(/* ".."+File.separator+ */"output.txt", zos);
		addToZipFile(/* ".."+File.separator+ */"table.txt", zos);
		zos.close();
		fos.close();

	}

	private void writeToFile(BitSet bs, File outputFile)
			throws FileNotFoundException, IOException {
		FileOutputStream fos = new FileOutputStream(outputFile);
		DataOutputStream dos = new DataOutputStream(fos);
		dos.write(bs.toByteArray());
		dos.close();
		fos.close();
	}

	private BitSet convertToBitSet(String comData) {
		BitSet bs = new BitSet();
		for (int i = 0; i < comData.length(); i++) {
			String s = "" + comData.charAt(i);
			s = s.trim();
			if (!s.isEmpty()) {
				int bit = Integer.parseInt(s);
				bs.set(i, (bit == 1) ? true : false);
			}
		}
		return bs;
	}

	private void addToZipFile(String fileName, ZipOutputStream zos)
			throws FileNotFoundException, IOException {
		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		ZipEntry ze = new ZipEntry(fileName);
		zos.putNextEntry(ze);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();

	}
}
