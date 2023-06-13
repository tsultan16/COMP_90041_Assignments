public class Human extends Character {

    private String profession;
    private boolean pregnant;
    private String ageCategory;

    public static final String DEFAULT_PROFESSION = "none";


    public Human(String gender, int age, String bodyType, String profession, String pregnant, int lineNum) {       
        super(gender, age, bodyType, lineNum);
        this.ageCategory = this.categorizeAge(this.getAge());
        
        if(!this.ageCategory.equals("adult")){
            // default profession for non-adults
            try{
                if(!profession.equals(DEFAULT_PROFESSION)) {
                     throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
                } else {            
                    this.profession = profession;
                }
            } catch (InvalidCharacteristicException e) {
                System.out.println(e.getMessage());
                this.profession = DEFAULT_PROFESSION;
            }
        } else {
            this.profession = this.validProfession(profession, lineNum);
        }
        
        if (this.getGender().equals("male")) {
            try {
                if(pregnant.equals("true")) {
                    throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
                } else {
                    this.pregnant = false;
                }
            } catch (InvalidCharacteristicException e) {
                System.out.println(e.getMessage());
                this.pregnant = false;
            }
        } else {
            if(!this.ageCategory.equals("adult")){
                try {
                    if(pregnant.equals("true")) {
                        throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
                    } else {
                        this.pregnant = false;
                    }
                } catch (InvalidCharacteristicException e) {
                    System.out.println(e.getMessage());
                    this.pregnant = false;
                }
            } else {
                // only adult females can be pregnant
                this.pregnant = Boolean.parseBoolean(pregnant);
            }    
        }
    }

    private String categorizeAge(int age) {
        String category;
        if ((age >= 0) && (age <= 4)) {
            category = "baby";
        } else if ((age >= 5) && (age<= 16)) {
            category = "child";
        } else if((age >= 17) && (age <= 68)) {
            category = "adult";
        } else {
            category = "senior";
        }
        return category;
    }

    private String validProfession(String pr, int lineNum) {
        String valid;
        /*
        try{
            if((pr.equals("doctor")) || (pr.equals("ceo")) || (pr.equals("criminal")) || 
               (pr.equals("homeless")) || (pr.equals("unemployed")) || (pr.equals("none")) ) {
                valid = pr;
            } else {
                throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
            } 
        } catch (InvalidCharacteristicException e) {
            System.out.println(e.getMessage());
            valid =  DEFAULT_PROFESSION;
        }
        */
        if((pr.equals("doctor")) || (pr.equals("ceo")) || (pr.equals("criminal")) || 
            (pr.equals("homeless")) || (pr.equals("unemployed")) || (pr.equals("none")) ||
            (pr.equals("student")) || (pr.equals("scientist"))) {
            valid = pr;
        } else {
            valid =  DEFAULT_PROFESSION;
        } 
        return valid;
    }

    //@Override
    public String toString() {
        String output = this.getBodyType() + " " + this.ageCategory + " ";
        if(this.ageCategory.equals("adult")) {
            output += this.profession + " ";
        } 
        output += this.getGender();
        if(this.pregnant) {
           output += " pregnant"; 
        }
        return output;
    }

}