package Ontology;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tools {

	public static int editDis(String str1, String str2) {
		// System.out.println("Inside editDis() ");
		str1 = cleanName(str1, "|||").toLowerCase();
		str2 = cleanName(str2, "|||").toLowerCase();
		str1 = "*" + str1;
		str2 = "*" + str2;
		int len1 = str1.length(), len2 = str2.length();
		int[][] d = new int[len1][len2];
		for (int i = 0; i < len1; i++) {
			d[i][0] = i;
		}
		for (int j = 0; j < len2; j++) {
			d[0][j] = j;
		}
		for (int i = 1; i < len1; i++) {
			for (int j = 1; j < len2; j++) {
				d[i][j] = Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1);
				int delta = (str1.charAt(i) == str2.charAt(j)) ? 0 : 1;
				d[i][j] = Math.min(d[i][j], d[i - 1][j - 1] + delta);
			}
		}
		return d[len1 - 1][len2 - 1];
	}
	//read a file and get the editDistance for given gid,aid pair
	//input file format-> gid|||aid|||Gname||Aname
		public static void getEditDistance(String inputFile, String outputFile,String delimiter){
			String line = "";
			String gid = "", aid= "" ,Gname ="",Aname=""; Map<String,String> mapNames = new HashMap();
			String value = "";
			int count = 0;
			try {
				BufferedReader reader = new BufferedReader(new FileReader(inputFile));
				FileWriter writer = new FileWriter(outputFile);
				while ((line = reader.readLine()) != null) {
					StringTokenizer stk = new StringTokenizer(line, delimiter);
					count = 1;
					if(line.equals(""))
						continue;
					System.out.println(line + ";token size:"+stk.countTokens());
					while (stk.hasMoreTokens()) {
						value = stk.nextToken();
						 if (count == 1) {
							gid = value;
						} 
						 else if (count == 2 && !(value.equalsIgnoreCase("nana"))  ) {
							aid = value;
						} 
						 else if (count == 3 && !(value.equalsIgnoreCase("nana"))  ) {
							Gname = value;
						}
						 else if (count == 4 && !(value.equalsIgnoreCase("nana"))  ) {
							Aname = getLongest(value,",");
						}
						count++;
					} // while loop for string tokenization
					//writerDebug.flush();
					//writer.append("key->"+key+";first string:"+mapNames.get("1")+"; 2nd string:"+mapNames.get("2")+"\n");
					writer.append(gid+"|||"+aid+"|||"+Gname+"|||"+Aname+"|||"+editDis(Gname,Aname)+"\n");
					writer.flush();
					mapNames.clear();
				} // while loop for line read of given file
			} catch (Exception e) {
				e.printStackTrace();
			}		
		}
	// clean the name method.
	public static String cleanName(String name, String delimiter) {
		name = name.replace("-", ",").replace("prof.", "").replace("dr.", "")
				.replace(".", " ").replace(" ph.d", "")
				.replace(" phd", "").replace("dr ", "").replace("Dr. ", "")
				.replace(" aka", "").replace("M.Sc", "").replace("B.Sc", "")
				.replace(" ms,", "").replace("(", "").replace(")", "")
				.replace("Associate", "").replace("professor", "")
				.replace("prof.", "").replace("prof ", "").replace("Ph. D", "")
				.replace(" md ", "").replace("ph.d", "").replace(",phd", "")
				.replace(" mba ", "").replace(" md,", "").replace(" mhs", "")
				.replace("@", "").replace("SMIEEE", "").replace(" or", "")
				.replace(" Research", "").replace("(", "").replace(")", "")
				.replace("-", " ").replace(";", ",").replace("'", " ")
				.replace(",", " ").replace(" ", delimiter)
				.replace(delimiter + delimiter, delimiter)
				.replace(delimiter + delimiter, delimiter);

		String value = "";
		String tname = "";
		int cnt = 1;
		// remove single character
		StringTokenizer stk = new StringTokenizer(name, delimiter);
		while (stk.hasMoreTokens()) {
			value = stk.nextToken();
			if (value.length() > 1 && cnt == 1)
				tname = value;
			else if (value.length() > 1 && cnt > 1)
				tname = tname + delimiter + value;
			cnt++;
		}
		// return the name
		return tname.toLowerCase();
	}

	// Clean special characters etc in the title
	public static String cleanTitle(String name) {
		name = name.replace("-", "").replace(".", "").replace("(", "")
				.replace(")", "").replace(":", "").replace("@", "")
				.replace("(", "").replace(")", "").replace("-", "")
				.replace(";", "").replace("'", "").replace(",", "")
				.replace(" ", "")
				.replace("_", "").replaceAll("&#8208", "")
				.replace("&#146", "").replaceAll("&#43", "")
				.replace("&#45", "").replaceAll("&#2817", "")
				.replace("&#039", "").replaceAll("&#8211", "")
				.replaceAll("&#8217", "")
				.replaceAll("&#xd", "")
				.replaceAll("&#x2014", "")
				.replace("&#150", "")
				.replace("&#8212", "").replaceAll("&#x2013", "")
				.replace("&#39", "")
				.replace("&#146", "")
				.replace("&#146", "")
				.replace("&#x208","")
				.replace("+", "")
				.toLowerCase();

		return name;
	}
	// Clean the name method , retain single length subnames
	public String cleanNameRetain(String name, String delimiter) {
		name = name.replace("-", ",").replace(".", " ").replace(" ph.d", "")
				.replace(" phd", "").replace("dr ", "").replace("Dr. ", "")
				.replace(" aka", "").replace("M.Sc", "").replace("B.Sc", "")
				.replace(" ms,", "").replace("(", "").replace(")", "")
				.replace("Associate", "").replace("professor", "")
				.replace("prof.", "").replace("prof ", "").replace("Ph. D", "")
				.replace(" md ", "").replace("ph.d", "").replace(",phd", "")
				.replace(" mba ", "").replace(" md,", "").replace(" mhs", "")
				.replace("@", "").replace("SMIEEE", "").replace(" or", "")
				.replace(" Research", "").replace("(", "").replace(")", "")
				.replace("-", " ").replace(";", ",").replace("'", " ")
				.replace(",", " ").replace(" ", delimiter)
				.replace(delimiter + delimiter, delimiter)
				.replace(delimiter + delimiter, delimiter);

		String value = "";
		String tname = "";
		int cnt = 1;
		// remove single character
		StringTokenizer stk = new StringTokenizer(name, delimiter);
		while (stk.hasMoreTokens()) {
			value = stk.nextToken();
			if (cnt == 1)
				tname = value;
			else if (cnt > 1)
				tname = tname + delimiter + value;
			cnt++;
		}
		// return the name
		return tname.toLowerCase();
	}
	// method to get the longest length from givne CSV
	// Michael Chau,Michael Chiu-Lung Chau -> Michael Chiu-Lung Chau
	public static String getLongest(String str, String delimiter) {
		str = str.replace(".", "").replace("-", "");
		System.out.println(str);
		StringTokenizer stk = new StringTokenizer(str, delimiter);
		String value = "";
		int pre_len = 0;
		String longest_str = "";
		// System.out.println( "  before while loop->"+ str);
		while (stk.hasMoreTokens()) {
			value = stk.nextToken();
			// System.out.println( " next token "+ value);
			if (value.length() >= pre_len) {
				longest_str = value;
			}
			// System.out.println( " prelength");
			pre_len = value.length();
		}
		// System.out.println( " return from getLongest->" +longest_str+"\n");
		return longest_str;
	}
	//read a file and get the longest 2 subNames from given name and write to an output file
	public static void getLongest2Names(String inputFile, String outputFile,String delimiter){
		String line = "";
		String key = ""; Map<String,String> mapNames = new HashMap();
		String value = "";
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			FileWriter writer = new FileWriter(outputFile);
			while ((line = reader.readLine()) != null) {
				StringTokenizer stk = new StringTokenizer(line, delimiter);
				count = 1;
				if(line.equals(""))
					continue;
				System.out.println(line + ";token size:"+stk.countTokens());
				while (stk.hasMoreTokens()) {
					value = stk.nextToken();
					if (count == 1) {
						key = value;
					} else if (count == 2 && !(value.equalsIgnoreCase("nana"))  ) {
						mapNames = get2Longest(value," ");
					}
					count++;
				} // while loop for string tokenization
				//writerDebug.flush();
				//writer.append("key->"+key+";first string:"+mapNames.get("1")+"; 2nd string:"+mapNames.get("2")+"\n");
				writer.append(key+"|||"+mapNames.get("1")+"|||"+mapNames.get("2")+"\n");
				writer.flush();
				mapNames.clear();
			} // while loop for line read of given file
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	// For Aminer: For given name, get the longest 2 names from it
	public static Map<String,String> get2Longest(String str, String delimiter) {
		str = str.replace(".", " ").replace("-", " ").replace("'", " ").replace(",", " ")
				 .replace("  ", " ").toLowerCase();
		System.out.println(str);
		StringTokenizer stk = new StringTokenizer(str, delimiter);
		String value = "";
		Map<String,String> names = new HashMap();
		Map<String,String> LongestNames = new HashMap();
		int pre_len = 0;
		String longest_str = "";
		// System.out.println( "  before while loop->"+ str);
		while (stk.hasMoreTokens()) {
			value = stk.nextToken();
			// System.out.println( " next token "+ value);
			if(!names.containsKey(value) && value.length()>1)
				names.put(value, "");
		}
		//get the first longest length
		for(String name: names.keySet() ){
			if (name.length() >= pre_len && name.length() > 1) {
				longest_str = name;
			}
			pre_len = value.length();
		}
		//System.out.println( " size:"+ names.size()  +"\n");
		//remove the first longest length and search for 2nd longest space
		LongestNames.put("1",longest_str);
		names.remove(longest_str);
		System.out.println( "First getLongest->" +longest_str+"; size:"+ names.size()  +"\n");
		pre_len =0; longest_str = "";
		//get the second longest length
		for(String name: names.keySet() ){
			//System.out.println( "\n proces->" + pre_len + ";"+ name.length() +";" +longest_str);
			if (name.length() >= pre_len && name.length() > 1) {
				longest_str = name;
			}
				pre_len = value.length();
		}
		if(longest_str.length() > 0)
			LongestNames.put("2",longest_str);
		// System.out.println( " prelength");
		System.out.println( "Second getLongest->" +longest_str+"\n");
		return LongestNames;
	}
	// find the occurence of given substring for a given string
	public static int count(final String string, final String substring) {
		int count = 0;
		int idx = 0;
		int offset = 0;

		while ((idx = string.indexOf(substring, idx + offset)) != -1) {
			idx++;
			count++;
			offset = 2;
		}

		return count;
	}

	// get noise words from given file
	public static Map getNoiseWords(String NoiseWordsFile) {
		Map<String, String> NoiseWordMap = new HashMap();
		try {
			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(NoiseWordsFile)));
			// read the file and put into map
			while ((line = reader.readLine()) != null) {
				NoiseWordMap.put(line, "");
			}
			System.out.println(" noise word size->" + NoiseWordMap.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NoiseWordMap;
	}

	// Clean the conference
	public static String CleanConference(Map<String, String> NoiseWords,
			String conferenceFileName, String OutputFile, FileWriter writerDebug) {

		String value = "";
		String conference_total = "", line = "";
		String aid = "", conf = "";
		try {
			// writerDebug.append("given conference:" + "" +";Noise Word size->"
			// +NoiseWords.size()+"\n");
			BufferedReader reader = new BufferedReader(new FileReader(
					conferenceFileName));
			FileWriter writer = new FileWriter(OutputFile);
			int count = 0;

			while ((line = reader.readLine()) != null) {
				line = line.toLowerCase().replace(";", " ").replace(".", " ")
						.replace(":", " ").replace(",", " ");
				count = 1;
				StringTokenizer stk1 = new StringTokenizer(line, "|||");
				while (stk1.hasMoreTokens()) { // token has gid and conf string
					value = stk1.nextToken();
					if (count == 1)
						aid = value;
					if (count == 2)
						conf = value;
					count++;
				} // while end of tokenization
				conference_total = "";
				StringTokenizer stk = new StringTokenizer(conf, " ");
				// for each of the noise words
				while (stk.hasMoreTokens()) { // for each word of given
												// conference
					value = stk.nextToken();
					if (NoiseWords.containsKey(value)) {
					} else
						conference_total = conference_total + " " + value;
				}// while end of tokenization
				writer.append(aid + "|||" + conference_total + "\n");
			} // outer while loop end
			// writerDebug.append("return conference:"+conference_total +"\n");
			writer.flush();
			// writerDebug.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conference_total;
	}

	// method to read from file ( it uses thread) and returns as Map
	public static Map readFromFile(final String f, Thread thread,
			final Map nameValueMap) {
		Runnable readRun = new Runnable() {
			public void run() {
				FileInputStream in = null;
				String text = null;
				try {
					Thread.sleep(0);
					File inputFile = new File(f);
					in = new FileInputStream(inputFile);
					byte bt[] = new byte[(int) inputFile.length()];
					in.read(bt);
					text = new String(bt);
					System.out.println(text);
					String line = "";
					String id = "";
					String names = "";
					BufferedReader reader = new BufferedReader(
							new FileReader(f));
					while ((line = reader.readLine()) != null) {
						id = line.substring(0, line.indexOf("|||") + 1)
								.replace("|", "");
						line = line.replace(id, "");
						names = line.substring(0, line.length())
								.replace(";", ",").replace("|", "");
						System.out.println("name->" + id + ";value->" + names);
						nameValueMap.put(id, names.toLowerCase());
					}
				} catch (Exception ex) {
				}
			}
		};
		thread = new Thread(readRun);
		thread.start();
		return nameValueMap;
	}

	// method to find strings with special characters from given line ( : ? " '
	// - )
	public static String getSpecialWords(Map<String, String> Title) {
		String value = "", SpecialWords = "";
		Map<String, String> specialWordMap = new HashMap();
		try {
			for (String line : Title.keySet()) {
				line = line.replace("  ", " ").replaceAll("\\p{Cntrl}", "");
				StringTokenizer stk = new StringTokenizer(line, " ");
				while (stk.hasMoreTokens()) { // for each word of given
												// conference
					value = stk.nextToken();
					if (value.indexOf("?") > 0 || value.indexOf("?") > 0
							|| value.indexOf(":") > 0 || value.indexOf("'") > 0
							|| value.indexOf("-") > 0 || value.indexOf(",") > 0
							|| value.indexOf("+") > 0 || value.indexOf("/") > 0
							|| value.indexOf(",") > 0) {
						if (SpecialWords.length() == 0)
							SpecialWords = value;
						else
							SpecialWords = SpecialWords + "|||" + value;

					}
				}// while loop inner
			}// while loop outer
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SpecialWords;
	}

	// method to find strings with ING in the ending from given line (Example:
	// linking, processing)
	public static String getINGwords(Map<String, String> Title) {
		String value = "";
		Map<String, String> specialWordMap = new HashMap();
		String SpecialWords = "";
		try {
			for (String line : Title.keySet()) {
				line = line.replace("  ", " ").replaceAll("\\p{Cntrl}", "");
				StringTokenizer stk = new StringTokenizer(line, " ");
				while (stk.hasMoreTokens()) { // for each word of given
												// conference
					value = stk.nextToken().toLowerCase();
					if (value.indexOf("ing") > 0
							&& !value.equalsIgnoreCase("using")
							&& !value.equalsIgnoreCase("doing")
							&& !value.equalsIgnoreCase("single")
							&& !value.equalsIgnoreCase("multiple")) {

						if (SpecialWords.length() == 0)
							SpecialWords = value;
						else
							SpecialWords = SpecialWords + "|||" + value;
					}
				}// while loop inner
			}// for loop outer
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SpecialWords.replace(".", "").replace(":", "").replace(",", "");
	}

	// method to find non-dictionary words - Personalized Words from given title
	public static String getPersonalizedWords(Map<String, String> Title,
			Map<String, String> DictionaryMap) {
		String value = "";
		Map<String, String> specialWordMap = new HashMap();
		String SpecialWords = "";
		Pattern pattern = Pattern.compile("[0-9]");
		Matcher matcher;

		try {
			for (String line : Title.keySet()) {
				line = line.replace("?", " ").replace("-", " ")
						.replace(":", " ").replace(",", " ").replace("-", " ")
						.replace("+", "").replace("/", "").replace(",", "")
						.replace("!", "").replace(".", "");
				line = line.replace("  ", " ").replaceAll("\\p{Cntrl}", "");
				StringTokenizer stk = new StringTokenizer(line, " ");

				while (stk.hasMoreTokens()) { // for each word of given
												// conference
					value = stk.nextToken().toLowerCase();
					matcher = pattern.matcher(value);

					if (!DictionaryMap.containsKey(value)
							&& !matcher.find()
							&& // not contains number
							value.indexOf("the") == -1
							&& value.indexOf("and") == -1
							&& value.indexOf("tha") == -1
							&& value.indexOf("ent") == -1
							&& value.indexOf("ing") == -1
							&& value.indexOf("ion") == -1
							&& value.indexOf("tio") == -1
							&& value.indexOf("for") == -1
							&& value.indexOf("nde") == -1
							&& value.indexOf("has") == -1
							&& value.indexOf("nce") == -1
							&& value.indexOf("edt") == -1
							&& value.indexOf("tis") == -1
							&& value.indexOf("oft") == -1
							&& value.indexOf("sth") == -1
							&& value.indexOf("men") == -1) {

						if (SpecialWords.length() == 0)
							SpecialWords = value;
						else
							SpecialWords = SpecialWords + "|||" + value;
					}
				}// while loop inner
			}// for loop outer
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SpecialWords;
	}
	// method to find strings with numbers characters from given line ( 2d or 3d
	// or three or two )
	public static String getNumbers(Map<String, String> Title) {
		String value = "";
		Pattern pattern = Pattern.compile("[0-9]");
		Matcher matcher;
		String SpecialWords = "";
		Map<String, String> specialWordMap = new HashMap();
		try {
			for (String line : Title.keySet()) {
				line = line.replace("  ", " ").replaceAll("\\p{Cntrl}", "");
				StringTokenizer stk = new StringTokenizer(line, " ");
				while (stk.hasMoreTokens()) { // for each word of given
												// conference
					value = stk.nextToken();
					matcher = pattern.matcher(value);
					if ((matcher.find() || value.equalsIgnoreCase("two") || value
							.equalsIgnoreCase("three")
							&& (value.length() > 0 && value
									.equalsIgnoreCase(" ")))) {
						if (SpecialWords.length() == 0)
							SpecialWords = value;
						else
							SpecialWords = SpecialWords + "|||" + value;
					}
				}// while loop inner
			}// while loop outer
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SpecialWords.replace(" ", "");
	}
	// load multiple values to maps
	public static void LoadMultipleValueToMap4(final Map mapName,
			final Map mapHomepage, final Map mapEmail, final Map mapPosition,
			String filename, String delimiter, FileWriter writerDebug) {
		String line = "";
		String key = "";
		String value = "";
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while ((line = reader.readLine()) != null) {
				line = line.replaceAll("\\p{Cntrl}", "");
				StringTokenizer stk = new StringTokenizer(line, delimiter);
				count = 1;
				while (stk.hasMoreTokens()) {
					value = stk.nextToken();
					if (count == 1) {
						key = value;
					} else if (count == 2 && !(value.equalsIgnoreCase("na"))) {
						// writerDebug.append(" hp map insertion "+
						// key+";"+value+"\n");
						mapName.put(key, value);
					} else if (count == 3 && !(value.equalsIgnoreCase("na"))) {
						// writerDebug.append(" hp map insertion "+
						// key+";"+value+"\n");
						mapHomepage.put(key, value);
					} else if (count == 4 && !(value.equalsIgnoreCase("na"))) {
						// writerDebug.append(" email map insertion "+
						// key+";"+value+"\n");
						mapEmail.put(key, value);
					} else if (count == 5 && !(value.equalsIgnoreCase("na"))) {
						// writerDebug.append("  Postion map insertion "+
						// key+";"+value+"\n");
						mapPosition.put(key, value);
					}
					count++;
				} // while loop for string tokenization
				writerDebug.flush();
			} // while loop for line read of given file
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// method to find score two given strings delimited with " "
	public static final Map<String, String> plainScore(String word1,
			String word2, String delimiter, FileWriter writerDebug) {
		word1 = word1.replace(delimiter, " ").toLowerCase();
		word2 = word2.replace(delimiter, " ").toLowerCase();
		delimiter = " ";
		word1.replace(delimiter + delimiter, delimiter);
		String[] awords = word1.split(delimiter);
		String common_words = "";
		String key = "";
		Map<String, String> scoreMap = new HashMap();
		int min_w_length = 0;
		int value = 0;
		int sum_freq = 0;
		int t_common_words = 0;
		Map gwordsmap = new TreeMap();
		Integer frequency = 0;
		Map awordsmap = new TreeMap();
		try {
			// writerDebug.append("\nInside plainScore:\nword1-->"+word1+"\nword2-->"+word2+"\narrayWord->"+awords.length);
			// For each word
			for (int i2 = 0, n = awords.length; i2 < n; i2++) {
				if (awords[i2].length() > 0) {
					frequency = (Integer) awordsmap.get(awords[i2]); // get key
																		// from
																		// value
					if (frequency == null) {
						frequency = 1;
					} else {
						value = frequency.intValue();
						frequency = new Integer(value + 1);
					}
				}
				awordsmap.put(awords[i2], frequency);
			}
			String gwords[] = word2.split(delimiter);
			System.out.println("g length ->" + gwords.length);
			// For each word
			for (int i2 = 0, n = gwords.length; i2 < n; i2++) {
				if (gwords[i2].length() > 0) {
					frequency = (Integer) gwordsmap.get(gwords[i2]); // get key
																		// from
																		// value
					if (frequency == null) {
						frequency = 1;
					} else {
						value = frequency.intValue();
						frequency = new Integer(value + 1);
					}
				}
				gwordsmap.put(gwords[i2], frequency);
			}
			Set set2 = awordsmap.entrySet();
			// Get an iterator
			Iterator it = set2.iterator();
			gwordsmap.remove(" ");
			awordsmap.remove(" ");
			// check for word matching
			while (it.hasNext()) {
				Map.Entry me = (Map.Entry) it.next();
				key = (String) me.getKey().toString().replace(" ", "");
				value = (Integer) me.getValue();
				// writerDebug.append("\n  key value ->"+ key+"\n"+value);
				// System.out.println( " aword  ->"+ key + "; freq->" +value);
				if (gwordsmap.containsKey(key) && !(key.equalsIgnoreCase(" "))
						&& key.length() > 0) {
					t_common_words = t_common_words + 1;
					sum_freq = sum_freq + value + (Integer) gwordsmap.get(key);
					// writerDebug.append("\n bfr common word->"+common_words);
					common_words = common_words + delimiter + key;
					// writerDebug.append("\n afr common word->"+common_words);
				}
			}
			common_words = common_words.replace(" ", "");
			// find min word length
			if (gwordsmap.size() <= awordsmap.size())
				min_w_length = gwordsmap.size();
			else
				min_w_length = awordsmap.size();

			// writerDebug.append("\nsum_freq ->" + sum_freq / 2 +
			// "; min length->"
			// + (min_w_length ) + ";common words->" +
			// t_common_words+";score got->"+String.valueOf( t_common_words /
			// (float) (min_w_length )));

			scoreMap.put("1",
					String.valueOf(t_common_words / (float) (min_w_length)));
			scoreMap.put("2", common_words);
			// writerDebug.append("\n returned from map->"+scoreMap.get("1")+";"+scoreMap.get("2"));

			// writerDebug.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scoreMap;
	}

	// load multiple values to maps - 3 values for conference
	// key$$$correct_abbreviation$$$correct_conference name
	// map1 contains key -> conference name ; map2 contains correct_abbr ->
	// conference name
	public static void LoadMultipleValueToMap3(
			final Map<String, String> keyFNameConfMap,
			final Map<String, String> cAbbrFNameConfMap, String filename,
			String delimiter, FileWriter writerDebug) {
		String line = "";
		String key = "", correctAbbr = "";
		String value = "";
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while ((line = reader.readLine()) != null) {
				StringTokenizer stk = new StringTokenizer(line, delimiter);
				count = 1;
				System.out.println("token size:" + stk.countTokens());
				while (stk.hasMoreTokens()) {
					value = stk.nextToken();
					if (count == 1) {
						key = value;
					} else if (count == 2 && !(value.equalsIgnoreCase("nana"))) {
						// writerDebug.append(" hp map insertion "+
						// key+";"+value+"\n");
						correctAbbr = value;
					} else if (count == 3 && !(value.equalsIgnoreCase("nana"))) {
						System.out.println("\n loading ->" + key + ";" + value
								+ ";" + correctAbbr);
						keyFNameConfMap.put(key, value); // key and full
															// conference name
						cAbbrFNameConfMap.put(correctAbbr, value);// correct
																	// abbreviation
																	// and full
																	// conference
																	// name
					}// if ends
					count++;
				} // while loop for string tokenization
					// writerDebug.flush();
			} // while loop for line read of given file
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	// method will take <key|||value1|||value2> output-> map<key,value1> , map<key,value2)
	public static void LoadMultipleValueToKeyMap3(
			final Map<String, String> urlMap,
			final Map<String, String> truthMap,
			String filename,
			String delimiter,
			FileWriter writerDebug) {
		String line = "";
		String key = "", url = "";
		String value = "";
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while ((line = reader.readLine()) != null) {
				writerDebug.append("new line , delimiter->"+line +"<->"+ delimiter);
				writerDebug.flush();
				StringTokenizer stk = new StringTokenizer(line, delimiter);
				count = 1;
				//System.out.println("token size:" + stk.countTokens());
				while (stk.hasMoreTokens()) {
					value = stk.nextToken();
					if (count == 1) {
						key = value;
					} else if (count == 2 && !(value.equalsIgnoreCase("nana"))) {
						// writerDebug.append(" hp map insertion "+
						// key+";"+value+"\n");
						url = value;
					} else if (count == 3 && !(value.equalsIgnoreCase("nana"))) {
						writerDebug.append("\n loading ->" + key + "<->" + value + "<->" + url);
						truthMap.put(key, value); // truth map
						urlMap.put(key, url); //url map
						writerDebug.flush();
					}// if ends
					count++;
				} // while loop for string tokenization
			} // while loop for line read of given file
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// clean title from input file and write to output file , input format
	// <gid|||Title>
	public static void cleanTitle(String inputFile, String outputFile,String delimiter){
		String line = "";
		String key = "", title = "";
		String value = "";
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			FileWriter writer = new FileWriter(outputFile);
			while ((line = reader.readLine()) != null) {
				StringTokenizer stk = new StringTokenizer(line, delimiter);
				count = 1;
				if(line.equals(""))
					continue;
				System.out.println(line + ";token size:"+stk.countTokens());
				while (stk.hasMoreTokens()) {
					value = stk.nextToken();
					if (count == 1) {
						key = value;
					} else if (count == 2 && !(value.equalsIgnoreCase("nana"))  ) {
						title = cleanTitle(value);
					}
					count++;
				} // while loop for string tokenization
				//writerDebug.flush();
				writer.append(key+"|||"+title+"\n");
				writer.flush();
			} // while loop for line read of given file
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	
	
	// main method
	public static void main(String[] args) throws IOException {
		/*System.out.println(editDis("Changjie Tang", "Chang-jie Tang"));
		System.out.println(getLongest("Changjie Tang,Chang-jie Tang", ","));
		System.out.println(getLongest("Michael Chau,Michael Chiu-Lung Chau",","));*/
		 
		String prefix = "D:\\share\\lenin\\";
		String inputFile = prefix +"GetEditDistance1.txt";
		String outputFile = prefix +"GetEditDistance1_out.txt";
		//cleanTitle(inputFile,outputFile,"|||");
		
		//getLongest2Names( inputFile , outputFile,"|||");
		getEditDistance( inputFile , outputFile,"|||");
		
		//get2Longest("T. Cacoullos,Theophilos Cacoullos"," ");
		 
	}
}