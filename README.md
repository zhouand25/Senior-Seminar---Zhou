# Senior-Seminar---Zhou
Senior Seminar

ROUGH SUMMARIES

1/16/24
Approach: 
(Simplified)
-Rank classes based on popularity (Weighted average, 10 points for 1, 5 points for 2, 3 points for 3, 2 points for 4, 1 point for 5)
-Calculate number of duplicate classes (Such that every slot is filled)
-Calculate popularity for the leftover classes
-Arrange so that popular classes are arranged with relatively less popular classes in the same time slot to avoid conflict
-(REMEMBER the duplicate session can not occur at the same time as the original session since there is only one instructor)
-Then, use the students to put into each class
-Delete any uneeded/superflous classes

[POSSIBLE OTHER APPROACH: Separate into main parts, minor parts]


1/18/24
-Github and git had problems with the imac so no github upload and fileio input not fully finished
-Evaluated approach more and noticed that 5 classes for 5 time slots so genearlly, they should all be able to be fufilled
-So new point popularity ranking is (5 points for 1, 4 points for 2, 3 points for 3, 2 points for 4, 1 point for 5)
-(Not counting collisions of course)
-Started with file.io and cleaning up data slightly

1/22/24
-Had some rough problems with vscode (however fixed, code runnable but commits need to be manual)
-Separate seminar class created from tester class (fileio/constructor built out)
-Creating data structures to store the data in a readable way
-Further cleaning data

1/24/24
-Not full work time since there was a POP
-However, I did lots of debugging for file IO and planning for the rest of the schedule
-The popularity list should naturally include the most popular but given oveflow at all, duplicate classes will be taken for all
-Then after the popularity list is generated, the duplicates will naturaly be ranked (in case of overflow) with the rest of the clases
-Then take the top (25) classes (including duplicates) and then arrange them in a way which would avoid conflict (namely popular with unpopular)
-Then after this process, arrange and place students (in the case a student can not be arranged place them randomly)

1/26/24
-Fixed the error within the fileio
-Cleaned up the data significantly (deleted the course names from the spreadsheet because they were redundant due to the fact that the map of ints to coursenames was already 
established and that the int course ids are already in the choice matrix)
-Started the popularity ranking function (Created two array lists to map the course ids and the popularity index used to reference one another)
-Created an iterator to run through choice matrix and calculate a value with the established weighting scale
-At current progress, might need some time outside of class to fully finish

2/3
-Finished popularRank Function, iterates through the choices matrix [a two D array of size 5 arrays storing the top 5 choices of each student]
-The function iterates through the function adding 5 for first vote, 4 for second vote, 3 for third vote and so on and adds them to a coursevote attribute of each course which measures its popularity
-This popularity index is eventually used within the schedule creation as a heuristic to minimize collisions
-Also the course duplication procedure is also implemented (within this function), the courses which have a great enough popularityIndex are copied and put into the course ArrayList as well

-The second phase after the popular Rank function is the sorting algorithm which was also implemented
-The sorting algorithm sorts the popularityIndex attribute of the various courses putting the most popular courses first and the less popular courses later in teh array
-Created sorted algorithm to sort rank of popularity
-Created algorithm and function to place the classes accordingly most popular with less popular

-Also implemented the schedule creation procedure with the given last couple of functions
-Puts the most popular courses with the least popular courses (uses the popularity sorted array) it first places the most popular courses and then goes to the back of the array List and places those courses on the same time slot/row

-Began implementation for the student placement function, basically it doesn't go for the easy solution of placing one student's all 5 choices and moving on to the next student
-It works by placing everyone's first choice and if a person's first choice fails, it picks their next best available option and puts them in a priority array list for them to get "priority" the next iteration in teh second selection and so on.

-Created classSchedule 2d array data structure to store courseIds to represent the schedue with each cell representing a course in a specific class and time block

2/4 
-Finished implementing priority list (pretty much working)
-Implemented auxillary function called availability that works in conjunction with the placement function
-Availability function checks if that course in that time slot is "AVAILABLE" to the user, checking one if the course's enrollemnt number is <=16, and two makes sure that the person has an open unoccupied time slot to accomodate the course (returns a true or false boolean value)
-LOTS OF OVERALL DEBUGGING

-Also, found small problem in the synthesis function (where two of the same course, original and duplciate occur in the same time slot)
-So, created a changeUp correction function where if it finds a duplicate with the findDup function, it attempts to exchange the duplicate class with one of the neighboring time bells to achieve a situation where both time slots are free of duplicate classes (as a teacher cannot teach in two classrooms at once)

-More debugging with many printing functions to show the internal processes + small tweaks to debug and clean up the code
-Helped fix the student enrollment variables, measuring number of students in a session

-When doing that, decided to implement a mini-optimization in order to make things more convenient for teachers and to balance class sizes
-For example, when a person has a option of being availble for both the main class and the duplicate class, the individual will pick teh class with lower class size
-Balancing class sizes is very useful and is an overall optimization to the program because it not only allows the teachers to not have class sizes with 3 in one session and 16 in another (being inconvenient and strange), it also keeps more choices open and unfilled for individuals
who are not available for both to have their option still open, ultimately improving the selection achievement rate

-Implemented the wildcard function. Those who either have run out of choices (where one of them cannot be fufilled) or have some other collision or circumstance will have a blank space in their schedule which is not filled in (also applies to those who did not fill out the form),
Therefore, they will be placed in the wildcard which will place them in a random class which is available to them in one of their open time slots.
-Trying to implement and get minimum optization of class enrollemnt balance by placing the individual in a class with low number of participants

2/5 
-Started to fix a pretty big flaw in my code and scheduling
-Originally, some of the classes were not popular enough leading to an overall number of sessions to be less than 25 (after duplication), so when creating a classSchedule, there would be some blank spots or free bells within the schedule, leading to an overall somewhat unoptimal set-up
-So, I had to reconfigure a good portion of my program running a duplication-check algorithm to duplicate the next most popular classes that already have not been duplicated to fully fill in the schedule

-Pretty much fully completed the course enrollment balance procedure
-Created a scanner output panel that allows the user to input things via java.util.Scanner and create printed output based on roster information or individual information
-However, currently the scanner device does not quite work and roster aggregation by session has not been completed (nor individual search)

2/10
-Cleaned up a lot of the code, deleted extra print functions and debugging tools
-Finished scanner code and device (deleted the closing part of the scanner, to allow scanner to be continually used)

-Finished implementation of user input and out 
-Created finish and gather functions
-Gather function gathers all of the individuals on the roster based on session ID, it loops through the actualSchedule which stores all of the courseIDs per time slot and adds them
to the data structure that I have created called allname, which is a length 25 array of arraylists of strings
-The Finish function is the actual printer which given the arrangements form gather, print out everything in a neat and organized form

-For gather, there is a procedure which converts rows and columns to index on the arrayList 5*row + col is the index on the larger array which will store an arraylist of strings of names

-Also, implemented individual searching, given an inputted name, the algorithm will search through the person array and find their corresponding index to then use it in the actualSchedule 2d matrix (with each of its elements holding a 5 element array 
with index 0 being time slot 1, index 1 being time slot 2 and etc. In each cell, will hold the courseId which indicates what course it is) (Taking advantage of the fact there can not be duplicates within the same time slot, so column can be reverse-calculated/searched)
-After the 2d matrix actual schedule is accessed, the courseID is pulled, crossed with the course array list to grab its full name, and then printed

-Lastly updated the tester class, with that main class acting as a "headquarters" and triggering the functions of seminar from that central station

2/11
-Rough clean up of code, deleting last print debugging statements
