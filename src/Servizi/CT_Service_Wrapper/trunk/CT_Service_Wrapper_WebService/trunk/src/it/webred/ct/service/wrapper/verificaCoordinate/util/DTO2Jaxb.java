package it.webred.ct.service.wrapper.verificaCoordinate.util;

import java.util.List;

import org.apache.log4j.Logger;

public class DTO2Jaxb  {

	private Logger log = Logger.getLogger(this.getClass().getName());
	
	private static DTO2Jaxb me;
	
	
	public static DTO2Jaxb getInstance() {
		if(me==null)
			me = new DTO2Jaxb();
		
		return me;
	}
	
	
	
	public it.webred.ct.service.jaxb.verificaCoordinate.response.VerificaCoordinate dto2JaxbObject (
			it.webred.verificaCoordinate.dto.response.VerificaCoordinateResponseDTO vcDTO) throws Exception {
		
		it.webred.ct.service.jaxb.verificaCoordinate.response.VerificaCoordinate vcJaxb = null;
		
		log.info("Trasformazione da DTO a oggetto jaxb  ["+vcDTO.getClass().getName()+"]");
		
		try {
			it.webred.ct.service.jaxb.verificaCoordinate.response.ObjectFactory of = 
				new it.webred.ct.service.jaxb.verificaCoordinate.response.ObjectFactory();
			
			vcJaxb = of.createVerificaCoordinate();
			vcJaxb.setTipo(vcDTO.getTipo());
			
			//ERR
			log.debug("ERR");
			if(vcDTO.isSetError()) {
				List<it.webred.verificaCoordinate.dto.response.ErrorDTO> erroriDTO = vcDTO.getError();
				for(it.webred.verificaCoordinate.dto.response.ErrorDTO errDTO: erroriDTO) {
					it.webred.ct.service.jaxb.verificaCoordinate.response.Error errJaxb = of.createError();
					errJaxb.setDesc(errDTO.getDesc());
					vcJaxb.getError().add(errJaxb);
				}
			}
			
			//OK
			log.debug("OK");
			it.webred.verificaCoordinate.dto.response.OkDTO okDTO = vcDTO.getOk();
			if(okDTO != null) {
				it.webred.ct.service.jaxb.verificaCoordinate.response.Ok okJaxb = of.createOk();
				okJaxb.setDesc(okDTO.getDesc());
				vcJaxb.setOk(okJaxb);
			}
			
			//INFO
			log.debug("INFO");
			it.webred.verificaCoordinate.dto.response.InfoDTO infoDTO = vcDTO.getInfo();
			if(infoDTO != null) {
				it.webred.ct.service.jaxb.verificaCoordinate.response.Info infoJaxb = of.createInfo();
				infoJaxb.setDesc(infoDTO.getDesc());
				vcJaxb.setInfo(infoJaxb);
			}
			
			//EV
			log.debug("EV");
			it.webred.verificaCoordinate.dto.response.ElencoVieDTO evDTO = vcDTO.getElencoVie();
			if(evDTO != null) {
				it.webred.ct.service.jaxb.verificaCoordinate.response.ElencoVie evJaxb = of.createElencoVie();
				if(evDTO.isSetVia()) {
					List<it.webred.verificaCoordinate.dto.response.ViaDTO> vieDTO = evDTO.getVia();
					for(it.webred.verificaCoordinate.dto.response.ViaDTO viaDTO: vieDTO) {
						it.webred.ct.service.jaxb.verificaCoordinate.response.Via viaJaxb = of.createVia();
						viaJaxb.setPrefisso(viaDTO.getPrefisso());
						viaJaxb.setNome(viaDTO.getNome());
						viaJaxb.setCodiceVia(viaDTO.getCodiceVia());
						
						evJaxb.getVia().add(viaJaxb);
					}
				}
				vcJaxb.setElencoVie(evJaxb);
			}
			
			//EU
			log.debug("EU");
			it.webred.verificaCoordinate.dto.response.ElencoUiuDTO euDTO = vcDTO.getElencoUiu();
			if(euDTO != null) {
				it.webred.ct.service.jaxb.verificaCoordinate.response.ElencoUiu euJaxb = of.createElencoUiu();
				if(euDTO.isSetUiu()) {
					List<it.webred.verificaCoordinate.dto.response.UiuDTO> uiuDTO = euDTO.getUiu();
					for(it.webred.verificaCoordinate.dto.response.UiuDTO uDTO: uiuDTO){
						it.webred.ct.service.jaxb.verificaCoordinate.response.Uiu uiuJaxb = of.createUiu();
						uiuJaxb.setFoglio(uDTO.getFoglio());
						uiuJaxb.setParticella(uDTO.getParticella());
						uiuJaxb.setSubalterno(uDTO.getSubalterno());
						uiuJaxb.setSezione(uDTO.getSezione());
						uiuJaxb.setClasse(uDTO.getClasse());
						uiuJaxb.setRendita(uDTO.getRendita());
						uiuJaxb.setDataInizioVal(uDTO.getDataInizioVal());
						uiuJaxb.setCategoria(uDTO.getCategoria());
						
						euJaxb.getUiu().add(uiuJaxb);
					}
				}
				vcJaxb.setElencoUiu(euJaxb);
			}
			
			//EC
			log.debug("EC");
			it.webred.verificaCoordinate.dto.response.ElencoCivicoDTO ecDTO = vcDTO.getElencoCivico();
			if(ecDTO != null) {
				it.webred.ct.service.jaxb.verificaCoordinate.response.ElencoCivico ecJaxb = of.createElencoCivico();
				if(ecDTO.isSetCivico()) {
					List<it.webred.verificaCoordinate.dto.response.CivicoDTO> civiciDTO = ecDTO.getCivico();
					for(it.webred.verificaCoordinate.dto.response.CivicoDTO civicoDTO: civiciDTO) {
						it.webred.ct.service.jaxb.verificaCoordinate.response.Civico civicoJaxb = of.createCivico();
						civicoJaxb.setNumero(civicoDTO.getNumero());
						
						if(civicoDTO.isSetVia()) {
							it.webred.ct.service.jaxb.verificaCoordinate.response.Via viaJaxb = of.createVia();
							viaJaxb.setPrefisso(civicoDTO.getVia().getPrefisso());
							viaJaxb.setNome(civicoDTO.getVia().getNome());
							viaJaxb.setCodiceVia(civicoDTO.getVia().getCodiceVia());
							
							civicoJaxb.setVia(viaJaxb);
						}
						
						ecJaxb.getCivico().add(civicoJaxb);
					}
				}
				vcJaxb.setElencoCivico(ecJaxb);
			}
			
			//EM
			log.debug("EM");
			it.webred.verificaCoordinate.dto.response.ElencoMappaleDTO emDTO = vcDTO.getElencoMappale();
			if(emDTO != null) {
				it.webred.ct.service.jaxb.verificaCoordinate.response.ElencoMappale emJaxb = of.createElencoMappale();
				if(emDTO.isSetMappale()) {
					List<it.webred.verificaCoordinate.dto.response.MappaleDTO> mappaliDTO = emDTO.getMappale();
					for(it.webred.verificaCoordinate.dto.response.MappaleDTO mappaleDTO: mappaliDTO) {
						it.webred.ct.service.jaxb.verificaCoordinate.response.Mappale mappaleJaxb = of.createMappale();
						mappaleJaxb.setFoglio(mappaleDTO.getFoglio());
						mappaleJaxb.setParticella(mappaleDTO.getParticella());
						mappaleJaxb.setDataInizioVal(mappaleDTO.getDataInizioVal());
						
						if(mappaleDTO.isSetZonaDecentramento()) {
							it.webred.ct.service.jaxb.verificaCoordinate.response.ZonaDecentramento zdJaxb = of.createZonaDecentramento();
							mappaleJaxb.setZonaDecentramento(zdJaxb);
							zdJaxb.setZona(mappaleDTO.getZonaDecentramento().getZona());
						}
						
						
						//attributi
						mappaleJaxb.setCensUrbano(mappaleDTO.getCensUrbano());
						mappaleJaxb.setCensTerreni(mappaleDTO.getCensTerreni());
						mappaleJaxb.setCartografia(mappaleDTO.getCartografia());
						//mappaleJaxb.setZonaDecentramento(mappaleDTO.getZonaDecentramento());
						
						//PRG
						log.debug("PRG: dati prg");
						it.webred.verificaCoordinate.dto.response.DatiPrgDTO dpDTO = mappaleDTO.getDatiPrg();
						if(dpDTO != null){
							it.webred.ct.service.jaxb.verificaCoordinate.response.DatiPrg dpJaxb = of.createDatiPrg();
							if(dpDTO.isSetPrg()) {
								List<it.webred.verificaCoordinate.dto.response.PrgDTO> prgsDTO = dpDTO.getPrg();
								for(it.webred.verificaCoordinate.dto.response.PrgDTO prgDTO: prgsDTO) {
									it.webred.ct.service.jaxb.verificaCoordinate.response.Prg prgJaxb = of.createPrg();
									
									prgJaxb.setDal(prgDTO.getDal());
									prgJaxb.setAl(prgDTO.getAl());
									prgJaxb.setId(prgDTO.getId());
									
									//prgJaxb.setZonaOmogenea(prgDTO.getZonaOmogenea());
									if(prgDTO.isSetZonaOmogenea()) {
										it.webred.ct.service.jaxb.verificaCoordinate.response.ZonaOmogenea zoJaxb = of.createZonaOmogenea();
										
										zoJaxb.setCodice(prgDTO.getZonaOmogenea().getCodice());
										zoJaxb.setDescrizione(prgDTO.getZonaOmogenea().getDescrizione());
										zoJaxb.setNota(prgDTO.getZonaOmogenea().getNota());
										
										prgJaxb.setZonaOmogenea(zoJaxb);
									}
									
									
									if(prgDTO.isSetDestFunzionale()) {
										it.webred.ct.service.jaxb.verificaCoordinate.response.DestFunzionale dfJaxb = of.createDestFunzionale();
										dfJaxb.setCodice(prgDTO.getDestFunzionale().getCodice());
										dfJaxb.setDescrizione(prgDTO.getDestFunzionale().getDescrizione());
										dfJaxb.setNota(prgDTO.getDestFunzionale().getNota());
										
										prgJaxb.setDestFunzionale(dfJaxb);
									}
									
									/*
									prgJaxb.setDestUrbanistica(prgDTO.getDestUrbanistica());
									prgJaxb.setAreaPart(prgDTO.getAreaPart());
									prgJaxb.setAreaPrg(prgDTO.getAreaPrg());
									prgJaxb.setAreaIntersezione(prgDTO.getAreaIntersezione());
									*/	

									if(prgDTO.isSetLinks()) {
										it.webred.ct.service.jaxb.verificaCoordinate.response.Links linksJaxb = of.createLinks();
										it.webred.verificaCoordinate.dto.response.LinksDTO linksDTO = prgDTO.getLinks();
										if(linksDTO.isSetLink()) {											
											List<it.webred.verificaCoordinate.dto.response.LinkDTO> linkDTO = linksDTO.getLink();
											for(it.webred.verificaCoordinate.dto.response.LinkDTO lkDTO: linkDTO) {
												it.webred.ct.service.jaxb.verificaCoordinate.response.Link linkJaxb = of.createLink();
												linkJaxb.setDescrizione(lkDTO.getDescrizione());
												linkJaxb.setUrl(lkDTO.getUrl());
												
												linksJaxb.getLink().add(linkJaxb);
											}
										}
										prgJaxb.setLinks(linksJaxb);
									}
									
									dpJaxb.getPrg().add(prgJaxb);
								}
							}
							mappaleJaxb.setDatiPrg(dpJaxb);
						}
						
						//PRG
						log.debug("PRG: vincoli");
						it.webred.verificaCoordinate.dto.response.VincoliTypeDTO vvDTO = mappaleDTO.getVincoli();
						if(vvDTO != null){
							it.webred.ct.service.jaxb.verificaCoordinate.response.VincoliType vvJaxb = of.createVincoliType();
							if(vvDTO.isSetVincolo()) {
								List<it.webred.verificaCoordinate.dto.response.VincoloTypeDTO> vListDTO = vvDTO.getVincolo();
								for(it.webred.verificaCoordinate.dto.response.VincoloTypeDTO vDTO: vListDTO) {
									it.webred.ct.service.jaxb.verificaCoordinate.response.VincoloType vJaxb = of.createVincoloType();
									vJaxb.setTipo(vDTO.getTipo());
									if(vDTO.isSetRiga()) {
										List<it.webred.verificaCoordinate.dto.response.RigaTypeDTO> rListDTO = vDTO.getRiga();
										for(it.webred.verificaCoordinate.dto.response.RigaTypeDTO rDTO: rListDTO) {
											it.webred.ct.service.jaxb.verificaCoordinate.response.RigaType rJaxb = of.createRigaType();
											if(rDTO.isSetAttributo()) {
												List<it.webred.verificaCoordinate.dto.response.AttributoTypeDTO> aListDTO = rDTO.getAttributo();
												for(it.webred.verificaCoordinate.dto.response.AttributoTypeDTO aDTO: aListDTO) {
													it.webred.ct.service.jaxb.verificaCoordinate.response.AttributoType aJaxb = of.createAttributoType();
													aJaxb.setNome(aDTO.getNome());
													aJaxb.setLabel(aDTO.getLabel());
													aJaxb.setValore(aDTO.getValore());
													
													rJaxb.getAttributo().add(aJaxb);
												}
											}											
											vJaxb.getRiga().add(rJaxb);
										}
									}
									vvJaxb.getVincolo().add(vJaxb);
								}
							}
							mappaleJaxb.setVincoli(vvJaxb);
						}
						
						
						log.debug("PRG: dati attesi");
						it.webred.verificaCoordinate.dto.response.DatiAttesiDTO daDTO = mappaleDTO.getDatiAttesi();
						if(daDTO != null){
							it.webred.ct.service.jaxb.verificaCoordinate.response.DatiAttesi daJaxb = of.createDatiAttesi();
							
							if(daDTO.isSetDatiAttesiResidenziale()) {
								List<it.webred.verificaCoordinate.dto.response.DatiAttesiResidenzialeDTO> ldarDTO = daDTO.getDatiAttesiResidenziale();
								
								for(it.webred.verificaCoordinate.dto.response.DatiAttesiResidenzialeDTO darDTO: ldarDTO) {
									it.webred.ct.service.jaxb.verificaCoordinate.response.DatiAttesiResidenziale darJaxb = of.createDatiAttesiResidenziale();
									
									if(darDTO.isSetClassiMaxCategoria()) {
										it.webred.ct.service.jaxb.verificaCoordinate.response.ClassiMaxCategoria cmcJaxb = of.createClassiMaxCategoria();
										darJaxb.setClassiMaxCategoria(cmcJaxb);
										
										it.webred.verificaCoordinate.dto.response.ClassiMaxCategoriaDTO cmcDTO = darDTO.getClassiMaxCategoria();
										if(cmcDTO.isSetClasseMaxCategoria()) {
											List<it.webred.verificaCoordinate.dto.response.ClasseMaxCategoriaDTO> lCmcDTO = cmcDTO.getClasseMaxCategoria();
											for(it.webred.verificaCoordinate.dto.response.ClasseMaxCategoriaDTO oCmcDTO: lCmcDTO) {
												it.webred.ct.service.jaxb.verificaCoordinate.response.ClasseMaxCategoria oCmcJaxb = of.createClasseMaxCategoria();
												oCmcJaxb.setCategoria(oCmcDTO.getCategoria());
												oCmcJaxb.setClasse(oCmcDTO.getClasse());
												
												cmcJaxb.getClasseMaxCategoria().add(oCmcJaxb);
											}
										}
									}
									
									darJaxb.setRenditaMinima(darDTO.getRenditaMinima());
									darJaxb.setTariffaPerVano(darDTO.getTariffaPerVano());
									darJaxb.setValoreCommerciale(darDTO.getValoreCommerciale());
									darJaxb.setValoreCommercialeMq(darDTO.getValoreCommercialeMq());
									
									if(darDTO.isSetClassamenti()) {
										it.webred.ct.service.jaxb.verificaCoordinate.response.Classamenti cJaxb = of.createClassamenti();
										darJaxb.setClassamenti(cJaxb);
										
										it.webred.verificaCoordinate.dto.response.ClassamentiDTO cDTO = darDTO.getClassamenti();
										if(cDTO.isSetClassamento()) {
											List<it.webred.verificaCoordinate.dto.response.ClassamentoDTO> lCDTO = cDTO.getClassamento();
											for(it.webred.verificaCoordinate.dto.response.ClassamentoDTO ccDTO: lCDTO) {
												it.webred.ct.service.jaxb.verificaCoordinate.response.Classamento ccJaxb = of.createClassamento();
												
												ccJaxb.setCategoria(ccDTO.getCategoria());
												ccJaxb.setClasse(ccDTO.getClasse());
												ccJaxb.setRenditaComplessiva(ccDTO.getRenditaComplessiva());
												ccJaxb.setTariffa(ccDTO.getTariffa());
												
												cJaxb.getClassamento().add(ccJaxb);
											}
										}
									}
									
									daJaxb.getDatiAttesiResidenziale().add(darJaxb);
								}
							}
							
							
							if(daDTO.isSetDatiAttesiNonResidenziale()) {
								List<it.webred.verificaCoordinate.dto.response.DatiAttesiNonResidenzialeDTO> ldarnDTO = daDTO.getDatiAttesiNonResidenziale();
								
								for(it.webred.verificaCoordinate.dto.response.DatiAttesiNonResidenzialeDTO darnDTO: ldarnDTO) {
									it.webred.ct.service.jaxb.verificaCoordinate.response.DatiAttesiNonResidenziale darnJaxb = of.createDatiAttesiNonResidenziale();
									darnJaxb.setClasseMediaRiferimento(darnDTO.getClasseMediaRiferimento());
									
									daJaxb.getDatiAttesiNonResidenziale().add(darnJaxb);
								}
							}
							
							mappaleJaxb.setDatiAttesi(daJaxb);
						}
						emJaxb.getMappale().add(mappaleJaxb);
					}
				}
				vcJaxb.setElencoMappale(emJaxb);
			}
			
			
			//UG
			log.debug("UG");
			it.webred.verificaCoordinate.dto.response.GrafDTO gDTO = vcDTO.getGraf();
			if(gDTO != null) {
				it.webred.ct.service.jaxb.verificaCoordinate.response.Graf gJaxb = of.createGraf();
				gJaxb.setFoglio(gDTO.getFoglio());
				gJaxb.setParticella(gDTO.getParticella());
				gJaxb.setUnimm(gDTO.getUnimm());
				gJaxb.setSezione(gDTO.getSezione());
				
				vcJaxb.setGraf(gJaxb);
			}
			
			//COORD
			log.debug("COORD");
			it.webred.verificaCoordinate.dto.response.CoordinateDTO coordDTO = vcDTO.getCoordinate();
			if(coordDTO != null) {
				it.webred.ct.service.jaxb.verificaCoordinate.response.Coordinate coordJaxb = of.createCoordinate();
				coordJaxb.setLat(coordDTO.getLat());
				coordJaxb.setLon(coordDTO.getLon());
				
				vcJaxb.setCoordinate(coordJaxb);
			}
			
		}catch(Exception e) {
			log.error("Eccezione dto2JaxbObject(): "+e.getMessage());
			throw e;
		}
		
		return vcJaxb;
	}
}
