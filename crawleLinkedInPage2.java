package Ontology;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import net.htmlparser.jericho.Source;
//import Ontology.tools;

	//Crawl linkedIn Page
	public  class crawleLinkedInPage2  extends Thread{
	 
		
		/*public crawleLinkedInPage(Hashtable<String, String> alreadyCrawledMap,
				Hashtable<String, String> urlMap, Map<String, String> truthMap,
				Map<Integer, String> mapProxy) {
			// TODO Auto-generated constructor stub
		}*/

		public void run(){
			// TODO Auto-generated constructor stub
			//run(crawledMap, urlMap , truthMap,
			//		  mapProxy);
		}

		public  crawleLinkedInPage2(Hashtable<String, String> alreadyCrawledMap,Hashtable<String, String> urlMap ,Map<String, String> truthMap,
													Map<Integer,String> mapProxy ) {
			int proxyIndex  = 1 ;
			 
			//Map<Integer,String> mapProxy = new HashMap(); 
			tools tl = new tools();
			//ConcurrentHashMap<String, String> urlMap =  new ConcurrentHashMap();
			//Hashtable<String, String> urlMap =  new Hashtable();
			//Map<String, String> truthMap = new HashMap();
			
			//Hashtable<String, String> alreadyCrawledMap =  new Hashtable();
			Hashtable<String, String> thisTimeCrawledMap = new Hashtable();
			//ConcurrentHashMap<String, String> alreadyCrawledMap =  new ConcurrentHashMap();
			//ConcurrentHashMap<String, String> thisTimeCrawledMap = new ConcurrentHashMap();
			String folder_prefix =  "/Users/lenin/Dropbox/Ontology/thread/";
			File folderFile = new File(folder_prefix);
			/*String inputFile =""; 
			String folder_prefix = ""; 
			*/
		
		try{
			//Thread.sleep(11000);
			Thread.sleep(1000);
			FileWriter writerDebug =  new FileWriter(folder_prefix+"linkedindebug.txt",true);
			FileWriter writeralsoViewProfile =  new FileWriter(folder_prefix+"linkedinalsoViewProfile.txt",true);
			String inputFile = folder_prefix +  "linkedinFile.txt";
		 
			int k = 1;
			
			String proxyFile = folder_prefix+"linkedinproxyInput_out.txt";
			BufferedReader readerProxy = new BufferedReader(new FileReader(proxyFile));
			/* boolean is_flag = false;
			 if(mapProxy.size() == 0){
					//load from file to map
					tl.LoadMultipleValueToKeyMap3( 	urlMap,
													truthMap,
													inputFile,
													delimiter,
													writerDebug);
					is_flag = true;
				 }
				
			
			while ((line = readerProxy.readLine()) != null & is_flag == true) {
				mapProxy.put(k, line);
				k++;
			}*/
			

			System.out.println("inside crawleLinkedInPage call: "+mapProxy.size());
			String renderedText2 = "";
			 FileWriter writer;
			
			
			String sourceUrlString=  "";//"http://www.linkedin.com/profile/view?id=46433885";
			Source source ; String value = ""; 
			String outputFile = "";
			String renderedText = "";
			FileWriter writerAlreadyCrawled =  new FileWriter(folder_prefix+"linkedinalreadyCrawled.txt",true);
			writerDebug.append(" input file ->"+ inputFile+"\n");
			writerDebug.append(" size of loaded map values: "+ urlMap.size() +";"+ truthMap.size());
			writerDebug.flush();
			// read the AlreadyCrawled.txt file and put into a Map 
			/*BufferedReader reader = new BufferedReader(new FileReader(folder_prefix+"linkedinalreadyCrawled.txt"));
			writerDebug.append(" Start of loading already existing crawled URLs \n");
			while ((value = reader.readLine()) != null && mapProxy.size() == 0 ) {
				alreadyCrawledMap.put(value, "");
				writerDebug.append("loading->"+value+"\n");
			}*/
			writerDebug.append(" End of loading already existing crawled URLs : "+  alreadyCrawledMap.size() + "\n");
			writerDebug.append(folder_prefix+"consolidatedOutput.txt"+"\n");
			writerDebug.flush();
			int randomInt =0 , randomInt2 = 0;
			Random randomGenerator = new Random();
			int count = 0 ;
			FileWriter writer2 = new FileWriter(folder_prefix+"linkedinconsolidatedOutput.txt",true);
			
			System.out.println("urlMap.size : "+urlMap.size());
			
			//for each of gid
			for(String i:urlMap.keySet() ){
				 
				randomGenerator = new Random();
				synchronized (this) {
					sourceUrlString = urlMap.get(i); // this is CSV value of URLs <gid,urlCSV>
				}
				StringTokenizer stk = new StringTokenizer(sourceUrlString, ",");
				 
				System.out.println("token size:" + stk.countTokens());
				count = 1;
				//for each url
				while (stk.hasMoreTokens()) {
					
					randomInt = randomGenerator.nextInt(2000);
					randomInt2 =randomInt +randomGenerator.nextInt(2000);
					value = stk.nextToken();
					
					if(thisTimeCrawledMap.containsKey(value) || alreadyCrawledMap.containsKey(value) ||
							value.indexOf("view?id") >= 0 )
						continue;
					
					outputFile = folder_prefix + i.substring(0,12) +"_"+randomInt+"_"+randomInt2+".txt";
					System.out.println("i URL "+i +" "+value);
					String name = Thread.currentThread().getName(); String line = "";
					synchronized (this) {
					//if not already crawled
					if(!alreadyCrawledMap.containsKey(value)){
						try{
							System.out.println( "not already exists :"+value + " urlMap:"+urlMap.size() + " alrady: "+alreadyCrawledMap.size()+
												" thread:"+name +"<- folder sz: "+folderFile.list().length +" mapProxy:"+mapProxy.size()+ "\n");
							writerDebug.append( "not exists->"+ value+ " thread:"+name+ "\n");
							 
							Random r = new Random();
							proxyIndex =(r.nextInt( mapProxy.size()));
							URL url = new URL(value);
							// HttpURLConnection conn = (HttpURLConnection)
							// url.openConnection();
							URLConnection conn = url.openConnection();
							String proxyIP = mapProxy.get(proxyIndex);
							String port = proxyIP.substring( proxyIP.indexOf(":")+1, proxyIP.length() );
							proxyIP= proxyIP.replace(":"+port, "");
							System.out.println("ip-->"+proxyIP +"; port-> "+port + " ;proxyIP-> "+proxyIndex);
							
							System.getProperties().put("proxySet", "true");
							System.setProperty("http.proxyHost",  proxyIP);
							System.setProperty("http.proxyPort",  port);
							conn.setConnectTimeout(900);
							conn.setReadTimeout(900);
							conn.setRequestProperty(
													"User-Agent",
													"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.83 Safari/535.11");
							
							BufferedReader rd  = new BufferedReader(new InputStreamReader(conn.getInputStream()));
							StringBuilder sb = new StringBuilder();
					        String conLine = "";
					          while ((line = rd.readLine()) != null)
					          {
					              //sb.append(line + '\n');
					              conLine = conLine +"\n"+line;
					          }
					         
							
							/*source = new Source(conn);
							//source = new Source(new URL(value));
							renderedText  =   source.getRenderer().toString();
							//renderedText2 =   source.getTextExtractor().setIncludeAttributes(true).toString();
							*/
							writer = new FileWriter(outputFile);
							writer.append( "filename->"+outputFile+" gid:"+i+"|||"+ value + "|||"+truthMap.get(i) +"\n"+ conLine.replace("%3A", ":").replace("%2F","/").replace("%2E",".").replace("%7E", "~") );
							writer.append(conLine);
							writer.flush();
							writerDebug.append("\n filename->"+outputFile);
							writerDebug.flush();
							
							alreadyCrawledMap.put(value, "");
							thisTimeCrawledMap.put(value, "");
							writerAlreadyCrawled.append(value+"\n");
							writerAlreadyCrawled.flush();				
						}
						catch(Exception e){
							writerDebug.append("outfle:"+outputFile+" error->"+ value+"|||"+ e.getMessage()+ " urlMap:"+urlMap.size() 
												+" folder sz: "+folderFile.list().length +"\n");
							writerDebug.flush();
						}

						//urlMap.remove(i); //if this gid crawled ,remove it 
						
					}// end of IF for not crawled only URL	
					else{
						System.out.println( "Already exists->"+ value+" "+outputFile+ urlMap.size() +" folder size: "+folderFile.list().length +"\n");
						writerDebug.append( "Already exists->"+ value+" "+outputFile+"\n");
						//alreadyCrawledMap.put(value, "");
						writerDebug.flush();
					}
					count++;
				}
				}// while end each url crawl 
			}// for end
		}//try end 
		catch(Exception e){
			e.getMessage();
			e.printStackTrace();
		}
		finally {
		
			System.out.println(" FINALLY : Author ; urlMap: " + urlMap.size() +" thisTimeCrawledMap: "+folderFile.list().length + "\n");
/*
			//arrayList.remove(authorURL);
			if( folderFile.list().length + 10 < urlMap.size()   )
			 
				crawleLinkedInPage(alreadyCrawledMap, urlMap , truthMap,
							  mapProxy);*/
				System.out.println("  finally... \n");
		 
		} //end of finally
	 
		}
	 //end of method 
 
	 
};
	