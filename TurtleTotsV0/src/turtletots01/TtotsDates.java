/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package turtletots01;

import java.util.Calendar;



/**
 *
 * @author Martin
 */
public class TtotsDates {
    
    // VARIABLES
    
    static int day;
    static int month;
    static int year;
    static String datebooked;
    
    // CONSTRUCTORS

    public static void main(String[] args){

        Calendar now = Calendar.getInstance();

        month = now.get(Calendar.MONTH) + 1;
        day = now.get(Calendar.DAY_OF_MONTH);
        year = now.get(Calendar.YEAR);

        String daystring;
        String monthstring;
        if (day < 10){
            daystring = "0" + day;
        } else {
            daystring = String.valueOf(day);
        }
        if (month < 10){
            monthstring = "0" + month;
        } else {
            monthstring = String.valueOf(month);
        }

        datebooked = daystring + "/" + monthstring + "/" + year;

        

//        DateFormat today = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);
//        SimpleDateFormat dF1 = new SimpleDateFormat("dd/MM/yy");
//        String enqdate = dF1.format(today);
//        
//        System.out.println("String enqdate = " + enqdate);
        
    }
    
    // METHODS
    
    
    
    // ACCESS MODIFIERS
    
    public String getTodaysDate(){
        return datebooked;
    }
    
    // MUTATOR MODIFIERS
    
    public int getYear(String date){
        
        String[] dateToNums = date.split("/");
        int iyear = 100 + (Integer.parseInt(dateToNums[2]));
        
        return iyear;
    }
    
    public int getMonth(String date){
        
        String[] dateToNums = date.split("/");
        int imonth = (Integer.parseInt(dateToNums[1]) - 1);
        
        return imonth;
    }
    
    public int getDay(String date){
        
        String[] dateToNums = date.split("/");
        int iday = Integer.parseInt(dateToNums[0]);
        
        return iday;
    }

}