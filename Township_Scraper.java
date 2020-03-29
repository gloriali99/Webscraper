package WebScrape; 

import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.net.URL;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.util.Scanner;


/*
 * This class is used for HTML parsing from URL using Jsoup.
 * @author Gloria
 */

public class Township_Scraper {
	public static void main(String args[]) throws IOException{
		
		//Create a new folder to store images 
		String path = assignDirectory();
		File images =createNewDirectory(path);
		File fout = new File(path,"Township_scraper.csv"); 
		FileWriter writer=  new FileWriter(fout);
		
		writer.append("Name");
		writer.append(',');
		writer.append("Description");
		writer.append(',');
		writer.append('\n');

		try {
			//Get Document object after parsing the html from given url.
			Document doc = Jsoup.connect("https://townshiptale.gamepedia.com/Category:Items").get();
			Elements category = doc.getElementsByClass("mw-category-group");
			for(Element i : category) {
				Elements links = i.select("a[href]");
				for(Element j : links) {
					
					String link = j.attr("abs:href");
					Document Item = Jsoup.connect(link).get();
					
					Elements Heading = Item.getElementsByClass("firstHeading");
					Elements Description = Item.select("div[class=mw-parser-output] > p"); 
					Elements Image = Item.select("a[class=image] > img");
					String imgURL = Image.attr("src"); 
					
					writer.append(Heading.text()); 
					writer.append(',');
					writer.write(Description.text());
					writer.append('\n');
					
					//Download the image
					downloadImage(imgURL,images.getAbsolutePath()); 
				}
			}
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		print("done");
	}
	
	
	
	//Function to create a new folder. 
	private static String assignDirectory() {
	      print("Enter the path to create a directory: ");
	      Scanner sc = new Scanner(System.in);
	      String path = sc.next();
	      print("Enter the name of the desired a directory: ");
	      path = path+sc.next();
	     
	      return path; 
	   
	 }
	
	private static File createNewDirectory(String path) {
		 File file = new File(path);
		 file.mkdirs();
		 return file; 
	}
	
	//Downloading the image.
	 private static void downloadImage(String imageURL, String directory ){
	        
	        //get file name from image path
		 	String[] url = imageURL.split("/");
		 	String imageName = url[url.length-2];
	        
	        try {
	            
	            URL urlImage = new URL(imageURL);
	            InputStream in = urlImage.openStream();
	            
	            byte[] buffer = new byte[4096];
	            int n = -1;
	            
	            OutputStream os = 
	                new FileOutputStream( directory + '/' + imageName );
	            
	            //write bytes to the output stream
	            while ( (n = in.read(buffer)) != -1){
	                os.write(buffer, 0, n);
	            }
	            
	            //close the stream
	            os.close();
	            
	            print("Image saved");
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	  }
	

	public static void print(String string) {
		System.out.println(string);
	}
}