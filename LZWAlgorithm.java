package com.company;

import java.io.*;
import java.security.KeyPair;
import java.util.*;


/*
Izraboteno od Andrej Veljanovski

Upatstvo za upotreba:
Po izvrsuvanje na programata se vnesuva absolutnata pateka na tekstualna datoteka ("*.txt") na cija tekstualna
sodrzina se izvrsuva LZW algoritmot. Rezultatot se zapisuva vo nova tekstualna datoteka "output.txt" koja se naogja
vo direktoriumot na aplikacijata.
 */

public class LZWAlgorithm {

    public static List<Integer> encode (String path) throws IOException {

        File f = new File(path);
        if(!f.exists()){
            System.out.println("Greska! Obidete se povtorno..");
            return null;
        }
        Scanner sc = new Scanner(f); // se koristi za da se procita tekstualnata datoteka

        Map<String, Integer> map = new HashMap<>();
        List<Integer> result = new ArrayList<>();

        File outputFile = new File("output.txt");
        FileWriter fileWriter = new FileWriter(outputFile);
        if(!outputFile.exists()){
            outputFile.createNewFile();
        }

        int mapSize = 256;

        for(int i = 0; i<mapSize; i++){ // 256 unikatni karakteri vo ASCII
            map.put(String.valueOf((char)i), i);
        }

        String current="";
        String iterated = "";
        String line;
        StringBuilder sb = new StringBuilder();
        while(sc.hasNext()){
            sb.append(sc.nextLine());
            sb.append(" ");
        }
        String parts[] = sb.toString().split("\\s+");

        for(int j = 0; j<parts.length;j++) {
            for (int i = 0; i < parts[j].length(); i++) {
                current = current + parts[j].charAt(i);

                if (map.containsKey(current)) {
                    iterated = current;
                } else {
                    result.add(map.get(iterated));
                    try{
                        fileWriter.write(map.get(iterated).toString());
                        fileWriter.write(" ");
                    }catch(IOException e){
                        System.out.println(e.getMessage());
                    }

                    //System.out.println("ADDING: " + iterated);
                    map.put(current, mapSize++);
                    //System.out.println("PUTTING: " + current);
                    current = "";
                    i -= 1;
                }
            }
            if (!current.isEmpty()) {
                result.add(map.get(current));
                try{
                    fileWriter.write(map.get(current).toString());
                    //fileWriter.write(" ");
                }catch(IOException e){
                    System.out.println(e.getMessage());
                }
                //System.out.println("FINALLY ADDING: " + current);
                if (!map.containsKey(current)) {
                    map.put(current, mapSize++);
                }
            }
        }

        fileWriter.flush();
        fileWriter.close();

        return result;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Vnesete ja absolutnata pateka na tekstualniot dokument koj sakate da go procitate.");

        String path = scanner.nextLine();

        List<Integer> test = null;
        try {
            test = encode(path);
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        for (int i = 0; i< Objects.requireNonNull(test).size(); i++){
            System.out.println(test.get(i));
        }



    }
}
