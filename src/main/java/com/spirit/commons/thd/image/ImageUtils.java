package com.spirit.commons.thd.image;

import static java.util.Arrays.asList;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * com.spirit.commons.thd.image class
 *
 * @author guoxiong
 * @date 2017/12/28 20:43
 */
public class ImageUtils {

    private static TextModel NAME_TEXT_MODEL;
    private static TextModel MONTH_TEXT_MODEL;
    private static TextModel DAY_TEXT_MODEL;
    private static TextModel YEAR_TEXT_MODEL;
    private static TextModel POST_COUNT_TEXT_MODEL;
    private static TextModel LIKE_COUNT_TEXT_MODEL;
    private static TextModel FOLLOW_COUNT_TEXT_MODEL;
    private static TextModel RECEIVED_LIKE_COUNT_TEXT_MODEL;
    private static TextModel FANS_COUNT_TEXT_MODEL;
    private static Color NAME_COLOR;
    private static Color NUMBER_COLOR;

    private static final Font NAME_FONT = new Font("黑体", Font.BOLD, 32);
    private static final Font DATE_FONT = new Font("微软雅黑", Font.BOLD, 32);
    private static final Font NUMBER_FONT = new Font("微软雅黑", Font.BOLD, 42);
    private static final List<Integer> NAME_COLOR_LIST = asList(0, 0, 0);
    private static final List<Integer> NUMBER_COLOR_LIST = asList(255, 45, 122);

    // init text model
    static {
        NAME_TEXT_MODEL = new TextModel(127, 101, 98, 38);
        MONTH_TEXT_MODEL = new TextModel(65.5f, 168, 35, 38);
        DAY_TEXT_MODEL = new TextModel(131, 168, 35, 38);
        YEAR_TEXT_MODEL = new TextModel(198, 168, 70, 38);
        LIKE_COUNT_TEXT_MODEL = new TextModel(486, 444, 136, 50);
        FOLLOW_COUNT_TEXT_MODEL = new TextModel(359, 577, 136, 50);

        POST_COUNT_TEXT_MODEL = new TextModel(484, 714, 136, 50);
        RECEIVED_LIKE_COUNT_TEXT_MODEL = new TextModel(349, 856, 136, 50);
        FANS_COUNT_TEXT_MODEL = new TextModel(355, 984, 136, 50);

        NAME_COLOR = new Color(NAME_COLOR_LIST.get(0), NAME_COLOR_LIST.get(1), NAME_COLOR_LIST.get(2), 255);
        NUMBER_COLOR = new Color(NUMBER_COLOR_LIST.get(0), NUMBER_COLOR_LIST.get(1), NUMBER_COLOR_LIST.get(2), 255);
    }

    private ImageUtils() {

    }

    public static void drawTextOnImage(Map<String, Object> info) {
        boolean isPost = false;
        String filePath = "";
        String tarImgPath = "/Users/liuguoxiong/Ideaprojects/test.jpg";
        if (info.containsKey("post_count")) {
            isPost = true;
            filePath = "/Users/liuguoxiong/Ideaprojects/bg_ed.png";
        } else {
            filePath = "/Users/liuguoxiong/Ideaprojects/bg_zero.png";
        }

        LocalDate localDate = LocalDate.now();
        try {
            // 读取原图片信息
            File srcImgFile = new File(filePath);//得到文件
            Image srcImg = ImageIO.read(srcImgFile);//文件转化为图片
            int srcImgWidth = srcImg.getWidth(null);//获取图片的宽
            int srcImgHeight = srcImg.getHeight(null);//获取图片的高

            // 加水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();

            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            AttributedString ats;
            // name
            int x;
            int y;
            g.setColor(NAME_COLOR); //根据图片的背景设置水印颜色
            g.setFont(NAME_FONT); //设置字体
            ats  = new AttributedString((String) info.get("user_name"));
            // 消除java.awt.Font字体的锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            ats.addAttribute(TextAttribute.FONT, NAME_FONT, 0, ((String) info.get("user_name")).length());
//            AttributedCharacterIterator iter = ats.getIterator();
//            g.drawString(iter, NAME_TEXT_MODEL.getStartX(), NAME_TEXT_MODEL.getStartY() + NAME_TEXT_MODEL.getHeight());

//            g.drawString((String) info.get("user_name"), NAME_TEXT_MODEL.getStartX(), NAME_TEXT_MODEL.getStartY() + NAME_TEXT_MODEL.getHeight());
            // 尝试居中
            FontMetrics metrics = g.getFontMetrics(NAME_FONT);
            x = (int) NAME_TEXT_MODEL.getStartX() + (NAME_TEXT_MODEL.getWidth() - metrics.stringWidth((String)info.get("user_name")))/2;
            y = (int) NAME_TEXT_MODEL.getStartY() + ((NAME_TEXT_MODEL.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
            g.drawString((String) info.get("user_name"), x, y);

            // month, day, year
            g.setColor(NUMBER_COLOR);
            g.setFont(DATE_FONT); //设置字体
            g.drawString(String.valueOf(localDate.getMonthValue()), MONTH_TEXT_MODEL.getStartX(), MONTH_TEXT_MODEL.getStartY() + MONTH_TEXT_MODEL.getHeight());
            g.drawString(String.valueOf(localDate.getDayOfMonth()), DAY_TEXT_MODEL.getStartX(), DAY_TEXT_MODEL.getStartY() + DAY_TEXT_MODEL.getHeight());
            g.drawString(String.valueOf(localDate.getYear()), YEAR_TEXT_MODEL.getStartX(), YEAR_TEXT_MODEL.getStartY() + YEAR_TEXT_MODEL.getHeight());

            // like_count, follow_count
            g.setColor(NUMBER_COLOR);
            g.setFont(NUMBER_FONT); //设置字体
//            g.drawString(String.valueOf(info.get("user_like_count")), LIKE_COUNT_TEXT_MODEL.getStartX(), LIKE_COUNT_TEXT_MODEL.getStartY() + LIKE_COUNT_TEXT_MODEL.getHeight());
//            g.drawString(String.valueOf(info.get("user_follow_count")), FOLLOW_COUNT_TEXT_MODEL.getStartX(), FOLLOW_COUNT_TEXT_MODEL.getStartY() + FOLLOW_COUNT_TEXT_MODEL.getHeight());
            metrics = g.getFontMetrics(NUMBER_FONT);
            x = (int) LIKE_COUNT_TEXT_MODEL.getStartX() + (LIKE_COUNT_TEXT_MODEL.getWidth() - metrics.stringWidth(String.valueOf(info.get("user_like_count"))))/2;
            y = (int) LIKE_COUNT_TEXT_MODEL.getStartY() + ((LIKE_COUNT_TEXT_MODEL.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
            g.drawString(String.valueOf(info.get("user_like_count")), x, y);
            metrics = g.getFontMetrics(NUMBER_FONT);
            x = (int) FOLLOW_COUNT_TEXT_MODEL.getStartX() + (FOLLOW_COUNT_TEXT_MODEL.getWidth() - metrics.stringWidth(String.valueOf(info.get("user_follow_count"))))/2;
            y = (int) FOLLOW_COUNT_TEXT_MODEL.getStartY() + ((FOLLOW_COUNT_TEXT_MODEL.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
            g.drawString(String.valueOf(info.get("user_follow_count")), x, y);

            g.dispose();
            // 输出图片
            FileOutputStream outImgStream = new FileOutputStream(tarImgPath);
            ImageIO.write(bufImg, "jpg", outImgStream);
            System.out.println("添加水印完成");
            outImgStream.flush();
            outImgStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main (String[] args) {
        Map<String, Object> info = new HashMap<>();
        info.put("user_name", "Test");
        info.put("user_like_count", 800);
        info.put("user_follow_count", 20);

        drawTextOnImage(info);
    }
}
