package it.webred.diogene.querybuilder.validator.tag;

import it.webred.web.validator.ValidatorsHolder;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class ValidationsTag extends TagSupport
{

	private String validatorHolder="";
	private String includeGlobalScript="true";
	@Override
	public int doEndTag() throws JspException
	{
		ValidatorsHolder vh=null;
		try
		{
			vh=(ValidatorsHolder)pageContext.getVariableResolver().resolveVariable(getValidatorHolder());
			if(vh!=null){
				JspWriter out=pageContext.getOut();
				out.print(vh.getValidateScript());
				if("true".equalsIgnoreCase(getIncludeGlobalScript()))out.print(vh.getGlobalScript());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return super.doEndTag();
	}
	public String getValidatorHolder()
	{
		return validatorHolder;
	}
	public void setValidatorHolder(String validatorHolder)
	{
		this.validatorHolder = validatorHolder;
	}
	public String getIncludeGlobalScript()
	{
		return includeGlobalScript;
	}
	public void setIncludeGlobalScript(String includeGlobalScript)
	{
		this.includeGlobalScript = includeGlobalScript;
	}


}
