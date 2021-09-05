package com.coconaut.bilis.postprocessing;

import com.coconaut.bilis.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Cocon on 11/06/2017.
 */
public class EffectTest extends PostEffect {
    public int execute(Color c, int x, int y, BufferedImage img) {
        return getAverageRGB(5, x, y, img).getRGB();
    }

    public Color getAverageRGB(int kernel, int x, int y, BufferedImage img) {
        if(kernel/2 == 0) return Color.black;
        int half = Math.abs(kernel/2);

        int pixels = kernel*kernel;
        int r = 0, g = 0, b = 0;

        for(int yy = y-half; yy< y+half; yy++) {
            if(yy > Game.HEIGHT || yy < 0) continue;
            for(int xx = x-half; xx< x+half; xx++) {
                if(xx > Game.WIDTH || xx < 0) continue;
                Color c = new Color(img.getRGB(xx, yy));
                r+=c.getRed();
                g+=c.getGreen();
                b+=c.getBlue();
            }
        }
        return new Color(r/pixels, g/pixels, b/pixels);
    }
}
