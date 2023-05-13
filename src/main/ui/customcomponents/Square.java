package ui.customcomponents;

import javax.swing.*;
import java.awt.*;

// Custom JPanel component representing a graphical chess board square
// Contains data about square's color and coordinate
public class Square extends JPanel {
    private Color defaultColor;
    private String coordinate;

    // EFFECTS: constructs square with given default color and coordinate
    public Square(Color defaultColor, String coordinate) {
        this.defaultColor = defaultColor;
        this.coordinate = coordinate;
    }

    // ===== getters =====
    public Color getDefaultColor() {
        return defaultColor;
    }

    public String getCoordinate() {
        return coordinate;
    }
}