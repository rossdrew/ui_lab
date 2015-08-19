package rox.gui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * @author Ross W. Drew
 *
 */
public class ConsolePanel extends JTextArea
{
	private InternalOutputStream internalStream = new InternalOutputStream();
	
	public PrintStream getStream()
	{
		return new PrintStream(this.internalStream, true);
	}
	
	private void updateTextPane(final String text) 
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				Document doc = getDocument();
				
				try 
				{
					doc.insertString(doc.getLength(), text, null);
				} 
				catch (BadLocationException e) 
				{
					throw new RuntimeException(e);
				}
				
				setCaretPosition(doc.getLength() - 1);
			}
		});
	}

	private class InternalOutputStream extends OutputStream
	{
	    @Override
	    public void write(final int b) throws IOException 
	    {
	      updateTextPane(String.valueOf((char) b));
	    }
	 
	    @Override
	    public void write(byte[] b, int off, int len) throws IOException 
	    {
	      updateTextPane(new String(b, off, len));
	    }
	 
	    @Override
	    public void write(byte[] b) throws IOException 
	    {
	      write(b, 0, b.length);
	    }
	}
}
