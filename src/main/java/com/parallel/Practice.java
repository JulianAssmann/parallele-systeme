package com.parallel;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Practice {

    private final Queue<Patient> waitingPatients = new LinkedList<>();
    private final Lock waitingPatientsLock = new ReentrantLock();
    private final Condition areTherePatientsInWaitingRoom = waitingPatientsLock.newCondition();

    private final Queue<Patient> patientsWaitingForDoctor = new LinkedList<>();
    private final Lock patientsWaitingForDoctorLock = new ReentrantLock();
    private final Condition areTherePatientsWaitingForDoctor = patientsWaitingForDoctorLock.newCondition();

    private final MedicalAssistant[] medicalAssistants;
    private final RoomManager roomManager;

    private final ExecutorService practiceExecutorService;

    private int numPatients = 0;

    private final Doctor doctor;

    public Practice(int numRooms, int numMedicalAssistants) {
        this.roomManager = new RoomManager(numRooms);

        this.practiceExecutorService = Executors.newCachedThreadPool();

        this.medicalAssistants = new MedicalAssistant[numMedicalAssistants];
        for (int i = 0; i < numMedicalAssistants; i++) {
            this.medicalAssistants[i] = new MedicalAssistant(this);
            practiceExecutorService.submit(this.medicalAssistants[i]);
        }

        this.doctor = new Doctor(this);
        practiceExecutorService.submit(this.doctor);

        practiceExecutorService.shutdown();
    }

    public void arrive(Patient patient) throws InterruptedException {
        try {
            waitingPatientsLock.lock();
            waitingPatients.add(patient);
            numPatients++;
            areTherePatientsInWaitingRoom.signalAll();
        } finally {
            waitingPatientsLock.unlock();
        }
    }

    public Patient getNextPatientForExamination() throws InterruptedException {
        waitingPatientsLock.lock();
        try {
            while (waitingPatients.isEmpty()) {
                areTherePatientsInWaitingRoom.await();
            }
            return waitingPatients.poll();
        } finally {
            waitingPatientsLock.unlock();
        }
    }

    public void waitForDoctor(Patient patient) throws InterruptedException {
        try {
            patientsWaitingForDoctorLock.lock();
            patientsWaitingForDoctor.add(patient);
            areTherePatientsWaitingForDoctor.signal();
        } finally {
            patientsWaitingForDoctorLock.unlock();
        }
    }

    public Patient getNextPatientForTreatment() throws InterruptedException {
        patientsWaitingForDoctorLock.lock();
        try {
            while (patientsWaitingForDoctor.isEmpty()) {
                areTherePatientsWaitingForDoctor.await();
            }
            return patientsWaitingForDoctor.poll();
        } finally {
            patientsWaitingForDoctorLock.unlock();
        }
    }

    public void leave(Patient patient) {
        roomManager.leaveRoom(patient);

        try {
            patientsWaitingForDoctorLock.lock();
            numPatients--;
        } finally {
            patientsWaitingForDoctorLock.unlock();
        }

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
