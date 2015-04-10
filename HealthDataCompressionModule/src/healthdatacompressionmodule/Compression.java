/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package healthdatacompressionmodule;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

/**
 * Health Data Compression Module (Group 2)
 * @author Chase Parks, Tyler Parker, Logan Lindon, Sarah Haman, Tim Strutz.
 * @author d35kumar
 * Source: https://github.com/dharam3/DS/blob/master/src/com/dk/greedy/HuffmanCoding.java
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
     * @param dataLine The incoming string from a data source
     */
    public void countCharacters(String dataLine){
        for (int i = 0; i < dataLine.length(); i++) {
            if(characterFrequencies.containsKey(dataLine.charAt(i))){
                characterFrequencies.put(dataLine.charAt(i), characterFrequencies.get(dataLine.charAt(i))+1);
            }else{
                characterFrequencies.put(dataLine.charAt(i), 1);
            }
        }
        data += dataLine;
    }
    
    /**
     * Find the count of the characters in the file pointed to by the incoming file name
     * Uses method countCharacters(String dataLine);
     * 
     * @param fileName 
     */
    public void countCharactersFile(String fileName){
       try{
           Scanner fileReader = new Scanner(new File(fileName));
           while(fileReader.hasNextLine()){
               countCharacters(fileReader.nextLine());
           }
       } catch(Exception e){
           System.err.println("Found not found...");
       }
    }
    
    /**
     * Construct the CharFreq nodes for the construction of a encoding tree
     */
    public void constructCharFreqs(){
        ArrayList<Character> charKeys = new ArrayList<>(characterFrequencies.keySet());
        Collections.sort(charKeys);
        for (Character charKey : charKeys) {
            charFreqs.add(new CharFreq(charKey, characterFrequencies.get(charKey)));
        }
    }

    /**
     * @param rootNode
     * @param code Starting with 0
     * @return It is used for just calculating the huffrecursion value further.
     * It's is not returning any final value
     * Source: https://github.com/dharam3/DS/blob/master/src/com/dk/greedy/HuffmanCoding.java 
     */
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
            rootNode.huffMan = code;
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
     * @param rootNode Root node of the tree
     * @param code Empty stringBuilder, which
     * Source: https://github.com/dharam3/DS/blob/master/src/com/dk/greedy/HuffmanCoding.java
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
            rootNode.huffMan = Integer.parseInt(code.toString());
        }
        // remove the last character while going back
        if (code.length() > 0) {
            code.deleteCharAt(code.length() - 1);
        }
    }

    /**
     * Create the Huffman tree and start the encoding on the root node of the tree, and then
     * calculates the encoding for all of the characters from the data source.
     * Source: https://github.com/dharam3/DS/blob/master/src/com/dk/greedy/HuffmanCoding.java
     */
    public void calculateHuffManEncoding() {
        Queue<CharFreq> pQueue = new PriorityQueue<>();
        //List<CharFreq> chF = Arrays.asList(charFreq);
        pQueue.addAll(charFreqs);
        // Create Huffman tree
        while (pQueue.size() > 1) {
            CharFreq left = pQueue.remove();
            CharFreq right = pQueue.remove();
            CharFreq internal = new CharFreq(left.freq + right.freq, left,
                    right);
            pQueue.add(internal);
        }

        // huffManEncoding(pQueue.remove(), new StringBuilder());
        huffManEncoding(pQueue.remove(), 0);
    }
    
    public void printHuffmanEncoding(){
        System.out.println("\n====================");
        System.out.println("Huffman codes for each character");
        for (CharFreq charFreq : charFreqs) {
            System.out.printf("%-5c%s\n", charFreq.ch,Integer.toBinaryString(charFreq.huffMan));
        }
    }
    
    public void printHuffmanEncoding(ArrayList<String> binaryStrings){
    	System.out.println("======================");
    	System.out.println("Huffman codes for each character");
    	for(int i = 0; i < binaryStrings.size(); i++){
    		System.out.printf("%-5c%s\n", charFreqs.get(i).ch, binaryStrings.get(i));
    	}
    }
    
    /**
     * A quick start method to start up a compression session.
     * @param s Can either be a fileName or a line of words.
     */
    public void startCompression(String s){
        if(s.endsWith(".txt")){
            countCharactersFile(s);
        }else{
            countCharacters(s);
        }
        constructCharFreqs();
        calculateHuffManEncoding();
    }
    
    public ArrayList<String> getBinaryHuffmanStrings(){
    	ArrayList<String> binaryStrings = new ArrayList<String>();
    	
    	// Get all of the Strings from the huffman tree
    	for (CharFreq charFreq : charFreqs) {
			binaryStrings.add(Integer.toBinaryString(charFreq.huffMan));
		}
    	// Get the length of the largest binary number
    	int largestNum = getLargestNumber(binaryStrings);
    	
    	normalizeBinaries(binaryStrings, largestNum);
    	
    	return binaryStrings;
    }
    
    private void normalizeBinaries(ArrayList<String> binaryStrings, int largestNum) {
		for(int i = 0; i < binaryStrings.size(); i++){
			String binary = binaryStrings.get(i);
			int zerosToAdd = largestNum - binary.length();
			for(int j = 0; j < zerosToAdd; j++){
				binary = "0" + binary;
			}
			binaryStrings.set(i,binary);
		}
		
	}

	private int getLargestNumber(ArrayList<String> binaryStrings) {
		int largestNum = 0;
		for (String string : binaryStrings) {
			if(string.length() > largestNum) largestNum = string.length();
		}
		return largestNum;
	}

	public void generateCompressedData() throws IOException{
    	PrintWriter writer = new PrintWriter("output.dat");
    	PrintWriter tableWriter = new PrintWriter("table.txt");
    	
    	ArrayList<String> binaryStrings = getBinaryHuffmanStrings();
    	
    	// Write the table file and close the tableWriter object afterward.
    	for (int i = 0; i < binaryStrings.size(); i++) {
    		tableWriter.printf("%-5c%s\n", charFreqs.get(i).ch,binaryStrings.get(i));
		}
    	tableWriter.close(); // Closing the tableWriter object
    	
    	// Write the data for the compressed data
    	for(int i = 0; i < data.length(); i++){
    		// Get the character from the string
    		char c = data.charAt(i);
    		
    		// Get the node to which the character corresponds to
    		CharFreq targetNode = null;
    		int targetString = 0;
    		for (CharFreq charFreq : charFreqs) {
				if(charFreq.ch == c){
					// target found go ahead an end the loop
					targetNode = charFreq;
					break;
				}
				targetString++;
			}
    		
    		// Write the huffman code to the file.
    		writer.print(binaryStrings.get(targetString));
    		
    	}
    	writer.close(); // Close the writer object
    	
    	
    }
}
