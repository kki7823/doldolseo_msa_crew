package com.doldolseo.doldolseo_msa_crew.utils;

public class AuthorityUtil {
    public boolean areYouCrewLeader(String role) {
        return role.equals("CREWLEADER");
    }

    public boolean areYouUser(String role){
        return role.equals("USER");
    }

    public boolean isYou(String memberId, String userId){
        return memberId.equals(userId);
    }
}
