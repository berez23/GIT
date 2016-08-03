package it.webred.ct.service.comma336.web.util;

import it.webred.ct.service.comma336.web.util.TiffUtilException;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.poi.util.IOUtils;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfWriter;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.TIFFDecodeParam;

public class TiffUtil {
	public static final int FORMATO_A3 = 3;
	public static final int FORMATO_A4 = 4;
	public static final int FORMATO_DEF = 4;
	
    public static  ByteArrayOutputStream jpgsTopdf(List<ByteArrayOutputStream> jpgs, boolean hasHeaderFooter, int formato) {

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
		    
			Iterator<ByteArrayOutputStream> itjpgs = jpgs.iterator();
			while (itjpgs.hasNext()) {
			  ByteArrayOutputStream jpg = (ByteArrayOutputStream)itjpgs.next();
			  document.newPage();
	          com.lowagie.text.Image ixImg = com.lowagie.text.Image.getInstance(jpg.toByteArray());
	          float width, height;
		      switch(formato) {
		      	case FORMATO_A3:
		    		width = PageSize.A3.getWidth();
		    		height = PageSize.A3.getHeight();
		    		break;
		      	case FORMATO_A4:
		    		width = PageSize.A4.getWidth();
		    		height = PageSize.A4.getHeight();
		    		break;
		      	default:
		    		width = PageSize.A4.getWidth();
	    			height = PageSize.A4.getHeight();
	    			break;
			  }
		      if (hasHeaderFooter) {		        	
			        ixImg.scaleToFit(width - 50F, height - 28F);
		      } else {
		    	  	//ixImg.scaleToFit(width, height);
		    	    //in BOD è così, ma in Diogene crea un PDF con due pagine!!! (la seconda è vuota)
		    	    //sembra assurdo, ma con - 0.001 questo non succede...
		        	ixImg.scaleToFit(width, height - 0.001F);
		      }	
	          document.add(ixImg);	
			}
	        document.close();
		} catch (DocumentException e) {
			throw new TiffUtilException(e);
		} catch (Exception e) {
			throw new TiffUtilException(e);
		}
		return baos;
    }
    
    /**
     * Dato un inputstream contenente un TIFF restituisce una lista di immagini
     * una per ogni pagina componente il TIFF
     * @param is
     * @return
     */
    public static  List<ByteArrayOutputStream> tiffToJpeg(InputStream is, boolean watermark, boolean openJpg) {
		//TIFFDecodeParam param = null;
		TIFFDecodeParam param = new TIFFDecodeParam();
		param.setDecodePaletteAsShorts(true);

		List<ByteArrayOutputStream> images = new ArrayList<ByteArrayOutputStream>();
		try {
			
			//se non devo mettere il watermark e sto cercando di aprire l'immagine (non il pdf) restituisco l'immagine come è
			if (!watermark && openJpg){
				
				ByteArrayOutputStream imageWithoutWatermark=new ByteArrayOutputStream();
				byte[] arrByte= IOUtils.toByteArray(is);
				
				imageWithoutWatermark.write(arrByte);
				imageWithoutWatermark.close();
				
				images.add(imageWithoutWatermark);
				
				    return images;
			} 
			
			//se devo mettere il watermark oppure se devo visualizzare il Pdf  trasformo in formato jpeg			
			ImageDecoder dec = ImageCodec.createImageDecoder("tiff", is, param);

			for (int i=0; i <= dec.getNumPages()-1; i++) {
				RenderedImage op1;
				op1 = dec.decodeAsRenderedImage(i);
				ByteArrayOutputStream outByte =  new ByteArrayOutputStream();
				ImageIO.write(op1, "jpg", outByte);
				BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(outByte.toByteArray()));
				outByte = new ByteArrayOutputStream();
	            JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(outByte);
	            JPEGEncodeParam jpegEncodeParam = jpegEncoder.getDefaultJPEGEncodeParam(bufferedImage);
	            jpegEncodeParam.setDensityUnit(JPEGEncodeParam.DENSITY_UNIT_DOTS_INCH);	            
	            jpegEncodeParam.setQuality(0.75f, false);
	            jpegEncodeParam.setXDensity(200);
	            jpegEncodeParam.setYDensity(200);
	            jpegEncoder.setJPEGEncodeParam(jpegEncodeParam);
	            jpegEncoder.encode(bufferedImage, jpegEncodeParam);
				if (watermark){
					ByteArrayOutputStream imageWithWatermark=null;
					imageWithWatermark = waterMark(outByte);
					images.add(imageWithWatermark);
				}
				else{
					ByteArrayOutputStream imageWithoutWatermark=null;
					imageWithoutWatermark = outByte;
					images.add(imageWithoutWatermark);
				}
						
			}

			return images;
		} catch (Exception e) {
			throw new TiffUtilException("Errore nel reperimento / conversione dell'immagine");
		}
    }
    
    private static ByteArrayOutputStream waterMark(ByteArrayOutputStream out) throws Exception {
		
    	ImageIcon photo = new ImageIcon(out.toByteArray());

    	//Create an image 200 x 200
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
		JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(out2);
        JPEGEncodeParam jpegEncodeParam = jpegEncoder.getDefaultJPEGEncodeParam(bufferedImage);
        jpegEncodeParam.setDensityUnit(JPEGEncodeParam.DENSITY_UNIT_DOTS_INCH);	            
        jpegEncodeParam.setQuality(0.75f, false);
        jpegEncodeParam.setXDensity(200);
        jpegEncodeParam.setYDensity(200);
        jpegEncoder.setJPEGEncodeParam(jpegEncodeParam);
        jpegEncoder.encode(bufferedImage, jpegEncodeParam);
		ImageIO.write(bufferedImage, "jpg", out2);

		return out2;
    }
    
    public static  List<ByteArrayOutputStream> tiffToJpegDiogene(InputStream is, boolean watermark, boolean openJpg) {
		//TIFFDecodeParam param = null;
		TIFFDecodeParam param = new TIFFDecodeParam();
		param.setDecodePaletteAsShorts(true);

		List<ByteArrayOutputStream> images = new ArrayList<ByteArrayOutputStream>();
		try {
			
			while (Runtime.getRuntime().freeMemory() < 20000000) {
				Runtime.getRuntime().runFinalization();
				Runtime.getRuntime().gc();
			}
			//System.out.println("MEMORIA LIBERA PRIMA, KB: " + Runtime.getRuntime().freeMemory() / 1024);
			//se non devo mettere il watermark e sto cercando di aprire l'immagine (non il pdf) restituisco l'immagine come è
			if (!watermark && openJpg){
				
				ByteArrayOutputStream imageWithoutWatermark=new ByteArrayOutputStream();
				byte[] arrByte= IOUtils.toByteArray(is);
				
				imageWithoutWatermark.write(arrByte);
				imageWithoutWatermark.close();
				
				images.add(imageWithoutWatermark);
				
				    return images;
			} 
			
			//se devo mettere il watermark oppure se devo visualizzare il Pdf  trasformo in formato jpeg			
			ImageDecoder dec = ImageCodec.createImageDecoder("tiff", is, param);

			for (int i=0; i <= dec.getNumPages()-1; i++) {
				RenderedImage op1;
				op1 = dec.decodeAsRenderedImage(i);
				ByteArrayOutputStream outByte =  new ByteArrayOutputStream();
				ImageIO.write(op1, "jpg", outByte);
				BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(outByte.toByteArray()));
				outByte = new ByteArrayOutputStream();
	            JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(outByte);
	            JPEGEncodeParam jpegEncodeParam = jpegEncoder.getDefaultJPEGEncodeParam(bufferedImage);
	            jpegEncodeParam.setDensityUnit(JPEGEncodeParam.DENSITY_UNIT_DOTS_INCH);	            
	            jpegEncodeParam.setQuality(0.75f, false);
	            jpegEncodeParam.setXDensity(200);
	            jpegEncodeParam.setYDensity(200);
	            jpegEncoder.setJPEGEncodeParam(jpegEncodeParam);
	            jpegEncoder.encode(bufferedImage, jpegEncodeParam);
				if (watermark){
					ByteArrayOutputStream imageWithWatermark=null;
					while (Runtime.getRuntime().freeMemory() < 20000000) {
						Runtime.getRuntime().runFinalization();
						Runtime.getRuntime().gc();
					}
					//System.out.println("MEMORIA LIBERA PRIMA DI WATERMARK, KB: " + Runtime.getRuntime().freeMemory() / 1024);
					imageWithWatermark = waterMark(outByte);
					images.add(imageWithWatermark);
				}
				else{
					ByteArrayOutputStream imageWithoutWatermark=null;
					imageWithoutWatermark = outByte;
					images.add(imageWithoutWatermark);
				}
						
			}

			while (Runtime.getRuntime().freeMemory() < 20000000) {
				Runtime.getRuntime().runFinalization();
				Runtime.getRuntime().gc();
			}
			//System.out.println("MEMORIA LIBERA ALLA FINE, KB: " + Runtime.getRuntime().freeMemory() / 1024);
			return images;
		} catch (Exception e) {
			throw new TiffUtilException("Errore nel reperimento / conversione dell'immagine");
		}
    }

}
