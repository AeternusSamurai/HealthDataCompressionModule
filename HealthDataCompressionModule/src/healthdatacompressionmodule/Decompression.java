package healthdatacompressionmodule;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Decompression {
	private String inputZipFile;
	private String outputFileDest;
	
	public Decompression(String izf, String ofd){
		inputZipFile = izf;
		outputFileDest = ofd;
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
			System.out.println("Another thing went wrong. Contact someone who knows what's going on...");
		}
	}
	
	
}
