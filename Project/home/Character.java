public class Character {

    private String gender;
    private int age;
    private String bodyType;

    public static final String DEFAULT_GENDER = "unknown";
    public static final int DEFAULT_AGE = 15;
    public static final String DEFAULT_BODYTYPE = "unspecified";

    // default constructor
    public Character(String gender, int age, String bodyType, int lineNum) {        
        this.gender = this.validGender(gender, lineNum);
        this.age = this.validAge(age, lineNum);
        this.bodyType = this.validBodyType(bodyType, lineNum);
    }

    // constructor without exception handling
    public Character(String gender, int age, String bodyType) {        
        this.gender = gender;
        this.age = age;
        this.bodyType = bodyType;
    }

    // copy constructor
    public Character(Character other) {
        this.gender = other.getGender();
        this.age = other.getAge();
        this.bodyType = other.getBodyType();
    }


    private String validGender(String gender, int lineNum) {
        String valid;    
        try {
            if((gender.equals("male")) || (gender.equals("female"))) {
                valid = gender;
            } else {
                throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
            }
        } catch(InvalidCharacteristicException e) {
            System.out.println(e.getMessage());
            valid= DEFAULT_GENDER;
        }
        return valid;
    }

    private int validAge(int age, int lineNum) {
        int valid;    
        try {
            if(age >= 0) {
                valid = age;
            } else {
                throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
            }
        } catch(InvalidCharacteristicException e) {
            System.out.println(e.getMessage());
            valid = DEFAULT_AGE;
        }
        return valid;
    }

    private String validBodyType(String bodyType, int lineNum) {
        String valid;    
        try {
            if((bodyType.equals("average")) || (bodyType.equals("athletic")) || (bodyType.equals("overweight"))) {
                valid = bodyType;
            } else {
                throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
            }
        } catch(InvalidCharacteristicException e) {
            System.out.println(e.getMessage());
            valid = DEFAULT_BODYTYPE;
        }
        return valid;
    }

    public int getAge() {
        return this.age;
    }

    public String getGender() {
        return this.gender;
    }

    public String getBodyType() {
        return this.bodyType;
    }

}