    import java.io.*;
    import java.nio.charset.Charset;
    import java.nio.file.*;
    import java.util.*;
    
    /**
     * The responder class represents a response generator object.
     * It is used to generate an automatic response, based on specified input.
     * Input is presented to the responder as a set of words, and based on those
     * words the responder will generate a String that represents the response.
     *
     * Internally, the reponder uses a HashMap to associate words with response
     * strings and a list of default responses. If any of the input words is found
     * in the HashMap, the corresponding response is returned. If none of the input
     * words is recognized, one of the default responses is randomly chosen.
     * 
     * @Nick.
     * @Lab 13
     * @author David J. Barnes and Michael Kölling.
     * @version 2016.02.29
     */
    public class Responder
    {
        // Used to map key words to responses.
        private HashMap<String, String> responseMap;
        //private HashMap<String, String> responseMap;
        // Default responses to use if we don't recognise a word.
        private ArrayList<String> defaultResponses;
        // The name of the file containing the default responses.
        private static final String FILE_OF_DEFAULT_RESPONSES = "default.txt";
        private static final String FILE_OF_RESPONSES = "responses.txt";
        private Random randomGenerator;
    
        /**
         * Construct a Responder
         */
        public Responder()
        { 
            defaultResponses = new ArrayList<String>();
            responseMap = new HashMap<>();
            fillDefaultResponses();
            randomGenerator = new Random();
        }
    
        /**
         * Generate a response from a given set of input words.
         * 
         * @param words  A set of words entered by the user
         * @return       A string that should be displayed as the response
         */
        public String generateResponse(HashSet<String> words)
        {
            Iterator<String> it = words.iterator();
            while(it.hasNext()) {
            String word = it.next();
            String response = responseMap.get(word);
            if(response != null) {
                    return response;
                }
            }
            return pickDefaultResponse();
         }    
         
         
     /**
     * responseMap read from text file
     * 
     */
    private void fillResponseMap()
    {
        Charset charset = Charset.forName("US-ASCII");
        Path path = Paths.get(FILE_OF_RESPONSES);
        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
            String response = reader.readLine();
            boolean isFollowing = false;
            String key = null;
            int blankLine = 0;
            do
            {
                if((response != null) && (!response.isEmpty()))
                {
                    StringBuilder StringBuilder = new StringBuilder();
                    blankLine--;
                    while((response != null) && (!response.trim().isEmpty()))
                    {
                        if(!isFollowing)
                        {
                            key = response;
                            isFollowing = true;
                        }
                        else
                        {
                            StringBuilder.append(response);
                            StringBuilder.append("\n");
                        }
                        response = reader.readLine();
                    }
                    blankLine++;
                    responseMap.put(key.trim(), StringBuilder.toString());
                    isFollowing = false;
                    
                }
                else
                {
                    blankLine++;
                }
                response = reader.readLine();
            }while (blankLine < 2);
        }
    
        catch(FileNotFoundException e) {
            System.err.println("Unable to open " + FILE_OF_DEFAULT_RESPONSES);
        }
        catch(IOException e) {
            System.err.println("A problem was encountered reading " +
                               FILE_OF_DEFAULT_RESPONSES);
        }
        
}

 
    /**
     * Build up a list of default responses from which we can pick
     * if we don't know what else to say.
     */
    private void fillDefaultResponses()
    {
        Charset charset = Charset.forName("US-ASCII");
        Path path = Paths.get(FILE_OF_DEFAULT_RESPONSES);
        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
            String response = reader.readLine();
            int blankLine = 0;
            do
            {
                if((response != null) && (!response.isEmpty()))
                {
                    StringBuilder StringBuilder  = new StringBuilder();
                    blankLine--;
                    while((response != null) && (!response.isEmpty()))
                    {
                        StringBuilder.append(response);
                        response = reader.readLine();
                    }
                    blankLine++;
                    defaultResponses.add(StringBuilder.toString());
                }
                else
                {
                    blankLine++;
                }

                
                response = reader.readLine();
                
            }while (blankLine < 2);
        }
        catch(FileNotFoundException e) {
            System.err.println("Unable to open " + FILE_OF_DEFAULT_RESPONSES);
        }
        catch(IOException e) {
            System.err.println("A problem was encountered reading " +
                               FILE_OF_DEFAULT_RESPONSES);
        }
        // Make sure we have at least one response.
        if(defaultResponses.size() == 0) {
            defaultResponses.add("Could you elaborate on that?");
        }
    }

    /**
     * Randomly select and return one of the default responses.
     * @return     A random default response
     */
    private String pickDefaultResponse()
    {
        // Pick a random number for the index in the default response list.
        // The number will be between 0 (inclusive) and the size of the list (exclusive).
        int index = randomGenerator.nextInt(defaultResponses.size());
        return defaultResponses.get(index);
    }
}
