# TagCloudGenerator

## Objectives
1. Familiarity with designing and coding a realistic component-based application program using only standard Java components.
2. Familiarity with using Java Collections Framework components (e.g., Map, List, and Collections from the java.util package).
3. Familiarity with using standard Java file I/O components (e.g., FileReader and FileWriter from the java.io package).
4. Exposure to handling checked exceptions (e.g., IOException) with the try-catch construct.

## The Problem
Write a Java program that generates a tag cloud from a given input text. The solution to this problem should have a lot in common with your solution for the word counter project you did at the start of the course. Here are some initial requirements for the new problem:

- The program shall ask the user for the name of an input file, for the name of an output file, and for the number of words to be included in the generated tag cloud (a positive integer, say N).
- The program shall respect the user input as being the complete relative or absolute path as the name of the input file, or the name of the output file, and will not augment the given path in any way, e.g., it will not supply its own filename extension. For example, a reasonable user response for the name of the input file could directly result in the String value "data/importance.txt"; similarly, a reasonable user response for the name of the output file could directly result in the String value "data/importance.html".
- In contrast with one or more past projects, the program shall check for invalid input; however, the program may (and probably should) rely on the SimpleReader and SimpleWriter family components to raise an error in response to conditions such as non-existent files or paths.
- The input file can be an arbitrary text file. No special requirements are imposed.
- The output shall be a single well-formed HTML file displaying the name of the input file in a heading followed by a tag cloud of the N words with the highest count in the input. The words shall appear in alphabetical order (in which, e.g., "bar" comes before "Foo", not the lexicographic order provided by the String compareTo method which would put capitalized words ahead of lower case ones, e.g., "Foo" would come before "bar"). The font size of each word in the tag cloud shall be proportional to the number of occurrences of the word in the input text (i.e., more frequent words will be displayed in a larger font than less frequent ones).
- Words contain no whitespace characters. Beyond that, it is up to you to come up with a reasonable definition of what a word is and what characters (in addition to whitespace characters) are considered separators. (For the sample inputs provided, the characters in the string " \t\n\r,-.!?[]';:/()" do a decent job of separating words.)
- You must use the SimpleReader and SimpleWriter family components for all the input and output needed.
- You must use the Map family components to keep track of the words and their counts.
- You must use the SortingMachine family components for all the sorting needed.
