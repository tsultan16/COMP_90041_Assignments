public class Animal extends Character {

    private String species;
    private boolean isPet;

    public Animal(String gender, int age, String bodyType, String species, String isPet, int lineNum) {
        super(gender, age, bodyType, lineNum);
        this.species = species;
        if((species.equals("cat")) || (species.equals("dog")) || (species.equals("ferret"))) {
            this.isPet = Boolean.parseBoolean(isPet);
        } else {
            // any species other than cat, dog or ferret cannot be pet
            this.isPet = false;
        }
    }

}