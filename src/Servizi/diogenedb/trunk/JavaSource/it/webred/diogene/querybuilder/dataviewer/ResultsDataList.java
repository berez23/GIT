package it.webred.diogene.querybuilder.dataviewer;

/**
 * TODO Scrivi descrizione
 * @author Giulio Quaresima
 * @version $Revision: 1.2 $ $Date: 2012/01/18 15:57:15 $
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;



public class ResultsDataList extends SortableList
{
    private DataModel data;
    private DataModel columnHeaders;
    private Integer realRowCount=0;
    private boolean exportable=false;

    private static final int SORT_ASCENDING = 1;
    private static final int SORT_DESCENDING = -1;

    public ResultsDataList()
    {
        super(null);

    }

    //==========================================================================
    // Getters
    //==========================================================================

    public DataModel getData()
    {
        sort(getSort(), isAscending());
        return data;
    }

    void setData(DataModel datamodel)
    {
    	System.out.println("preserved datamodel updated");
    	// just here to see if the datamodel is updated if preservedatamodel=true
    }

    public DataModel getColumnHeaders()
    {
        return columnHeaders;
    }

    //==========================================================================
    // Public Methods
    //==========================================================================

    public Object getColumnValue()
    {
        Object columnValue = null;
        if (data.isRowAvailable() && columnHeaders.isRowAvailable())
        {
            columnValue = ((List)data.getRowData()).get(columnHeaders.getRowIndex());
        }
        return columnValue;
    }

    public void setColumnValue(Object value)
    {
      if (data.isRowAvailable() && columnHeaders.isRowAvailable())
      {
          ((List)data.getRowData()).set(columnHeaders.getRowIndex(), value);
      }
    }

    public String getColumnWidth()
    {
        String columnWidth = null;
        if (data.isRowAvailable() && columnHeaders.isRowAvailable())
        {
            columnWidth = ((ColumnHeader)columnHeaders.getRowData()).getWidth();
        }
        return columnWidth;
    }

    public boolean isValueModifiable()
    {
        boolean valueModifiable = false;
        if (data.isRowAvailable() && columnHeaders.isRowAvailable())
        {
            valueModifiable = ((ColumnHeader)columnHeaders.getRowData()).isEditable();
        }
        return valueModifiable;
    }
    public void updateDataValue(List<Object[]> lista)
	{
        // crea le informazioni per l'header
        List headerList = new ArrayList();
        if (lista.size()>0){
        	for (Object riga : lista.get(0))
			{
        		headerList.add(new ColumnHeader(riga.toString(),"150",false));		
			}
        }
        columnHeaders = new ListDataModel(headerList);

        // creo la lista di liste (data)
        List<Object[]> rowList = new ArrayList<Object[]>();
        List rowListDest= new ArrayList();
        boolean firstRow=true;
        for (Object[] riga : lista)
		{
            if(!firstRow){
            	List rigaList = Arrays.asList(riga);
                rowListDest.add(rigaList);
            }
            firstRow=false;
		}
        data = new ListDataModel(rowListDest);
		
	}
    //==========================================================================
    // Protected Methods
    //==========================================================================

    protected boolean isDefaultAscending(String sortColumn)
    {
        return true;
    }

    protected void sort(final String column, final boolean ascending)
    {
        if (column != null)
        {
            int columnIndex = getColumnIndex(column);
            int direction = (ascending) ? SORT_ASCENDING : SORT_DESCENDING;
            sort(columnIndex, direction);
        }
    }

    protected void sort(final int columnIndex, final int direction)
    {
        Comparator comparator = new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                int result = 0;
                Object column1 = ((List)o1).get(columnIndex);
                Object column2 = ((List)o2).get(columnIndex);
                if (column1 == null && column2 != null)
                    result = -1;
                else if (column1 == null && column2 == null)
                    result = 0;
                else if (column1 != null && column2 == null)
                    result = 1;
                else
                    result = ((Comparable)column1).compareTo(column2) * direction;
                return result;
            }
        };
        Collections.sort((List)data.getWrappedData(), comparator);
    }

    //==========================================================================
    // Private Methods
    //==========================================================================

    private int getColumnIndex(final String columnName)
    {
        int columnIndex = -1;
        List headers = (List) columnHeaders.getWrappedData();
        for (int i=0;i<headers.size() && columnIndex==-1;i++)
        {
            ColumnHeader header = (ColumnHeader) headers.get(i);
            if (header.getLabel().equals(columnName))
            {
                columnIndex = i;
            }
        }
        return columnIndex;
    }

	public Integer getRealRowCount()
	{
		return realRowCount;
	}

	public void setRealRowCount(Integer realRowCount)
	{
		this.realRowCount = realRowCount;
	}

	public boolean isExportable()
	{
		return exportable;
	}

	public void setExportable(boolean exportable)
	{
		this.exportable = exportable;
	}

}
