import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author Rohan Patel, Zack Zhu
 *
 */

public class TagCloudGenerator {

    /**
     * Comparator used to sort the pairs with larger value have larger priority.
     */
    private static final Comparator<Map.Entry<String, Integer>> INT_COM = new Comparator<Map.Entry<String, Integer>>() {
        @Override
        public int compare(Entry<String, Integer> o1,
                Entry<String, Integer> o2) {
            return o2.getValue().compareTo(o1.getValue());
        }
    };

    /**
     * Comparator used to sort the string with alphabetical order (case
     * insensitive).
     */
    private static final Comparator<Map.Entry<String, Integer>> STRING_COM = new Comparator<Map.Entry<String, Integer>>() {
        @Override
        public int compare(Entry<String, Integer> o1,
                Entry<String, Integer> o2) {
            return o1.getKey().toLowerCase()
                    .compareTo(o2.getKey().toLowerCase());
        }
    };

    /**
     * The maximum value of counts.
     */
    private static int max;
    /**
     * The minimum value of counts.
     */
    private static int min;

    /**
     * A sorting machine of all key (word) value (count of occurrence) pairs,
     * ordered by the values.
     */

    /**
     * Default constructor.
     */
    public TagCloudGenerator() {

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
        assert str != null : "Violation of: str is not null";
        assert strSet != null : "Violation of: strSet is not null";
        for (int i = 0; i < str.length(); i++) {
            if (!(strSet.contains(str.charAt(i)))) {
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
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        String textCopy = text.toLowerCase();
        int posCopy = position;
        String word = "";
        while (posCopy != textCopy.length()
                && !separators.contains(textCopy.charAt(posCopy))) {
            word += textCopy.charAt(posCopy);
            posCopy++;
        }
        if (word.equals("") && separators.contains(textCopy.charAt(posCopy))) {
            word = Character.toString(textCopy.charAt(posCopy));
        }
        return word;
    }

    /*
     * Outputs the generated HTML file
     *
     * @param out: the file to write to
     *
     * @param title: name of file to be written to
     *
     * @param list: elements that are output in the html file
     *
     * @requires [the root of channel is a <channel> tag] and out.is_open
     *
     * @ensures out.content = #out.content * [the HTML file]
     */
    public static void generateHTML(BufferedWriter out, String title,
            List<Map.Entry<String, Integer>> list) {
        try {
            out.write("<html>\n");
            out.write("<head>\n");
            out.write("<title>Top " + list.size() + " words in " + title
                    + "</title>\n");
            out.write(
                    "<link href =\"http://web.cse.ohio-state.edu/software/2231/web-sw2/assignments/projects/tag-cloud-generator/data/tagcloud.css\" rel=\"stylesheet\" type=\"text/css\">\n");
            out.write("</head>\n");
            out.write("<body>\n");
            out.write("<h2>Top " + list.size() + " words in " + title
                    + "</h2>\n");
            out.write("<hr>\n");
            out.write("<div class=\"cdiv\">\n");
            out.write("<p class=\"cbox\">\n");
            for (int i = 0; i < list.size(); i++) {
                int fontSize = 37 * (list.get(i).getValue() - min) / (max - min)
                        + 11;
                out.write("            <span style=\"cursor:default\" class=\"f"
                        + fontSize + "\" title=\"count: "
                        + list.get(i).getValue() + "\">" + list.get(i).getKey()
                        + "</span>\n");
            }
            out.write("</p>\n");
            out.write("</div>\n");
            out.write("</body>\n");
            out.write("</html>\n");
        } catch (IOException e) {
            System.err.println("Error writing to file");
            return;
        }
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        /*
         * Define separator characters for test
         */
        final String separatorStr = " \t\n\r,-.!?[]';:/()";

        Set<Character> separatorSet = new HashSet<>();
        generateElements(separatorStr, separatorSet);
        Map<String, Integer> wordCount = new HashMap<String, Integer>();
        List<Map.Entry<String, Integer>> numOrder = new ArrayList<Map.Entry<String, Integer>>();

        /*
         * Open input and output streams
         */
        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.print("Enter N: ");
        int n;
        try {
            n = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            System.err.println("Input is invalid");
            return;
        }

        System.out.print("Enter the input file: ");

        String inputFileName;
        try {
            inputFileName = in.readLine();
        } catch (IOException e) {
            System.err.println("Input is invalid");
            return;
        }

        BufferedReader inFile;
        try {
            inFile = new BufferedReader(new FileReader(inputFileName));
        } catch (IOException e) {
            System.err.println("Error reading from file");
            return;
        }

        System.out.print("Enter a name for the ouput file: ");
        String outputFileName;
        try {
            outputFileName = in.readLine();
        } catch (IOException e) {
            System.err.println("Input is invalid");
            return;
        }

        BufferedWriter outFile;
        try {
            outFile = new BufferedWriter(new FileWriter(outputFileName));
        } catch (IOException e) {
            System.err.println("Error writing to file");
            return;
        }

        //Put words into wordCount
        int position = 0;
        String line;
        try {
            line = inFile.readLine();
            while (line != null) {
                while (position < line.length()) {
                    String token = nextWordOrSeparator(line, position,
                            separatorSet);
                    if (!separatorSet.contains(token.charAt(0))) {
                        if (wordCount.containsKey(token)) {
                            int i = wordCount.get(token) + 1;
                            wordCount.replace(token, i);
                        } else {
                            wordCount.put(token, 1);
                        }
                    }
                    position += token.length();
                }
                position = 0;
                line = inFile.readLine();
            }
        } catch (IOException e1) {
            System.err.println("Error reading from file");
            return;
        }

        //Put map into numOrder and sort them by values
        for (Map.Entry<String, Integer> i : wordCount.entrySet()) {
            numOrder.add(i);
        }
        numOrder.sort(INT_COM);

        //Get rid of extra entries and sort by keys
        for (int i = n; i < numOrder.size(); i++) {
            numOrder.remove(i);
            i--;
        }
        max = numOrder.get(0).getValue();
        min = numOrder.get(numOrder.size() - 1).getValue();
        numOrder.sort(STRING_COM);

        generateHTML(outFile, inputFileName, numOrder);

        try {
            in.close();
            inFile.close();
            outFile.close();
        } catch (IOException e) {
            System.err.println("Error closing file");
        }
    }

}
