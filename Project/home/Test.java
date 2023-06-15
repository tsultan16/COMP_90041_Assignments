import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collections;

public class Test {

    public static void main(String[] args) {
       ArrayList<String> items = new ArrayList<String>(Arrays.asList("human", "pet", "adult", "human", "cat", "doctor", "pregnant", "cat", "human", "adult"));


        Map<String, Integer> table = new HashMap<String, Integer>();

        // get all distinct items from the list
        Set<String> itemSet = new HashSet<String>(items);

        //System.out.println("Distinct items:");
        for(String item: itemSet) {
            int count = Collections.frequency(items, item);
            table.put(item, count);
        }

        double num = 2.3456;
        double num2 = 2.35659;
        
        String numStr = String.format("%.2f", num);
        String numStr2 = String.format("%.2f", num2);
        Double numParsed = Double.parseDouble(numStr);
        Double num2Parsed = Double.parseDouble(numStr2);


        System.out.println(num);
        System.out.println(num2);
        System.out.println(numStr);
        System.out.println(numStr2);
        System.out.println(numParsed);
        System.out.println(num2Parsed);

        System.out.println(numParsed.compareTo(num2Parsed));    


        String s1 = "abcad";
        String s2 = "abcbd";

        System.out.println(s1.compareTo(s2));

        double dnum = Math.ceil(100 * (double) 3/ (double) 9)/100;
        System.out.println(dnum);
        String snum = String.format("%.2f", dnum); 
        System.out.println(snum);


    }

}