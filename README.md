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
