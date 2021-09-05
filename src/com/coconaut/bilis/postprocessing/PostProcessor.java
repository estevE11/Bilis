package com.coconaut.bilis.postprocessing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * Created by Cocon on 11/06/2017.
 */
public class PostProcessor {
    LinkedList<PostEffect> effects = new LinkedList<PostEffect>();

    public BufferedImage execute(BufferedImage img) {
        for(int y = 0; y < img.getHeight(); y++) {
            for(int x = 0; x < img.getWidth(); x++) {
                for(PostEffect effect : effects) {
                    img.setRGB(x, y, effect.execute(new Color(img.getRGB(x, y)), x, y, img));
                }
            }
        }
        return img;
    }

    public void add(PostEffect effect) {
        this.effects.add(effect);
    }
}
