package com.parallel;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RoomManager {
    TreatmentRoom[] treatmentRooms;

    private final Lock treatmentRoomsLock = new ReentrantLock();
    private final Condition treatmentRoomFree = treatmentRoomsLock.newCondition();

    RoomManager(int numRooms) {
        this.treatmentRooms = new TreatmentRoom[numRooms];
        for (int i = 0; i < numRooms; i++) {
            this.treatmentRooms[i] = new TreatmentRoom();
        }
    }

    // Returns whether there is a room available or not
    private boolean isRoomAvailable() {
        try {
            treatmentRoomsLock.lock();
            for (TreatmentRoom room : treatmentRooms) {
                if (room.getIsAvailable()) {
                    return true;
                }
            }
        } finally {
            treatmentRoomsLock.unlock();
        }

        return false;
    }

    // Books a room for the patient
    public void bookRoom(Patient patient) throws InterruptedException {
        try {
            treatmentRoomsLock.lock();
            // Wait until a room is available
            while (!isRoomAvailable()) {
                treatmentRoomFree.await();
            }

            // Assign the patient to the first available room
            for (TreatmentRoom room : treatmentRooms) {
                if (room.getIsAvailable()) {
                    room.assignPatient(patient);
                    break;
                }
            }
        } finally {
            treatmentRoomsLock.unlock();
        }
    }

    // Clears the room of the patient
    public void leaveRoom(Patient patient) {
        try {
            treatmentRoomsLock.lock();
            // Clear the room of the patient
            for (TreatmentRoom room : treatmentRooms) {
                if (room.getPatient() == patient) {
                    room.leave();
                    break;
                }
            }
            treatmentRoomFree.signal();
        } finally {
            treatmentRoomsLock.unlock();
        }
    }

    public boolean isEmpty() {
        try {
            treatmentRoomsLock.lock();
            for (TreatmentRoom room : treatmentRooms) {
                if (!room.getIsAvailable()) {
                    return false;
                }
            }
        } finally {
            treatmentRoomsLock.unlock();
        }
        return true;
    }
}
