package it.webred.mui.consolidation;

import it.webred.mui.model.Familiare;
import it.webred.docfa.model.DocfaDap;
import it.webred.mui.model.Possesore;

import java.util.List;

public interface DocfaDapEvaluator {
	public enum MultiPossessoType {
		SINGLE, SEVERAL, REPEATED
	};

	public List<Familiare> getFamiliares(DocfaDap dap);

	public List<Possesore> getPossesores(DocfaDap dap);

	public MultiPossessoType getPossedutoImmobiles(DocfaDap dap);
}
