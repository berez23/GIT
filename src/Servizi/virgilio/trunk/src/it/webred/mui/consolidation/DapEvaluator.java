package it.webred.mui.consolidation;

import it.webred.mui.model.Familiare;
import it.webred.mui.model.MiConsDap;
import it.webred.mui.model.Possesore;

import java.util.List;

public interface DapEvaluator {
	public enum MultiPossessoType {
		SINGLE, SEVERAL, REPEATED
	};

	public List<Familiare> getFamiliares(MiConsDap dap);

	public List<Possesore> getPossesores(MiConsDap dap);

	public MultiPossessoType getPossedutoImmobiles(MiConsDap dap);
}
