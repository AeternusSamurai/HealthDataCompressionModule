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
 * @author Chase Parks, Tyler Parker, Logan Lindon, Sarah Haman, Tim Strutz.
 * @author d35kumar
 * Source: https://github.com/dharam3/DS/blob/master/src/com/dk/greedy/HuffmanCoding.java
 */

/*
 * TODO Add in decompression packet: reading both files and reconstructing the compressed data
 */
public class HealthDataCompressionModule {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        char[] ch = {'a', 'b', 'c', 'd', 'e', 'f'};
        int[] freq = {5, 9, 12, 13, 16, 45};
        String data = "";
        for (int i = 0; i < ch.length; i++) {
            for (int j = 0; j < freq[i]; j++) {
                data+=ch[i];
            }
        }
        Compression session = new Compression();
        String data2 = "Profit' is a dirty word only to the leeches of the world. They want it seen as evil, so they can more easily snatch what they did not earn.";
        String data3 = "input.txt";
        session.startCompression(data3);
        session.printHuffmanEncoding(session.getBinaryHuffmanStrings());
        try {
			session.generateCompressedData();
		} catch (IOException e) {
			System.out.println("Something happened. Contact someone who might be able to fix it.");
		}
    }

}
