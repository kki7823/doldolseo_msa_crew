package com.doldolseo.doldolseo_msa_crew.utils;

public class AuthorityUtil {
    public boolean areYouCrewLeader(String role) {
        if (!role.equals("CREWLEADER")) return false;
        return true;
    }

    public boolean areYouUser(String role){
        if (!role.equals("USER")) return false;
        return true;
    }
}
