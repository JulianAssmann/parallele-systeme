package com.parallel;

public class RoomManager {
    TreatmentRoom[] treatmentRooms;

    RoomManager(int numRooms) {
        this.treatmentRooms = new TreatmentRoom[numRooms];
        for (int i = 0; i < numRooms; i++) {
            this.treatmentRooms[i] = new TreatmentRoom();
        }
    }

    // Returns whether there is a room available or not
    private synchronized boolean isRoomAvailable() {
        for (TreatmentRoom room : treatmentRooms) {
            if (room.getIsAvailable()) {
                return true;
            }
        }
        return false;
    }

    // Books a room for the patient
    public synchronized void bookRoom(Patient patient) throws InterruptedException {
        // Wait until a room is available
        while (!isRoomAvailable()) {
            wait();
        }

        // Assign the patient to the first available room
        for (TreatmentRoom room : treatmentRooms) {
            if (room.getIsAvailable()) {
                room.assignPatient(patient);
                break;
            }
        }
    }

    // Clears the room of the patient
    public synchronized void leaveRoom(Patient patient) {
        for (TreatmentRoom room : treatmentRooms) {
            if (room.getPatient() == patient) {
                room.leave();
                Logger.log("Patient " + patient.getId() + " has left the room " + room.getId());
                break;
            }
        }

        // Notify waiting patients that a room is available
        notifyAll();
    }

    public boolean isEmpty() {
        for (TreatmentRoom room : treatmentRooms) {
            if (!room.getIsAvailable()) {
                return false;
            }
        }
        return true;
    }
}
