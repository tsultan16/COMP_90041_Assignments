public class Test {

    public static void main(String[] args) {

        String s = "1,2,300000000000000000000000000000000000,4";
        String s2 = "1,,,";

        String[] sp1 = s.split(",", -1);
        String[] sp2 = s2.split(",", -1);
        
        System.out.println(sp1.length);
        System.out.println(sp2.length);

        System.out.println("sp1:");  
        for(int i = 0; i < sp1.length; i++){
            System.out.printf("%d: %s\n", i, sp1[i]);
        }

        System.out.println("sp2:");  
        for(int i = 0; i < sp2.length; i++){
            System.out.printf("%d: %s, %b \n", i, sp2[i], sp2[i].equals(""));
        }
        
    }

}