 package Ontology;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Random;
import net.htmlparser.jericho.Source;
import java.util.concurrent.*;
 
import java.util.concurrent.ExecutionException;
 
  


/***
 * author lenin
 * 
 * 1) Run crawleLinkedInPage() method    -> gets  1, 0 truth values
 * 2) Run getSimilarProfiles() methods   -> gets -1 truth value, and adds these new URL to "linkedinFile.txt", 
 *                                          crawl these new URLs, Also Viewed profiles of them
 * 3) Run getAlsoViewedProfiles() method -> gets the 4 depth of "also viewed profiles" names of list from (1)
 */


public class CrawlLinkedIn2 {
	//main method
	public static void main(String[] args) throws Exception {
		String folder_prefix = "/Users/lenin/Dropbox/Ontology/thread/";
		FileWriter writerDebug =  new FileWriter(folder_prefix+"linkedindebug.txt");
		FileWriter writeralsoViewProfile =  new FileWriter(folder_prefix+"linkedinalsoViewProfile.txt");
		String inputFile = folder_prefix +  "linkedinFile.txt";
		boolean is_crawl_from_inputFile = false; String delimiter = "|||"; String line = "";
		boolean is_crawl_AlsoViewedProfile = false;
		Hashtable<String,String> alreadyCrawledMap = new Hashtable(); String value = "";
		try{
			Map<Integer,String> mapProxy = new HashMap(); 
			tools tl = new tools();
			//ConcurrentHashMap<String, String> urlMap =  new ConcurrentHashMap();
			Hashtable<String, String> urlMap =  new Hashtable();
			Map<String, String> truthMap = new HashMap();
			Hashtable<String, String> thisTimeCrawledMap = new Hashtable();
			File folderFile = new File(folder_prefix);
			String proxyFile = folder_prefix+"linkedinproxyInput_out.txt";
			BufferedReader readerProxy = new BufferedReader(new FileReader(proxyFile));
			 boolean is_flag = false;
		//	 if(mapProxy.size() == 0){
					//load from file to map
					tl.LoadMultipleValueToKeyMap3( 	urlMap,
													truthMap,
													inputFile,
													delimiter,
													writerDebug);
					is_flag = true;
			//	 }
				
			int k = 1;
			while ((line = readerProxy.readLine()) != null & is_flag == true) {
				mapProxy.put(k, line);
				k++;
			}
			BufferedReader reader = new BufferedReader(new FileReader(folder_prefix+"linkedinalreadyCrawled.txt"));
			writerDebug.append(" Start of loading already existing crawled URLs \n");
			while ((value = reader.readLine()) != null  ) {
				alreadyCrawledMap.put(value, "");
				writerDebug.append("loading->"+value+"\n");
			}
		writerDebug.append("\n------>before calling craweldMap\n");
		writerDebug.flush();
		//input file has gid|||url|||truth , output the crawled file as a map
		 ExecutorService executor = Executors.newFixedThreadPool(5);
		 
		System.out.println(" before calling..");
		
		crawleLinkedInPage2 t1 = new   crawleLinkedInPage2(alreadyCrawledMap,urlMap , truthMap, mapProxy);
		new Thread(t1).start();
		
		crawleLinkedInPage2 t2 = new   crawleLinkedInPage2(alreadyCrawledMap,urlMap , truthMap, mapProxy);
		new Thread(t2).start();

		crawleLinkedInPage2 t3 = new   crawleLinkedInPage2(alreadyCrawledMap,urlMap , truthMap, mapProxy);
		new Thread(t3).start();
		
		crawleLinkedInPage2 t4 = new   crawleLinkedInPage2(alreadyCrawledMap,urlMap , truthMap, mapProxy);
		new Thread(t4).start();

		crawleLinkedInPage2 t5 = new   crawleLinkedInPage2(alreadyCrawledMap,urlMap , truthMap, mapProxy);
		new Thread(t5).start();
		
		//crawleLinkedInPage(inputFile,folder_prefix,"|||",crawledMap,writerDebug); 
		writerDebug.append("\n------>before calling getSimilarProfiles:"+ alreadyCrawledMap.size()+"\n");
		writerDebug.flush();
		//Read from a folder and get the similar profile URL into linkedinFile.txt file , input the previously crawled map
		//getSimilarProfiles(folder_prefix,folder_prefix+"linkedinFile.txt",writeralsoViewProfile,crawledMap,writerDebug);
		System.out.println("size of crawledmap:"+alreadyCrawledMap.size());
		writerDebug.append("\n------>before calling getAlsoViewedProfiles:");
		writerDebug.flush();
		//get the names of "Also Viewed Profiles"
		//getAlsoViewedProfiles(folder_prefix,writeralsoViewProfile,1,crawledMap,writerDebug);
		 
		if(is_crawl_AlsoViewedProfile == true){
		for (int i = 0; i < 50; i++) {
			Runnable task = new getAlsoViewedProfiles();
		    Thread worker = new Thread(task);
		    // Start the thread, never call method run() direct
		    worker.start();
		    // Remember the thread for later usage
		    executor.execute(worker);
		}
		}
		
		//Write the crawledMap to the file/
		FileWriter writerCrawledFile =  new FileWriter(folder_prefix+"linkedinalreadyCrawled.txt",true); //keep appending to this file
		for(String g:alreadyCrawledMap.keySet()){
			writerCrawledFile.append(g+"\n");
			writerCrawledFile.flush();
		}// end of for loop
		System.out.println("\n------>after calling getAlsoViewedProfiles:");	
		writerDebug.append("\n------>after calling getAlsoViewedProfiles:");
		writerDebug.flush();
		}
		catch(Exception e){
			System.out.println("catch");
			writerDebug.append("Error in main:"+e.getMessage());	
			e.printStackTrace();
		  }
	} //end of main
	
	//get "Also viewed profiles" , Read given directory files and extract the "Also Viewed Profiles"
	 
	// get Similar profiles URL from already crawled linkedIn profile, and crawl the extracted link
	public static void getSimilarProfiles(String folder_prefix, String outputFile,FileWriter  writerAlsoViewed, Map<String,String> crawledMap,FileWriter writerDebug){
		File file = new File(folder_prefix);
		
		if(file.isDirectory() == false)
		{
			System.out.println(" Given file is not a directory");
		//	return;
		}
        String absolutePathOfInputFile = file.getAbsolutePath();
        Map<String,String> urlMap = new HashMap();
        String Parent =  file.getParent(); 
        String[] Filenames = file.list(); String line = "";
		String Flag = "N"; String netURL = "";
		try{
			FileWriter writer =  new FileWriter(outputFile,true);
			//FileWriter writerAlsoViewed =  new FileWriter(fileAlsoViewedProfile,true);
			Random randomGenerator = new Random();
			int lineNumber = 0 ;
	        //While loop to read each file and write into.
			for (int i = 0; i < Filenames.length; i++)
			{
			  Flag = "N";  netURL  = "";
			  lineNumber = 0;
			  try{
				  File fileinput = new File(folder_prefix+ "/" + Filenames[i]);
				  
				  if(fileinput.isDirectory() || Filenames[i].indexOf("linkedin") >= 0 )// || !Filenames[i].equalsIgnoreCase("3QzkGpsAAAAJ_1"))
					  continue;
				  
				  System.out.println ("File Count::"+ i  + " ; Total file : "+ Filenames.length +" ; output file:"+ outputFile);
				  System.out.println ("File reading::"+folder_prefix+ "/" + Filenames[i]);
				  System.out.println (fileinput.getAbsoluteFile());
				  BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileinput.getAbsoluteFile())));
				  //while loop for each line read of processing file
				  while ((line = reader.readLine()) != null) {
					  line = line.toLowerCase();
					  lineNumber++;
					  System.out.println("line->"+line+";"+line.indexOf("find a different") );
					  if(line.indexOf("find a different") >= 0 ){
						 System.out.println("---->flag Set :"+line);
						 Flag = "Y";
					  }
					  //get the URLs
					  if(Flag.equalsIgnoreCase("Y") && line.indexOf("http://") > 0){
							  urlMap.put(line.substring(line.indexOf("http"), line.indexOf(">") - 1),"dummy");
						 
							 
					  }  				  
				  } // end of while loop for each line read
				  System.out.println("netURL:"+netURL);
				  netURL = ""; String NetSimUrl = "";Map<String,String> simProfMap = new HashMap();
				  writerDebug.append("\n Size of URL from similar Profiles extracted:"+urlMap.size()+" ;File name->"+Filenames[i]);
				  writerDebug.append("\n folder_prefix,   outputFile,  fileAlsoViewedProfile->"+folder_prefix+";"+outputFile);
				  writerDebug.flush();
				  //crawl and make netURL
				  for(String j:urlMap.keySet()){
					  netURL = netURL + ","+ j;
					  //crawl the page if not already crawled
					  if(!crawledMap.containsKey(j)){
						  
						//crawl
					  	simProfMap = crawl(j, folder_prefix+Filenames[i].replace(".txt","_similar_")+ randomGenerator.nextInt(2000)+"_" +randomGenerator.nextInt(2000)+".txt",
					  					writerAlsoViewed,
					  					Filenames[i], -1, crawledMap, writerDebug);
					  	
					  	// get the string to CSV
					    for(String SimUrl:simProfMap.keySet()){
					    	  writerDebug.append("\nDebug sim: ->"+SimUrl +";"+SimUrl.length() );
					    	 
					    	  if(SimUrl.length() > 5)
					    		  NetSimUrl =  NetSimUrl +","+ SimUrl ;
					    }
					      writerDebug.append("\nDebug sim: netSimUrl ->"+NetSimUrl );
				    	  writerDebug.flush();
				    	  writerAlsoViewed.append("\nmappingSim->" +Filenames[i]+"|||" +j+"|||"+NetSimUrl + "|||"+ simProfMap.get("name") );
						  writerDebug.append("\nDebug sim: after writing   ->"+ simProfMap.get("name")  );
						  writerAlsoViewed.flush();
					  }
				
				  }
				  
				  //write to "linkedinFile.txt"
				  if(!netURL.equalsIgnoreCase("") && !netURL.equalsIgnoreCase(",")){
					  	int randomInt = randomGenerator.nextInt(2000);    
					  	writer.append(Filenames[i]  + randomInt+"_"+randomGenerator.nextInt(2000)+"|||"+ netURL+"|||"+"-1"+ "\n");
				  		writer.flush();
				  }
				   
				  
			  }
			  catch(Exception e)
			  {
				  System.out.println("An exception occurred while calling Write method ..!!");	
				  e.printStackTrace();
			  }
			} // end for loop for each file read
		}
		catch(Exception e){
			System.out.println(" Error in getSimilarProfiles");
			e.printStackTrace();
			try {
				writerDebug.append("crawl() method error: msg and file name"+e.getMessage()+";"+outputFile);
				writerDebug.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}	
	}
	//crawl and return the "Also view profiles" links as Map
	public static HashMap<String,String> crawl(String url, String fileName, FileWriter writerAlsoViewed, String gid, int truth, Map<String,String> crawledMap,FileWriter writerDebug){
		tools tl = new tools();  String name = "";
		HashMap<String,String> Url = new HashMap(); 
		try{
			Random randomGenerator = new Random();
			writerDebug.append("\n crawling from crawl()->"+url+" ;filename->"+fileName);
			writerDebug.flush();
			Source source = new Source(new URL(url));
			String renderedText  =   source.getRenderer().toString();
			FileWriter writer = new FileWriter(fileName);
			//FileWriter writerAlsoViewed = new FileWriter(fileName);
			writer.append( gid+ "|||"+ url + "|||"+ truth +"\n"+ renderedText.replace("%3A", ":").replace("%2F","/").replace("%2E",".").replace("%7E", "~") );
			writer.flush();
			crawledMap.put(url, "dummy");
			//writer.close();
			String line = "", Flag = "",netURL = "", currURL = "", consolidatedLine = "",lastlastURL = "", lastCurrURL = "", netName = "";
			netName = "";
			 BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream( fileName )));
			 //while loop for each line read of processing file ( for same above crawled file)
			  while ((line = reader.readLine()) != null) {
				  line = line.toLowerCase(); 
				  //System.out.println("line->"+line+";"+line.indexOf("find a different") );
				  if(line.indexOf("viewers of this profile also viewed") >= 0 ||
					 (line.indexOf( "*") >=0 &&  line.indexOf("http://") >= 0  ) ||
					 (lastCurrURL.indexOf( "*") >=0 &&  line.indexOf("http://") >= 0 ) || 
					 (lastlastURL.indexOf( "*") >=0 &&  line.indexOf("http://") >= 0 ) ){
				  //if(line.indexOf("find a different") >= 0 ){
					 System.out.println("---->flag Set :"+line);
					 Flag = "Y";
				  }
				  //get the URLs
				  if(Flag.equalsIgnoreCase("Y") && line.indexOf("http://") > 0){
					  currURL 	 = line.substring(line.indexOf("http"), line.indexOf(">") - 1);
					  
					  	  consolidatedLine = consolidatedLine + line;
						  Url.put(line.substring(line.indexOf("http"), line.indexOf(">") - 1), "dummy");
					 
					  // pick the names
					  if(currURL.equalsIgnoreCase(lastCurrURL) || currURL.equalsIgnoreCase(lastlastURL) ){
						  name = consolidatedLine.replace(currURL, "");
						  int j = name.indexOf("<p>");
						  writerDebug.append("\n j  index->"+ j +" ||| "+  name.indexOf("<p>")  +"\n");
						  name = name.substring( j+3 , name.indexOf("<p>",j+1) ).replaceAll("\t", "").trim();
						   
						  System.out.println( consolidatedLine.replace(currURL, "") );
						  writerDebug.append("\nconsolidate->"+ consolidatedLine.replace(currURL, "") );
						  writerDebug.append("\nname->"+ name +"\n");
						  netName = netName + ";" + name;
						  writerDebug.flush();
						  consolidatedLine = "";
					  }
					  writerDebug.append("\ndebug->"+ currURL+" ||| "+ lastCurrURL + " ||| "+lastlastURL);
					  writerDebug.flush();
					  lastCurrURL = currURL;
				  }  
				  else if(Flag.equalsIgnoreCase("Y") && line.indexOf("http://") < 0){
					  consolidatedLine = consolidatedLine + line;
					  lastlastURL = lastCurrURL;
				  }
				  //do not pick the "find a different" urls
				  if(line.indexOf("find a different") >= 0 ){
					  break;
				  }
				  
			  } // end of while loop for each line read
			  
			  Url.put("name",netName);
			  //crawl and make netURL
			  for(String j:Url.keySet()){
				  netURL = netURL + ","+ j;
			  }
			  Url.put("url",netURL);
			  writerDebug.append("\nname->"+fileName+"|||" + netURL +"|||"+ netName );
			  writerDebug.flush();
			  	 
		}
		catch(Exception e){
			System.out.println(" Error in crawl->"+e.getMessage());
			e.printStackTrace();
		}
		return Url;
	}
}