package com.example.adeyyo.controller;

import org.json.JSONException;
import org.json.XML;

import com.example.adeyyo.webserv.CountryService;
import com.example.adeyyo.webserv.Datecalculate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@RestController
public class adeyyocontroller {

    @Autowired
    private Datecalculate datacalc;


    @GetMapping("/hello")
    public String hello(){
        return "Hello world";

    }

    @GetMapping("/magento/{id}/{country}")
    public void earthquakes(@PathVariable int id, @PathVariable String country) throws IOException, ParseException {
        String date= datacalc.toreturn(id);
        String today= datacalc.todaysdate();
        String uri="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=" + date + "&endtime=" +today; //2022-05-01&endtime=" + date;
        String inline="";
        URL url=new URL(uri);

        HttpURLConnection conn=(HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");

        conn.connect();

        int responsecode=conn.getResponseCode();

        if(responsecode!=200)
            throw new RuntimeException("HttpResponseCode: "+ responsecode);

        else{
            Scanner sc=new Scanner(url.openStream());
            while(sc.hasNext()){
                inline += sc.nextLine();

            }
            System.out.println(inline);
            sc.close();

            JSONParser parse=new JSONParser();
            JSONObject jobj=(JSONObject)parse.parse(inline);
            JSONArray jsonarr_1=(JSONArray) jobj.get("features");


            for(int i=0;i<jsonarr_1.size();i++) {

                    JSONObject jsonobj_1=(JSONObject) jsonarr_1.get(i);
                    JSONObject temp=(JSONObject) jsonobj_1.get("geometry");
                    JSONArray coordinate=(JSONArray) temp.get("coordinates");

                    double longitude=(double)coordinate.get(0);
                    double latitude=(double)coordinate.get(1);

                    String uri2="http://api.geonames.org/findNearbyPlaceName?lat=" + latitude +"&lng=" + longitude +"&username=tavananna";
                    String inline2="";
                    URL url2=new URL(uri2);


                    HttpURLConnection conn2=(HttpURLConnection) url2.openConnection();

                    conn2.setRequestMethod("GET");

                    conn2.connect();

                    Scanner sc2=new Scanner(url2.openStream());
                    while(sc2.hasNext()){
                        inline2+=sc2.nextLine();
                    }

                    try{
                        org.json.JSONObject jsonum=XML.toJSONObject(inline2);
                        String country2=(String) jsonum.get("countryName");
                        System.out.println(country2);

                    }
                    catch (JSONException e) {

                        System.out.println(e.toString());
                    }

                /* Geonames adlı 3.parti bir uygulamadan ülke ismini çekmeye çalıştım fakat günlük kısıtlamadan dolayı bazen değer getirmiyor
                Kodun genel mantığına bakılırsa buradan sonra kullanıcının girdiği country ile koordinatlardan elde edilmiş country bilgisinin
                eşleşip eşleşmediğine bakılır ve eşleşiyorsa yeni bir JSON yapısına dahil edilerek ilgili tarih aralıklarında o ülkede olan depremler elde edilir
                 */


            }
        }
    }





}














/*  public ResponseEntity<Object> earthquakes(@PathVariable int id,@PathVariable String country){
        String date= datacalc.toreturn(id);
        String today= datacalc.todaysdate();
        String uri="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=" + date + "&endtime=" +today; //2022-05-01&endtime=" + date;
        //     String uri="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-01-02";

        RestTemplate restTemplate=new RestTemplate();

        //   Object[] eartquakelist=restTemplate.getForObject(uri, Object[].class);
        //  return eartquakelist;

        ResponseEntity<Object> results =restTemplate.getForEntity(uri,Object.class);




        // return Arrays.asList(eartquakelist);
        return results;

    }
        */













