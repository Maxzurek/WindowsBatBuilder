package gui.frame.tab.project;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import project.data.command.Command;

class CommandCellRenderer extends DefaultListCellRenderer 
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
            Command commandObject = (Command) value;
            value = commandObject.getTooltip();
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}