package com.example.adeyyo.webserv;


import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class Datecalculate {

    public String toreturn(int id){
        LocalDate mydate=LocalDate.now();
        LocalDate target=mydate.minusDays(id);

        return String.valueOf(target);

      //  return "2014-01-02";
    }

    public String todaysdate(){
        LocalDate mydate=LocalDate.now();
        return String.valueOf(mydate);
    }
}
