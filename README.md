# VITyarthi_Project-3
# Exam Seating Arrangement Generator

A console-based Java mini project that generates exam seating arrangements for
students across multiple rooms, while trying to avoid seating students of the
same branch/class next to each other.

## Features

1. **Add Student** – Store Roll No, Name, and Branch.
2. **Add Room** – Define a room by Room No, number of rows, and number of columns.
3. **View All Students** – List all students currently added.
4. **View All Rooms** – List all rooms and their seating capacity.
5. **Generate Seating Arrangement** – Automatically allots students to seats,
   avoiding same-branch neighbours (left/right/up/down) wherever possible.
6. **Display Seating Charts** – Prints a grid view of every room's seating.
7. **Save Seating Arrangement to File** – Exports the full seating report as a `.txt` file.
8. **Load Students from File** – Bulk-import students from a CSV-style text file.
9. **Search Student Seat** – Look up which room/row/column a student is seated in.
10. **Show Statistics** – Total students, total rooms, total capacity, and branch-wise counts.

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

1. Students are grouped by branch.
2. A round-robin (interleaved) ordering is built so that consecutive students
   in the list belong to different branches as often as possible.
3. While filling each room row by row, the generator checks the **left** and
   **up** neighbour of every seat. If the next candidate would clash
   (same branch as a neighbour), it looks ahead in the list for a
   non-clashing student and swaps them in.
4. If no clash-free student is available, it falls back to the next student
   in line (a clash may occasionally be unavoidable when one branch heavily
   outnumbers the others).

## Requirements

- Java JDK 8 or above
- No external libraries — uses only `java.util` and `java.io`

## How to Compile & Run

```bash
javac ExamSeatingGenerator.java
java ExamSeatingGenerator
```

## Usage Walkthrough

1. Run the program.
2. Choose option `1` a few times to add students (Roll No, Name, Branch).
3. Choose option `2` to add one or more rooms (Room No, Rows, Columns).
4. Choose option `5` to generate the seating arrangement.
5. Choose option `6` to view the seating chart in the console.
6. Choose option `7` to save the arrangement as a text file (e.g. `seating.txt`).
7. Choose option `9` anytime to check where a specific student is seated.

### Bulk-loading students from a file (option 8)

Create a text file where each line follows the format:

```
rollNo,name,branch
101,Aman Sharma,CSE
102,Riya Verma,ECE
103,Karan Singh,ME
104,Sana Khan,CSE
```

Then choose option `8` and enter the file name/path when prompted.

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
