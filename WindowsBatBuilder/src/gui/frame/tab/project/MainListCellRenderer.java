package gui.frame.tab.project;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import project.data.*;

public class MainListCellRenderer extends DefaultListCellRenderer 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public Component getListCellRendererComponent(
    		JList<?> list, 
    		Object value, int index,
    		boolean isSelected,
            boolean cellHasFocus) 
    {
        if (value == null) 
        {
            value = "";
        } 
        else 
        {
        	Data data = (Data)value;

        	value = data.getName();     		
    	
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
