/*
 * ============================================================
 *  EXAM SEATING ARRANGEMENT GENERATOR
 *  A Java mini project (console based)
 * ------------------------------------------------------------
 *  Features:
 *   1. Add / Remove students (Roll No, Name, Branch/Class)
 *   2. Add / Remove exam rooms (Room No, Rows, Columns)
 *   3. Generate seating arrangement such that students from the
 *      SAME branch are not seated next to each other
 *      (left/right/front/back) as far as possible.
 *   4. Display room-wise seating chart
 *   5. Save seating arrangement to a text file
 *   6. Load student list from a text file
 *   7. Search a student's allotted seat
 *   8. View overall statistics
 * ============================================================
 */

import java.io.*;
import java.util.*;

/* ---------------------- Student Class ---------------------- */
class Student {
    private String rollNo;
    private String name;
    private String branch;

    public Student(String rollNo, String name, String branch) {
        this.rollNo = rollNo;
        this.name = name;
        this.branch = branch;
    }

    public String getRollNo() { return rollNo; }
    public String getName() { return name; }
    public String getBranch() { return branch; }

    @Override
    public String toString() {
        return rollNo + " | " + name + " | " + branch;
    }
}

/* ---------------------- Room Class ---------------------- */
class Room {
    private String roomNo;
    private int rows;
    private int cols;
    private Student[][] seats;

    public Room(String roomNo, int rows, int cols) {
        this.roomNo = roomNo;
        this.rows = rows;
        this.cols = cols;
        this.seats = new Student[rows][cols];
    }

    public String getRoomNo() { return roomNo; }
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public int getCapacity() { return rows * cols; }
    public Student[][] getSeats() { return seats; }

    public void placeStudent(int r, int c, Student s) {
        seats[r][c] = s;
    }

    public boolean isFull() {
        for (Student[] row : seats)
            for (Student s : row)
                if (s == null) return false;
        return true;
    }

    public void printChart() {
        System.out.println("\n===== Room: " + roomNo + " (" + rows + " x " + cols + ") =====");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Student s = seats[i][j];
                if (s == null) {
                    System.out.printf("%-18s", "[ EMPTY ]");
                } else {
                    System.out.printf("%-18s", "[" + s.getRollNo() + "-" + s.getBranch() + "]");
                }
            }
            System.out.println();
        }
    }
}

/* ---------------------- Seating Generator ---------------------- */
class SeatingGenerator {
    private List<Room> rooms;

    public SeatingGenerator(List<Room> rooms) {
        this.rooms = rooms;
    }

    /*
     * Generates seating such that adjacent seats (left, right, up, down)
     * do not belong to the same branch, wherever possible.
     * Strategy: Group students branch-wise, then interleave students
     * from different branches while filling seats row by row.
     */
    public void generateSeating(List<Student> studentList) {
        // Group students by branch
        Map<String, Queue<Student>> branchMap = new LinkedHashMap<>();
        for (Student s : studentList) {
            branchMap.computeIfAbsent(s.getBranch(), k -> new LinkedList<>()).add(s);
        }

        // Create a round-robin ordering of students across branches
        List<Student> interleaved = new ArrayList<>();
        boolean added;
        do {
            added = false;
            for (Queue<Student> q : branchMap.values()) {
                if (!q.isEmpty()) {
                    interleaved.add(q.poll());
                    added = true;
                }
            }
        } while (added);

        int index = 0;
        int total = interleaved.size();

        for (Room room : rooms) {
            for (int i = 0; i < room.getRows(); i++) {
                for (int j = 0; j < room.getCols(); j++) {
                    if (index >= total) return; // no more students
                    Student candidate = interleaved.get(index);

                    // Try to avoid same-branch neighbours by looking ahead
                    if (clashesWithNeighbour(room, i, j, candidate)) {
                        int swapIndex = findNonClashingCandidate(room, i, j, interleaved, index);
                        if (swapIndex != -1) {
                            Collections.swap(interleaved, index, swapIndex);
                            candidate = interleaved.get(index);
                        }
                    }
                    room.placeStudent(i, j, candidate);
                    index++;
                }
            }
        }
    }

    private boolean clashesWithNeighbour(Room room, int r, int c, Student candidate) {
        Student left = (c > 0) ? room.getSeats()[r][c - 1] : null;
        Student up = (r > 0) ? room.getSeats()[r - 1][c] : null;

        if (left != null && left.getBranch().equals(candidate.getBranch())) return true;
        if (up != null && up.getBranch().equals(candidate.getBranch())) return true;
        return false;
    }

    private int findNonClashingCandidate(Room room, int r, int c, List<Student> pool, int fromIndex) {
        Student left = (c > 0) ? room.getSeats()[r][c - 1] : null;
        Student up = (r > 0) ? room.getSeats()[r - 1][c] : null;

        for (int k = fromIndex + 1; k < pool.size(); k++) {
            Student cand = pool.get(k);
            boolean clashLeft = (left != null && left.getBranch().equals(cand.getBranch()));
            boolean clashUp = (up != null && up.getBranch().equals(cand.getBranch()));
            if (!clashLeft && !clashUp) return k;
        }
        return -1; // no better candidate found
    }
}

/* ---------------------- File Handler ---------------------- */
class FileHandler {

    // Save seating chart of all rooms to a text file
    public static void saveSeatingToFile(List<Room> rooms, String fileName) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            pw.println("EXAM SEATING ARRANGEMENT REPORT");
            pw.println("================================\n");
            for (Room room : rooms) {
                pw.println("Room: " + room.getRoomNo() + "  (" + room.getRows() + " x " + room.getCols() + ")");
                pw.println("--------------------------------");
                Student[][] seats = room.getSeats();
                for (int i = 0; i < room.getRows(); i++) {
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < room.getCols(); j++) {
                        Student s = seats[i][j];
                        sb.append(s == null ? "[EMPTY]" : "[" + s.getRollNo() + "-" + s.getBranch() + "]");
                        sb.append("\t");
                    }
                    pw.println(sb.toString());
                }
                pw.println();
            }
            System.out.println("Seating arrangement saved to file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error while saving file: " + e.getMessage());
        }
    }

    // Load student list from a CSV-like text file: rollNo,name,branch
    public static List<Student> loadStudentsFromFile(String fileName) {
        List<Student> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    list.add(new Student(parts[0].trim(), parts[1].trim(), parts[2].trim()));
                }
            }
            System.out.println("Loaded " + list.size() + " students from " + fileName);
        } catch (IOException e) {
            System.out.println("Error while reading file: " + e.getMessage());
        }
        return list;
    }
}

/* ---------------------- Main Application ---------------------- */
public class ExamSeatingGenerator {

    private static List<Student> students = new ArrayList<>();
    private static List<Room> rooms = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            printMenu();
            choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1: addStudent(); break;
                case 2: addRoom(); break;
                case 3: viewStudents(); break;
                case 4: viewRooms(); break;
                case 5: generateSeatingArrangement(); break;
                case 6: displaySeatingCharts(); break;
                case 7: saveArrangementToFile(); break;
                case 8: loadStudentsFromFile(); break;
                case 9: searchStudentSeat(); break;
                case 10: showStatistics(); break;
                case 0: System.out.println("Exiting... Thank you!"); break;
                default: System.out.println("Invalid choice, try again.");
            }
        } while (choice != 0);
        sc.close();
    }

    private static void printMenu() {
        System.out.println("\n================ EXAM SEATING ARRANGEMENT GENERATOR ================");
        System.out.println("1.  Add Student");
        System.out.println("2.  Add Room");
        System.out.println("3.  View All Students");
        System.out.println("4.  View All Rooms");
        System.out.println("5.  Generate Seating Arrangement");
        System.out.println("6.  Display Seating Charts");
        System.out.println("7.  Save Seating Arrangement to File");
        System.out.println("8.  Load Students from File");
        System.out.println("9.  Search Student Seat");
        System.out.println("10. Show Statistics");
        System.out.println("0.  Exit");
        System.out.println("======================================================================");
    }

    private static void addStudent() {
        System.out.print("Enter Roll No: ");
        String roll = sc.nextLine().trim();
        System.out.print("Enter Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Enter Branch: ");
        String branch = sc.nextLine().trim();
        students.add(new Student(roll, name, branch));
        System.out.println("Student added successfully!");
    }

    private static void addRoom() {
        System.out.print("Enter Room No: ");
        String roomNo = sc.nextLine().trim();
        int r = readInt("Enter number of rows: ");
        int c = readInt("Enter number of columns: ");
        rooms.add(new Room(roomNo, r, c));
        System.out.println("Room added successfully! Capacity = " + (r * c));
    }

    private static void viewStudents() {
        if (students.isEmpty()) {
            System.out.println("No students added yet.");
            return;
        }
        System.out.println("\nRollNo\t| Name\t\t| Branch");
        System.out.println("--------------------------------------");
        for (Student s : students) {
            System.out.println(s.getRollNo() + "\t| " + s.getName() + "\t| " + s.getBranch());
        }
    }

    private static void viewRooms() {
        if (rooms.isEmpty()) {
            System.out.println("No rooms added yet.");
            return;
        }
        System.out.println("\nRoomNo\t| Rows x Cols\t| Capacity");
        System.out.println("--------------------------------------");
        for (Room r : rooms) {
            System.out.println(r.getRoomNo() + "\t| " + r.getRows() + " x " + r.getCols() + "\t\t| " + r.getCapacity());
        }
    }

    private static void generateSeatingArrangement() {
        if (students.isEmpty() || rooms.isEmpty()) {
            System.out.println("Please add students and rooms before generating seating.");
            return;
        }
        int totalCapacity = 0;
        for (Room r : rooms) totalCapacity += r.getCapacity();

        if (totalCapacity < students.size()) {
            System.out.println("Warning: Total room capacity (" + totalCapacity +
                    ") is less than number of students (" + students.size() + ").");
            System.out.println("Only the first " + totalCapacity + " students will be seated.");
        }

        SeatingGenerator generator = new SeatingGenerator(rooms);
        generator.generateSeating(students);
        System.out.println("Seating arrangement generated successfully!");
    }

    private static void displaySeatingCharts() {
        if (rooms.isEmpty()) {
            System.out.println("No rooms available.");
            return;
        }
        for (Room r : rooms) {
            r.printChart();
        }
    }

    private static void saveArrangementToFile() {
        if (rooms.isEmpty()) {
            System.out.println("No rooms to save.");
            return;
        }
        System.out.print("Enter file name to save (e.g., seating.txt): ");
        String fileName = sc.nextLine().trim();
        FileHandler.saveSeatingToFile(rooms, fileName);
    }

    private static void loadStudentsFromFile() {
        System.out.print("Enter file name to load (format: rollNo,name,branch per line): ");
        String fileName = sc.nextLine().trim();
        List<Student> loaded = FileHandler.loadStudentsFromFile(fileName);
        students.addAll(loaded);
    }

    private static void searchStudentSeat() {
        System.out.print("Enter Roll No to search: ");
        String roll = sc.nextLine().trim();
        boolean found = false;
        for (Room room : rooms) {
            Student[][] seats = room.getSeats();
            for (int i = 0; i < room.getRows(); i++) {
                for (int j = 0; j < room.getCols(); j++) {
                    Student s = seats[i][j];
                    if (s != null && s.getRollNo().equalsIgnoreCase(roll)) {
                        System.out.println("Found! " + s.getName() + " (" + s.getBranch() + ") is seated in Room "
                                + room.getRoomNo() + " at Row " + (i + 1) + ", Column " + (j + 1));
                        found = true;
                    }
                }
            }
        }
        if (!found) System.out.println("Student with Roll No " + roll + " not found in any seating chart.");
    }

    private static void showStatistics() {
        Map<String, Integer> branchCount = new TreeMap<>();
        for (Student s : students) {
            branchCount.put(s.getBranch(), branchCount.getOrDefault(s.getBranch(), 0) + 1);
        }
        int totalCapacity = 0;
        for (Room r : rooms) totalCapacity += r.getCapacity();

        System.out.println("\n---------------- STATISTICS ----------------");
        System.out.println("Total Students   : " + students.size());
        System.out.println("Total Rooms      : " + rooms.size());
        System.out.println("Total Capacity   : " + totalCapacity);
        System.out.println("Branch-wise Count:");
        for (Map.Entry<String, Integer> entry : branchCount.entrySet()) {
            System.out.println("   " + entry.getKey() + " -> " + entry.getValue());
        }
        System.out.println("---------------------------------------------");
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            System.out.print("Invalid input. Enter a number: ");
            sc.next();
        }
        int val = sc.nextInt();
        sc.nextLine(); // consume newline
        return val;
    }
}
