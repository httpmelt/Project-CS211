package ku.cs;

import javafx.scene.text.Font;
import ku.cs.cs211671project.MainApplication;

public class Main {

    public static void main(String[] args) {
        Font.loadFont(Main.class.getResource("/font/Fahkwang/Fahkwang-Bold.ttf").toExternalForm(), 12);
        Font.loadFont(Main.class.getResource("/font/Fahkwang/Fahkwang-BoldItalic.ttf").toExternalForm(), 12);
        Font.loadFont(Main.class.getResource("/font/Fahkwang/Fahkwang-ExtraLight.ttf").toExternalForm(), 12);
        Font.loadFont(Main.class.getResource("/font/Fahkwang/Fahkwang-ExtraLightItalic.ttf").toExternalForm(), 12);
        Font.loadFont(Main.class.getResource("/font/Fahkwang/Fahkwang-Italic.ttf").toExternalForm(), 12);
        Font.loadFont(Main.class.getResource("/font/Fahkwang/Fahkwang-Light.ttf").toExternalForm(), 12);
        Font.loadFont(Main.class.getResource("/font/Fahkwang/Fahkwang-LightItalic.ttf").toExternalForm(), 12);
        Font.loadFont(Main.class.getResource("/font/Fahkwang/Fahkwang-Medium.ttf").toExternalForm(), 12);
        Font.loadFont(Main.class.getResource("/font/Fahkwang/Fahkwang-MediumItalic.ttf").toExternalForm(), 12);
        Font.loadFont(Main.class.getResource("/font/Fahkwang/Fahkwang-Regular.ttf").toExternalForm(), 12);
        Font.loadFont(Main.class.getResource("/font/Fahkwang/Fahkwang-SemiBold.ttf").toExternalForm(), 12);
        Font.loadFont(Main.class.getResource("/font/Fahkwang/Fahkwang-SemiBoldItalic.ttf").toExternalForm(), 12);
        MainApplication.main(args);
    }
}
