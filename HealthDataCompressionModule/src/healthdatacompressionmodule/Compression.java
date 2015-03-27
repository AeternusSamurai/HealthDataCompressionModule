/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package healthdatacompressionmodule;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

/**
 *
 * @author Chase
 */
public class Compression {
    private HashMap<Character, Integer> characterFrequencies;
    private ArrayList<CharFreq> charFreqs;
    
    
    public Compression() {
        charFreqs = new ArrayList<>();
        characterFrequencies = new HashMap<>();
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
        for (Character charKey : charKeys) {
            charFreqs.add(new CharFreq(charKey, characterFrequencies.get(charKey)));
        }
    }

    /**
     * @param rootNode
     * @param code Starting with 0
     * @return It is used for just calculating the huffrecursion value further.
     * It's is not returning any final value
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
            System.out.printf("%-5c%s\n", charFreq.ch,Integer.toBinaryString(charFreq.freq));
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
}
