package groupproject.mapeditor.components;

import javax.swing.*;

public class NumberInputVerifier extends InputVerifier {
    @Override
    public boolean verify(JComponent input) {
        String text = ((JTextField) input).getText();
        try {
            return Integer.parseInt(text) > 0;
        } catch(NumberFormatException e) {
            return false;
        }
    }
}
