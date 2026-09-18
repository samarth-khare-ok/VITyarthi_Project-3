# VITyarthi_Project-3
# Exam Seating Arrangement Generator

A console-based Java mini project that generates exam seating arrangements for
students across multiple rooms, while trying to avoid seating students of the
The same branch or class appears next to one another.

## Features

1. To add a student, store their roll number, name, and branch.
2. **Add Room** – A room should be defined by giving its Room No, the number of rows, and the number of columns.
– View All Students: display a list of all students who have currently been added.
4. **View All Rooms** – Provide a list of all the rooms together with their seating capacity.
– Generate the seating arrangement by automatically assigning seats to the students,
   Wherever possible, avoid having neighbours on the same branch (to the left/right/up/down).
6. **Show the seating charts** – it prints out a grid view of the seating arrangement in each room.
7. **Save Seating Arrangement to File** – The complete seating report is exported as a .txt file.
– Import students from a file** using a CSV-style text file.
9. **Search for a student's seat** – Find out which room, row, and column the student is sitting in.
10. **Show Statistics** – the total number of students, the total number of rooms, the total capacity, and the figures broken down by branch.

## Project Structure

The entire project is contained in a single file for easy compilation:

```
ExamSeatingGenerator.java
├── Student            // Model class: rollNo, name, branch
├── Room                // Model class: roomNo, rows, cols, 2D seat grid
├── SeatingGenerator     // Core logic: interleaves branches & assigns seats
├── FileHandler         // Handles saving reports / loading student lists
└── ExamSeatingGenerator // Main class with menu-driven console interface
```

## How the Seating Logic Works

1. The students are divided according to their branch.
2. A round-robin (interleaved) order is arranged in such a way that the students come one after another
   Wherever possible, the items should be assigned to different branches.
3. The generator, while filling each row of rooms, checks the **left** and
   Every seat has a neighbour. If the following candidate were to clash
   (same branch as a neighbour), it looks ahead in the list for a
   The student who doesn't clash with him is then substituted in.
4. If there is no available student who does not cause a clash, the system resorts to the next student.
   in line (a clash may occasionally be unavoidable when one branch heavily
   outnumbers the others).

## Requirements

- Java JDK 8 or above
- No external libraries. Uses only `java.util` and `java.io`.

## How to Compile & Run

```bash
javac ExamSeatingGenerator.java
java ExamSeatingGenerator
```

## Usage Walkthrough

1. Run the program.
2. Press option `1` a number of times if you want to add students (Roll No, Name, Branch).
To add one or more rooms choose option 2.
4. To generate the seating arrangement select option 5.
To view the seating chart in the console please select option 6.
To save the arrangement as a text file choose option 7 (for example seating.txt).
7. If you want to see where a particular student is sitting, select option 9.

### Bulk-loading students from a file (option 8)

Create a text file where each line follows the format:

```
rollNo,name,branch
101,Aman Sharma,CSE
102,Riya Verma,ECE
103,Karan Singh,ME
104,Sana Khan,CSE
```

Instead, select number 8 and type in the file name or path when asked to.

## Sample Output (Seating Chart)

```
===== Room: R101 (2 x 3) =====
[101-CSE]         [102-ECE]         [103-ME]
[104-CSE]         [105-ECE]         [EMPTY]
```

## Sample Output (Saved Report File)

```
EXAM SEATING ARRANGEMENT REPORT
================================

Room: R101  (2 x 3)
--------------------------------
[101-CSE]	[102-ECE]	[103-ME]
[104-CSE]	[105-ECE]	[EMPTY]
```

## Possible Enhancements

- Assign invigilators per room
- Export seating charts as PDF
- Add a GUI (Java Swing/JavaFX) instead of console menu
- Support seating by exam subject/session, not just branch
- Randomize seat order for added fairness

## Author / Notes

This project is intended as an academic mini-project demonstrating:
- Object-Oriented Programming (classes, encapsulation)
- Collections (List, Map, Queue)
- File I/O
- Basic algorithmic logic for constraint-based arrangement
