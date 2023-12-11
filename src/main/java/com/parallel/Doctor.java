package com.parallel;

public class Doctor implements Runnable {
    private final Practice practice;

    public Doctor(Practice practice) {
        this.practice = practice;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Get the next patient for treatment
                Patient patient = practice.getNextPatientForTreatment();

                // Treat the patient
                Logger.log("Patient " + patient.getId() + " is being treated by the doctor for "
                        + patient.getTreatmentTime() + " minutes.");
                Thread.sleep(patient.getTreatmentTime() * 1000);
                Logger.log("Doctor has finished treating patient " + patient.getId() + ".");

                // Tell the patient to leave
                patient.tellToLeave();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
