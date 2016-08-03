package it.webred.util.common;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.swing.ImageIcon;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfWriter;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.JPEGEncodeParam;
import com.sun.media.jai.codec.TIFFDecodeParam;

public class TiffUtill {
	
	public static final int FORMATO_A3 = 3;
	public static final int FORMATO_A4 = 4;
	public static final int FORMATO_DEF = 4;
	
    public static ByteArrayOutputStream jpgsTopdf(List<ByteArrayOutputStream> jpgs, boolean hasHeaderFooter, int formato) {

    	switch(formato) {
	    	case FORMATO_A3:
	    		break;
	    	case FORMATO_A4:
	    		break;
	    	default:
	    		formato = FORMATO_DEF;
	    		break;
	    }
    	
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	try {
	 	    Document document = null;
	 	    
	 	    switch(formato) {
		    	case FORMATO_A3:
		    		document = new Document(PageSize.A3);
		    		break;
		    	case FORMATO_A4:
		    		document = new Document(PageSize.A4);
		    		break;
		    	default:
		    		document = new Document(PageSize.A4);
		    		break;
		    }
	 	    
		    PdfWriter writer = PdfCopy.getInstance(document, baos); 
	      	writer.setPageEmpty(false);

		    if (hasHeaderFooter) {
		    	com.lowagie.text.Font fnt = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, com.lowagie.text.Font.NORMAL); 
		      	HeaderFooter header = new HeaderFooter(new Phrase(4, "Riproduzione di documento originale", fnt), false);
		 	    header.setAlignment(Element.ALIGN_CENTER);
		 	    header.setBorderWidth(0);
		 	    document.setHeader(header);
		 	    HeaderFooter footer = new HeaderFooter(new Phrase(4, "Pag. ", fnt), new Phrase(4, "/" + jpgs.size(), fnt) );
		 	    footer.setAlignment(Element.ALIGN_CENTER);
		 	    footer.setBorderWidth(0);
		 	    document.setFooter(footer);      	
		      	document.setMargins(14F, 14F, 14F, 14F);
		    } else {
		    	document.setMargins(0F, 0F, 0F, 0F);
		    }
	 	    
	 	    document.open();
		    //document.setPageCount(jpgs.size());
		    
			Iterator itjpgs = jpgs.iterator();
			while (itjpgs.hasNext()) {
				ByteArrayOutputStream jpg = (ByteArrayOutputStream)itjpgs.next();
				document.newPage();
		        com.lowagie.text.Image ixImg = com.lowagie.text.Image.getInstance(jpg.toByteArray());
		        float width, height;
		        switch(formato) {
			    	case FORMATO_A3:
			    		width = PageSize.A3.width();
			    		height = PageSize.A3.height();
			    		break;
			    	case FORMATO_A4:
			    		width = PageSize.A4.width();
			    		height = PageSize.A4.height();
			    		break;
			    	default:
			    		width = PageSize.A4.width();
		    			height = PageSize.A4.height();
		    			break;
			    }
		        if (hasHeaderFooter) {		        	
			        ixImg.scaleToFit(width - 50F, height - 28F);
		        } else {
		        	ixImg.scaleToFit(width, height);
		        }		
		        document.add(ixImg);
			}
	        document.close();
        	
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}   
		return baos;
    }
    
 
    
    
    /**
     * Dato un inputstream contenente un TIFF restituisce una lista di immagini
     * una per ogni pagina componente il TIFF
     * @param is
     * @return
     */
    public static  List<ByteArrayOutputStream> tiffToJpeg(InputStream is) {
		TIFFDecodeParam param = null;

		List<ByteArrayOutputStream> images = new ArrayList<ByteArrayOutputStream>();
		try {
					ImageDecoder dec = ImageCodec.createImageDecoder("tiff", is, param);
					
					for (int i=0; i <= dec.getNumPages()-1; i++) {
						RenderedImage op1;
						op1 =dec.decodeAsRenderedImage(i);
						
					            
						JPEGEncodeParam jparam = new JPEGEncodeParam();
						
						ByteArrayOutputStream outByte =  new ByteArrayOutputStream();
						ImageEncoder encoder = ImageCodec.createImageEncoder("JPEG", outByte, jparam); 
						encoder.encode(op1);
						ByteArrayOutputStream imageWithWatermark = waterMark(outByte);
						images.add(imageWithWatermark);
					}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return images;
    }//-------------------------------------------------------------------------
    
    private static ByteArrayOutputStream waterMark(ByteArrayOutputStream out) throws Exception {
		//byte[] imagedata = null;
		//imagedata = out.toByteArray();
		

		
		
		ImageIcon photo = new ImageIcon(out.toByteArray());

//		Create an image 200 x 200
        BufferedImage bufferedImage = new BufferedImage(photo.getIconWidth(),
                photo.getIconHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();

        g2d.drawImage(photo.getImage(), 0, 0, null);

        //Create an alpha composite of 50%
        AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 
                                                          0.5f);
        g2d.setComposite(alpha);

        g2d.setColor(Color.RED);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setFont(new Font("Arial", Font.BOLD, 50));

        String watermark = "DOCUMENTO AD ESCLUSIVO USO INTERNO";

        FontMetrics fontMetrics = g2d.getFontMetrics();
        Rectangle2D rect = fontMetrics.getStringBounds(watermark, g2d);

        g2d.drawString(watermark,
                        (photo.getIconWidth() - (int) rect.getWidth()) / 2,
                        (photo.getIconHeight() - (int) rect.getHeight()) / 2);

        //Free graphic resources
        g2d.dispose();
        

		ByteArrayOutputStream out2 = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "jpg",out2 );					

	    //byte[] compressedData = out2.toByteArray();
	    //InputStream is = new ByteArrayInputStream(compressedData);


	    
	    
	    
	    
		return out2;
    }
    
    private BufferedImage toBufferedImage(Image image)
    {
        image = new ImageIcon(image).getImage();
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null)
        ,image.getHeight(null),BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0,0,image.getWidth(null),image.getHeight(null));
        g.drawImage(image,0,0,null);
        g.dispose();

        return bufferedImage;
    }    
    
    /*
     * Metodi per l'inserimento di un immagine nel documento allegato, NON usati
     * 
     * 
     */
    /* 
    public static byte[] convertImage(Image img) {
    		    ParameterBlock pb = new ParameterBlock();
    		    pb.add(img) ;
    		    PlanarImage image = JAI.create("awtImage", pb,null);
    		 
    		    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    	
    		    JAI.create("encode", image, stream, "tiff");
    		    byte[] b = stream.toByteArray();
    		    return b;
    		}
    
    public static byte[] readFile(String path){
    	
    	 String strFileExt="jpg";
    	
    	 try {
    	 ImageIcon objImageIcon = new ImageIcon(path);
    	
    	 BufferedImage objBI = new BufferedImage(objImageIcon.getIconWidth(), objImageIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
    	 Graphics2D g2 = objBI.createGraphics();
    	 g2.drawImage(objImageIcon.getImage(), 0, 0, null);
    	 ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	 ImageIO.write(objBI, strFileExt, baos);
    	
    	 return baos.toByteArray();
    	
    	 } catch (IOException ex) {
    	 ex.printStackTrace();
    	 return null;
    	 }
    	 }
    	 
    	 */
}
