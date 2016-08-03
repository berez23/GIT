package it.webred.ct.service.muidocfa.web.bean.export;

import it.webred.ct.data.access.basic.diagnostiche.DiagnosticheDataIn;
import it.webred.ct.data.access.basic.diagnostiche.dto.RicercaDocfaReportDTO;
import it.webred.ct.data.model.diagnostiche.DocfaIciReport;
import it.webred.ct.data.model.diagnostiche.DocfaIciReportSogg;
import it.webred.ct.service.muidocfa.web.bean.MuiDocfaBaseBean;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

public class ExportTXTBean extends MuiDocfaBaseBean {

	private String dataFornitura;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	

	public void reportIciExportToTxt() {
		Writer writer = null;
		SimpleDateFormat sdfAnno = new SimpleDateFormat("yyyy");

		try {

			File file = new File("ListaDocfaIci_"+ dataFornitura.replaceAll("/", "-") +".txt");
			writer = new BufferedWriter(new FileWriter(file));
			
			RicercaDocfaReportDTO dto = new RicercaDocfaReportDTO();
			dto.setFornitura(sdf.parse(dataFornitura));
			DiagnosticheDataIn dataIn = new DiagnosticheDataIn();
			dataIn.setRicerca(dto);
			fillEnte(dataIn);
			List<DocfaIciReport> lista = docfaIciReportService.searchReport(dataIn);

			for(DocfaIciReport rep: lista){
				dataIn.setObj(rep.getIdRep());
				List<DocfaIciReportSogg> listaSogg = docfaIciReportService.getReportSogg(dataIn);
				if(listaSogg.size() > 0){
					for(DocfaIciReportSogg repSogg: listaSogg){
						
						writer.write((repSogg.getCodiFisc() != null? repSogg.getCodiFisc(): "") + insSeparator());
						writer.write((repSogg.getRagiSoci() != null? repSogg.getRagiSoci(): "") + insSeparator());
						writer.write((repSogg.getNome() != null? repSogg.getNome(): "") + insSeparator());
						writer.write((repSogg.getDataNasc() != null? sdf.format(repSogg.getDataNasc()):"") + insSeparator());
						writer.write((repSogg.getComuNasc() != null? repSogg.getComuNasc(): "") + insSeparator());
						writer.write((repSogg.getTitolo() != null? repSogg.getTitolo(): "") + insSeparator());
						writer.write((repSogg.getPercPoss() != null? repSogg.getPercPoss().toString(): "") + insSeparator());
						writer.write((repSogg.getId().getDataInizioPossesso() != null? sdf.format(repSogg.getId().getDataInizioPossesso()): "") + insSeparator());
						writer.write((repSogg.getId().getDataFinePossesso() != null? sdf.format(repSogg.getId().getDataFinePossesso()): "") + insSeparator());
						writer.write((repSogg.getNumFamiliari() != null? repSogg.getNumFamiliari(): "") + insSeparator());
						writer.write((rep.getProtocolloDocfa() != null? rep.getProtocolloDocfa(): "") + insSeparator());
						writer.write((rep.getDataDocfaToDate() != null? sdfAnno.format(rep.getDataDocfaToDate()): "") + insSeparator());
						writer.write((rep.getDicCognome() != null? rep.getDicCognome(): "") + insSeparator());
						writer.write((rep.getDicNome() != null? rep.getDicNome(): "") + insSeparator());
						writer.write((rep.getDicResIndir() != null? rep.getDicResIndir(): "") + insSeparator());
						writer.write((rep.getDicResNumciv() != null? rep.getDicResNumciv(): "") + insSeparator());
						writer.write((rep.getDicResCom() != null? rep.getDicResCom(): "") + insSeparator());
						writer.write((rep.getDicResCap() != null? rep.getDicResCap(): "") + insSeparator());
						writer.write((rep.getDicResProv() != null? rep.getDicResProv(): "") + insSeparator());
						writer.write((rep.getCategoriaDocfa()!= null? rep.getCategoriaDocfa(): "") + insSeparator());
						writer.write((rep.getClasseDocfa() != null? rep.getClasseDocfa(): "") + insSeparator());
						writer.write((rep.getConsistenzaDocfa() != null? rep.getConsistenzaDocfa(): "") + insSeparator());
						writer.write((rep.getRenditaDocfa() != null? rep.getRenditaDocfa(): "") + insSeparator());
						writer.write((rep.getFoglio() != null? rep.getFoglio(): "") + insSeparator());
						writer.write((rep.getParticella() != null? rep.getParticella(): "") + insSeparator());
						writer.write((rep.getSubalterno() != null? rep.getSubalterno(): "") + insSeparator());
						writer.write((rep.getViaDocfa() != null? rep.getViaDocfa(): "") + insSeparator());
						writer.write((rep.getTipoOperDocfa() != null? rep.getTipoOperDocfa(): "") + insSeparator());
						writer.write((repSogg.getFlgResidIndDcf3112() != null? repSogg.getFlgResidIndDcf3112(): "") + insSeparator());
						writer.write((repSogg.getFlgResidIndCat3112() != null? repSogg.getFlgResidIndCat3112(): "") + insSeparator());
						writer.write((repSogg.getFlgTitolareDataDocfa() != null? repSogg.getFlgTitolareDataDocfa(): "") + insSeparator());
						writer.write((repSogg.getFlgTitolare3112() != null? repSogg.getFlgTitolare3112(): "") + insSeparator());
						writer.write((repSogg.getFlgTitIciUiuAnte() != null? repSogg.getFlgTitIciUiuAnte(): "") + insSeparator());
						writer.write((repSogg.getFlgTitIciUiuPost() != null? repSogg.getFlgTitIciUiuPost(): "") + insSeparator());
						writer.write((repSogg.getFlgTitIciCivAnte() != null? repSogg.getFlgTitIciCivAnte(): "") + insSeparator());
						writer.write((repSogg.getFlgTitIciCivPost() != null? repSogg.getFlgTitIciCivPost(): "") + insSeparator());
						writer.write((repSogg.getFlgTitIciAnte() != null? repSogg.getFlgTitIciAnte(): "") + insSeparator());
						writer.write((repSogg.getFlgTitIciPost() != null? repSogg.getFlgTitIciPost(): "") + insSeparator());
						writer.write((rep.getDocfaInAnno() != null? rep.getDocfaInAnno(): "") + insSeparator());
						writer.write((rep.getFlgIndirizzoInToponomastica() != null? rep.getFlgIndirizzoInToponomastica(): "") + insSeparator());
						writer.write((rep.getFlgUiuCatasto() != null? rep.getFlgUiuCatasto(): "") + insSeparator());
						//NUOVI campi : Val.Imponibili
						writer.write((rep.getImponibileUiuPre() != null? rep.getImponibileUiuPre(): "") + insSeparator());
						writer.write((rep.getImponibileUiuPost() != null? rep.getImponibileUiuPost(): "") + insSeparator());
						writer.write((rep.getVarImponibileUiu() != null? rep.getVarImponibileUiu(): "") + insSeparator());
						writer.write((rep.getSumImponibilePre() != null? rep.getSumImponibilePre(): "") + insSeparator());
						writer.write((rep.getSumImponibilePost() != null? rep.getSumImponibilePost(): "") + insSeparator());
						writer.write((rep.getVarPercSumImponibile() != null? rep.getVarPercSumImponibile(): "") + insSeparator());
						
						writer.write((rep.getFlgIciAnteDocfa() != null? rep.getFlgIciAnteDocfa(): "") + insSeparator());
						writer.write((rep.getFlgVarAnteCategoria() != null? rep.getFlgVarAnteCategoria(): "") + insSeparator());
						writer.write((rep.getFlgVarAnteClasse() != null? rep.getFlgVarAnteClasse(): "") + insSeparator());
						writer.write((rep.getFlgVarAnteRendita() != null? rep.getFlgVarAnteRendita(): "") + insSeparator());
						writer.write((rep.getFlgIciPostDocfa() != null? rep.getFlgIciPostDocfa(): "") + insSeparator());
						writer.write((rep.getFlgVarPostCategoria() != null? rep.getFlgVarPostCategoria(): "") + insSeparator());
						writer.write((rep.getFlgVarPostClasse() != null? rep.getFlgVarPostClasse(): "") + insSeparator());
						writer.write((rep.getFlgVarPostRendita() != null? rep.getFlgVarPostRendita(): "") + insSeparator());
						writer.write((rep.getCodAnomalie() != null? rep.getCodAnomalie().replace('|','@'): "") + insSeparator());
						writer.write((rep.getFlgElaborato() != null? rep.getFlgElaborato(): "") + insSeparator());
						writer.write((rep.getDataAggiornamento() != null? sdf.format(rep.getDataAggiornamento()): "") + insSeparator());
	
						writer.write("\r\n");
						
					}
				}else{
					
					writer.write(insSeparator());
					writer.write(insSeparator());
					writer.write(insSeparator());
					writer.write(insSeparator());
					writer.write(insSeparator());
					writer.write(insSeparator());
					writer.write(insSeparator());
					writer.write(insSeparator());
					writer.write(insSeparator());
					writer.write((rep.getProtocolloDocfa() != null? rep.getProtocolloDocfa(): "") + insSeparator());
					writer.write((rep.getDataDocfaToDate() != null? sdfAnno.format(rep.getDataDocfaToDate()): "") + insSeparator());
					writer.write((rep.getDicCognome() != null? rep.getDicCognome(): "") + insSeparator());
					writer.write((rep.getDicNome() != null? rep.getDicNome(): "") + insSeparator());
					writer.write((rep.getDicResIndir() != null? rep.getDicResIndir(): "") + insSeparator());
					writer.write((rep.getDicResNumciv() != null? rep.getDicResNumciv(): "") + insSeparator());
					writer.write((rep.getDicResCom() != null? rep.getDicResCom(): "") + insSeparator());
					writer.write((rep.getDicResCap() != null? rep.getDicResCap(): "") + insSeparator());
					writer.write((rep.getDicResProv() != null? rep.getDicResProv(): "") + insSeparator());
					writer.write((rep.getCategoriaDocfa()!= null? rep.getCategoriaDocfa(): "") + insSeparator());
					writer.write((rep.getClasseDocfa() != null? rep.getClasseDocfa(): "") + insSeparator());
					writer.write((rep.getConsistenzaDocfa() != null? rep.getConsistenzaDocfa(): "") + insSeparator());
					writer.write((rep.getRenditaDocfa() != null? rep.getRenditaDocfa(): "") + insSeparator());
					writer.write((rep.getFoglio() != null? rep.getFoglio(): "") + insSeparator());
					writer.write((rep.getParticella() != null? rep.getParticella(): "") + insSeparator());
					writer.write((rep.getSubalterno() != null? rep.getSubalterno(): "") + insSeparator());
					writer.write((rep.getViaDocfa() != null? rep.getViaDocfa(): "") + insSeparator());
					writer.write((rep.getTipoOperDocfa() != null? rep.getTipoOperDocfa(): "") + insSeparator());
					writer.write(insSeparator());
					writer.write(insSeparator());
					writer.write(insSeparator());
					writer.write(insSeparator());
					writer.write(insSeparator());
					writer.write(insSeparator());
					writer.write(insSeparator());
					writer.write(insSeparator());
					writer.write(insSeparator());
					writer.write(insSeparator());
					writer.write((rep.getDocfaInAnno() != null? rep.getDocfaInAnno(): "") + insSeparator());
					writer.write((rep.getFlgIndirizzoInToponomastica() != null? rep.getFlgIndirizzoInToponomastica(): "") + insSeparator());
					writer.write((rep.getFlgUiuCatasto() != null? rep.getFlgUiuCatasto(): "") + insSeparator());
					
					//Nuovi campi : Val.Imponibili
					writer.write((rep.getImponibileUiuPre() != null? rep.getImponibileUiuPre(): "") + insSeparator());
					writer.write((rep.getImponibileUiuPost() != null? rep.getImponibileUiuPost(): "") + insSeparator());
					writer.write((rep.getVarImponibileUiu() != null? rep.getVarImponibileUiu(): "") + insSeparator());
					writer.write((rep.getSumImponibilePre() != null? rep.getSumImponibilePre(): "") + insSeparator());
					writer.write((rep.getSumImponibilePost() != null? rep.getSumImponibilePost(): "") + insSeparator());
					writer.write((rep.getVarPercSumImponibile() != null? rep.getVarPercSumImponibile(): "") + insSeparator());
					
					writer.write((rep.getFlgIciAnteDocfa() != null? rep.getFlgIciAnteDocfa(): "") + insSeparator());
					writer.write((rep.getFlgVarAnteCategoria() != null? rep.getFlgVarAnteCategoria(): "") + insSeparator());
					writer.write((rep.getFlgVarAnteClasse() != null? rep.getFlgVarAnteClasse(): "") + insSeparator());
					writer.write((rep.getFlgVarAnteRendita() != null? rep.getFlgVarAnteRendita(): "") + insSeparator());
					writer.write((rep.getFlgIciPostDocfa() != null? rep.getFlgIciPostDocfa(): "") + insSeparator());
					writer.write((rep.getFlgVarPostCategoria() != null? rep.getFlgVarPostCategoria(): "") + insSeparator());
					writer.write((rep.getFlgVarPostClasse() != null? rep.getFlgVarPostClasse(): "") + insSeparator());
					writer.write((rep.getFlgVarPostRendita() != null? rep.getFlgVarPostRendita(): "") + insSeparator());
					writer.write((rep.getCodAnomalie() != null? rep.getCodAnomalie(): "") + insSeparator());
					writer.write((rep.getFlgElaborato() != null? rep.getFlgElaborato(): "") + insSeparator());
					writer.write((rep.getDataAggiornamento() != null? sdf.format(rep.getDataAggiornamento()): "") + insSeparator());

					writer.write("\r\n");
					
				}
			}
			
			writer.close();
			showTxt(file);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Throwable t) {
			super.addErrorMessage("export.pdf.error", t.getMessage());
			t.printStackTrace();
		}
	}
	
	private void showTxt(File f) throws IOException {

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		int DEFAULT_BUFFER_SIZE = 10240;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

		try {

			bis = new BufferedInputStream(new FileInputStream(f),DEFAULT_BUFFER_SIZE);

			response.setContentType("text/plain");
			response.setHeader("Content-Length", String.valueOf(f.length()));
			String fileName = f.getName();
			response.setHeader("Content-Disposition", "attachment; filename=\""+ fileName + "\"");
			bos = new BufferedOutputStream(response.getOutputStream(),DEFAULT_BUFFER_SIZE);

			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int length;
			while ((length = bis.read(buffer)) > 0) {
				bos.write(buffer, 0, length);
			}

			bos.flush();

		} catch (Throwable t) {
			super.addErrorMessage("file.download.error", t.getMessage());
			t.printStackTrace();
		} finally {
			close(bos);
			close(bis);
			f.delete();
		}

		facesContext.responseComplete();

	}

	private String insSeparator(){
		return "|";
	}
	
	private static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getDataFornitura() {
		return dataFornitura;
	}

	public void setDataFornitura(String dataFornitura) {
		this.dataFornitura = dataFornitura;
	}

}
