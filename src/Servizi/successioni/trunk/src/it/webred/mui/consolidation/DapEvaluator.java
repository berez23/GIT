package it.webred.mui.consolidation;

import it.webred.mui.model.Familiare;
import it.webred.mui.model.MiConsDap;
import it.webred.mui.model.Possesore;
import it.webred.mui.model.Coniuge;

import java.util.List;

public interface DapEvaluator {
	public enum MultiPossessoType {
		SINGLE, SEVERAL, REPEATED
	};

	public List<Familiare> getFamiliares(MiConsDap dap);

	public List<Possesore> getPossesores(MiConsDap dap);

	public List<Possesore> getQuote(MiConsDap dap);

	public MultiPossessoType getPossedutoImmobiles(MiConsDap dap);

	public Coniuge getConiugeInVita(MiConsDap dap);

}
