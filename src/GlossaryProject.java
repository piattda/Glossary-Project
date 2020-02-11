import java.io.File;
import java.util.Comparator;

import components.map.Map;
import components.map.Map.Pair;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * A simple glossary program. 
 * 
 * @author David Piatt
 */



public final class GlossaryProject {
    
    

	/**
     * Make a map of the input file
     * 
     * @param filename
     * 	file name for the input file
     * 
     * @param orderedList
     * 	An ordered queue sorted alphabetically to properly print the 
     * 	glossary in alphabetical order in the index.html file.
     * 
     * @updates out.content 
     * 
     * @requires inFile /= null
     * 
     * @ensures [map contains term as key and definition as value]
     * 
     */
	
	public static Map<String, String> getGlossMap(String fileName, Queue<String> orderedList) {
        assert fileName != null : "Violation of: fileName is not null";
        
        //decalre a map. 
        Map<String, String> glossMap = new Map1L<String,String>();
    	
        // where are we reading from? declare it. 
    	SimpleReader input = new SimpleReader1L(fileName);    	
    	
    	
    	while( !input.atEOS() ){
    		
    		String term = input.nextLine();
    		orderedList.enqueue(term);
    		StringBuffer definition = new StringBuffer(input.nextLine());
    		String isBlank = input.nextLine();
    		while(!isBlank.equals("")){
    			
    			//in case definition is more than one line
    			definition.append(isBlank);
    			 
    			
    			isBlank = input.nextLine();
    		}
    		String def = definition.toString();
    		glossMap.add(term, def);
    		
    	}
    	return glossMap;

    }
	
	/**
	 * makes the IndexHTML
	 * @param glossMap
	 * 			Map that contains terms and definitions
	 * @param folder
	 * 			folder to write files to
	 * @param order	
	 * 			Alphabetically sorted queue that is used in generation of index.html
	 * 
	 * @updates
	 * 
	 * out.content		
	 * 
	 * @requires
	 * 
	 * glossmap =/ empty
	 * queue order sorted
	 * 			
	 * @ensures
	 * The index.html file will be printed in alphabetical oder. 
	 *  The supportingHTML method will be called to make the terms html files.
	 * 
	 */
	
	public static void makeIndexHTML(Map <String, String> glossMap, String folder, Queue<String> order){
		
		//I put this here to avoid putting it through a while loop and creating
		//the set multiple times. 
		
		//This separates the separators out of the word. It's the delimeter.
		final String separatorStr = " \t, "; 

		//make set of characters. 
		Set<Character> separatorSet = new Set1L<>();

	    generateElements(separatorStr, separatorSet);

		
		
		for(int i = 0; i < order.length(); i++){
		
		SimpleWriter out = new SimpleWriter1L(folder + "/index.html");
		
		//This is the header for the index. Not for the supporting HTML pages. 
		
		out.println("<html>");
		out.println("<head>");
		out.println("<title> The best glossary </title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h2> The best amazing-ist glossary in the world </h2>");
		out.println("<hr />");
		out.println("<h3> Index </h3>");
		out.println("<ul>");
		
		//because queue length is changing let's declare a constant for its length. 
		
		int length = order.length();

		for(int k = 0; k < length; k++){
			
			//take one term out
			String current = order.dequeue();
			
			//Begin to make file to print to.
			String currentFile = folder + "/" + current;
			
			//Get definition
			String definition = glossMap.value(current);
			
			//Finish compositng the output file name.
			String outputFileName = currentFile + ".html";

			//Declare simple writer to the output file. 
			SimpleWriter newOut = new SimpleWriter1L(outputFileName);			
			
			//write the supporting html files. eg. links etc...			
			supportingHTMLFiles(current, definition, newOut, glossMap, separatorSet);
			
			out.println("<li>"+ "<a href = " + current + ".html>" + current +"</a>" + "</li>");

			
			order.enqueue(current);
		}
		out.println("</ul>");
		out.println("</body>");
		out.println("</html>");
		
		}
	}
	
	/**
	 * Generates the set of characters in the given {@code String} into the
	 * given {@code Set}.
	 * 
	 * @param str
	 *            the given {@code String}
	 * @param strSet
	 *            the {@code Set} to be replaced
	 * @replaces strSet
	 * @ensures strSet = entries(str)
	 */
	private static void generateElements(String str, Set<Character> strSet) {
		
		for(int i = 0; i < str.length(); i ++){
			if(!strSet.contains(str.charAt(i))){
			strSet.add(str.charAt(i));
			}
			
		}
		
	}
	
	/**
	 * Returns the first "word" (maximal length string of characters not in
	 * {@code separators}) or "separator string" (maximal length string of
	 * characters in {@code separators}) in the given {@code text} starting at
	 * the given {@code position}.
	 * 
	 * note I worked with Nafisa Hassan on this method in lab with Emily Sheng.
	 *
	 * @param text
	 *            the {@code String} from which to get the word or separator
	 *            string
	 * @param position
	 *            the starting index
	 * @param separators
	 *            the {@code Set} of separator characters
	 * @return the first word or separator string found in {@code text} starting
	 *         at index {@code position}
	 * @requires 0 <= position < |text|
	 * @ensures <pre>
	 * nextWordOrSeparator =
	 *   text[position, position + |nextWordOrSeparator|)  and
	 * if entries(text[position, position + 1)) intersection separators = {}
	 * then
	 *   entries(nextWordOrSeparator) intersection separators = {}  and
	 *   (position + |nextWordOrSeparator| = |text|  or
	 *    entries(text[position, position + |nextWordOrSeparator| + 1))
	 *      intersection separators /= {})
	 * else
	 *   entries(nextWordOrSeparator) is subset of separators  and
	 *   (position + |nextWordOrSeparator| = |text|  or
	 *    entries(text[position, position + |nextWordOrSeparator| + 1))
	 *      is not subset of separators)
	 * </pre>
	 */

	private static String nextWordOrSeparator(String text, int position,
	        Set<Character> separators) {

	    String toReturn = "";
        int index = position;
        char tmp = text.charAt(index);
        
        if(separators.contains(tmp) && index < text.length()){
        	while(separators.contains(tmp)){
        		index++;
        		if(index < text.length()){
        			tmp = text.charAt(index);
        		}
        	}
        } else{
        	while(!separators.contains(tmp)&& index < text.length()){
        		index ++;
        		if(index < text.length()){
        			tmp = text.charAt(index);
        			
        		}
        	}
        }
        
        return text.substring(position, index);
	}

	/**
	 * Outputs the header for any of the definition HTML pages.
	 * 
	 * @param out
	 * 	The out writer to print out to the page. 
	 * 
	 * @param term
	 * 	The term to be placed at the top of the page. 
	 * 
	 */

	public static void outputHeader(SimpleWriter out, String term){
		out.println("<html>");
		out.println("<head>");
		out.println("<title>" + term + "</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h2><b><i><font color='red'>" + term + "</font></i></b></h2>");
	}


	
	/*
	 * makes the term.html files.
	 * @param term
	 * 			String with the term
	 * @param definition
	 * 			string with the definition
	 * @param out	
	 * 			out writer to the html file.
	 * @param map
	 * 			copy of gloss map used to find terms nested within definitions
	 * 
	 * @updates
	 * 		out.content
	 * 
	 * 
	 * @requires
	 * 			term, definition, map /= empty
	 * glossmap =/ empty
	 * queue order sorted
	 * 			
	 * @ensures
	 * The term_name.html file will be printed in alphabetical oder. 
	 *  Any terms within definitions will be hyperlinked to.
	 * 
	 */
	
	public static void supportingHTMLFiles(String term, String definition, SimpleWriter out, Map<String,String> Map, Set<Character> separatorSet){
	
	    
	    //to avoid magic number errors
		int position = 0;

	    String nextWordorSeparator = nextWordOrSeparator(definition, position, separatorSet);
		
		position = nextWordorSeparator.length();
		
		outputHeader(out, term);
		
		String definition2 = "";
		
		while(position < definition.length()){
			if(Map.hasKey(nextWordorSeparator)){
				definition2 = definition2 + "<a href=\"" + nextWordorSeparator + ".html\">" + nextWordorSeparator + "</a>";
				position += nextWordorSeparator.length();
				
				if(position < definition.length()){
				nextWordorSeparator = nextWordOrSeparator(definition, position, separatorSet);
				
				}
				
				
				
			} else {
				definition2 = definition2 + nextWordorSeparator;
				position += nextWordorSeparator.length();
				if(position < definition.length()){
				nextWordorSeparator = nextWordOrSeparator(definition, position, separatorSet);
				}
			}
		}
	    
		
		
		out.println("<blockquote>" + definition2 + "</blockquote>");
		out.println("<hr />");
		out.println("<p>Return to <a href='index.html'>index</a>.</p>");
		out.println("</body>");
		out.println("</html>");
		
		
	}
	/**
     * Main method.
     * 
     * @param args
     *            the command line arguments; unused here
     */
    
    public static void main(String[] args) {
        
    	SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        
        //input in file

        out.println("Please enter a file to turn into an amazing glossary: ");
        String inputFileName = in.nextLine();
        
       
        
        //input folder to output

        out.println("Please enter the name of the destination folder for said amazing glossary: ");
        String outputFolderName = in.nextLine();
        
        String unModifiedFolderName /*because I'm stupid and changed it agh*/ = outputFolderName;
        
        //set path to print to. 
        outputFolderName += "/index.html";
        
        SimpleWriter newOut = new SimpleWriter1L(outputFolderName);
        
        
        

        Queue<String> orderedList = new Queue1L();
        
        //Like in the Pizza order lab, read in the delimeted values and 
        // map them to each other. This will allow us to form a relation
        // between the words and terms. 
        
        Map<String, String> glossMap = getGlossMap(inputFileName, orderedList);
        
        //This is more efficient way of sorting the queue instead of using
        //the comparator method. 

        orderedList.sort(String.CASE_INSENSITIVE_ORDER);
        
        
        
        //begin outputing the web page. Pass in the map with the terms and definitions
        // pass in folder to output to. Pass in the ordered list. 

        makeIndexHTML(glossMap, unModifiedFolderName, orderedList);
       
    	
    }

}
