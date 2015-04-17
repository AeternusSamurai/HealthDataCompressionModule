package healthdatacompressionmodule;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Decompression {
	private String inputZipFile;
	private String outputFileDest;
	private HashMap<String,Character> characterEncoding;
	private long compressedData;
	private int compressedDataSize;
	
	public Decompression(String izf, String ofd){
		inputZipFile = izf;
		outputFileDest = ofd;
		characterEncoding = new HashMap<String, Character>();
		extractFiles();
	}
	
	public void extractFiles(){
		byte[] buffer = new byte[1024];
		try{
			File folder = new File(outputFileDest);
			if(!folder.exists()){
				folder.mkdir();
			}
			
			ZipInputStream zis = new ZipInputStream(new FileInputStream(inputZipFile));
			ZipEntry ze = zis.getNextEntry();
			
			while(ze != null){
				String fileName = ze.getName();
				File newFile = new File(outputFileDest+ File.separator + fileName);
				FileOutputStream fos = new FileOutputStream(newFile);
				
				int length;
				while((length = zis.read(buffer)) > 0){
					fos.write(buffer, 0, length);
				}
				
				fos.close();
				ze = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
		}catch (Exception e){
			System.err.println("DECOMPRESS: Another thing went wrong. Contact someone who knows what's going on...");
		}
	}
	
	public String decompress(){
		String data = "";
		String decompressedData ="";
		try{
			data = extractData();
		}catch(IOException e){
			System.err.println("DECOMPRESS: Some thing went wrong. Contact someone who knows what's going on...");
		}
		ArrayList<String> encodings = new ArrayList<String>(characterEncoding.keySet());
		int bitSize = encodings.get(0).length();
		if(data.length() != compressedDataSize){
			System.out.println("Data not the same size. Correcting...");
		}
		
		// Recreating the original data
		
		for(int end = bitSize; end < data.length(); end = end + bitSize){ // start bitSize positions in the data
			String bitSection = data.substring(end-bitSize, end);
			if(!characterEncoding.containsKey(bitSection)){
				System.err.println("This isn't right....");
			}
			decompressedData += characterEncoding.get(bitSection);
		}
		
		return decompressedData;
	}
	
	private String extractData() throws IOException{
		// Get the files
		Scanner tableReader = new Scanner(new File(outputFileDest+File.separator+"table.txt"));
		Scanner outputReader = new Scanner(new File(outputFileDest+File.separator+"output.txt"));
		compressedData = outputReader.nextLong();
		
		compressedDataSize = tableReader.nextInt();
		while(tableReader.hasNext()){
			Character ch = tableReader.next().charAt(0);
			String encode = tableReader.next();
			characterEncoding.put(encode, ch);
		}
		
		outputReader.close();
		tableReader.close();
		return Long.toBinaryString(compressedData);
	}
	
}
