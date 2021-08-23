package games;

public class coinflip {
    public String coinflippen() {
        String result;
        if (Math.random() > 0.5) {
            result = "KOPF";
        } else {
            result = "ZAHL";
        }
        return result;
    }
}
