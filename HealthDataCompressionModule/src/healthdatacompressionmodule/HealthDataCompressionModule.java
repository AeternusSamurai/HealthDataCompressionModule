/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package healthdatacompressionmodule;

/**
 *
 * @author Chase
 */
public class HealthDataCompressionModule {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        char[] ch = {'a', 'b', 'c', 'd', 'e', 'f'};
        int[] freq = {5, 9, 12, 13, 16, 45};
        String data2 = "";
        for (int i = 0; i < ch.length; i++) {
            for (int j = 0; j < freq[i]; j++) {
                data2+=ch[i];
            }
        }
        Compression session = new Compression();
        String data = "Profit' is a dirty word only to the leeches of the world. They want it seen as evil, so they can more easily snatch what they did not earn.";
        session.startCompression(data2);
        session.printHuffmanEncoding();
    }

}
