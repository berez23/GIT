package it.webred.diogene.querybuilder.dataviewer;

public class ColumnHeader

{
    public ColumnHeader()
    {
    }

    private String _label;
    private String _width;
    private boolean _editable;

    public ColumnHeader(String label, String width, boolean editable)
    {
        _label = label;
        _width = width;
        _editable = editable;
    }

    //=========================================================================
    // Getters
    //=========================================================================

    public String getLabel()
    {
        return _label;
    }

    public String getWidth()
    {
        return _width;
    }

    public boolean isEditable()
    {
        return _editable;
    }

    //=========================================================================
    // Getters
    //=========================================================================

    public void setLabel(String label)
    {
        _label = label;
    }

    public void setWidth(String width)
    {
        _width = width;
    }

    public void setEditable(boolean editable)
    {
        _editable = editable;
    }

}
