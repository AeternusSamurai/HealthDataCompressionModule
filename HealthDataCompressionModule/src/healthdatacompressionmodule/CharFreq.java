/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package healthdatacompressionmodule;

/**
 * Health Data Compression Module (Group 2)
 * @author Chase Parks, Tyler Parker, Logan Lindon, Sarah Haman, Tim Strutz.
 * @author d35kumar
 * Source: https://github.com/dharam3/DS/blob/master/src/com/dk/greedy/HuffmanCoding.java
 */
public class CharFreq implements Comparable<CharFreq> {

    /**
     * Character. If it's internal node, this will be blank
     */
    char ch;
    /**
     * Frequency of character
     */
    int freq;
    /**
     * If it is internal node, then it's left node
     */
    CharFreq left;
    /**
     * If it is internal node, then it's right node
     */
    CharFreq right;
    /**
     * This will be populated while traversing the huffman tree. By default -1
     */
    int huffMan = -1;

    /**
     * @param ch
     * @param freq
     */
    public CharFreq(char ch, int freq) {
        this.ch = ch;
        this.freq = freq;
    }

    /**
     * @param ch
     * @param freq
     */
    public CharFreq(int freq, CharFreq left, CharFreq right) {
        this.freq = freq;
        this.left = left;
        this.right = right;
    }

    @Override
    public int compareTo(CharFreq o) {
        if(this.freq < o.freq){
        	return -1;
        }else if(this.freq > o.freq){
        	return 1;
        }else{
        	return 0;
        }
    }

    /**
     * @return
     */
    public boolean isLeafNode() {
        return left == null && right == null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CharFreq [ch=" + ch + ", freq=" + freq + ", huffMan="
                + huffMan + "]";
    }
}
