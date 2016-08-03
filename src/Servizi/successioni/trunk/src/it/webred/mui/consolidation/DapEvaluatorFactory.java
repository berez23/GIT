package it.webred.mui.consolidation;

import it.webred.mui.model.Coniuge;
import it.webred.mui.model.Familiare;
import it.webred.mui.model.MiConsDap;
import it.webred.mui.model.Possesore;
import it.webred.mui.model.Residenza;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import net.skillbill.logging.Logger;

public class DapEvaluatorFactory {

	private DapEvaluatorFactory() {

	}

	public static DapEvaluatorFactory getInstance() {
		return new DapEvaluatorFactory();
	}
	public DapEvaluator getDapEvaluator(){
		try {
			return new DapEvaluatorImpl();
		} catch (ParseException e) {
			Logger.log().error(this.getClass().getName(),
					"error while getting DapEvaluatorImpl ", e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			Logger.log().error(this.getClass().getName(),
					"error while getting DapEvaluatorImpl ", e);
			throw new RuntimeException(e);
		}
	}
}

class DapEvaluatorFakeImpl implements DapEvaluator {
	public List<Familiare> getFamiliares(MiConsDap dap){
		return null;
	}
	public List<Possesore> getPossesores(MiConsDap dap){
		return null;
	}
	public MultiPossessoType getPossedutoImmobiles(MiConsDap dap){
		return MultiPossessoType.SINGLE;
	}
	public Coniuge getConiugeInVita(MiConsDap dap) {
		return null;
	}
	public List<Possesore> getQuote(MiConsDap dap) {
		// TODO Auto-generated method stub
		return null;
	}
}