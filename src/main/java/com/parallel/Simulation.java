package com.parallel;

enum Weekday {
    Monday, Tuesday, Wednesday
}

public class Simulation {
    public static void main(String[] args) throws InterruptedException {
        int numRooms = 4;
        int numMedicalAssistants = 3;
        Practice practice = new Practice(numRooms, numMedicalAssistants);

        // For every weekday run the simulation
        for (Weekday weekday : Weekday.values()) {
            Logger.reset();
            Logger.log("It's " + weekday + " and the practice is open for business.");

            // The patients arrive in eight 15 minute intervals, totaling 2 hours
            for (int i = 0; i < 8; i++) {

                int numPatients = 0;

                if (weekday == Weekday.Monday) {
                    numPatients = 2 + (int) (Math.random() * 3); // 2 to 4 patients randomly
                } else if (weekday == Weekday.Tuesday) {
                    numPatients = 3 + (int) (Math.random() * 5); // 1 to 3 patients randomly
                } else if (weekday == Weekday.Wednesday) {
                    numPatients = 1 + (int) (Math.random() * 4); // 3 to 6 patients randomly
                }

                for (int j = 0; j < numPatients; j++) {
                    Patient patient = new Patient(practice);
                    practice.arrive(patient);
                    Logger.log("Patient " + patient.getId() + " has arrived at the practice.");
                }

                // Wait 15 minutes
                Thread.sleep(15 * 1000);
            }

            // Wait until all patients have left the practice
            while (!practice.isEmpty()) {
                Thread.sleep(1000);
            }

            Logger.log("All patients have left the practice for " + weekday + " and the practice is now closed.");

            System.out
                    .println(
                            "--------------------------------------------------------------------------------------");
            for (int i = 0; i < 3; i++) {
                System.out.println();
            }
        }

    }
}