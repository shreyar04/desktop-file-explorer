/*
 * Decompiled with CFR 0.152.
 */
package fileexplorer;

import fileexplorer.FileUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class IconUtils {
    private static final Map<String, Icon> ICON_CACHE = new HashMap<String, Icon>();
    public static final Color COLOR_BLUE_ACCENT = new Color(0, 103, 192);
    public static final Color COLOR_FOLDER_BODY = new Color(255, 179, 0);
    public static final Color COLOR_FOLDER_TAB = new Color(255, 202, 40);
    public static final Color COLOR_FOLDER_BACK = new Color(255, 143, 0);
    public static final Color COLOR_TEXT_MUTED = new Color(112, 112, 112);
    public static final Color COLOR_ICON_DARK = new Color(44, 44, 44);

    private IconUtils() {
    }

    private static BufferedImage createBuffer(int n, int n2) {
        BufferedImage bufferedImage = new BufferedImage(n, n2, 2);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        return bufferedImage;
    }

    public static Icon getBackIcon(int n, Color color) {
        String string = "back_" + n + "_" + color.getRGB();
        if (ICON_CACHE.containsKey(string)) {
            return ICON_CACHE.get(string);
        }
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(color);
        graphics2D.setStroke(new BasicStroke((float)n * 0.11f, 1, 1));
        float f = (float)n / 2.0f;
        float f2 = (float)n * 0.25f;
        float f3 = (float)n * 0.75f;
        float f4 = (float)n * 0.22f;
        graphics2D.drawLine((int)f2, (int)f, (int)f3, (int)f);
        graphics2D.drawLine((int)f2, (int)f, (int)(f2 + f4), (int)(f - f4));
        graphics2D.drawLine((int)f2, (int)f, (int)(f2 + f4), (int)(f + f4));
        graphics2D.dispose();
        ImageIcon imageIcon = new ImageIcon(bufferedImage);
        ICON_CACHE.put(string, imageIcon);
        return imageIcon;
    }

    public static Icon getForwardIcon(int n, Color color) {
        String string = "fwd_" + n + "_" + color.getRGB();
        if (ICON_CACHE.containsKey(string)) {
            return ICON_CACHE.get(string);
        }
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(color);
        graphics2D.setStroke(new BasicStroke((float)n * 0.11f, 1, 1));
        float f = (float)n / 2.0f;
        float f2 = (float)n * 0.25f;
        float f3 = (float)n * 0.75f;
        float f4 = (float)n * 0.22f;
        graphics2D.drawLine((int)f2, (int)f, (int)f3, (int)f);
        graphics2D.drawLine((int)f3, (int)f, (int)(f3 - f4), (int)(f - f4));
        graphics2D.drawLine((int)f3, (int)f, (int)(f3 - f4), (int)(f + f4));
        graphics2D.dispose();
        ImageIcon imageIcon = new ImageIcon(bufferedImage);
        ICON_CACHE.put(string, imageIcon);
        return imageIcon;
    }

    public static Icon getUpIcon(int n, Color color) {
        String string = "up_" + n + "_" + color.getRGB();
        if (ICON_CACHE.containsKey(string)) {
            return ICON_CACHE.get(string);
        }
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(color);
        graphics2D.setStroke(new BasicStroke((float)n * 0.11f, 1, 1));
        float f = (float)n / 2.0f;
        float f2 = (float)n * 0.25f;
        float f3 = (float)n * 0.75f;
        float f4 = (float)n * 0.22f;
        graphics2D.drawLine((int)f, (int)f3, (int)f, (int)f2);
        graphics2D.drawLine((int)f, (int)f2, (int)(f - f4), (int)(f2 + f4));
        graphics2D.drawLine((int)f, (int)f2, (int)(f + f4), (int)(f2 + f4));
        graphics2D.dispose();
        ImageIcon imageIcon = new ImageIcon(bufferedImage);
        ICON_CACHE.put(string, imageIcon);
        return imageIcon;
    }

    public static Icon getRefreshIcon(int n, Color color) {
        String string = "refresh_" + n + "_" + color.getRGB();
        if (ICON_CACHE.containsKey(string)) {
            return ICON_CACHE.get(string);
        }
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(color);
        graphics2D.setStroke(new BasicStroke((float)n * 0.1f, 1, 1));
        int n2 = (int)((float)n * 0.22f);
        int n3 = n - n2 * 2;
        graphics2D.drawArc(n2, n2, n3, n3, 45, 275);
        int n4 = (int)((float)n * 0.72f);
        int n5 = (int)((float)n * 0.28f);
        int n6 = (int)((float)n * 0.14f);
        graphics2D.drawLine(n4, n5, n4 + n6, n5 - (int)((float)n6 * 0.2f));
        graphics2D.drawLine(n4, n5, n4 - (int)((float)n6 * 0.2f), n5 - n6);
        graphics2D.dispose();
        ImageIcon imageIcon = new ImageIcon(bufferedImage);
        ICON_CACHE.put(string, imageIcon);
        return imageIcon;
    }

    public static Icon getNewIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(COLOR_BLUE_ACCENT);
        graphics2D.fill(new Ellipse2D.Float(1.0f, 1.0f, n - 2, n - 2));
        graphics2D.setColor(Color.WHITE);
        graphics2D.setStroke(new BasicStroke((float)n * 0.14f, 1, 1));
        int n2 = n / 2;
        int n3 = (int)((float)n * 0.26f);
        graphics2D.drawLine(n2, n2 - n3, n2, n2 + n3);
        graphics2D.drawLine(n2 - n3, n2, n2 + n3, n2);
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getCutIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(COLOR_ICON_DARK);
        graphics2D.setStroke(new BasicStroke((float)n * 0.09f, 1, 1));
        int n2 = (int)((float)n * 0.16f);
        graphics2D.drawOval((int)((float)n * 0.15f), (int)((float)n * 0.6f), n2, n2);
        graphics2D.drawOval((int)((float)n * 0.55f), (int)((float)n * 0.6f), n2, n2);
        graphics2D.drawLine((int)((float)n * 0.28f), (int)((float)n * 0.6f), (int)((float)n * 0.75f), (int)((float)n * 0.15f));
        graphics2D.drawLine((int)((float)n * 0.6f), (int)((float)n * 0.6f), (int)((float)n * 0.18f), (int)((float)n * 0.15f));
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getCopyIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(COLOR_ICON_DARK);
        graphics2D.setStroke(new BasicStroke((float)n * 0.09f, 1, 1));
        int n2 = (int)((float)n * 0.48f);
        int n3 = (int)((float)n * 0.58f);
        graphics2D.drawRoundRect((int)((float)n * 0.32f), (int)((float)n * 0.12f), n2, n3, 3, 3);
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRoundRect((int)((float)n * 0.15f), (int)((float)n * 0.28f), n2, n3, 3, 3);
        graphics2D.setColor(COLOR_ICON_DARK);
        graphics2D.drawRoundRect((int)((float)n * 0.15f), (int)((float)n * 0.28f), n2, n3, 3, 3);
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getPasteIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(COLOR_ICON_DARK);
        graphics2D.setStroke(new BasicStroke((float)n * 0.08f, 1, 1));
        int n2 = (int)((float)n * 0.18f);
        int n3 = (int)((float)n * 0.18f);
        int n4 = (int)((float)n * 0.64f);
        int n5 = (int)((float)n * 0.72f);
        graphics2D.drawRoundRect(n2, n3, n4, n5, 4, 4);
        graphics2D.setColor(COLOR_BLUE_ACCENT);
        graphics2D.fillRoundRect((int)((float)n * 0.35f), (int)((float)n * 0.1f), (int)((float)n * 0.3f), (int)((float)n * 0.14f), 2, 2);
        graphics2D.setColor(new Color(230, 235, 245));
        graphics2D.fillRect((int)((float)n * 0.28f), (int)((float)n * 0.32f), (int)((float)n * 0.44f), (int)((float)n * 0.46f));
        graphics2D.setColor(COLOR_BLUE_ACCENT);
        graphics2D.setStroke(new BasicStroke((float)n * 0.06f));
        graphics2D.drawLine((int)((float)n * 0.34f), (int)((float)n * 0.42f), (int)((float)n * 0.64f), (int)((float)n * 0.42f));
        graphics2D.drawLine((int)((float)n * 0.34f), (int)((float)n * 0.52f), (int)((float)n * 0.64f), (int)((float)n * 0.52f));
        graphics2D.drawLine((int)((float)n * 0.34f), (int)((float)n * 0.62f), (int)((float)n * 0.54f), (int)((float)n * 0.62f));
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getRenameIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(COLOR_ICON_DARK);
        graphics2D.setStroke(new BasicStroke((float)n * 0.09f, 1, 1));
        int n2 = (int)((float)n * 0.52f);
        int n3 = (int)((float)n * 0.68f);
        graphics2D.drawRoundRect((int)((float)n * 0.15f), (int)((float)n * 0.15f), n2, n3, 3, 3);
        graphics2D.setColor(COLOR_BLUE_ACCENT);
        graphics2D.setStroke(new BasicStroke((float)n * 0.11f, 1, 1));
        graphics2D.drawLine((int)((float)n * 0.5f), (int)((float)n * 0.78f), (int)((float)n * 0.82f), (int)((float)n * 0.46f));
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getDeleteIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(COLOR_ICON_DARK);
        graphics2D.setStroke(new BasicStroke((float)n * 0.09f, 1, 1));
        graphics2D.drawLine((int)((float)n * 0.2f), (int)((float)n * 0.28f), (int)((float)n * 0.8f), (int)((float)n * 0.28f));
        graphics2D.drawLine((int)((float)n * 0.4f), (int)((float)n * 0.2f), (int)((float)n * 0.6f), (int)((float)n * 0.2f));
        int n2 = (int)((float)n * 0.28f);
        int n3 = (int)((float)n * 0.34f);
        int n4 = (int)((float)n * 0.44f);
        int n5 = (int)((float)n * 0.52f);
        graphics2D.drawRoundRect(n2, n3, n4, n5, 3, 3);
        graphics2D.drawLine((int)((float)n * 0.42f), (int)((float)n * 0.42f), (int)((float)n * 0.42f), (int)((float)n * 0.74f));
        graphics2D.drawLine((int)((float)n * 0.58f), (int)((float)n * 0.42f), (int)((float)n * 0.58f), (int)((float)n * 0.74f));
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getPropertiesIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(COLOR_BLUE_ACCENT);
        graphics2D.setStroke(new BasicStroke((float)n * 0.09f));
        graphics2D.drawOval((int)((float)n * 0.12f), (int)((float)n * 0.12f), (int)((float)n * 0.76f), (int)((float)n * 0.76f));
        graphics2D.fillRect((int)((float)n * 0.46f), (int)((float)n * 0.28f), (int)((float)n * 0.1f), (int)((float)n * 0.1f));
        graphics2D.fillRect((int)((float)n * 0.46f), (int)((float)n * 0.44f), (int)((float)n * 0.1f), (int)((float)n * 0.28f));
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getSortIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(COLOR_ICON_DARK);
        graphics2D.setStroke(new BasicStroke((float)n * 0.09f, 1, 1));
        graphics2D.drawLine((int)((float)n * 0.32f), (int)((float)n * 0.75f), (int)((float)n * 0.32f), (int)((float)n * 0.25f));
        graphics2D.drawLine((int)((float)n * 0.32f), (int)((float)n * 0.25f), (int)((float)n * 0.2f), (int)((float)n * 0.4f));
        graphics2D.drawLine((int)((float)n * 0.68f), (int)((float)n * 0.25f), (int)((float)n * 0.68f), (int)((float)n * 0.75f));
        graphics2D.drawLine((int)((float)n * 0.68f), (int)((float)n * 0.75f), (int)((float)n * 0.8f), (int)((float)n * 0.6f));
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getViewGridIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(COLOR_ICON_DARK);
        int n2 = (int)((float)n * 0.32f);
        int n3 = (int)((float)n * 0.14f);
        int n4 = (int)((float)n * 0.18f);
        int n5 = n4 + n2 + n3;
        graphics2D.fillRoundRect(n4, n4, n2, n2, 2, 2);
        graphics2D.fillRoundRect(n5, n4, n2, n2, 2, 2);
        graphics2D.fillRoundRect(n4, n5, n2, n2, 2, 2);
        graphics2D.fillRoundRect(n5, n5, n2, n2, 2, 2);
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getViewDetailsIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(COLOR_ICON_DARK);
        graphics2D.setStroke(new BasicStroke((float)n * 0.1f, 1, 1));
        for (int i = 0; i < 3; ++i) {
            int n2 = (int)((float)n * (0.25f + (float)i * 0.25f));
            graphics2D.fillOval((int)((float)n * 0.18f), n2 - 2, 4, 4);
            graphics2D.drawLine((int)((float)n * 0.38f), n2, (int)((float)n * 0.82f), n2);
        }
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getSearchIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(COLOR_TEXT_MUTED);
        graphics2D.setStroke(new BasicStroke((float)n * 0.11f, 1, 1));
        int n2 = (int)((float)n * 0.44f);
        graphics2D.drawOval((int)((float)n * 0.15f), (int)((float)n * 0.15f), n2, n2);
        graphics2D.drawLine((int)((float)n * 0.55f), (int)((float)n * 0.55f), (int)((float)n * 0.85f), (int)((float)n * 0.85f));
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getCloseIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(COLOR_TEXT_MUTED);
        graphics2D.setStroke(new BasicStroke((float)n * 0.14f, 1, 1));
        int n2 = (int)((float)n * 0.25f);
        int n3 = (int)((float)n * 0.75f);
        graphics2D.drawLine(n2, n2, n3, n3);
        graphics2D.drawLine(n2, n3, n3, n2);
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getHomeIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(COLOR_BLUE_ACCENT);
        Polygon polygon = new Polygon();
        polygon.addPoint(n / 2, (int)((float)n * 0.15f));
        polygon.addPoint((int)((float)n * 0.12f), (int)((float)n * 0.5f));
        polygon.addPoint((int)((float)n * 0.88f), (int)((float)n * 0.5f));
        graphics2D.fillPolygon(polygon);
        graphics2D.fillRect((int)((float)n * 0.24f), (int)((float)n * 0.48f), (int)((float)n * 0.52f), (int)((float)n * 0.4f));
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect((int)((float)n * 0.42f), (int)((float)n * 0.62f), (int)((float)n * 0.16f), (int)((float)n * 0.26f));
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getDesktopIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(new Color(0, 120, 215));
        graphics2D.fillRoundRect((int)((float)n * 0.12f), (int)((float)n * 0.16f), (int)((float)n * 0.76f), (int)((float)n * 0.54f), 4, 4);
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect((int)((float)n * 0.18f), (int)((float)n * 0.22f), (int)((float)n * 0.64f), (int)((float)n * 0.42f));
        graphics2D.setColor(new Color(100, 100, 100));
        graphics2D.fillRect((int)((float)n * 0.44f), (int)((float)n * 0.7f), (int)((float)n * 0.12f), (int)((float)n * 0.12f));
        graphics2D.fillRect((int)((float)n * 0.3f), (int)((float)n * 0.82f), (int)((float)n * 0.4f), (int)((float)n * 0.06f));
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getDownloadsIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(new Color(16, 124, 65));
        graphics2D.setStroke(new BasicStroke((float)n * 0.11f, 1, 1));
        graphics2D.drawPolyline(new int[]{(int)((float)n * 0.18f), (int)((float)n * 0.18f), (int)((float)n * 0.82f), (int)((float)n * 0.82f)}, new int[]{(int)((float)n * 0.6f), (int)((float)n * 0.82f), (int)((float)n * 0.82f), (int)((float)n * 0.6f)}, 4);
        int n2 = n / 2;
        graphics2D.drawLine(n2, (int)((float)n * 0.18f), n2, (int)((float)n * 0.64f));
        graphics2D.drawLine(n2, (int)((float)n * 0.64f), (int)((float)n * 0.32f), (int)((float)n * 0.44f));
        graphics2D.drawLine(n2, (int)((float)n * 0.64f), (int)((float)n * 0.68f), (int)((float)n * 0.44f));
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getDocumentsIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(new Color(0, 120, 215));
        graphics2D.fillRoundRect((int)((float)n * 0.14f), (int)((float)n * 0.18f), (int)((float)n * 0.72f), (int)((float)n * 0.64f), 4, 4);
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect((int)((float)n * 0.28f), (int)((float)n * 0.32f), (int)((float)n * 0.44f), (int)((float)n * 0.08f));
        graphics2D.fillRect((int)((float)n * 0.28f), (int)((float)n * 0.46f), (int)((float)n * 0.44f), (int)((float)n * 0.08f));
        graphics2D.fillRect((int)((float)n * 0.28f), (int)((float)n * 0.6f), (int)((float)n * 0.26f), (int)((float)n * 0.08f));
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getPicturesIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(new Color(230, 81, 0));
        graphics2D.fillRoundRect((int)((float)n * 0.12f), (int)((float)n * 0.18f), (int)((float)n * 0.76f), (int)((float)n * 0.64f), 4, 4);
        graphics2D.setColor(Color.WHITE);
        Polygon polygon = new Polygon();
        polygon.addPoint((int)((float)n * 0.2f), (int)((float)n * 0.74f));
        polygon.addPoint((int)((float)n * 0.46f), (int)((float)n * 0.4f));
        polygon.addPoint((int)((float)n * 0.64f), (int)((float)n * 0.6f));
        polygon.addPoint((int)((float)n * 0.74f), (int)((float)n * 0.48f));
        polygon.addPoint((int)((float)n * 0.82f), (int)((float)n * 0.74f));
        graphics2D.fillPolygon(polygon);
        graphics2D.fillOval((int)((float)n * 0.62f), (int)((float)n * 0.26f), (int)((float)n * 0.16f), (int)((float)n * 0.16f));
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getMusicIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(new Color(233, 30, 99));
        graphics2D.fill(new Ellipse2D.Float(1.0f, 1.0f, n - 2, n - 2));
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillOval((int)((float)n * 0.26f), (int)((float)n * 0.58f), (int)((float)n * 0.24f), (int)((float)n * 0.2f));
        graphics2D.fillRect((int)((float)n * 0.44f), (int)((float)n * 0.26f), (int)((float)n * 0.08f), (int)((float)n * 0.42f));
        graphics2D.fillRect((int)((float)n * 0.44f), (int)((float)n * 0.24f), (int)((float)n * 0.28f), (int)((float)n * 0.12f));
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getRecycleBinIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(new Color(0, 120, 215));
        graphics2D.setStroke(new BasicStroke((float)n * 0.09f, 1, 1));
        graphics2D.drawLine((int)((float)n * 0.2f), (int)((float)n * 0.26f), (int)((float)n * 0.8f), (int)((float)n * 0.26f));
        graphics2D.drawRoundRect((int)((float)n * 0.26f), (int)((float)n * 0.32f), (int)((float)n * 0.48f), (int)((float)n * 0.56f), 3, 3);
        graphics2D.drawLine((int)((float)n * 0.42f), (int)((float)n * 0.42f), (int)((float)n * 0.42f), (int)((float)n * 0.76f));
        graphics2D.drawLine((int)((float)n * 0.58f), (int)((float)n * 0.42f), (int)((float)n * 0.58f), (int)((float)n * 0.76f));
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getDriveIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(new Color(220, 224, 230));
        graphics2D.fillRoundRect((int)((float)n * 0.12f), (int)((float)n * 0.26f), (int)((float)n * 0.76f), (int)((float)n * 0.48f), 4, 4);
        graphics2D.setColor(new Color(110, 115, 125));
        graphics2D.setStroke(new BasicStroke((float)n * 0.07f));
        graphics2D.drawRoundRect((int)((float)n * 0.12f), (int)((float)n * 0.26f), (int)((float)n * 0.76f), (int)((float)n * 0.48f), 4, 4);
        graphics2D.setColor(new Color(76, 175, 80));
        graphics2D.fillOval((int)((float)n * 0.22f), (int)((float)n * 0.46f), (int)((float)n * 0.12f), (int)((float)n * 0.12f));
        graphics2D.setColor(new Color(110, 115, 125));
        graphics2D.drawLine((int)((float)n * 0.42f), (int)((float)n * 0.52f), (int)((float)n * 0.76f), (int)((float)n * 0.52f));
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getPinIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(COLOR_TEXT_MUTED);
        graphics2D.setStroke(new BasicStroke((float)n * 0.12f, 1, 1));
        graphics2D.drawLine((int)((float)n * 0.35f), (int)((float)n * 0.35f), (int)((float)n * 0.65f), (int)((float)n * 0.65f));
        graphics2D.drawLine((int)((float)n * 0.25f), (int)((float)n * 0.45f), (int)((float)n * 0.45f), (int)((float)n * 0.25f));
        graphics2D.drawLine((int)((float)n * 0.65f), (int)((float)n * 0.65f), (int)((float)n * 0.8f), (int)((float)n * 0.8f));
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getFolderIcon(int n) {
        String string = "win11_folder_" + n;
        if (ICON_CACHE.containsKey(string)) {
            return ICON_CACHE.get(string);
        }
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int n2 = (int)((float)n * 0.08f);
        int n3 = n - n2 * 2;
        int n4 = (int)((float)n3 * 0.78f);
        int n5 = (int)((float)n * 0.16f);
        graphics2D.setColor(COLOR_FOLDER_BACK);
        graphics2D.fillRoundRect(n2, n5, (int)((float)n3 * 0.42f), (int)((float)n4 * 0.38f), 6, 6);
        graphics2D.setColor(COLOR_FOLDER_TAB);
        graphics2D.fillRoundRect(n2, n5 + (int)((float)n4 * 0.14f), n3, (int)((float)n4 * 0.86f), 8, 8);
        int n6 = n5 + (int)((float)n4 * 0.26f);
        int n7 = (int)((float)n4 * 0.74f);
        graphics2D.setColor(new Color(230, 140, 0, 70));
        graphics2D.fillRoundRect(n2 + 1, n6 + 1, n3 - 2, n7, 8, 8);
        graphics2D.setColor(COLOR_FOLDER_BODY);
        graphics2D.fillRoundRect(n2, n6, n3, n7, 8, 8);
        graphics2D.setColor(new Color(255, 255, 255, 60));
        graphics2D.fillRoundRect(n2 + 2, n6 + 2, n3 - 4, (int)((float)n7 * 0.22f), 6, 6);
        graphics2D.dispose();
        ImageIcon imageIcon = new ImageIcon(bufferedImage);
        ICON_CACHE.put(string, imageIcon);
        return imageIcon;
    }

    public static Icon getFileIcon(File file, int n) {
        if (file == null) {
            return IconUtils.getDefaultFileIcon(n);
        }
        if (file.isDirectory()) {
            return IconUtils.getFolderIcon(n);
        }
        String string = FileUtils.getFileExtension(file).toLowerCase();
        String string2 = "file_" + string + "_" + n;
        if (ICON_CACHE.containsKey(string2)) {
            return ICON_CACHE.get(string2);
        }
        Icon icon = switch (string) {
            case "png", "jpg", "jpeg", "gif", "bmp", "webp" -> IconUtils.getImageFileIcon(n);
            case "txt", "log", "md" -> IconUtils.getTextFileIcon(n);
            case "doc", "docx" -> IconUtils.getOfficeBadgeIcon(n, new Color(43, 87, 154), "W");
            case "xls", "xlsx", "csv" -> IconUtils.getOfficeBadgeIcon(n, new Color(33, 115, 70), "X");
            case "ppt", "pptx" -> IconUtils.getOfficeBadgeIcon(n, new Color(183, 71, 42), "P");
            case "pdf" -> IconUtils.getPdfFileIcon(n);
            case "mp3", "wav", "m4a", "flac" -> IconUtils.getAudioFileIcon(n);
            case "mp4", "mkv", "avi", "mov" -> IconUtils.getVideoFileIcon(n);
            case "zip", "rar", "7z", "tar", "gz" -> IconUtils.getZipFileIcon(n);
            case "java", "class", "py", "c", "cpp", "html", "css", "js", "json", "xml" -> IconUtils.getCodeFileIcon(n);
            default -> IconUtils.getDefaultFileIcon(n);
        };
        ICON_CACHE.put(string2, icon);
        return icon;
    }

    public static Icon getDefaultFileIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int n2 = (int)((float)n * 0.64f);
        int n3 = (int)((float)n * 0.82f);
        int n4 = (n - n2) / 2;
        int n5 = (n - n3) / 2;
        int n6 = (int)((float)n2 * 0.3f);
        GeneralPath generalPath = new GeneralPath();
        generalPath.moveTo(n4, n5);
        generalPath.lineTo(n4 + n2 - n6, n5);
        generalPath.lineTo(n4 + n2, n5 + n6);
        generalPath.lineTo(n4 + n2, n5 + n3);
        generalPath.lineTo(n4, n5 + n3);
        generalPath.closePath();
        graphics2D.setColor(Color.WHITE);
        graphics2D.fill(generalPath);
        graphics2D.setColor(new Color(210, 215, 225));
        graphics2D.setStroke(new BasicStroke(1.5f));
        graphics2D.draw(generalPath);
        GeneralPath generalPath2 = new GeneralPath();
        generalPath2.moveTo(n4 + n2 - n6, n5);
        generalPath2.lineTo(n4 + n2 - n6, n5 + n6);
        generalPath2.lineTo(n4 + n2, n5 + n6);
        generalPath2.closePath();
        graphics2D.setColor(new Color(230, 235, 245));
        graphics2D.fill(generalPath2);
        graphics2D.setColor(new Color(190, 195, 205));
        graphics2D.draw(generalPath2);
        graphics2D.setColor(new Color(215, 220, 230));
        graphics2D.setStroke(new BasicStroke((float)n * 0.04f, 1, 1));
        int n7 = n4 + (int)((float)n2 * 0.2f);
        int n8 = (int)((float)n2 * 0.6f);
        graphics2D.drawLine(n7, n5 + (int)((float)n3 * 0.44f), n7 + n8, n5 + (int)((float)n3 * 0.44f));
        graphics2D.drawLine(n7, n5 + (int)((float)n3 * 0.58f), n7 + n8, n5 + (int)((float)n3 * 0.58f));
        graphics2D.drawLine(n7, n5 + (int)((float)n3 * 0.72f), n7 + (int)((float)n8 * 0.6f), n5 + (int)((float)n3 * 0.72f));
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getTextFileIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int n2 = (int)((float)n * 0.64f);
        int n3 = (int)((float)n * 0.82f);
        int n4 = (n - n2) / 2;
        int n5 = (n - n3) / 2;
        int n6 = (int)((float)n2 * 0.28f);
        GeneralPath generalPath = new GeneralPath();
        generalPath.moveTo(n4, n5);
        generalPath.lineTo(n4 + n2 - n6, n5);
        generalPath.lineTo(n4 + n2, n5 + n6);
        generalPath.lineTo(n4 + n2, n5 + n3);
        generalPath.lineTo(n4, n5 + n3);
        generalPath.closePath();
        graphics2D.setColor(Color.WHITE);
        graphics2D.fill(generalPath);
        graphics2D.setColor(new Color(200, 205, 215));
        graphics2D.setStroke(new BasicStroke(1.2f));
        graphics2D.draw(generalPath);
        GeneralPath generalPath2 = new GeneralPath();
        generalPath2.moveTo(n4 + n2 - n6, n5);
        generalPath2.lineTo(n4 + n2 - n6, n5 + n6);
        generalPath2.lineTo(n4 + n2, n5 + n6);
        generalPath2.closePath();
        graphics2D.setColor(new Color(230, 235, 245));
        graphics2D.fill(generalPath2);
        graphics2D.setColor(new Color(190, 195, 205));
        graphics2D.draw(generalPath2);
        graphics2D.setColor(new Color(244, 67, 54, 180));
        graphics2D.setStroke(new BasicStroke(1.0f));
        int n7 = n4 + (int)((float)n2 * 0.24f);
        graphics2D.drawLine(n7, n5 + (int)((float)n3 * 0.15f), n7, n5 + n3 - (int)((float)n3 * 0.1f));
        graphics2D.setColor(new Color(220, 225, 235));
        int n8 = n7 + 3;
        int n9 = n4 + n2 - (int)((float)n2 * 0.14f);
        for (int i = 0; i < 5; ++i) {
            int n10 = n5 + (int)((float)n3 * (0.34f + (float)i * 0.12f));
            graphics2D.drawLine(n8, n10, n9, n10);
        }
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getImageFileIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int n2 = (int)((float)n * 0.78f);
        int n3 = (int)((float)n * 0.62f);
        int n4 = (n - n2) / 2;
        int n5 = (n - n3) / 2;
        graphics2D.setColor(new Color(235, 240, 250));
        graphics2D.fillRoundRect(n4, n5, n2, n3, 6, 6);
        graphics2D.setColor(new Color(190, 200, 215));
        graphics2D.setStroke(new BasicStroke(1.2f));
        graphics2D.drawRoundRect(n4, n5, n2, n3, 6, 6);
        graphics2D.setColor(new Color(100, 181, 246));
        graphics2D.fillRoundRect(n4 + 3, n5 + 3, n2 - 6, n3 - 6, 4, 4);
        graphics2D.setColor(new Color(255, 238, 88));
        graphics2D.fillOval(n4 + (int)((float)n2 * 0.68f), n5 + (int)((float)n3 * 0.18f), (int)((float)n2 * 0.18f), (int)((float)n2 * 0.18f));
        Polygon polygon = new Polygon();
        polygon.addPoint(n4 + 3, n5 + n3 - 3);
        polygon.addPoint(n4 + (int)((float)n2 * 0.38f), n5 + (int)((float)n3 * 0.38f));
        polygon.addPoint(n4 + (int)((float)n2 * 0.72f), n5 + n3 - 3);
        graphics2D.setColor(new Color(76, 175, 80));
        graphics2D.fillPolygon(polygon);
        Polygon polygon2 = new Polygon();
        polygon2.addPoint(n4 + (int)((float)n2 * 0.44f), n5 + n3 - 3);
        polygon2.addPoint(n4 + (int)((float)n2 * 0.74f), n5 + (int)((float)n3 * 0.48f));
        polygon2.addPoint(n4 + n2 - 3, n5 + n3 - 3);
        graphics2D.setColor(new Color(56, 142, 60));
        graphics2D.fillPolygon(polygon2);
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getOfficeBadgeIcon(int n, Color color, String string) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int n2 = (int)((float)n * 0.64f);
        int n3 = (int)((float)n * 0.82f);
        int n4 = (n - n2) / 2;
        int n5 = (n - n3) / 2;
        int n6 = (int)((float)n2 * 0.28f);
        GeneralPath generalPath = new GeneralPath();
        generalPath.moveTo(n4, n5);
        generalPath.lineTo(n4 + n2 - n6, n5);
        generalPath.lineTo(n4 + n2, n5 + n6);
        generalPath.lineTo(n4 + n2, n5 + n3);
        generalPath.lineTo(n4, n5 + n3);
        generalPath.closePath();
        graphics2D.setColor(Color.WHITE);
        graphics2D.fill(generalPath);
        graphics2D.setColor(new Color(205, 210, 220));
        graphics2D.setStroke(new BasicStroke(1.2f));
        graphics2D.draw(generalPath);
        GeneralPath generalPath2 = new GeneralPath();
        generalPath2.moveTo(n4 + n2 - n6, n5);
        generalPath2.lineTo(n4 + n2 - n6, n5 + n6);
        generalPath2.lineTo(n4 + n2, n5 + n6);
        generalPath2.closePath();
        graphics2D.setColor(new Color(230, 235, 245));
        graphics2D.fill(generalPath2);
        int n7 = (int)((float)n2 * 0.72f);
        int n8 = (int)((float)n3 * 0.44f);
        int n9 = n4 + (n2 - n7) / 2;
        int n10 = n5 + (int)((float)n3 * 0.38f);
        graphics2D.setColor(color);
        graphics2D.fillRoundRect(n9, n10, n7, n8, 4, 4);
        graphics2D.setColor(Color.WHITE);
        int n11 = Math.max(10, (int)((float)n8 * 0.72f));
        graphics2D.setFont(new Font("Segoe UI", 1, n11));
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        int n12 = n9 + (n7 - fontMetrics.stringWidth(string)) / 2;
        int n13 = n10 + (n8 - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();
        graphics2D.drawString(string, n12, n13);
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getPdfFileIcon(int n) {
        return IconUtils.getOfficeBadgeIcon(n, new Color(211, 47, 47), "PDF");
    }

    public static Icon getAudioFileIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int n2 = (int)((float)n * 0.64f);
        int n3 = (int)((float)n * 0.82f);
        int n4 = (n - n2) / 2;
        int n5 = (n - n3) / 2;
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRoundRect(n4, n5, n2, n3, 6, 6);
        graphics2D.setColor(new Color(205, 210, 220));
        graphics2D.setStroke(new BasicStroke(1.2f));
        graphics2D.drawRoundRect(n4, n5, n2, n3, 6, 6);
        graphics2D.setColor(COLOR_ICON_DARK);
        int n6 = n4 + (int)((float)n2 * 0.28f);
        int n7 = n5 + (int)((float)n3 * 0.6f);
        int n8 = (int)((float)n2 * 0.22f);
        graphics2D.fillOval(n6, n7, n8, (int)((float)n8 * 0.8f));
        graphics2D.fillRect(n6 + n8 - 3, n5 + (int)((float)n3 * 0.3f), 3, (int)((float)n3 * 0.38f));
        graphics2D.fillRect(n6 + n8 - 3, n5 + (int)((float)n3 * 0.28f), (int)((float)n2 * 0.26f), (int)((float)n3 * 0.12f));
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getVideoFileIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int n2 = (int)((float)n * 0.76f);
        int n3 = (int)((float)n * 0.64f);
        int n4 = (n - n2) / 2;
        int n5 = (n - n3) / 2;
        graphics2D.setColor(new Color(45, 52, 64));
        graphics2D.fillRoundRect(n4, n5, n2, n3, 6, 6);
        Polygon polygon = new Polygon();
        polygon.addPoint(n4 + (int)((float)n2 * 0.4f), n5 + (int)((float)n3 * 0.3f));
        polygon.addPoint(n4 + (int)((float)n2 * 0.7f), n5 + n3 / 2);
        polygon.addPoint(n4 + (int)((float)n2 * 0.4f), n5 + (int)((float)n3 * 0.7f));
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillPolygon(polygon);
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getZipFileIcon(int n) {
        Icon icon = IconUtils.getFolderIcon(n);
        BufferedImage bufferedImage = new BufferedImage(n, n, 2);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        icon.paintIcon(null, graphics2D, 0, 0);
        int n2 = n / 2;
        graphics2D.setColor(new Color(245, 245, 245));
        graphics2D.fillRect(n2 - 3, (int)((float)n * 0.3f), 6, (int)((float)n * 0.48f));
        graphics2D.setColor(new Color(80, 80, 80));
        for (int i = 0; i < 4; ++i) {
            int n3 = (int)((float)n * (0.34f + (float)i * 0.1f));
            graphics2D.fillRect(n2 - 4, n3, 8, 2);
        }
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static Icon getCodeFileIcon(int n) {
        BufferedImage bufferedImage = IconUtils.createBuffer(n, n);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int n2 = (int)((float)n * 0.64f);
        int n3 = (int)((float)n * 0.82f);
        int n4 = (n - n2) / 2;
        int n5 = (n - n3) / 2;
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRoundRect(n4, n5, n2, n3, 6, 6);
        graphics2D.setColor(new Color(205, 210, 220));
        graphics2D.setStroke(new BasicStroke(1.2f));
        graphics2D.drawRoundRect(n4, n5, n2, n3, 6, 6);
        graphics2D.setColor(COLOR_BLUE_ACCENT);
        graphics2D.setFont(new Font("Consolas", 1, (int)((float)n2 * 0.38f)));
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        String string = "</>";
        int n6 = n4 + (n2 - fontMetrics.stringWidth(string)) / 2;
        int n7 = n5 + (n3 - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();
        graphics2D.drawString(string, n6, n7);
        graphics2D.dispose();
        return new ImageIcon(bufferedImage);
    }
}

