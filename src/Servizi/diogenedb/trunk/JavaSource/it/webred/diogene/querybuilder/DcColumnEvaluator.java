package it.webred.diogene.querybuilder;

import it.webred.diogene.db.DcAttributes;
import it.webred.diogene.db.model.DcAttribute;
import it.webred.diogene.db.model.DcColumn;

public class DcColumnEvaluator
{
	private DcColumn dcColumn;
	private boolean 
		primaryKey = false, 
		externalKey = false, 
		dataInizioVal = false, 
		dataFineVal = false,
		groupBy = false,
		orderBy = false;
	
	private DcColumnEvaluator() {}
	public DcColumnEvaluator(DcColumn dcColumn)
	{
		this.dcColumn = dcColumn;
		evaluate();
	}
	private void evaluate()
	{
		if (dcColumn != null)
		{
			for (Object item : dcColumn.getDcAttributes())
				if (DcAttributes.KEY.name().equals(((DcAttribute) item).getAttributeSpec()))
					primaryKey = true;
				else if (DcAttributes.EXTERNAL_KEY.name().equals(((DcAttribute) item).getAttributeSpec()))
					externalKey = true;
				else if (DcAttributes.DT_INIZIO_VAL.name().equals(((DcAttribute) item).getAttributeSpec()))
					dataInizioVal = true;
				else if (DcAttributes.DT_FINE_VAL.name().equals(((DcAttribute) item).getAttributeSpec()))
					dataFineVal = true;
			groupBy = !dcColumn.getDvUeGroups().isEmpty();
			orderBy = !dcColumn.getDcExpression().getDvUeOrders().isEmpty();
		}
	}
	
	
	public boolean isDataFineVal()
	{
		return dataFineVal;
	}
	public boolean isDataInizioVal()
	{
		return dataInizioVal;
	}
	public boolean isPrimaryKey()
	{
		return primaryKey;
	}
	public boolean isGroupBy()
	{
		return groupBy;
	}
	public boolean isOrderBy()
	{
		return orderBy;
	}
	public boolean isExternalKey()
	{
		return externalKey;
	}

}
