package com.generalprocessingunit.processing.music;

import java.util.HashMap;
import java.util.Map;

public class MusicNote {
    int note;
    RhythmType rhythm;
    final int staffPosition;

    public MusicNote(int note, RhythmType rhythm) {
        this.note = note;
        this.rhythm = rhythm;
        staffPosition = noteToStaffPosition.get(note);
    }

    //TODO: express in Scala
    //TODO: bass clef

    //TODO: needs to be more intelligent, based on note name
    public static final Map<Integer, Integer> noteToStaffPosition = new HashMap<>();
    static {
        noteToStaffPosition.put(56, -4);
        noteToStaffPosition.put(57, -4);
        noteToStaffPosition.put(58, -3);
        noteToStaffPosition.put(59, -3);
        noteToStaffPosition.put(60, -2); //c
        noteToStaffPosition.put(61, -2);
        noteToStaffPosition.put(62, -1);
        noteToStaffPosition.put(63, -1);
        noteToStaffPosition.put(64, 0);
        noteToStaffPosition.put(65, 1);
        noteToStaffPosition.put(66, 1);
        noteToStaffPosition.put(67, 2);
        noteToStaffPosition.put(68, 2);
        noteToStaffPosition.put(69, 3);
        noteToStaffPosition.put(70, 3);
        noteToStaffPosition.put(71, 4);
        noteToStaffPosition.put(72, 5); //c
        noteToStaffPosition.put(73, 5);
        noteToStaffPosition.put(74, 6);
        noteToStaffPosition.put(75, 6);
        noteToStaffPosition.put(76, 7);
        noteToStaffPosition.put(77, 8);
        noteToStaffPosition.put(78, 8);
        noteToStaffPosition.put(79, 9);
        noteToStaffPosition.put(80, 9);
        noteToStaffPosition.put(81, 10);
        noteToStaffPosition.put(82, 10);
        noteToStaffPosition.put(83, 11);
        noteToStaffPosition.put(84, 12);
    }
}
