/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.util.converter;

/**
 *
 * @author anoir
 */
public class IntConverter {
    
    public static String intTostring(int i){
        return String.valueOf(i);
    }
    
    public static int stringToInt(String s){
        if(s == null || s.isEmpty()){
            return 0;
        }else{
            return Integer.parseInt(s);
        }
    }
}
