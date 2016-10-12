// Daniel Hernandez
// 11/23/15
// CSC 311
// Project 4

/* 
   Project 4: This program implements a simple word correction program as
   required in the Project 4 PDF file and follows all directions and instrutions.
   It loads a dictionary of accepted words into an array. The array then gets 
   sorted. The user is asked to input one word at a time with $ indicating that
   no more input is to be taken. For each word entered, a binary search is used
   to find a match. A linear search is performed to check for an anagram and if
   one is found, the valid word will be suggested.
*/

// Required imports (Scanner input, and Files)
import java.util.*;
import java.io.*;

public class wordDictionary
{
    public static void main(String[] args) // begin main() method [8 lines]
    {
        wordDictionary words = new wordDictionary(); // create object from class
        
        //call required methods
        words.openWordList();   // load the text file with words
        words.readWordList();   // put all words in an array
        words.stringSort();     // sort the array of words
        words.inputWords();     // takes input and performs required operations
    } //end main()
    
    /* Method inputWords() asks the user to input one word at a time, after each
       input, the method recursiveBinarySearch(String[], String) is called. The
       method stops taking input when $ is entered.
    */
    public void inputWords() // begin inputWords() method
    {
        Scanner input = new Scanner(System.in);     // required to take input
        String word;    // used to store the word typed by the user
        
        // do while loop repeats, asking for input until $ is entered.
        do
        {
            System.out.println("Please enter a word or $ to stop input.");
            System.out.print("> ");
            word = input.nextLine();    // take input.
            
            // when $ is not entered, pass the input along with the sorted array
            // to the recursiveBinarySearch(String[], String) method
            if(!(word.equals("$")))
            {
                recursiveBinarySearch(wordDictionary.words, word);
            }
        } while(!(word.equals("$")));
    } // end inputWords()
    
    /* Method readWordList() takes the file that was opened by method
       openWordList() and begins to read each word in this text file.
       For each word read, it will be put into an array. The word list
       file contains 1,000 words so the array is fixed to contain 1,000
       elements.
    */
    public void readWordList() //begin readWordList() method
    {
        String line;    // store the word in the current line being read
        String wordRead;    // store the word from String Tokenizer st
        int i = 0;      // counter used for adding elements one by one
        
        File wordList = openWordList(); // Open the file
        
        try
        {
            // Required to properly read the file
            FileReader fr = new FileReader(wordList);
            BufferedReader br = new BufferedReader(fr);
            
            // while loop continues until the end of the file is reached
            while((line = br.readLine()) != null)
            {
                StringTokenizer st = new StringTokenizer(line); // grab the word
                
                wordRead = st.nextToken(); // store the word to the wordRead variable
                
                // add the word to the array and increment counter
                wordDictionary.words[i] = wordRead;
                i++;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace(); // catch the exception
        }
    } // end ReadWordList()
    
    /* Method openWordList() looks for the file wordList.txt which is the file
       that contains the 1,000 words (unsorted). If found, the file is opened
       and is returned to to the method that calls this method.
    */
    public File openWordList() // begin OpenWordList() method
    {
        try
        {
            File wordList = new File("wordlist.txt"); // Look for file wordList.txt
            
            if(!wordList.exists())
            {
                wordList.createNewFile(); // if not found, create the file
            }
            
            return wordList; // return the file
        }
        catch (IOException e)
        {
            return null; // catch the exception
        }
    } // end openWordList()
    
    /* Method findAnagram(String[], String) goes through the sorted array
       linearly and compares each element with the word that was entered by
       the user. If an anagram is found, the element that matched is stored
       to the global variable suggestedWord which will be shown in later on
       in the program.
    */
    public void findAnagram(String[] words, String word) // begin findAnagram(String[], String) method
    {
        char[] word1; // character array for word in sorted array element
        char[] word2; // character array for word entered by user.
        
        // For loop goes through sorted array checking each element for an anagram
        for(int i = 0; i < words.length; i++)
        {
            if(words[i].length() != word.length())
            {
                // do nothing if the lengths dont match.
            }
            else
            {
                word1 = words[i].toCharArray(); // make the string to a character array.
                word2 = word.toCharArray(); // make the string to a character array.
                
                word1 = charSort(word1); // call method charSort which sorts the characters.
                word2 = charSort(word2); // call method charSort which sorts the characters.
                
                // Once sorted, check if arrays are equal using built in Arrays.equals() mehtod
                if(Arrays.equals(word1, word2))
                {
                    suggestedWord = words[i]; // store the matched word from sorted array
                    matchFound = true; // state that a match was found.
                }
            }
        } // end for loop
    } // end findAnagram
    
    // Wrapper method for the recursiveBinarySearch method.
    public void recursiveBinarySearch(String[] words, String word)
    {
        findAnagram(words, word); // linear search of an anagram,
        recursiveBinarySearch(words, word, 0, words.length - 1); // Recursive search
    }
    
    /* method recursiveBinarySearch(String[], String, int, int) takes in the array
       the word that was entered by the user, the first index of array, and last index
       of the array. The method will perform a binary search recursively on each pass
       searching for a valid word that matches the word entered. If no match is found,
       then either a suggested word is displayed or no suggestion if no word can be
       suggested. ***adapted from the method found in the slides***
    */
    private void recursiveBinarySearch(String[] words, String word, int firstIndex, int lastIndex) //begin recursiveBinarySearch()
    {
        // no more elements to search
        if(firstIndex > lastIndex)
        {
            if(matchFound == true)  // a match was found by method findAnagram()
            {
                System.out.println("Suggested word: " + suggestedWord); // display suggested word
            }
            
            if(matchFound == false) // no match was found by method findAnagram()
            {
                System.out.println("No suggestion."); // output no suggestion
            }
            
            matchFound = false; // re-set matchFound to false.
        }
        else
        {
            int middleIndex = (firstIndex + lastIndex) / 2; // calculate middle
            int compareResult = word.compareTo(words[middleIndex]); // compare the words
            
            if(compareResult == 0) // when the word is found
            {
                System.out.println("Corect."); // Output correct
            }
            else if(compareResult < 0) // when the word is to the left of the middle
            {
                // Recursively call method recursiveBinarySearch with the left half of the original array being passed in.
                recursiveBinarySearch(words, word, firstIndex, middleIndex - 1);
            }
            else // when the word is to the right of the middle
            {
                // Recursively call method recursiveBinarySearch with the right half of the original array being passed in.
                recursiveBinarySearch(words, word, middleIndex + 1, lastIndex);
            }
        }
    } // end recursiveBinarySearch()
    
    /*  method StringSort() goes through the unsorted array and makes it a sorted array.
        This sorting is based on the insertionSort that we implemented in class which has
        been modified slightly to sort Strings instead. The global array used in the entire
        program is wordDictionary.words[[. It is at first unsorted but is then sorted when
        this method is called.
    */
    public void stringSort() // begin stringSort() method
    {
        int n = wordDictionary.words.length; // get length of array.
        
        for(int i = 1; i < n; i++)
        {
            String temp = wordDictionary.words[i];  // store current element as a temp
            int j = i - 1;
            
            // compare rest of elements with temp
            while((j > -1) && (wordDictionary.words[j].compareTo(temp)) > 0)
            {
                wordDictionary.words[j + 1] = wordDictionary.words[j]; // shift
                j--;
            }
            
            wordDictionary.words[j + 1] = temp; // add temp to the end.
        }
    } // end stringSort()
    
    /*  method charSort(char[]) takes in an array of characters, sorts them in
        the same way as stringSort() does above by following the insertionSort
        algorithm that we implemented in class but this one has been modified
        slightly to work with character arrays. This method returns a character
        array that has been sorted.
    */
    public char[] charSort(char[] array)
    {
        // same process as stringSort() see comments above in method stringSort()
        char[] inputArray = array;
        
        int n = inputArray.length;
        
        for(int i = 1; i < n; i++)
        {
            char temp = inputArray[i];
            int j = i - 1;
            
            while((j > -1) && (inputArray[j] > temp))
            {
                inputArray[j + 1] = inputArray[j];
                j--;
            }
            
            inputArray[j + 1] = temp;
        }
        
        return inputArray; // return a character array that is sorted
    }
    
    // Required global variables for this program
    private static final String[] words = new String[1000]; // array that is used, first unsorted until the sorting method is called.
    private boolean matchFound; // keep track of a suggested word being found
    private String suggestedWord; // stores the current suggested word.
}