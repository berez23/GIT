package it.webred.ct.service.spprof.web.user.bean.interventi;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.ejb.EJB;

import it.webred.ct.config.model.AmKeyValueExt;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.data.access.basic.common.CommonDataIn;
import it.webred.ct.data.access.basic.common.CommonService;
import it.webred.ct.service.geospatial.data.access.GeospatialAreaLayerService;
import it.webred.ct.service.geospatial.data.access.dto.GeospatialDTO;
import it.webred.ct.service.spprof.data.access.SpProfInterventoService;
import it.webred.ct.service.spprof.data.access.SpProfProgettoService;
import it.webred.ct.service.spprof.data.access.dto.ProgettoShapeDTO;
import it.webred.ct.service.spprof.web.user.SpProfBaseBean;
import it.webred.ct.service.spprof.web.user.bean.util.PageBean;
import it.webred.gdal.main.ogr2ogr;

import org.apache.log4j.Logger;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

public class UploadBean extends SpProfBaseBean {

	protected ParameterService parameterService = (ParameterService) getEjb("CT_Service", "CT_Config_Manager", "ParameterBaseService");

	protected SpProfProgettoService spProfProgettoService = (SpProfProgettoService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfProgettoServiceBean");
	
	public GeospatialAreaLayerService geospatialAreaLayerService = (GeospatialAreaLayerService) getEjb("GeospatialProvider", "GeospatialProvider_EJB", "GeospatialAreaLayerServiceBean");
	
	public CommonService commonService = (CommonService) getEjb("CT_Service", "CT_Service_Data_Access", "CommonServiceBean");

	private String idIntervento;

	private File allegato;
	private String fileName;
	private String parameterPath;
	private boolean checkExtension;

	public void doInitPanel() {

		PageBean pBean = (PageBean) getBeanReference("pageBean");
		pBean.goUpload();

		allegato = null;
		checkExtension = true;
		if (parameterPath == null) {
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("dir.shape.files");
			AmKeyValueExt kv = parameterService.getAmKeyValueExt(criteria);
			if (kv != null)
				parameterPath = kv.getValueConf();
		}
	}

	/**
	 * Prepara il caricamento di un file
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void listener(UploadEvent event) throws Exception {
		logger.debug("Uploading file listener");
		UploadItem item = event.getUploadItem();
		allegato = item.getFile();

		// Inserita per gestire il diverso comportamento di Explorer
		// nell'estrazione del nome del file da UploadItem
		File tempFile = new File(item.getFileName());
		fileName = tempFile.getName();
		checkExtension = item.getFileName().endsWith("dxf")
				|| item.getFileName().endsWith("DXF");

	}

	/**
	 * Effettua il caricamento dell'allegato nel file system
	 */
	public void doUpload() {
		logger.debug("Uploading file");

		if (allegato == null) {
			super.addErrorMessage("upload.empty", "");
			return;
		}

		if (!checkExtension) {
			super.addErrorMessage("upload.notdxf", "");
			return;
		}

		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileInputStream fis2 = null;
		FileOutputStream fos2 = null;

		try {

			String name = getFilePath(fileName);

			logger.debug("File [" + name + "]");

			File fout = new File(name);

			fis = new FileInputStream(allegato);
			fos = new FileOutputStream(fout);

			byte[] buff = new byte[1024];

			while (fis.read(buff) != -1)
				fos.write(buff);

			// conversione dxf to shp
			String filenameNoExt = fileName;
			if (fileName.contains(".dxf"))
				filenameNoExt = fileName.replace(".dxf", "");
			if (fileName.contains(".DXF"))
				filenameNoExt = fileName.replace(".DXF", "");
			
			String now = new Long((new Date()).getTime()).toString();
			String[] args = {parameterPath + filenameNoExt + now +".shp",
			parameterPath + fileName}; ogr2ogr.main(args);

			// salvataggio shp
			ProgettoShapeDTO dto = new ProgettoShapeDTO(parameterPath,
					filenameNoExt + now, filenameNoExt, new Long(idIntervento));
			dto.setEnteId(getEnte());
			dto.setUserId(getUsername());
			spProfProgettoService.saveProgetto(dto);
			
			//validazione layer
			CommonDataIn cDataIn = new CommonDataIn();
			cDataIn.setEnteId(getEnte());
			cDataIn.setUserId(getUsername());
			cDataIn.setObj("select count(*) from S_SP_PROGETTO a where SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT(a.geometry , 0.000005) != 'TRUE'");
			commonService.executeNativeQuery(cDataIn);

			//salvataggio file dxf in cartella
			File directoryDxf = new File(parameterPath + "uploadedDXF");
			if(!directoryDxf.exists())
				directoryDxf.mkdir();
			fis2 = new FileInputStream(parameterPath + fileName);
			fos2 = new FileOutputStream(directoryDxf +"/"+idIntervento +"_"+ fileName);
			byte [] dati = new byte[fis2.available()];
			fis2.read(dati);
			fos2.write(dati);
			
			// eliminazione files
			File directory = new File(parameterPath);
			File[] files = directory.listFiles();
			for (File file : files) {
				if(!file.isDirectory()){
					if (!file.delete()) {
						logger.info("File " + file + " non eliminato, ancora in uso");
					}
				}
			}
			
			//controllo intersezione layer progetto/particelle
			GeospatialDTO gDto = new GeospatialDTO();
			gDto.setObj(new Long(idIntervento));
			gDto.setEnteId(getEnte());
			gDto.setUserId(getUsername());
			if(!geospatialAreaLayerService.isProgettoOnParticelle(gDto))
				super.addErrorMessage("upload.inter.error", "");
			else super.addInfoMessage("upload");

			doInitPanel();

		} catch (Throwable t) {
			super.addErrorMessage("upload.error", t.getMessage());
			logger.error("upload.error", t);
		} finally {
			try {
				if (fos != null)
					fos.close();
				if (fis != null)
					fis.close();
				if (fos2 != null)
					fos2.close();
				if (fis2 != null)
					fis2.close();
			} catch (Throwable t) {
				super.addErrorMessage("upload.error", t.getMessage());
				logger.error("upload.error", t);

			}
		}
	}

	/**
	 * Compone il nome del file passato a parametro, con il percorso di
	 * directory in cui risiedono gli allegati.
	 * 
	 * @param fileName
	 *            Nome del file completo
	 * @return Percorso del file completo sul file system
	 */
	private String getFilePath(String fileName) {
		String path = parameterPath;
		createDirectoryPath(path);

		String pathFile = path + File.separatorChar + fileName;
		return pathFile;
	}

	public void createDirectoryPath(String path) {
		// Se non esiste il percorso, lo crea
		File dir = new File(path);
		try {
			if (!dir.exists())
				dir.mkdirs();
		} catch (Throwable t) {
			logger.error("createDirectoryPath error", t);

		}
	}

	public String getIdIntervento() {
		return idIntervento;
	}

	public void setIdIntervento(String idIntervento) {
		this.idIntervento = idIntervento;
	}

	public File getAllegato() {
		return allegato;
	}

	public void setAllegato(File allegato) {
		this.allegato = allegato;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getParameterPath() {
		return parameterPath;
	}

	public void setParameterPath(String parameterPath) {
		this.parameterPath = parameterPath;
	}

}
