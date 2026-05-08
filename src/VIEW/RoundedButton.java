//make the button that is used everywhere
package VIEW;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {
    private int radius;
    private Color borderColor;
    private int borderThickness;

    public RoundedButton(String text, int radius, Color borderColor, int borderThickness) {
        super(text);
        this.radius = radius;
        this.borderColor = borderColor;
        this.borderThickness = borderThickness;
        
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void setCustomBorder(Color color, int thickness) {
        this.borderColor = color;
        this.borderThickness = thickness;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        
        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        if (borderColor != null && borderThickness > 0) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderThickness));
            g2.drawRoundRect(borderThickness / 2, borderThickness / 2, 
                             getWidth() - borderThickness, getHeight() - borderThickness, 
                             radius, radius);
            g2.dispose();
        }
    }
}
