package impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import impl.model.Img;

public class MockImg extends Img {
    public static List<int[]> traj = new ArrayList<>();
    public static List<Object[]> txt_traj = new ArrayList<>();

    public String img;
    public int W, H;

    public MockImg() {
        super(null);
        this.img = "MOCK-PIXELS";
        this.W = 1;
        this.H = 1;
    }

    public MockImg read(String path, Object... args) {
        this.W = 1;
        this.H = 1;
        return this;
    }

    @Override
    public MockImg read(String path) {
        return read(path, null);
    }

    @Override
    public void drawAt(Img other, int x, int y, int w, int h) {
        //TODO
        traj.add(new int[]{x, y});
    }

    @Override
    public void putText(String txt, int x, int y, float fontSize, Color color, int thickness) {
        txt_traj.add(new Object[]{new int[]{x, y}, txt});
    }

    @Override
    public void show() {
        // do nothing
    }

    public static void reset() {
        traj.clear();
        txt_traj.clear();
    }
}
