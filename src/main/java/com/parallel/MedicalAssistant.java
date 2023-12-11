package com.parallel;

class MedicalAssistant implements Runnable {

    private final Practice practice;
    private static int counter = 1;
    private final int id;

    public MedicalAssistant(Practice practice) {
        this.practice = practice;
        this.id = counter++;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Patient patient = practice.getNextPatientForExamination();

                // Get a treatment room
                practice.getTreatmentRoom(patient);

                // Do the pre-examination
                Logger.log("Patient " + patient.getId() + " is being pre-examined by medical assistant "
                        + this.id + " for "
                        + patient.getPreExaminationTime() + " minutes.");
                Thread.sleep(patient.getPreExaminationTime() * 1000);

                if (patient.getNeedsFurtherTreatment()) {
                    Logger.log("Medical assistant " + this.id + " has finished the pre-examination of patient "
                            + patient.getId()
                            + " and has determined that the patient needs further treatment. The patient is waiting for the doctor.");
                    practice.waitForDoctor(patient);
                } else {
                    Logger.log("Medical assistant " + this.id + " has finished the pre-examination of patient "
                            + patient.getId()
                            + " and has determined that the patient does not need further treatment. The patient is leaving the practice.");
                    patient.tellToLeave();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
