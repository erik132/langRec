import engine.MainEngine;

public class Main {

    public static void main(String[] args) {
        MainEngine engine = new MainEngine("inputFile.txt", "outputxml.xml");
        engine.startParse();
    }
}
