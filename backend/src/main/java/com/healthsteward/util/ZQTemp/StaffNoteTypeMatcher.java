package com.healthsteward.util.ZQTemp;

public class StaffNoteTypeMatcher {
    public static StaffNoteType parse(String noteType){
        switch (noteType.toLowerCase()){
            case "signednote":
                return StaffNoteType.SIGNED;
            case "talkingnote":
                return StaffNoteType.TALKING;
            default:
                return null;
        }
    }

}
