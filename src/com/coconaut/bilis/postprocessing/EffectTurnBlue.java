package com.coconaut.bilis.postprocessing;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Cocon on 11/06/2017.
 */
public class EffectTurnBlue extends PostEffect {
    public int execute(Color c, int x, int y, BufferedImage img) {
        int result;
        Color eq = Color.red;
        if(c.getRed() > 0 && c.getGreen() == 0 && c.getBlue() == 0) result = new Color(0, 0, c.getRed()).getRGB();
        else result = c.getRGB();
        return result;
    }
}
