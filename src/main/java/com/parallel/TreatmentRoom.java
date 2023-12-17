package com.parallel;

/// A treatment room that can be used by a medical assistant
/// to perform a pre-examination or a treatment.
public class TreatmentRoom {

    /// Whether the treatment room is available or not.
    private volatile boolean isAvailable = true;

    /// The patient that is currently in the treatment room.
    private Patient patient;

    private static int counter = 1;
    private final int id;

    public TreatmentRoom() {
        this.id = counter++;
    }

    /// Use the treatment room.
    public void assignPatient(Patient patient) {
        this.isAvailable = false;
        this.patient = patient;

        Logger.log("Patient " + patient.getId() + " has entered the room " + this.id);
    }

    /// Leave the treatment room.
    public void leave() {
        this.isAvailable = true;
        this.patient = null;
    }

    /// Returns whether the treatment room is available or not.
    public boolean getIsAvailable() {
        return this.isAvailable;
    }

    /// Returns the id of the treatment room.
    public int getId() {
        return this.id;
    }

    /// Returns the patient that is currently in the treatment room.
    public Patient getPatient() {
        return this.patient;
    }
}
