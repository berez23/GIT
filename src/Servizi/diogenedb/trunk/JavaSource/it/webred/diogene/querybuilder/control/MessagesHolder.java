package it.webred.diogene.querybuilder.control;

import it.webred.diogene.querybuilder.QueryBuilderMessage;

import java.util.List;

public interface MessagesHolder
{
	/**
	 * @return
	 * La lista di tutti i messaggi accodati.
	 */
	public List<QueryBuilderMessage> getQBMessages();
	/**
	 * Accoda un messaggio.
	 * @param message
	 */
	public void appendQBMessage(QueryBuilderMessage message);
}
