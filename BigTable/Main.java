/********************************* Student Information ********************************* 
 * Name: Himanshu Sehgal
 * Student Number: 8688440
 * Course: CSI 2110
 * Programming Assignment P2 - Big Tables
****************************************************************************************/
import java.util.*;

public class Main{

    // The following is the declaration of all the global variables used throughout the 
    // implementation of BigTable Backtracking Memoization. 

    static int bestK = -1;
    static int n;
    static int currX[];
    static int bestX[];
    static int L;
    static int lengthArr[];
    static int totalCarLength;
    static int newS;
    static boolean visited[][];
    static int endSpace;
    static int partialCarLength;
    static int spaceRight;
    

    public static void main(String[] args) {

        // Decalration of the fileElements as an ArrayList, this stores the items obtained from
        // the given text file.

        ArrayList<String> fileElements = new ArrayList<String>();

        // Scanner implementation to iterate over each line of the inputted text file to append
        // each element into the arraylist. The String lineOfInput stores the value of each line
        // as the iteration is processed.
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String lineOfInput = scanner.nextLine();
            fileElements.add(lineOfInput);
            //System.out.println(lineOfInput);
        }

        // Obtain the number of ferries in the given text file.
        int numberOfFerries = Integer.parseInt(fileElements.get(0));
        int numFerry = 0;
        ArrayList<ArrayList<Integer>> eachFerry = new ArrayList<>();
        eachFerry.add(new ArrayList<Integer>());

        // The following takes the array list derived above from the input text file and appends 
        // each ferry's data into an arraylist which is then fed into another arraylist. This 
        // is an ArrayList of type ArrayList; it also removes any blank spaces and 0 from the input.
        for( int i = 2; i < fileElements.size(); i++){
            if(fileElements.get(i) != null && !(fileElements.get(i).isEmpty())){
                int carSize = Integer.parseInt(fileElements.get(i));
                if (carSize != 0 ){
                    eachFerry.get(numFerry).add(carSize);
                }

                else{
                    if(numFerry < numberOfFerries -1){
                        numFerry += 1;
                        eachFerry.add(new ArrayList<Integer>());
                    }
                }
            }
        }

        //Testing prints
        // System.out.println();
        //System.out.println(eachFerry);
        // System.out.println();
        
        endSpace = 0;

        //The following code segment iterates over the number of ferries in the input text file.
        for(int i = 0; i < eachFerry.size(); i++){

            // Since each input of can have varies values for the ship's configuration, the following
            // segments re-initializes the values based on the ferry under observation.
            //lengthOfShip = ((eachFerry.get(i).get(0))*100);
            L = ((eachFerry.get(i).get(0))*100);    //convert 50m to cm
            n = eachFerry.get(i).size() - 1;
            lengthArr = new int[n + 1];   //Initialization of the length array  
            ArrayList<String> finalResults = new ArrayList<String>();   // Used to generate the output.txt file

            //Populates the length array from the elements in the eachFerry array list
            int j = 0; 
            while(j < (eachFerry.get(i).size() - 1)){
                lengthArr[j] = eachFerry.get(i).get(j+1);
                j++;
            }

            // Declaration for a boolean big table array, capable of being large enough to store values of the nodes 
            // visited is done globaly 
            //Used to fill the entire nxm matrix with an inital value
            visited = new boolean[n + 1][L + 1];

            for(boolean[] row: visited){
                Arrays.fill(row,false);
            }

            // Once the length array is generated, the summation of the car lengths is implemented below.
            totalCarLength = 0;
            for(int k = 0; k < lengthArr.length; k++){
                totalCarLength = totalCarLength + lengthArr[k];
                
            }

            bestK = -1;
            currX = new int[n];
            bestX = new int[n];

            //Initialize the values to be -1 so they are not confused with 0,1 for left or right loading
            Arrays.fill(currX,-1);
            Arrays.fill(bestX,-1);

            // Call to the recursive back track method
            int[] resultsArr = BacktrackSolve(0,L);  

            //Prints for testing
            //System.out.println(Arrays.toString(resultsArr));

            // Once the back track method provides the final result for the bestX array in terms of zeros
            // and ones, the following iterates over the result to classify each result as either Port or Starboard.
            
            for(int s = 0; s < resultsArr.length; s++){
                if(resultsArr[s] == 1){
                     finalResults.add("port");
                }
                else if(resultsArr[s] == 0){
                    finalResults.add("starboard");
                }
            }
            
            // As required by the sample output file, the first segment is the length of the bestX array which
            // then moves forward to printing the results of each car row by row, and a blank line to seperate 
            // each individual ferry.

            System.out.println(finalResults.size());
            for(int a = 0; a < finalResults.size(); a++){
                System.out.println(finalResults.get(a));
            }  
            //To make sure an extra space isn't printed
            endSpace++;
            if (!(endSpace == eachFerry.size())){
                System.out.println("");
            }
            
            // Prints for test
            // for(int s = 0; s < resultsArr.length; s++){
            //     bestXArrayList.add(resultsArr[s]);
            // }


        }
}

// The following is the implementation of the recursive backtrack method, its implementation is based on the
// pseudocode provided in the assignment documents. 
 public static int[] BacktrackSolve(int currK, int currS){   
    //System.out.println(Arrays.toString(currX));
    //This is to keep track of the total car length by each iteration
    
    partialCarLength = 0;
    //Populates after each recurrsive call
    for(int i = 0; i < currK; i++){
        partialCarLength = partialCarLength + lengthArr[i];
    }

    //In order to validate our right side, we must determine how much space we have on the right side
    spaceRight = ((2*L) - partialCarLength - currS);

    int spaceOnLeft = currS - lengthArr[currK];
    int spaceOnRight = spaceRight - lengthArr[currK];
     
     if (currK > bestK){
         bestK = currK;

         for(int i = 0; i < bestX.length; i++){
            bestX[i] = currX[i]; 
         }
        //  System.out.println("a");   
     }
     
     if(currK < n){
         //System.out.println("b");    

        // System.out.println("CurrS: " + currS);
        //  System.out.println("");
        //  System.out.println("left side: " + (currS - lengthArr[currK]));
        
        
        if((spaceOnLeft >= 0) && (visited[currK + 1][currS - lengthArr[currK]] == false)){ 
             //System.out.println((bigTable[currK + 1][currS - lengthArr[currK]] == false)); 
            currX[currK] = 1;
        
            newS = currS - lengthArr[currK];

            // Prints for testing/debugging
            // System.out.println(newS);

            // System.out.println(Arrays.toString(currX));
            // System.out.println(currS);
            // System.out.println(currX[currK]);

            // System.out.println("c"); 
               
            BacktrackSolve(currK+1, newS);
            // System.out.println("d");
            visited[currK+1][newS] = true;

            // Prints for testing/debugging
            // System.out.println(newS);
            // System.out.println(Arrays.toString(currX));
            // System.out.println(currX[currK]);
               
         }
         
        //  System.out.println("");
        //  System.out.println("spaceRight: " + spaceRight);
        //  System.out.println("");
        //  System.out.println("space on right: " + (spaceRight - lengthArr[currK]));
        if((spaceOnRight >= 0) && (visited[currK + 1][currS] == false)){
            //System.out.println((bigTable[currK + 1][currS] == false));
            currX[currK] = 0;
            // Prints for testing/debugging
            // System.out.println("e");
            
            // System.out.println(Arrays.toString(currX));
            // System.out.println(currS);
            // System.out.println(currX[currK]);

            BacktrackSolve(currK+1, currS);
            // System.out.println("f");
            visited[currK+1][currS] = true;

            // Prints for testing/debugging
            // System.out.println(Arrays.toString(currX));
            // System.out.println(currX[currK]);
            
         }
     }
     
     
     return bestX;
    }
    
}