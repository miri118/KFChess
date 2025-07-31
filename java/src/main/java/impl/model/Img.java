package impl.model;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Img {

    private BufferedImage img;

    public Img() {
        this.img = null; // Initialize with no image
    }

    public Img(BufferedImage image) {
        this.img = image;
    }

    // ----------- load & optional resize -----------
    public Img read(String path, Dimension targetSize, boolean keepAspect, Object interpolation /* ignored */) {
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot load image: " + path);
        }
        if (img == null)
            throw new IllegalArgumentException("Unsupported image: " + path);

        if (targetSize != null) {
            int tw = targetSize.width, th = targetSize.height;
            int w = img.getWidth(), h = img.getHeight();

            int nw, nh;
            if (keepAspect) {
                double s = Math.min(tw / (double) w, th / (double) h);
                nw = (int) Math.round(w * s);
                nh = (int) Math.round(h * s);
            } else {
                nw = tw;
                nh = th;
            }

            BufferedImage dst = new BufferedImage(
                    nw, nh,
                    img.getColorModel().hasAlpha()
                            ? BufferedImage.TYPE_INT_ARGB
                            : BufferedImage.TYPE_INT_RGB);

            Graphics2D g = dst.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(img, 0, 0, nw, nh, null);
            g.dispose();
            img = dst;
        }
        return this;
    }

    public Img read(String path) {
        return read(path, null, false, null);
    }

    public void drawOn(Img board, int cellRow, int cellCol, int cellW, int cellH) {
        if (img == null || board.img == null)
            throw new IllegalStateException("Both images must be loaded.");

        // position in pixels
        int xPix = cellCol * cellW;
        int yPix = cellRow * cellH;

        if (xPix + cellW > board.img.getWidth() || yPix + cellH > board.img.getHeight()) {
            throw new IllegalArgumentException("Drawing out of bounds: (" + xPix + "," + yPix + ")");
        }

        // צייר ישירות על תמונת הלוח
        Graphics2D g = board.img.createGraphics();
        g.setComposite(AlphaComposite.SrcOver);

        // שנה גודל אם צריך
        Image tmp = img.getScaledInstance(cellW, cellH, Image.SCALE_SMOOTH);
        g.drawImage(tmp, xPix, yPix, null);
        g.dispose();
    }

    public void drawAt(Img target, int xPix, int yPix, int wPix, int hPix) {
        if (img == null || target.getImg() == null)
            throw new IllegalStateException("Both images must be loaded.");
        Image tmp = img.getScaledInstance(wPix, hPix, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(wPix, hPix, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resized.createGraphics();
        g2.drawImage(tmp, 0, 0, null);
        g2.dispose();
        Graphics2D g = target.getImg().createGraphics();
        g.setComposite(AlphaComposite.SrcOver);
        g.drawImage(resized, xPix, yPix, null);
        g.dispose();
        System.out.printf(":art: Img.drawAt → drawing at (%d, %d), size %dx%d\n", xPix, yPix, wPix, hPix);
    }

    public void putText(String txt, int x, int y, float fontSize,
            Color color, int thickness /* unused in Java2D */) {

        if (img == null)
            throw new IllegalStateException("Image not loaded.");

        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(color);
        g.setFont(img.getGraphics().getFont().deriveFont(fontSize * 12)); // simple scale
        g.drawString(txt, x, y);
        g.dispose();
    }

    public void show() {
        if (img == null)
            throw new IllegalStateException("Image not loaded.");

        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Image");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.add(new JLabel(new ImageIcon(img)));
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public Img clone() {
        Img copy = new Img(null);
        if (img != null) {
            copy.img = new BufferedImage(img.getWidth(), img.getHeight(),
                    img.getType());
            Graphics2D g = copy.img.createGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();
        }
        return copy;
    }

    public int getWidth() {
        return img != null ? img.getWidth() : 0;
    }

    public int getHeight() {
        return img != null ? img.getHeight() : 0;
    }

}