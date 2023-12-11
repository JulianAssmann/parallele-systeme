package com.parallel;

import java.util.LinkedList;
import java.util.Queue;

public class Practice {
    private final Queue<Patient> waitingPatients = new LinkedList<>();
    private final Queue<Patient> patientsWaitingForDoctor = new LinkedList<>();

    private final MedicalAssistant[] medicalAssistants;
    private final RoomManager roomManager;

    private int numPatients = 0;

    private final Doctor doctor;

    public Practice(int numRooms, int numMedicalAssistants) {
        this.roomManager = new RoomManager(numRooms);

        this.medicalAssistants = new MedicalAssistant[numMedicalAssistants];
        for (int i = 0; i < numMedicalAssistants; i++) {
            this.medicalAssistants[i] = new MedicalAssistant(this);
        }

        this.doctor = new Doctor(this);

        // Start the threads
        for (MedicalAssistant medicalAssistant : medicalAssistants) {
            new Thread(medicalAssistant).start();
        }
        new Thread(doctor).start();
    }

    public synchronized void arrive(Patient patient) throws InterruptedException {
        numPatients++;
        waitingPatients.add(patient);
        notifyAll();
    }

    public synchronized Patient getNextPatientForExamination() throws InterruptedException {
        while (waitingPatients.isEmpty()) {
            wait();
        }
        return waitingPatients.poll();
    }

    public synchronized void waitForDoctor(Patient patient) throws InterruptedException {
        patientsWaitingForDoctor.add(patient);

        // Notify the doctor that there is a patient waiting for him
        notifyAll();
    }

    public synchronized Patient getNextPatientForTreatment() throws InterruptedException {
        while (patientsWaitingForDoctor.isEmpty()) {
            wait();
        }
        return patientsWaitingForDoctor.poll();
    }

    public synchronized void leave(Patient patient) {
        roomManager.leaveRoom(patient);
        numPatients--;
        Logger.log("Patient " + patient.getId() + " has left the practice");
    }

    public void getTreatmentRoom(Patient patient) throws InterruptedException {
        roomManager.bookRoom(patient);
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public boolean isEmpty() {
        return numPatients == 0;
    }
}
