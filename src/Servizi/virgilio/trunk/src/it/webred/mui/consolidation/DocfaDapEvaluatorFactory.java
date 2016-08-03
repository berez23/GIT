package it.webred.mui.consolidation;

import it.webred.mui.model.Familiare;
import it.webred.docfa.model.DocfaDap;
import it.webred.mui.model.Possesore;

import java.text.ParseException;
import java.util.List;

import net.skillbill.logging.Logger;

public class DocfaDapEvaluatorFactory {

	private DocfaDapEvaluatorFactory() {

	}

	public static DocfaDapEvaluatorFactory getInstance() {
		return new DocfaDapEvaluatorFactory();
	}
	public DocfaDapEvaluator getDapEvaluator(){
		try {
			return new DocfaDapEvaluatorImpl();
		} catch (ParseException e) {
			Logger.log().error(this.getClass().getName(),
					"error while getting DapEvaluatorImpl ", e);
			throw new RuntimeException(e);
		}
	}
}

class DocfaDapEvaluatorFakeImpl implements DocfaDapEvaluator {
	public List<Familiare> getFamiliares(DocfaDap dap){
		return null;
	}
	public List<Possesore> getPossesores(DocfaDap dap){
		return null;
	}
	public MultiPossessoType getPossedutoImmobiles(DocfaDap dap){
		return MultiPossessoType.SINGLE;
	}
}