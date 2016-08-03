package it.webred.mui.hibernate;

import it.webred.mui.input.MuiFornituraParser;
import it.webred.mui.model.CodiciDup;
import it.webred.mui.model.MiDupFornitura;
import it.webred.mui.model.MiDupNotaTras;
import it.webred.mui.model.MiDupTitolarita;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

import net.skillbill.logging.Logger;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.EmptyInterceptor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.mapping.DenormalizedTable;
import org.hibernate.type.Type;

public class MuiInterceptor extends EmptyInterceptor {
	private int updates;

	private int creates;

	private int loads;

	private static final FieldConverter yearDateConverter = new FieldConverter() {
		public Object convertObject(Object val) {
			Object res = null;
			if (val != null) {
				try {
					return MuiFornituraParser.yearParser.parse((String.valueOf(val)));
				} catch (ParseException e) {
					Logger.log().error(this.getClass().getName(), "error while converting field as year date val=" + val, e);
				}
			}
			Logger.log().info(this.getClass().getName(), "converted field as year date " + res);
			return res;
		}

		public Object convertObjects(Object[] val) {
			return null;
		}
	};

	private static final FieldConverter dateDateConverter = new FieldConverter() {
		public Object convertObject(Object val) {
			Object res = null;
			if (val != null) {
				try {
					return MuiFornituraParser.dateParser.parse((String.valueOf(val)));
				} catch (ParseException e) {
					Logger.log().error(this.getClass().getName(), "error while converting field as year date val=" + val, e);
				}
			}
			Logger.log().info(this.getClass().getName(), "converted field as year date " + res);
			return res;
		}

		public Object convertObjects(Object[] val) {
			return null;
		}
	};

	private static final FieldConverter longConverter = new FieldConverter() {
		public Object convertObject(Object val) {
			Object res = null;
			if (val != null) {
				try {
					return Long.valueOf((String) val);
				} catch (Throwable e) {
					Logger.log().error(this.getClass().getName(), "error while converting field as year long val=" + val, e);
				}
			}
			Logger.log().info(this.getClass().getName(), "converted field as number= " + res);
			return res;
		}

		public Object convertObjects(Object[] val) {
			return null;
		}
	};

	private static final FieldConverter quotaConverter = new FieldConverter() {
		public Object convertObject(Object val) {
			return null;
		}

		public Object convertObjects(Object[] val) {
			boolean isNotNull = false;
			for (int i = 0; i < val.length; i++) {
				isNotNull = isNotNull || (val[i] != null && !((String) val[i]).trim().equals(""));
			}
			if (!isNotNull) {
				return null;
			}
			try {

				// double numeratore = Integer.valueOf((String) val[0] + (String)
				// val[2]);
				// double denominatore = Integer.valueOf((String) val[1] + (String)
				// val[3]);
				String val0 = (val[0] != null) ? (String) val[0] : "";
				String val1 = (val[1] != null) ? (String) val[1] : "";
				String val2 = (val[2] != null) ? (String) val[2] : "";
				String val3 = (val[3] != null) ? (String) val[3] : "";
				double numeratore = (Double.valueOf(val0 + val2)).doubleValue();
				double denominatore = (Double.valueOf(val1 + val3)).doubleValue();
				/*double numeratore = (Double.valueOf((String) val[0] + (String) val[2])).doubleValue();
				double denominatore = (Double.valueOf((String) val[1] + (String) val[3])).doubleValue();*/
				//java.math.BigDecimal quota = java.math.BigDecimal.valueOf(numeratore / denominatore / 10d);
				// Modified by MaX - 29/09/2007 - Tolta la divisione per 10
				java.math.BigDecimal quota = java.math.BigDecimal.valueOf(numeratore / denominatore);
				Logger.log().debug(this.getClass().getName(), "Quota: " + quota + " - Numeratore: " + numeratore + " - Denominatore: " + denominatore);
				return quota;
			} catch (Throwable e) {
				Logger.log().error(this.getClass().getName(), "error while converting field as BigDecimal value=" + val + " - Numeratore: " + val[0] + val[2] + " - Denominatore: " + val[1] + val[3], e);
				return null;
			}
		}
	};

	private static final FieldConverter codiciDupConverter = new FieldConverter() {
		public Object convertObject(Object val) {
			return null;
		}

		public Object convertObjects(Object[] val) {
			if (val == null) {
				return null;
			} else {
				try {
					Session session = HibernateUtil.currentSession();
					Object valDec = (val[0] != null && !((String) val[0]).trim().equals("")) ? val[0] : val[1];
					if (valDec == null || valDec.toString().trim().length() == 0) {
						return null;
					}
					Logger.log().debug(this.getClass().getName(), "looking CodiciDup for value " + valDec);
					CodiciDup codice = (CodiciDup) session.load(CodiciDup.class, (Serializable) valDec);
					return codice;
				} catch (Throwable e) {
					Logger.log().error(this.getClass().getName(), "error while converting field as CodiciDup value=" + val, e);
					return null;
				}
			}
		}
	};

	// Added by MaX 19/08/2007
	private static final FieldConverter stringConverter = new FieldConverter() {
		public Object convertObject(Object val) {
			Object res = null;
			if (val != null) {
				return "" + val;
			}
			return res;
		}

		public Object convertObjects(Object[] val) {
			return null;
		}
	};

	public MuiInterceptor() {
		super();
	}

	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		// do nothing
	}

	public boolean onFlushDirty(Object entity, Serializable id, Object[] state, Object[] previousState, String[] propertyNames, Type[] types) {
		if (entity instanceof MiDupNotaTras) {
			updates++;
			syncMiDupNotaTras((MiDupNotaTras) entity, state, propertyNames);
			return true;
		}
		if (entity instanceof MiDupFornitura) {
			updates++;
			syncMiDupFornitura((MiDupFornitura) entity, state, propertyNames);
			return true;
		}
		if (entity instanceof MiDupTitolarita) {
			updates++;
			syncMiDupTitolarita((MiDupTitolarita) entity, state, propertyNames);
			return true;
		}
		return false;
	}

	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if (entity instanceof MiDupNotaTras) {
			loads++;
		}
		return false;
	}

	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if (entity instanceof MiDupNotaTras) {
			creates++;
			syncMiDupNotaTras((MiDupNotaTras) entity, state, propertyNames);
			return true;
		}
		if (entity instanceof MiDupFornitura) {
			creates++;
			syncMiDupFornitura((MiDupFornitura) entity, state, propertyNames);
			return true;
		}
		if (entity instanceof MiDupTitolarita) {
			creates++;
			syncMiDupTitolarita((MiDupTitolarita) entity, state, propertyNames);
			return true;
		}
		return false;
	}

	private void syncMiDupNotaTras(MiDupNotaTras nota, Object[] state, String[] propertyNames) {
		setDataValiditaAttoDate(nota, state, propertyNames);
		setAnnoNotaDate(nota, state, propertyNames);
		setNumeroNotaTrasLong(nota, state, propertyNames);
	}

	private void syncMiDupFornitura(MiDupFornitura fornitura, Object[] state, String[] propertyNames) {
		/*
		 * for (int i = 0; i < propertyNames.length; i++) {
		 * log.error(propertyNames[i]+":"+state[i]); if(state[i] != null){
		 * log.error(state[i].getClass()); } } setDataIniziale(state,
		 * propertyNames); setDataFinale(state, propertyNames); for (int i = 0; i <
		 * propertyNames.length; i++) { if
		 * ("dataIniziale".equals(propertyNames[i])) { try {
		 * fornitura.setDataInizialeDate(MuiFornituraParser.dateParser
		 * .parse((String) state[i])); } catch (ParseException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } } } for (int i = 0; i <
		 * propertyNames.length; i++) { if
		 * ("dataInizialeDate".equals(propertyNames[i])) { state[i] =
		 * fornitura.getDataInizialeDate(); } } fornitura.setDataFinaleDate(new
		 * Date());
		 */
		setDataIniziale(fornitura, state, propertyNames);
		setDataFinale(fornitura, state, propertyNames);
	}

	private void syncMiDupTitolarita(MiDupTitolarita entity, Object[] state, String[] propertyNames) {
		typefyField(entity, quotaConverter, state, propertyNames, new String[] { "scQuotaNumeratore", "scQuotaDenominatore", "sfQuotaNumeratore", "sfQuotaDenominatore" }, "quota");
		typefyField(entity, codiciDupConverter, state, propertyNames, new String[] { "scCodiceDiritto", "sfCodiceDiritto" }, "codiceDup");
	}

	private void setDataIniziale(MiDupFornitura fornitura, Object[] state, String[] propertyNames) {
		setAsDate(fornitura, "dataIniziale", dateDateConverter, state, propertyNames);

	}

	private void setDataFinale(MiDupFornitura fornitura, Object[] state, String[] propertyNames) {
		setAsDate(fornitura, "dataFinale", dateDateConverter, state, propertyNames);
	}

	private void setDataValiditaAttoDate(Object entity, Object[] state, String[] propertyNames) {
		setAsDate(entity, "dataValiditaAtto", dateDateConverter, state, propertyNames);
	}

	private void setNumeroNotaTrasLong(Object entity, Object[] state, String[] propertyNames) {
		setAsLong(entity, "numeroNotaTras", state, propertyNames);
		// Added commented code by MaX 19/08/2007
		// setAsString(entity, "numeroNotaTras", state, propertyNames);
	}

	private void setAnnoNotaDate(Object entity, Object[] state, String[] propertyNames) {
		setAsDate(entity, "annoNota", yearDateConverter, state, propertyNames);
	}

	private void setAsDate(Object entity, String fieldName, FieldConverter fc, Object[] state, String[] propertyNames) {
		String fieldSrcName = fieldName;
		String fieldDestName = fieldName + "Date";
		typefyField(entity, fc, state, propertyNames, fieldSrcName, fieldDestName);
	}

	private void setAsLong(Object entity, String fieldName, Object[] state, String[] propertyNames) {
		String fieldSrcName = fieldName;
		String fieldDestName = fieldName + "Long";
		typefyField(entity, longConverter, state, propertyNames, fieldSrcName, fieldDestName);
	}

	// Added by MaX 19/08/2007
	private void setAsString(Object entity, String fieldName, Object[] state, String[] propertyNames) {
		String fieldSrcName = fieldName;
		String fieldDestName = fieldName + "String";
		typefyField(entity, stringConverter, state, propertyNames, fieldSrcName, fieldDestName);
	}

	private void typefyField(Object entity, FieldConverter fc, Object[] state, String[] propertyNames, String fieldSrcName, String fieldDestName) {
		int i = 0;
		int previousFieldIndex = -1;
		while (i < propertyNames.length) {
			if ((fieldDestName).equals(propertyNames[i])) {
				previousFieldIndex = i;
				i++;
				while (i < propertyNames.length) {
					if (fieldSrcName.equals(propertyNames[i])) {
						Object currState = state[i];
						if (currState != null) {
							try {
								// If code added by MaX - 22/08/2007 - 
								// E' un workaround per poter inserire nel campo SUC_DUP_NOTA_TRAS.NUMERO_NOTA_TRAS un valore alfanumerico 
								// e poi da questo estrapolare il valore numerico da inserire in SUC_DUP_NOTA_TRAS.NUMERO_NOTA_TRAS_LONG
								if ("numeroNotaTras".equalsIgnoreCase(fieldSrcName)) {
									String numeroNotaAsString = (String) state[i];
									if (numeroNotaAsString != null && numeroNotaAsString.length() == 19) {
										currState = numeroNotaAsString.substring(10, 16);
									}
								}
								Object valBean = fc.convertObject(currState);
								if (valBean != null) {
									BeanUtils.setProperty(entity, fieldDestName, valBean);
								}
								state[previousFieldIndex] = valBean;
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						return;
					}
					i++;
				}
				return;
			}
			if (fieldSrcName.equals(propertyNames[i])) {
				previousFieldIndex = i;
				i++;
				while (i < propertyNames.length) {
					if ((fieldDestName).equals(propertyNames[i])) {
						Object currState = state[previousFieldIndex];
						if (currState != null) {
							try {
								// If code added by MaX - 22/08/2007 - 
								// E' un workaround per poter inserire nel campo SUC_DUP_NOTA_TRAS.NUMERO_NOTA_TRAS un valore alfanumerico 
								// e poi da questo estrapolare il valore numerico da inserire in SUC_DUP_NOTA_TRAS.NUMERO_NOTA_TRAS_LONG
								if (currState != null) {
									System.out.println("Classe=" + currState.getClass() + " Value=" +  currState);
								}
								if ("numeroNotaTrasLong".equalsIgnoreCase(fieldDestName) && (currState instanceof String)) {
									String numeroNotaAsString = (String) currState;
									if (numeroNotaAsString != null && numeroNotaAsString.length() == 19) {
										currState = numeroNotaAsString.substring(10, 16);
									}
								}
								Object valBean = fc.convertObject(currState);
								if (valBean != null) {
									BeanUtils.setProperty(entity, fieldDestName, valBean);
								}
								state[i] = valBean;
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						return;
					}
					i++;
				}
				return;
			}
			i++;
		}
		return;
	}

	private void typefyField(Object entity, FieldConverter fc, Object[] state, String[] propertyNames, String[] fieldSrcName, String fieldDestName) {
		Object[] srcVals = new Object[fieldSrcName.length];
		int i = 0;
		while (i < propertyNames.length) {
			for (int j = 0; j < fieldSrcName.length; j++) {
				if (fieldSrcName[j].equals(propertyNames[i])) {
					srcVals[j] = state[i];
				}
			}
			i++;
		}
		i = 0;
		while (i < propertyNames.length) {
			if (propertyNames[i].equals(fieldDestName)) {
				Object valBean = null;
				try {
					valBean = fc.convertObjects(srcVals);
				} catch (Exception e ) {
					Logger.log().error(this.getClass().getName(), e);
				}
				try {
					if (valBean != null) {
						BeanUtils.setProperty(entity, fieldDestName, valBean);
					}
					state[i] = valBean;
				} catch (HibernateException e) {
					String srcValues = "";
					for (int j = 0; j < srcVals.length; j++) {
						srcValues += srcVals[j] + ",";
					}
					Logger.log().error(this.getClass().getName(), "HibernateException while setting bean (entity=" + entity.getClass() + ") property: " + fieldDestName + " srcVals= " + srcValues + " valBean=" + valBean, e);
					System.exit(1);
					// throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					Logger.log().error(this.getClass().getName(), "Error while setting bean (entity=" + entity.getClass() + ") property: " + fieldDestName + " srcVals= " + srcVals, e);
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					Logger.log().error(this.getClass().getName(), "Error while setting bean (entity=" + entity.getClass() + ") property: " + fieldDestName + " srcVals= " + srcVals, e);
					throw new RuntimeException(e);
				} catch (Throwable e) {
					Logger.log().error(this.getClass().getName(), "Error while setting bean (entity=" + entity.getClass() + ") property: " + fieldDestName + " srcVals= " + srcVals, e);
					throw new RuntimeException(e);
				}
				/*
				 * catch (NoSuchMethodException e) { // TODO Auto-generated catch
				 * block e.printStackTrace(); }
				 */
				break;
			}
			i++;
		}
		return;
	}

	public void afterTransactionCompletion(Transaction tx) {
		if (tx.wasCommitted()) {
			Logger.log().debug(this.getClass().getName(), "Creations: " + creates + ", Updates: " + updates + ", Loads: " + loads + " tx=" + tx);
		}
		updates = 0;
		creates = 0;
		loads = 0;
	}
}
