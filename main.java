import java.util.Scanner;

public class Main {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final IncidentManager manager =
            new IncidentManager();

    public static void main(String[] args) {

        System.out.println(
                "========================================"
        );

        System.out.println(
                "          CYBERSHIELD LITE"
        );

        System.out.println(
                "   Cyber Incident Response Simulator"
        );

        System.out.println(
                "========================================"
        );

        loadSampleIncidents();

        boolean running = true;

        while (running) {

            showMenu();

            int choice = readInt(
                    "Enter your choice: "
            );

            switch (choice) {

                case 1:
                    reportIncident();
                    break;

                case 2:
                    manager.displayAllIncidents();
                    break;

                case 3:
                    searchIncident();
                    break;

                case 4:
                    assignTeam();
                    break;

                case 5:
                    updateStatus();
                    break;

                case 6:
                    runSimulation();
                    break;

                case 7:
                    ReportGenerator.generate(manager);
                    break;

                case 8:
                    manager.displayTeams();
                    break;

                case 0:
                    running = false;
                    System.out.println(
                            "\nThank you for using CyberShield Lite!"
                    );
                    break;

                default:
                    System.out.println(
                            "\nInvalid choice. Try again."
                    );
            }
        }

        scanner.close();
    }

    private static void showMenu() {

        System.out.println("\n========== MAIN MENU ==========");

        System.out.println(
                "1. Report New Incident"
        );

        System.out.println(
                "2. View All Incidents"
        );

        System.out.println(
                "3. Search Incident"
        );

        System.out.println(
                "4. Assign Response Team"
        );

        System.out.println(
                "5. Update Incident Status"
        );

        System.out.println(
                "6. Run Cyber Attack Simulation"
        );

        System.out.println(
                "7. Generate Report"
        );

        System.out.println(
                "8. View Response Teams"
        );

        System.out.println(
                "0. Exit"
        );

        System.out.println(
                "=============================="
        );
    }

    private static void reportIncident() {

        System.out.println(
                "\n========== REPORT INCIDENT =========="
        );

        System.out.print("Incident Title: ");
        String title = scanner.nextLine();

        if (title.trim().isEmpty()) {
            System.out.println(
                    "Title cannot be empty."
            );
            return;
        }

        IncidentType type = chooseIncidentType();

        int users = readInt(
                "Affected Users: "
        );

        int systems = readInt(
                "Affected Systems: "
        );

        if (users < 0 || systems < 0) {
            System.out.println(
                    "Values cannot be negative."
            );
            return;
        }

        System.out.print(
                "Sensitive Data Involved? (yes/no): "
        );

        String answer =
                scanner.nextLine().trim().toLowerCase();

        boolean sensitive =
                answer.equals("yes");

        Incident incident =
                new Incident(
                        title,
                        type,
                        users,
                        systems,
                        sensitive
                );

        manager.addIncident(incident);
    }

    private static IncidentType chooseIncidentType() {

        System.out.println("\nSelect Incident Type:");

        IncidentType[] types =
                IncidentType.values();

        for (int i = 0; i < types.length; i++) {

            System.out.println(
                    (i + 1) + ". " + types[i]
            );
        }

        int choice = readInt(
                "Enter type: "
        );

        if (choice < 1 ||
                choice > types.length) {

            System.out.println(
                    "Invalid type. PHISHING selected."
            );

            return IncidentType.PHISHING;
        }

        return types[choice - 1];
    }

    private static void searchIncident() {

        int id = readInt(
                "\nEnter Incident ID: "
        );

        manager.searchIncident(id);
    }

    private static void assignTeam() {

        int id = readInt(
                "\nEnter Incident ID: "
        );

        manager.assignTeam(id);
    }

    private static void updateStatus() {

        int id = readInt(
                "\nEnter Incident ID: "
        );

        Incident incident =
                manager.findIncident(id);

        if (incident == null) {
            System.out.println(
                    "Incident not found."
            );
            return;
        }

        System.out.println(
                "Current Status: " +
                incident.getStatus()
        );

        System.out.println("\nSelect new status:");

        System.out.println(
                "1. INVESTIGATING"
        );

        System.out.println(
                "2. CONTAINED"
        );

        System.out.println(
                "3. RESOLVED"
        );

        int choice = readInt(
                "Enter choice: "
        );

        IncidentStatus status;

        switch (choice) {

            case 1:
                status = IncidentStatus.INVESTIGATING;
                break;

            case 2:
                status = IncidentStatus.CONTAINED;
                break;

            case 3:
                status = IncidentStatus.RESOLVED;
                break;

            default:
                System.out.println(
                        "Invalid status."
                );
                return;
        }

        manager.updateStatus(id, status);
    }

    private static void runSimulation() {

        System.out.println(
                "\n========== CYBER ATTACK SIMULATION =========="
        );

        Incident i1 = new Incident(
                "Simulated Phishing Attack",
                IncidentType.PHISHING,
                30,
                1,
                false
        );

        Incident i2 = new Incident(
                "Simulated Malware Attack",
                IncidentType.MALWARE,
                80,
                5,
                true
        );

        Incident i3 = new Incident(
                "Simulated Data Leak",
                IncidentType.DATA_LEAK,
                60,
                4,
                true
        );

        manager.addSimulationIncident(i1);
        manager.addSimulationIncident(i2);
        manager.addSimulationIncident(i3);

        Thread t1 =
                new Thread(
                        new IncidentProcessor(i1, manager)
                );

        Thread t2 =
                new Thread(
                        new IncidentProcessor(i2, manager)
                );

        Thread t3 =
                new Thread(
                        new IncidentProcessor(i3, manager)
                );

        t1.start();
        t2.start();
        t3.start();

        try {

            t1.join();
            t2.join();
            t3.join();

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.out.println(
                    "Simulation interrupted."
            );
        }

        System.out.println(
                "\n========== SIMULATION COMPLETE =========="
        );
    }

    private static int readInt(String message) {

        while (true) {

            try {

                System.out.print(message);

                int value =
                        Integer.parseInt(
                                scanner.nextLine()
                        );

                return value;

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }

    private static void loadSampleIncidents() {

        Incident sample1 =
                new Incident(
                        "Fake College Email",
                        IncidentType.PHISHING,
                        40,
                        2,
                        false
                );

        Incident sample2 =
                new Incident(
                        "Ransomware Infection",
                        IncidentType.MALWARE,
                        100,
                        6,
                        true
                );

        Incident sample3 =
                new Incident(
                        "Unauthorized Login",
                        IncidentType.UNAUTHORIZED_ACCESS,
                        10,
                        1,
                        false
                );

        manager.addSimulationIncident(sample1);
        manager.addSimulationIncident(sample2);
        manager.addSimulationIncident(sample3);

        manager.assignTeam(sample1.getId());
        manager.assignTeam(sample2.getId());
        manager.assignTeam(sample3.getId());

        System.out.println(
                "\nSample incidents loaded."
        );
    }
}
