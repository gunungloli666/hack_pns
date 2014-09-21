package fjr.com.pns;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class KabkotPNS {
	
	static final String basedir  = (System.getProperty("user.dir") + "/data/"  ).replace("\\", "/");  
		
	static String fileLogger  = basedir+  "kabkot.txt"; 
	
	static String regexAllkota = "^[0-9]+\\.?(\\s|\\t)+(Kabupaten|Kota).*"; 

	public static void main(String[] args) throws IOException{
		
		FileWriter file_writer = new FileWriter(new File(fileLogger)); 
			
		BufferedWriter bufferedWriter = new BufferedWriter(file_writer); 
		
		bufferedWriter.write("Daftar Seluruh kota dan kabupaten di Indonesia:\n\n"); 
		
		File fileAllKota = new File(basedir+ "daftar_kabupaten_dan_kota_di_indonesia_per_2013.html"); 
		
		Document document=Jsoup.parse(fileAllKota, "UTF-8");
		 
		Elements rows2 = document.select("span.cls_006"); 
	
		for(Element row : rows2){
			 String text = row.text(); 
			 if(text.matches(regexAllkota)){
				 bufferedWriter.write(text+ "\n"); 
			 }
		 }		
		 
		File filekotaPns = new File(basedir+ "pns_kabupaten_kota.htm"); 
		
		Document doc = Jsoup.parse(filekotaPns, "UTF-8"); 
		
		Elements tables = doc.select("table.category");   
		
		Element tablepns = tables.get(0); 
		
		Elements rows = tablepns.select("tr");
		
		bufferedWriter.write("\n\n\nDaftar kabupaten dan kota yang menyediakan PNS:\n\n"); 
		
		for(Element row : rows){
			Elements cols = row.select("td");  
			Element col = cols.get(0); 
			bufferedWriter.write(col.text() + "\n"); 
		}
		
		
		bufferedWriter.write("\n\n\ndaftar kabupaten/kota yang tidak mengadakan formasi PNS:\n\n"); 
		
		Elements temp_list = new Elements(rows); 
		int iterasi = 1; 
		A: for(Element row : rows2){
			String text = row.text(); 
			if(text.matches(regexAllkota)){
				String text1 = text.replaceAll("[0-9]+\\.?(\\s|\\t)+(Kabupaten|Kota)", "").trim(); 
				for(Element row_row : temp_list){
					Elements cols = row_row.select("td");  
					Element col = cols.get(0); 
					String text_1 = col.text(); 
					text1 = text1.replace("-", " ").replaceAll("\\s{2,100}", " "); 
					text_1 = text_1.replaceAll("(KAB\\.|KOTA\\s)", "").replaceAll("\\s{2,100}", " ").trim(); 
					if(text1.equalsIgnoreCase(text_1)){
						continue A ; 
					}
				}
				bufferedWriter.write((iterasi++)+". "+text.replaceAll("[0-9]+\\.?", "").trim()+"\n");
			}
		}
		
		bufferedWriter.close(); 
	
		System.out.println("FINISH"); 
	}
	

}
