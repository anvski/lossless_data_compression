package com.company;

import java.math.BigInteger;
import java.util.*;

public class ArithmeticCoding {
    
     private static HashMap<Character, Long> cumulativeFreq(HashMap<Character, Long> freq) {
        long total = 0;
        HashMap<Character, Long> cf = new HashMap<Character, Long>();
        for (int i = 0; i < 256; ++i) {
            char c = (char) i;
            Long v = freq.get(c);
            if (v != null) {
                cf.put(c, total);
                total += v;
            }
        }
        return cf;
    }

    private static class Triple<A, B, C> {
        A a;
        B b;
        C c;

        Triple(A a, B b, C c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }
    // za polesna implementacija upotreben e BigInteger i se raboti so frekfencija na javuvanje namesto
    // so verojatnost za javuvanje (za da se izbegnat decimali).

    private static Triple<BigInteger, Integer, HashMap<Character, Long>> arithmeticCoding(String str, Long radix) {
        char[] chars = str.toCharArray();

        HashMap<Character, Long> freq = new HashMap<Character, Long>();
        for (char c : chars) {
            if (!freq.containsKey(c))
                freq.put(c, 1L);
            else
                freq.put(c, freq.get(c) + 1);
        }

        HashMap<Character, Long> cf = cumulativeFreq(freq);

        BigInteger base = BigInteger.valueOf(chars.length);

        BigInteger lower = BigInteger.ZERO;

        BigInteger pf = BigInteger.ONE;

        for (char c : chars) {
            BigInteger x = BigInteger.valueOf(cf.get(c));
            lower = lower.multiply(base).add(x.multiply(pf));
            pf = pf.multiply(BigInteger.valueOf(freq.get(c)));
        }

        BigInteger upper = lower.add(pf);

        int powr = 0;
        BigInteger bigRadix = BigInteger.valueOf(radix);

        while (true) {
            pf = pf.divide(bigRadix);
            if (pf.equals(BigInteger.ZERO)) break;
            powr++;
        }

        BigInteger diff = upper.subtract(BigInteger.ONE).divide(bigRadix.pow(powr));
        return new Triple<>(diff, powr, freq);
    }

    private static String arithmeticDecoding(BigInteger num, long radix, int pwr, HashMap<Character, Long> freq) {
        BigInteger power = BigInteger.valueOf(radix);
        BigInteger enc = num.multiply(power.pow(pwr));
        long base = 0;
        for (Long v : freq.values()) base += v;

        HashMap<Character, Long> cf = cumulativeFreq(freq);

        Map<Long, Character> dict = new HashMap<>();
        for (Map.Entry<Character, Long> entry : cf.entrySet()) dict.put(entry.getValue(), entry.getKey());

        long lchar = -1;
        for (long i = 0; i < base; ++i) {
            Character v = dict.get(i);
            if (v != null) {
                lchar = v;
            } else if (lchar != -1) {
                dict.put(i, (char) lchar);
            }
        }

        StringBuilder decoded = new StringBuilder((int) base);
        BigInteger bigBase = BigInteger.valueOf(base);
        for (long i = base - 1; i >= 0; --i) {
            BigInteger pow = bigBase.pow((int) i);
            BigInteger div = enc.divide(pow);
            Character c = dict.get(div.longValue());
            BigInteger fv = BigInteger.valueOf(freq.get(c));
            BigInteger cv = BigInteger.valueOf(cf.get(c));
            BigInteger diff = enc.subtract(pow.multiply(cv));
            enc = diff.divide(fv);
            decoded.append(c);
        }
        return decoded.toString();
    }

    public static void main(String[] args) {
        long radix = 10;
        System.out.println("Vnesete redovi tekstovi koi sakate da se kodiraat");
        System.out.println("Za zavrsuvanje vnesete prazna linija tekst");
        Scanner sc = new Scanner(System.in);
        String line = "";
        List<String> strings = new ArrayList<>();
        while(true){
            line = sc.nextLine();

            if(line.isEmpty()){
                break;
            }
            strings.add(line);
        }
        
        for (String str : strings) {
            Triple<BigInteger, Integer, HashMap<Character, Long>> encoded = arithmeticCoding(str, radix);
            String decoded = arithmeticDecoding(encoded.a, radix, encoded.b, encoded.c);
            System.out.println("Originalno:\t" + str + "\tDekodirano(Po kodiranje):\t" + decoded);
            System.out.printf("Kodirano:\t%s\t*\t%d^%s\n",encoded.a, radix, encoded.b);
        }
    }
}