package games;

public class Guess {
    String[] colorlist = new String[4];
    int internguesscounter=0;

    public Guess(String color1, String color2, String color3, String color4) {
        colorlist[0]=color1;
        colorlist[1]=color2;
        colorlist[2]=color3;
        colorlist[3]=color4;
    }
    public Guess() {
    }
    public String getcolorlistatindex(int index){
        return colorlist[index];
    }
    public void addcolor(String color){
        colorlist[internguesscounter]=color;
        internguesscounter++;
    }
    public int getinternguesscounter(){
        return internguesscounter;
    }
    //copy constructor
    //constructor
    public void removecolor(){
        colorlist[internguesscounter-1]="";
        internguesscounter--;
    }
    public void removeallcolor(){
        while (internguesscounter>0){
            removecolor();
        }
    }
    public void removecoloratlocation(int index){
        colorlist[index]="";
    }
}
