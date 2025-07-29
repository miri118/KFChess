package It1_interfaces.tests.model;

import org.junit.jupiter.api.Test;

import impl.model.Img;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class ImgTest {
    private static final String IMAGE_PATH = "src/test/resources/board.png"; // ודא שהתמונה קיימת

    @Test
    public void testReadImage() {
        Img img = new Img().read(IMAGE_PATH);
        assertNotNull(img.getImg(), "התמונה לא נטענה כראוי");
    }

    @Test
    public void testReadImageWithResize() {
        Dimension targetSize = new Dimension(100, 50);
        Img img = new Img().read(IMAGE_PATH, targetSize, true, null);
        BufferedImage result = img.getImg();

        assertNotNull(result, "התמונה ריקה");
        assertTrue(result.getWidth() <= 100 && result.getHeight() <= 50,
                "גודל התמונה שגוי לאחר resize עם keepAspect");
    }

    @Test
    public void testDrawOn() {
        Img background = new Img().read(IMAGE_PATH);
        Img patch = new Img().read(IMAGE_PATH, new Dimension(10, 10), false, null);

        assertDoesNotThrow(() -> patch.drawOn(background, 5, 5),
                "ציור על תמונה גרם לחריגה");
    }

    @Test
    public void testPutText() {
        Img img = new Img().read(IMAGE_PATH);
        assertDoesNotThrow(() ->
                img.putText("Hello", 10, 20, 1.0f, Color.RED, 1),
                "כתיבת טקסט זרקה חריגה");
    }

    @Test
    public void testCloneImage() {
        Img original = new Img().read(IMAGE_PATH);
        Img cloned = original.clone();

        assertNotSame(original.getImg(), cloned.getImg(), "clone צריך להיות אובייקט חדש");
        assertEquals(original.getImg().getWidth(), cloned.getImg().getWidth(), "רוחב שונה בין המקור לשכפול");
        assertEquals(original.getImg().getHeight(), cloned.getImg().getHeight(), "גובה שונה בין המקור לשכפול");
    }

    @Test
    public void testDrawOutOfBoundsThrows() {
        Img base = new Img().read(IMAGE_PATH);
        Img small = new Img().read(IMAGE_PATH, new Dimension(100, 100), false, null);
        assertThrows(IllegalArgumentException.class, () ->
                small.drawOn(base, 5000, 5000),
                "ציור מחוץ לתחום היה צריך לזרוק חריגה");
    }
}
