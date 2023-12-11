package com.parallel;

import java.util.Random;

class Patient {
    private int preExaminationTime;
    private int treatmentTime;
    private boolean needsFurtherTreatment;
    private Practice practice;
    private static int counter = 1;
    private final int id;

    public Patient(Practice practice) {
        this.id = counter++;
        this.practice = practice;

        Random rand = new Random();
        this.preExaminationTime = 5 + rand.nextInt(16); // 5 to 20 minutes
        this.treatmentTime = 3 + rand.nextInt(14); // 3 to 16 minutes
        this.needsFurtherTreatment = rand.nextFloat() > 0.25; // 25% probability of not needing further treatment
    }

    public boolean getNeedsFurtherTreatment() {
        return this.needsFurtherTreatment;
    }

    public int getPreExaminationTime() {
        return this.preExaminationTime;
    }

    public int getTreatmentTime() {
        return this.treatmentTime;
    }

    public int getId() {
        return this.id;
    }

    public void tellToLeave() {
        practice.leave(this);
    }
}
