package it.webred.ct.service.spprof.web.user.bean.interventi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import it.webred.ct.config.model.AmKeyValueExt;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.service.spprof.data.access.SpProfAreaService;
import it.webred.ct.service.spprof.data.access.dto.ProgettoShapeDTO;
import it.webred.ct.service.spprof.web.user.SpProfBaseBean;

import org.apache.log4j.Logger;

import com.vividsolutions.jump.feature.FeatureDataset;

public class DownloadBean extends SpProfBaseBean {

	protected SpProfAreaService spProfAreaService = (SpProfAreaService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfAreaServiceBean");

	protected ParameterService parameterService = (ParameterService) getEjb("CT_Service", "CT_Config_Manager", "ParameterBaseService");

	private String idIntervento;
	private String parameterPath;

	public void doDownload() {

		try {
			if (parameterPath == null) {
				ParameterSearchCriteria criteria = new ParameterSearchCriteria();
				criteria.setKey("dir.shape.files");
				AmKeyValueExt kv = parameterService.getAmKeyValueExt(criteria);
				if (kv != null)
					parameterPath = kv.getValueConf();
			}

			logger.info("Output shpe folder: " + parameterPath);

			ProgettoShapeDTO dto = new ProgettoShapeDTO(null, null, null, new Long(
					idIntervento));
			dto.setEnteId(super.getEnte());
			dto.setUserId(super.getUsername());

			Map mappaAree = spProfAreaService.download(dto);

			// creazione cartella temporanea delntro path
			File folder = new File(parameterPath + "download/");
			if (!folder.exists()) {
				folder.mkdir();
				logger.info("Creazione folder: " + folder.getPath());
			}

			// AREE FABB
			com.vividsolutions.jump.feature.FeatureDataset featureDataset = (FeatureDataset) mappaAree
					.get("S_SP_AREA_FABB");
			if (featureDataset != null) {
				com.vividsolutions.jump.io.DriverProperties driverProps = new com.vividsolutions.jump.io.DriverProperties();

				driverProps.set("DefaultValue", folder.getPath() + "/shape_"
						+ "fabbricati.shp");
				driverProps.set("ShapeType", "xy");

				logger.info("Scrittura shape "
						+ driverProps.getProperty("DefaultValue"));

				com.vividsolutions.jump.io.ShapefileWriter writer = new com.vividsolutions.jump.io.ShapefileWriter();
				writer.write(featureDataset, driverProps);
			}

			// AREE PART
			featureDataset = (FeatureDataset) mappaAree.get("S_SP_AREA_PART");
			if (featureDataset != null) {
				com.vividsolutions.jump.io.DriverProperties driverProps = new com.vividsolutions.jump.io.DriverProperties();

				driverProps.set("DefaultValue", folder.getPath() + "/shape_"
						+ "particelle.shp");
				driverProps.set("ShapeType", "xy");

				logger.info("Scrittura shape "
						+ driverProps.getProperty("DefaultValue"));

				com.vividsolutions.jump.io.ShapefileWriter writer = new com.vividsolutions.jump.io.ShapefileWriter();
				writer.write(featureDataset, driverProps);
			}
			
			// AREE LAYER
			featureDataset = (FeatureDataset) mappaAree.get("S_SP_AREA_LAYER");
			if (featureDataset != null) {
				com.vividsolutions.jump.io.DriverProperties driverProps = new com.vividsolutions.jump.io.DriverProperties();

				driverProps.set("DefaultValue", folder.getPath() + "/shape_"
						+ "layer.shp");
				driverProps.set("ShapeType", "xy");

				logger.info("Scrittura shape "
						+ driverProps.getProperty("DefaultValue"));

				com.vividsolutions.jump.io.ShapefileWriter writer = new com.vividsolutions.jump.io.ShapefileWriter();
				writer.write(featureDataset, driverProps);
			}

			// creazione zip
			String nomeFile = "download_" + idIntervento + "_"
					+ Calendar.getInstance().getTimeInMillis() + ".zip";
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
					parameterPath + nomeFile));
			logger.info("Creazione " + parameterPath + nomeFile);

			// get a listing of the directory content
			String[] dirList = folder.list();
			int bytesIn = 0;
			for (int i = 0; i < dirList.length; i++) {
				File f = new File(folder, dirList[i]);
				logger.info("Current file: " + f.getPath());

				FileInputStream fis = new FileInputStream(f);
				ZipEntry anEntry = new ZipEntry(f.getName());
				logger.info("Current zip entry: " + anEntry.getName());

				zos.putNextEntry(anEntry);

				byte[] readBuffer = new byte[fis.available()];
				fis.read(readBuffer);
				zos.write(readBuffer);
				fis.close();
				zos.closeEntry();
				zos.flush();
			}

			zos.close();

			// download file zip
			String filePath = parameterPath + nomeFile;
			File f = new File(filePath);

			logger.info("Downloading file " + nomeFile + "...");
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			int DEFAULT_BUFFER_SIZE = 10240;
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			HttpServletResponse response = (HttpServletResponse) externalContext
					.getResponse();

			try {

				bis = new BufferedInputStream(new FileInputStream(f),
						DEFAULT_BUFFER_SIZE);

				response.setContentType("application/" + getContentType());
				response
						.setHeader("Content-Length", String.valueOf(f.length()));
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + f.getName() + "\"");
				bos = new BufferedOutputStream(response.getOutputStream(),
						DEFAULT_BUFFER_SIZE);

				byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
				int length;
				while ((length = bis.read(buffer)) > 0) {
					bos.write(buffer, 0, length);
				}

				bos.flush();

			} catch (Throwable t) {
				super.addErrorMessage("download.error", t.getMessage());
				logger.error(t);
			} finally {
				close(bos);
				close(bis);
			}

			facesContext.responseComplete();

			// eliminazione files
			deleteDir(new File(parameterPath + "download"));
			File shapeDir = new File(parameterPath);
			String[] children = shapeDir.list();
			for (int i = 0; i < children.length; i++) {
				File file = new File(shapeDir, children[i]);
				file.delete();
			}

		} catch (Throwable t) {
			super.addErrorMessage("download.error", t.getMessage());
			logger.error(t);
		}
	}

	private static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

	protected String getContentType() {

		String ct = "application/download";

		return ct;
	}

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory() && !dir.getAbsolutePath().endsWith("uploadedDXF")) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

	public String getIdIntervento() {
		return idIntervento;
	}

	public void setIdIntervento(String idIntervento) {
		this.idIntervento = idIntervento;
	}

	public String getParameterPath() {
		return parameterPath;
	}

	public void setParameterPath(String parameterPath) {
		this.parameterPath = parameterPath;
	}

}
