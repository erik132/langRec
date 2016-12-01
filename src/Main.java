import engine.MainEngine;

public class Main {

    public static void main(String[] args) {
        MainEngine engine = new MainEngine("inputFile.xml", "outputxml.xml");
        engine.startParse();
    }
}
