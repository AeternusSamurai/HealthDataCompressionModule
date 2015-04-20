package healthdatacompressionmodule;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Decompression {
	private String inputZipFile;
	private String outputFileDest;
	private HashMap<String, Character> characterEncoding;
	private byte[] compressedData;
	private int compressedDataSize;

	public Decompression(String izf, String ofd) {
		inputZipFile = izf;
		outputFileDest = ofd;
		characterEncoding = new HashMap<String, Character>();
		extractFiles();
	}

	public void extractFiles() {
		byte[] buffer = new byte[1024];
		try {
			File folder = new File(outputFileDest);
			if (!outputFileDest.isEmpty() && !folder.exists()) {
				folder.mkdir();
			}

			ZipInputStream zis = new ZipInputStream(new FileInputStream(
					inputZipFile));
			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {
				String fileName = ze.getName();
				File newFile = new File(((outputFileDest.isEmpty()) ? fileName
						: outputFileDest + File.separator + fileName));
				FileOutputStream fos = new FileOutputStream(newFile);

				int length;
				while ((length = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, length);
				}

				fos.close();
				ze = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
		} catch (Exception e) {
			System.err
					.println("DECOMPRESS [EXTRACT]: Another thing went wrong. Contact someone who knows what's going on...");
		}
	}

	public String decompress() {
		// TODO rewrite to use BitSet
		String data = "";
		String decompressedData = "";
		try {
			data = extractData();
		} catch (IOException e) {
			System.err
					.println("DECOMPRESS [DECOMPRESS]: Some thing went wrong. Contact someone who knows what's going on...");
		}
		ArrayList<String> encodings = new ArrayList<String>(
				characterEncoding.keySet());
		int bitSize = encodings.get(0).length();
		if (data.length() != compressedDataSize) {
			System.out.println("Data not the same size. Correcting...");
		}

		// Recreating the original data

		for (int end = bitSize; end <= data.length(); end = end + bitSize) { // start
																				// bitSize
																				// positions
																				// in
																				// the
																				// data
			String bitSection = data.substring(end - bitSize, end);
			if (!characterEncoding.containsKey(bitSection)) {
				System.err.println("This isn't right....");
			}
			decompressedData += characterEncoding.get(bitSection);
		}

		return decompressedData;
	}

	private String extractData() throws IOException {
		// Get the files
		Scanner tableReader = new Scanner(new File(
				((outputFileDest.isEmpty()) ? "table.txt" : outputFileDest
						+ File.separator + "table.txt")));
		Scanner outputReader = new Scanner(new File(
				((outputFileDest.isEmpty()) ? "output.txt" : outputFileDest
						+ File.separator + "output.txt")));
		FileInputStream fis = new FileInputStream(new File(
				((outputFileDest.isEmpty()) ? "output.txt" : outputFileDest
						+ File.separator + "output.txt")));
		DataInputStream dis = new DataInputStream(fis);

		// Read the data from the file
		ArrayList<Byte> compressed = new ArrayList<Byte>();
		int read;
		byte[] temp = new byte[1024];
		while ((read = dis.read(temp)) > 0) {
			for (int i = 0; i < read; i++) {
				compressed.add(temp[i]);
			}
		}
		compressedData = new byte[compressed.size()];
		for (int i = 0; i < compressed.size(); i++) {
			compressedData[i] = compressed.get(i);
		}

		// Get the encoding table for the compressed data
		compressedDataSize = tableReader.nextInt();
		while (tableReader.hasNext()) {
			String character = tableReader.next();
			Character ch = ' ';
			if (character.equals("\\n")) { //Take care of newline characters
				ch = '\n';
			} else if (character.equals("\\t")) { // Take care of tab characters
				ch = '\t';
			} else if (character.equals("\\s")) { // Take care of whitespace characters
				ch = ' ';
			} else {
				ch = character.charAt(0);
			}

			String encode = tableReader.next();
			characterEncoding.put(encode, ch);
		}

		outputReader.close();
		tableReader.close();

		String data = "";
		BitSet bs = BitSet.valueOf(compressedData);
		for (int i = 0; i < compressedDataSize; i++) {
			data += (bs.get(i) ? "1" : "0");
		}
		System.out.println(data);
		return data;
	}
}
