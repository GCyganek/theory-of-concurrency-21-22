Concurrent implementation of Gaussian Elimination with Backward Substitution (maven)
===========

Running the application
-----------

Put the input file into the src/main/resources directory. Run the application with the name of the input file
provided. Output file named "solved_output.txt" will be created in the main project directory

Input and output file format
-----------
size - integer      
matrix - double     
RHS - double        

example input:  
3   
2.0 1.0 3.0     
4.0 3.0 8.0     
6.0 5.0 16.0    
6.0 15.0 27.0   

example output:     
3       
1.0 0.0 0.0     
0.0 1.0 0.0     
0.0 0.0 1.0     
1.0 1.0 1.0         

