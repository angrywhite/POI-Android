package com.example.poipptxnew;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.pbdavey.awt.Graphics2D;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import and.awt.BufferedImage;
import and.awt.Color;
import and.awt.Dimension;
import and.awt.ImageIO;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

public class MainActivity extends Activity {

	ImageView mImageView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
		System.setProperty("javax.xml.stream.XMLInputFactory",
				"com.sun.xml.stream.ZephyrParserFactory");
		System.setProperty("javax.xml.stream.XMLOutputFactory",
				"com.sun.xml.stream.ZephyrWriterFactory");
		System.setProperty("javax.xml.stream.XMLEventFactory",
				"com.sun.xml.stream.events.ZephyrEventFactory");
		
		Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
    
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mImageView = (ImageView) findViewById(R.id.image);

		Logger.getLogger("org.teleal.cling").setLevel(Level.FINEST);

		try {
			pptx2png();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private void pptx2png() throws InvalidFormatException {
    	String file = "/sdcard/PhoneGap.pptx";
		int scale = 1;
		int slidenum = -1;
		
		System.out.println("Processing " + file);
        XMLSlideShow ppt = new XMLSlideShow(OPCPackage.open(file));

        Dimension pgsize = ppt.getPageSize();
        int width = (int) (pgsize.width * scale);
        int height = (int) (pgsize.height * scale);

        XSLFSlide[] slide = ppt.getSlides();
        for (int i = 0; i < slide.length; i++) {
            if (slidenum != -1 && slidenum != (i + 1)) continue;

            String title = slide[i].getTitle();
            System.out.println("Rendering slide " + (i + 1) + (title == null ? "" : ": " + title));

            Bitmap bmp = Bitmap.createBitmap((int) pgsize.getWidth(),
					(int) pgsize.getHeight(), Config.RGB_565);
			Canvas canvas = new Canvas(bmp);
			Paint paint = new Paint();
			paint.setColor(android.graphics.Color.WHITE);
			paint.setFlags(Paint.ANTI_ALIAS_FLAG);
			canvas.drawPaint(paint);

			Graphics2D graphics2d = new Graphics2D(canvas);
            
//            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//            Graphics2D graphics = img.createGraphics();

            // default rendering options
//            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

//            graphics.setColor(Color.white);
//            graphics.clearRect(0, 0, width, height);
//
//            graphics.scale(scale, scale);

            // draw stuff
            slide[i].draw(graphics2d);

//            // save the result
//            int sep = file.lastIndexOf(".");
//            String fname = file.substring(0, sep == -1 ? file.length() : sep) + "-" + (i + 1) +".png";
//            FileOutputStream out = new FileOutputStream(fname);
//            ImageIO.write(img, "png", out);
//            out.close();
            mImageView.setImageBitmap(bmp);
        }
        System.out.println("Done");
    }
}
