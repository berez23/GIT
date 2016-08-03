package it.escsolution.escwebgis.common;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.media.jai.NullOpImage;
import javax.media.jai.OpImage;
import javax.media.jai.PlanarImage;
import javax.swing.ImageIcon;

import org.apache.poi.util.IOUtils;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.TIFFDecodeParam;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;


public class TiffUtill {
	
	public static final int FORMATO_A3 = 3;
	public static final int FORMATO_A4 = 4;
	public static final int FORMATO_DEF = 4;
	
	public static final boolean IGNORA_FORMATO = true;
	//tolleranza in pixel
	public static final float TOL_WIDTH = 10;
	public static final float TOL_HEIGTH = 10;
	
	public static final int DEF_DPI = 72;
	
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
    		
    		boolean rotate = false;
    		float docWidth = 0;
    		float docHeight = 0;
    		float imgWidth = 0;
    		float imgHeight = 0;
    		float cropWidth = 0;
    		float cropHeight = 0;
    		
    		Iterator itjpgs = jpgs.iterator();
			while (itjpgs.hasNext()) {
			  ByteArrayOutputStream jpg = (ByteArrayOutputStream)itjpgs.next();
	          com.lowagie.text.Image ixImg = com.lowagie.text.Image.getInstance(jpg.toByteArray());
	          imgWidth = ixImg.width() * DEF_DPI / ixImg.getDpiX();
	          imgHeight = ixImg.height() * DEF_DPI / ixImg.getDpiY();
	          if (imgHeight < imgWidth) {
	        	  rotate = true;
	          }	                    
	          if (imgWidth > docWidth) {
	        	  docWidth = imgWidth;
	          }
	          if (imgHeight > docHeight) {
	        	  docHeight = imgHeight;
	          }
			}
	 	    
	 	    if (IGNORA_FORMATO) {
	 	    	if (hasHeaderFooter) {
	 	    		docWidth += 14F;
	 	    		docHeight += 14F;
	 	    	}
 	    		
	 	    	Rectangle[] dimensioni = new Rectangle[] {PageSize.A0, PageSize.A1, PageSize.A2, PageSize.A3, PageSize.A4};
	 	    	if (rotate) {
	 	    		dimensioni = new Rectangle[] {PageSize.A0.rotate(), PageSize.A1.rotate(), PageSize.A2.rotate(), PageSize.A3.rotate(), PageSize.A4.rotate()};
	 	    	}
	 	    	Rectangle dimensione = null;
	 	    	for (Rectangle myDimensione : dimensioni) {
	 	    		if (docWidth > (myDimensione.width() + TOL_WIDTH) || docHeight > (myDimensione.height() + TOL_HEIGTH)) {
	 	    			if (dimensione == null) {
	 	    				//nel caso in cui sia più grande anche di A0...
	 	    				document = new Document(new Rectangle(docWidth, docHeight));
	 	    			} else {
	 	    				document = new Document(dimensione);
	 	    				if (docWidth >= dimensione.width()) {
	 	    					cropWidth = dimensione.width();
	 	    				}
	 	    				if (docHeight >= dimensione.height()) {
	 	    					cropHeight = dimensione.height();
	 	    				}
	 	    			}
	 	    			dimensione = null;
	 	    			break;
	 	    		}
	 	    		dimensione = myDimensione;
	 	    	}
 	    		if (dimensione != null) {
 	    			//la dimensione è la più piccola prevista (A4)
 	    			document = new Document(dimensione);
 	    			if (docWidth >= dimensione.width()) {
	    				cropWidth = dimensione.width();
    				}
    				if (docHeight >= dimensione.height()) {
    					cropHeight = dimensione.height();
    				}
 	    		}	 	    	
	 	    } else {
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
	 	    }			
	 	    
	 	    if (rotate && !IGNORA_FORMATO) {
	 	    	//orizzontale; se IGNORA_FORMATO = true, lo ha già fatto automaticamente
	 	    	document.setPageSize(document.getPageSize().rotate());
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
		    
			itjpgs = jpgs.iterator();
			while (itjpgs.hasNext()) {
			  ByteArrayOutputStream jpg = (ByteArrayOutputStream)itjpgs.next();
			  document.newPage();
	          com.lowagie.text.Image ixImg = com.lowagie.text.Image.getInstance(jpg.toByteArray());
	          
	          float width, height;
		      /*switch(formato) {
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
			  }*/
	          /*width = document.getPageSize().width();
	          height =  document.getPageSize().height();*/
	          width = ixImg.width() * DEF_DPI / ixImg.getDpiX();
	          height = ixImg.height() * DEF_DPI / ixImg.getDpiY();
	          
		      if (hasHeaderFooter) {		        	
			        ixImg.scaleToFit(width - 50F, height - 28F);
		      } else {		    	  
		    	  if (cropWidth > 0 || cropHeight > 0) {
		    		  //sembra assurdo, ma senza - 0.001F vengono create due pagine (la seconda vuota)...
			    	  if (cropWidth > 0) {
			    		  width = cropWidth - 0.001F;
			    	  }
			    	  if (cropHeight > 0) {
			    		  height = cropHeight - 0.001F;
			    	  }
			    	  //
			    	  Document d = new Document(new Rectangle(width, height));
			    	  ByteArrayOutputStream b = new ByteArrayOutputStream();
			    	  PdfWriter pw = PdfWriter.getInstance(d, b);
			    	  PdfContentByte cb = writer.getDirectContent();
			    	  PdfTemplate t = cb.createTemplate(width, height);
			    	  t.addImage(ixImg, width, 0, 0, height, 0, 0);
			    	  ixImg = com.lowagie.text.Image.getInstance(t);
			    	  //
		    	  } else {
		    		  ixImg.scaleAbsolute(width, height);
		    	  }        	
		      }
	          
	          document.add(ixImg);	
			}
	        document.close();
		} catch (DocumentException e) {
			throw new DiogeneException(e);
		} catch (Exception e) {
			throw new DiogeneException(e);
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
			
			while (Runtime.getRuntime().freeMemory() < 20000000) {
				Runtime.getRuntime().runFinalization();
				Runtime.getRuntime().gc();
			}
			//log.debug("MEMORIA LIBERA PRIMA, KB: " + Runtime.getRuntime().freeMemory() / 1024);
			//se non devo mettere il watermark e sto cercando di aprire l'immagine (non il pdf) restituisco l'immagine così come è
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
				boolean okWrite = ImageIO.write(op1, "jpg", outByte);
				BufferedImage bufferedImage = null;
				if (okWrite) {
					bufferedImage = ImageIO.read(new ByteArrayInputStream(outByte.toByteArray()));					
				} else {
					PlanarImage plan = new NullOpImage(dec.decodeAsRenderedImage(i), null, null, OpImage.OP_IO_BOUND);
					ColorModel cm = plan.getColorModel();
					WritableRaster raster = cm.createCompatibleWritableRaster(plan.getWidth(), plan.getHeight());
					Hashtable<String, Object> properties = new Hashtable<String, Object>();
					String[] keys = plan.getPropertyNames();
					if (keys!=null) {
					    for (int idx = 0; idx < keys.length; idx++) {
					        properties.put(keys[i], plan.getProperty(keys[i]));
					    }
					}
					bufferedImage = new BufferedImage(cm, raster, false, properties);
					plan.copyData(raster);
				}
				outByte = new ByteArrayOutputStream();
	            JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(outByte);
	            JPEGEncodeParam jpegEncodeParam = null;
	            jpegEncodeParam = jpegEncoder.getDefaultJPEGEncodeParam(bufferedImage);
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
					//log.debug("MEMORIA LIBERA PRIMA DI WATERMARK, KB: " + Runtime.getRuntime().freeMemory() / 1024);
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
			//log.debug("MEMORIA LIBERA ALLA FINE, KB: " + Runtime.getRuntime().freeMemory() / 1024);
			return images;
		} catch (Exception e) {
			throw new DiogeneException("Errore nel reperimento / conversione dell'immagine", e);
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
        
        String watermark = "DOCUMENTO AD ESCLUSIVO USO INTERNO";
        
        int fontDim = 50;
        
        while (true) {
        	g2d.setFont(new Font("Arial", Font.BOLD, fontDim));
            FontMetrics fontMetrics = g2d.getFontMetrics();
            Rectangle2D rect = fontMetrics.getStringBounds(watermark, g2d);
            if (rect.getWidth() < photo.getIconWidth() - 10 && rect.getHeight() < photo.getIconHeight() - 10) {
            	g2d.drawString(watermark,
                        (photo.getIconWidth() - (int) rect.getWidth()) / 2,
                        (photo.getIconHeight() - (int) rect.getHeight()) / 2);
            	break;
            }
            fontDim -= 1;
        }

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
    
}
