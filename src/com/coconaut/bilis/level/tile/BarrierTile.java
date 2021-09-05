package com.coconaut.bilis.level.tile;

import com.coconaut.bilis.Resources;
import com.coconaut.bilis.gfx.Screen;
import com.coconaut.bilis.gfx.Sprite;
import com.coconaut.bilis.level.Level;

import java.util.Random;

public class BarrierTile extends Tile {

    private Sprite sheet;
    private Random r = new Random();
    private int idd;

    public BarrierTile(int id) {
        super(id);
        this.sheet = Resources.i.spr_tile_barrier;
        this.idd = this.r.nextInt(2);
    }

    public void render(int x, int y, Level level, Screen screen) {
        screen.render(this.sheet, x*16, y*16);
    }
}
