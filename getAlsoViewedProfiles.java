package Ontology;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.htmlparser.jericho.Source;
 
public class getAlsoViewedProfiles  implements Runnable{


public void run(){

try{
	Map<Integer,String> ProxyMap = new HashMap(); 
	String folder_prefix = "d:\\share\\lenin\\dropbox\\ontology\\newdata\\";
	FileWriter writerDebug =  new FileWriter(folder_prefix+"linkedindebug.txt");
	FileWriter writeralsoViewProfile =  new FileWriter(folder_prefix+"linkedinalsoViewProfile.txt");
	String inputFile = folder_prefix +  "linkedinFile.txt";
	String delimiter = "|||";String line = "";
	int k = 1;
	
	String proxyFile = folder_prefix+"\\linkedinproxyInput_out.txt";
	BufferedReader readerProxy = new BufferedReader(new FileReader(proxyFile));
	while ((line = readerProxy.readLine()) != null) {
		ProxyMap.put(k, line);
		k++;
	}
		
	File file = new File(folder_prefix);
	if(file.isDirectory() == false){
	System.out.println(" Given file is not a directory");
	//	return;
	}
	String absolutePathOfInputFile = file.getAbsolutePath();
	String Parent =  file.getParent(); int lineNumber = 0 ; String value = "";
	String[] Filenames = file.list(); //get list of files from given directory
	line = ""; String lastCurrURL= ""; String consolidatedLine= "";
	String Flag = "N"; String netURL = ""; String currURL = "", lastlastURL = "", netName ="";String startURL = "";
	HashMap<String,String> Url = new HashMap();
	HashMap<String,String> crawledMap = new HashMap();
	
	BufferedReader reader2 = new BufferedReader(new FileReader(folder_prefix+"linkedinalreadyCrawled.txt"));
	writerDebug.append(" Start of loading already existing crawled URLs \n");
	//get already crawled map
	while ((value = reader2.readLine()) != null) {
		crawledMap.put(value, "");
		writerDebug.append("loading->"+value+"\n");
	}
	
	//FileWriter writer =  new FileWriter(outputFile);
	Random randomGenerator = new Random();
	int randomInt= 0;
	//While loop in each file  
	for (int i = 0; i < Filenames.length; i++)
	{
	Flag = "N";  netURL  = ""; lineNumber = 0;
	try{
	File fileinput = new File(folder_prefix+ "/" + Filenames[i]);
	
	if(fileinput.isDirectory()  || Filenames[i].indexOf("linkedin") >= 0 || 
	Filenames[i].indexOf("DS_Store") >= 0  )// || !Filenames[i].equalsIgnoreCase("3QzkGpsAAAAJ_1"))
	continue;
	
	
	System.out.println ("File Count::"+ i  + " ; Total file : "+ Filenames.length);
	System.out.println ("File reading::"+folder_prefix+ "/" + Filenames[i]);
	System.out.println (fileinput.getAbsoluteFile());
	writerDebug.append("\n------>Processing File->"+Filenames[i]+"\n");
	BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileinput.getAbsoluteFile())));
	//while loop for each line read of processing file
	while ((line = reader.readLine()) != null) {
	line = line.toLowerCase(); lineNumber++;
	
	if(lineNumber == 1)
	startURL = line.substring(line.indexOf("http"), line.indexOf("|||",20) );
	
	System.out.println("line->"+line+";"+line.indexOf("find a different") );
	if(line.indexOf("viewers of this profile also viewed") >= 0 ){
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
	String name = consolidatedLine.replace(currURL, "");
	int j = name.indexOf("<p>");
	writerDebug.append("\n j  index->"+ j +" ||| "+  name.indexOf("<p>")  +"\n");
	
	name = name.substring( j+3 , name.indexOf("<p>",j+1) ).replaceAll("\t", "").trim();
	netName = netName +"," + name;	
	
	System.out.println( consolidatedLine.replace(currURL, "") );
	writerDebug.append("\nconsolidate->"+ consolidatedLine.replace(currURL, "") );
	writerDebug.append("\nname->"+ name +"\n");
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
	System.out.println("netURL:"+netURL);
	
	//get netURL of "also viewed page"
	for(String j:Url.keySet()){
	netURL = netURL + ","+ j;
	}
	//append to ".txt"
	/*if(!netURL.equalsIgnoreCase("")){
	randomInt = randomGenerator.nextInt(2000);    
	writer.append(Filenames[i] + "_" + randomInt+"|||"+ netURL+"|||"+"11"+ "\n");
	writer.flush();
	}*/
	}
	catch(Exception e){
	System.out.println("An exception occurred while calling Write method ..!!");	
	e.printStackTrace();
	}
	
	int count2 = 12;
	HashMap<String,String> map1 = new HashMap();
	HashMap<String,String> map2 = new HashMap();
	HashMap<String,String> map3 = new HashMap();
	HashMap<String,String> map4 = new HashMap();
	HashMap<String,String> AllMap = new HashMap();
	writerDebug.append("\nsize of map:"+ Url.size());
	// method : input: url , filename , output: Map containing , get 1nd stage
	for(String url:Url.keySet()){
	writerDebug.append("\n url Url->"+url);
	writerDebug.flush();
	AllMap.put(url, "dummy");
	if(url.length() >5)
	map1 = crawl( url, folder_prefix + Filenames[i] + "_alsoViewed"+count2+"_" + randomInt+"_"+randomGenerator.nextInt(2000)+".txt" ,
					writeralsoViewProfile	, Filenames[i], count2, crawledMap, writerDebug);
	}
	writerDebug.append("\n name of map1:"+ map1.get("name"));
	//get the names from map and concatenate to netName
	if(map1.containsKey("name"))
	netName = netName +"," + map1.get("name");
	
	writerDebug.append("\n--->size of map1:"+ map1.size());
	count2++;
	
	//get 2rd stage
	for(String url:map1.keySet()){
	writerDebug.append("\n url map1->"+url);
	writerDebug.flush();
	AllMap.put(url, "dummy");
	if(url.length() >5)
	map2 = crawl(url, folder_prefix + Filenames[i] + "_alsoViewed"+count2+"_"+ randomInt+"_"+randomGenerator.nextInt(2000) +".txt", 
			writeralsoViewProfile ,Filenames[i], count2, crawledMap, writerDebug); 
	}
	writerDebug.append("\n name of map2:"+ map2.get("name"));
	//get the names from map and concatenate to netName
	if(map2.containsKey("name"))
	netName = netName +"," + map2.get("name");
	
	writerDebug.append("\n--->size of map2:"+ map2.size());
	count2++;
	//get 3th stage
	for(String url:map2.keySet()){
	writerDebug.append("\n url map2->"+url);
	writerDebug.flush();
	AllMap.put(url, "dummy");
	if(url.length() >5)
	map3 = crawl( url, folder_prefix + Filenames[i] + "_alsoViewed"+count2+"_" + randomInt+"_"+randomGenerator.nextInt(2000)+".txt" , 
			writeralsoViewProfile, Filenames[i], count2, crawledMap, writerDebug);
	}
	writerDebug.append("\n name of map3:"+ map3.get("name"));
	//get the names from map and concatenate to netName
	if(map3.containsKey("name"))
	netName = netName +"," + map3.get("name");
	
	writerDebug.append("\n--->size of map3:"+ map3.size());
	count2++;
	//get 4th stage
	for(String url:map3.keySet()){
	writerDebug.append("\n url map3->"+url);
	writerDebug.flush();
	AllMap.put(url, "dummy");
	if(url.length() >5)
	map4 = crawl( url, folder_prefix + Filenames[i] + "_alsoViewed"+count2+"_" + randomInt+"_"+randomGenerator.nextInt(2000)+".txt" , 
					writeralsoViewProfile,Filenames[i], count2, crawledMap, writerDebug);
	}
	writerDebug.append("\n name of map4:"+ map4.get("name"));
	//get the names from map and concatenate to netName
	if(map4.containsKey("name"))
	netName = netName +"," + map4.get("name");
	
	writerDebug.append("\n--->size of map4:"+ map4.size());
	writerDebug.flush();
	
	
	netURL = "";
	// get the netURL
	for(String url:AllMap.keySet()){
		netURL = netURL+";"+url;
	}
	writeralsoViewProfile.append("\nmapping->"+Filenames[i]+"|||"+startURL +"|||" +netURL+"|||"+ netName +"\n");
	writeralsoViewProfile.flush();
	netURL = ""; netName = ""; lastCurrURL ="";
	currURL = ""; lastlastURL = ""; startURL = ""; consolidatedLine = "";
	AllMap.clear();Url.clear();
	map1.clear(); map2.clear(); map3.clear(); map4.clear();
	} // end for loop for each file read
	
	}
	catch(Exception e){
	System.out.println(" Error in getAlsoViewedProfiles");
	e.printStackTrace();
	}
}//end of run metho

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