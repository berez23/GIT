package it.webred.mui.http;

import it.webred.mui.input.MuiFornituraParser;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import net.skillbill.logging.Logger;

import org.hibernate.Query;

public class WhereClause implements Serializable {
	public enum ClauseType {
		EQUAL_NUM, EQUAL, NOT_EQUAL, CONTAINS, STARTS_WITH, ENDS_WITH, MORE_THAN, LESS_THAN, MORE_OR_EQUAL_THAN, LESS_OR_EQUAL_THAN, AFTER_THAN, BEFORE_THAN, YEAR_GREATER_OR_EQUAL_THAN, YEAR_SMALLER_OR_EQUAL_THAN, YEAR_EQUAL, DATE_GREATER_OR_EQUAL_THAN, DATE_SMALLER_OR_EQUAL_THAN, DATE_EQUAL, IS_IN,
	};

	String _entity;

	String _field;

	String _fieldTag;
	
	String _fieldValue = null;

	boolean _consider;

	ClauseType _cType = ClauseType.EQUAL;

	/*
	public WhereClause(String field, String entity, String fieldValue,ClauseType cType) {
		_consider = true;
		_field = field;
		_entity = entity;
		_fieldValue = fieldValue;
		_cType = cType;
	}
	public WhereClause(String field, String entity, String fieldValue, HttpServletRequest req) {
		_consider = checkQueryParameter(req,fieldValue, field);
		_field = field;
		_entity = entity;
		_fieldValue = fieldValue;
	}
*/
	public WhereClause(String field, String entity, HttpServletRequest req) {
		_consider = checkQueryParameter(req, field);
		_entity = entity;
		// i campi uguali di entita diverse vengono distinti aggiungendo l'entita che qui va tolta
		_field = field.endsWith("@"+_entity)?field.substring(0,field.lastIndexOf("@"+_entity)):field;
		_fieldTag =  _field.indexOf(".") == -1 ?_field:_field.replace(".","_");
		Logger.log().info("_field ", _field);
		_fieldValue = req.getParameter(field);
		if (checkQueryParameter(req, "clause_" + field)) {
			String clauseField = req.getParameter("clause_" + field);
			if ("EQUAL_NUM".equals(clauseField)) {
				_fieldValue = _fieldValue.replace(',', '.');
				_cType = ClauseType.EQUAL_NUM;
			}
			if ("EQUAL".equals(clauseField)) {
				_cType = ClauseType.EQUAL;
			}
			if ("NOT_EQUAL".equals(clauseField)) {
				_cType = ClauseType.NOT_EQUAL;
			}
			if ("CONTAINS".equals(clauseField)) {
				_cType = ClauseType.CONTAINS;
			}
			if ("STARTS_WITH".equals(clauseField)) {
				_cType = ClauseType.STARTS_WITH;
			}
			if ("ENDS_WITH".equals(clauseField)) {
				_cType = ClauseType.ENDS_WITH;
			}
			if ("MORE_THAN".equals(clauseField)) {
				_fieldValue = _fieldValue.replace(',', '.');
				_cType = ClauseType.MORE_THAN;
			}
			if ("LESS_THAN".equals(clauseField)) {
				_fieldValue = _fieldValue.replace(',', '.');
				_cType = ClauseType.LESS_THAN;
			}
			if ("MORE_OR_EQUAL_THAN".equals(clauseField)) {
				_fieldValue = _fieldValue.replace(',', '.');
				_cType = ClauseType.MORE_OR_EQUAL_THAN;
			}
			if ("LESS_OR_EQUAL_THAN".equals(clauseField)) {
				_fieldValue = _fieldValue.replace(',', '.');
				_cType = ClauseType.LESS_OR_EQUAL_THAN;
			}
			if ("AFTER_THAN".equals(clauseField)) {
				_cType = ClauseType.AFTER_THAN;
			}
			if ("BEFORE_THAN".equals(clauseField)) {
				_cType = ClauseType.BEFORE_THAN;
			}
			if ("YEAR_GREATER_OR_EQUAL_THAN".equals(clauseField)) {
				_cType = ClauseType.YEAR_GREATER_OR_EQUAL_THAN;
			}
			if ("YEAR_EQUAL".equals(clauseField)) {
				_cType = ClauseType.YEAR_EQUAL;
			}
			if ("YEAR_SMALLER_OR_EQUAL_THAN".equals(clauseField)) {
				_cType = ClauseType.YEAR_SMALLER_OR_EQUAL_THAN;
			}
			if ("DATE_GREATER_OR_EQUAL_THAN".equals(clauseField)) {
				_cType = ClauseType.DATE_GREATER_OR_EQUAL_THAN;
			}
			if ("DATE_EQUAL".equals(clauseField)) {
				_cType = ClauseType.DATE_EQUAL;
			}
			if ("DATE_SMALLER_OR_EQUAL_THAN".equals(clauseField)) {
				_cType = ClauseType.DATE_SMALLER_OR_EQUAL_THAN;
			}
			if ("IS_IN".equals(clauseField)) {
				_cType = ClauseType.IS_IN;
			}
			if (checkQueryParameter(req, "divideByFactor_" + field)) {
				try {
					Integer _divideByFactor = Integer.valueOf(req
							.getParameter("divideByFactor_" + field));
					BigDecimal numVal = (new BigDecimal(_fieldValue))
							.divide(new BigDecimal(_divideByFactor));
					_fieldValue = numVal.toString();
				} catch (Throwable e) {
				}

			}
			if (checkQueryParameter(req, "multiplyByFactor_" + field)) {
				try {
					Integer _divideByFactor = Integer.valueOf(req
							.getParameter("multiplyByFactor_" + field));
					BigDecimal numVal = (new BigDecimal(_fieldValue))
							.multiply(new BigDecimal(_divideByFactor));
					_fieldValue = numVal.toString();
				} catch (Throwable e) {
				}

			}
		}
	}

	public String addClausetoQuery(String query) {
		if (_consider) {
			switch (_cType) {
			case EQUAL_NUM:
			case EQUAL:
				query += " and " + _entity + "." + _field + " = :" + _fieldTag
						+ " ";
				break;
			case NOT_EQUAL:
				query += " and " + _entity + "." + _field + " != :" + _fieldTag
						+ " ";
				break;
			case CONTAINS:
			case ENDS_WITH:
			case STARTS_WITH:
				query += " and " + _entity + "." + _field + " LIKE :" + _fieldTag
						+ " ";
				break;
			case YEAR_GREATER_OR_EQUAL_THAN:
				query += " and " + _entity + "." + _field + "Date >= :"
						+ _fieldTag + " ";
				break;
			case YEAR_SMALLER_OR_EQUAL_THAN:
				query += " and " + _entity + "." + _field + "Date <= :"
						+ _fieldTag + " ";
				break;
			case YEAR_EQUAL:
				query += " and " + _entity + "." + _field + "Date = :" + _fieldTag
						+ " ";
				break;
			case DATE_GREATER_OR_EQUAL_THAN:
				query += " and " + _entity + "." + _field + "Date >= :"
						+ _fieldTag + " ";
				break;
			case DATE_SMALLER_OR_EQUAL_THAN:
				query += " and " + _entity + "." + _field + "Date <= :"
						+ _fieldTag + " ";
				break;
			case DATE_EQUAL:
				query += " and " + _entity + "." + _field + "Date = :" + _fieldTag
						+ " ";
				break;
			case MORE_OR_EQUAL_THAN:
				query += " and " + _entity + "." + _field + " >= :" + _fieldTag
						+ " ";
				break;
			case MORE_THAN:
				query += " and " + _entity + "." + _field + " > :" + _fieldTag
						+ " ";
				break;
			case LESS_OR_EQUAL_THAN:
				query += " and " + _entity + "." + _field + " <= :" + _fieldTag
						+ " ";
				break;
			case LESS_THAN:
				query += " and " + _entity + "." + _field + " < :" + _fieldTag
						+ " ";
				break;
			}
		}
		return query;
	}

	public void setParameter(Query query) {
		if (_consider) {
			switch (_cType) {
			case NOT_EQUAL:
			case EQUAL:
				query.setString(_fieldTag, _fieldValue.toUpperCase().trim());
				break;
			case CONTAINS:
				query.setString(_fieldTag, "%" + _fieldValue.toUpperCase().trim()
						+ "%");
				break;
			case STARTS_WITH:
				query.setString(_fieldTag, _fieldValue.toUpperCase().trim() + "%");
				break;
			case ENDS_WITH:
				query.setString(_fieldTag, "%" + _fieldValue.toUpperCase().trim());
				break;
			case YEAR_EQUAL:
			case YEAR_SMALLER_OR_EQUAL_THAN:
			case YEAR_GREATER_OR_EQUAL_THAN:
				try {
					query.setDate(_fieldTag, MuiFornituraParser.yearParser
							.parse(_fieldValue));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case DATE_EQUAL:
			case DATE_SMALLER_OR_EQUAL_THAN:
			case DATE_GREATER_OR_EQUAL_THAN:
				try {
					query.setDate(_fieldTag, MuiFornituraParser.dateParser
							.parse(_fieldValue.replaceAll("/", "")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case EQUAL_NUM:
			case MORE_OR_EQUAL_THAN:
			case MORE_THAN:
			case LESS_OR_EQUAL_THAN:
			case LESS_THAN:
				try {
					query.setBigDecimal(_fieldTag, new BigDecimal(_fieldValue));
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
	}

	public static boolean checkQueryParameter(HttpServletRequest req,
			String param) {
		return (req.getParameter(param) != null
				&& !req.getParameter(param).trim().equals("") && !"IGNORE"
				.equals(req.getParameter("clause_" + param)));
	}

	public static boolean checkQueryParameter(HttpServletRequest req,String fieldValue,
			String param) {
		return (fieldValue != null
				&& !fieldValue.trim().equals("") && !"IGNORE"
				.equals(req.getParameter("clause_" + param)));
	}

	public String getEntity() {
		return _entity;
	}
}