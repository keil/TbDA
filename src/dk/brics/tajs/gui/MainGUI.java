package dk.brics.tajs.gui;

import static dk.brics.tajs.util.Collections.newList;
import static java.awt.EventQueue.invokeLater;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.SolverSynchronizer;
import dk.brics.tajs.util.Loader;

public class MainGUI {
	
	public static void main(String[] args) {
		open();
	}

	static void open() {
		invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					new Window().setVisible(true);
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		});
	}

	private static class Window extends JFrame {

		JTextField sourcefile_textfield;
		JCheckBox localpathsensitivity_checkbox; 	
		JCheckBox contextsensitivity_checkbox; 	
		JCheckBox recency_checkbox; 	
		JCheckBox modified_checkbox; 
		JCheckBox exceptions_checkbox; 
		JCheckBox gc_checkbox; 
		JCheckBox lazy_checkbox; 
		JCheckBox copyonwrite_checkbox; 
		JCheckBox unsound_checkbox; 
		JCheckBox debug_checkbox; 
		JCheckBox newflow_checkbox; 
		JCheckBox states_checkbox; 
		JCheckBox timing_checkbox; 
		JCheckBox dom_checkbox; 
		JCheckBox mozilla_checkbox; 

		JTextArea output_textarea;
		JFileChooser sourcefile_chooser;
		JButton run_button;
		JButton step_button;
		JButton pause_button;
		JButton stop_button;
		JTabbedPane tab_pane;
		JTextArea source_textarea;
		JPanel flowgraph_panel;
		JPanel callgraph_panel;
		JPanel stategraph_panel;
		PrintWriter out;
		
		Thread worker;

		boolean loading;
		boolean source_modified;

		SolverSynchronizer sync = new SolverSynchronizer() {
			@Override
			public void waiting() {
				pause_button.setEnabled(false);
				stop_button.setEnabled(true);
				run_button.setEnabled(true);
				step_button.setEnabled(true);
			}
		};

		private Window() {
			JLabel sourcefile_label = new JLabel("Source path/URL: ");
			sourcefile_textfield = new JTextField(40);
			JButton sourcefileselect_button = new JButton("Select file...");
			JButton sourcefileload_button = new JButton("Load");

			run_button = new JButton("Run");
			step_button = new JButton("Step");
			pause_button = new JButton("Pause");
			stop_button = new JButton("Stop");
			JButton reset_button = new JButton("Reset");
			JButton about_button = new JButton("About");
			JButton exit_button = new JButton("Exit");

			localpathsensitivity_checkbox = new JCheckBox("Local path sensitivity", true); 
			contextsensitivity_checkbox = new JCheckBox("Context sensitivity", true); 
			recency_checkbox = new JCheckBox("Recency abstraction", true); 
			modified_checkbox = new JCheckBox("Modified flags", true); 
			exceptions_checkbox = new JCheckBox("Implicit exception flow", true); 
			gc_checkbox = new JCheckBox("Abstract garbage collection", true); 
			lazy_checkbox = new JCheckBox("Lazy propagation", true); 
			copyonwrite_checkbox = new JCheckBox("Copy-on-write", true); 
			unsound_checkbox = new JCheckBox("Unsound assumptions"); 
			debug_checkbox = new JCheckBox("Debug information"); 
			newflow_checkbox = new JCheckBox("Output new function entry flow"); 
			states_checkbox = new JCheckBox("Output intermediate states"); 
			timing_checkbox = new JCheckBox("Output analysis time"); 
			dom_checkbox = new JCheckBox("Mozilla DOM browser model"); 
			mozilla_checkbox = new JCheckBox("Mozilla deviations"); 

			output_textarea = new JTextArea();
			output_textarea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, output_textarea.getFont().getSize()));
			output_textarea.setEditable(false);

			sourcefile_chooser = new JFileChooser(System.getProperty("user.dir"));
			sourcefile_chooser.setMultiSelectionEnabled(true);
			sourcefile_chooser.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.isDirectory() || f.getName().endsWith(".js") || f.getName().endsWith(".html")
					 || f.getName().endsWith(".xhtml") || f.getName().endsWith(".htm");
				}
				@Override
				public String getDescription() {
					return "JavaScript or HTML/XHTML files";
				}
			});
			
			JPanel logo_panel = new JPanel(new BorderLayout());
			JLabel logo_label = new JLabel("TAJS", JLabel.LEFT);
			logo_label.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
			logo_label.setFont(new Font("Lucida Sans", Font.BOLD, 42));
			logo_panel.add(logo_label);

			JPanel sourcefile_panel = new JPanel();
			sourcefile_panel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.NONE;
			c.gridx = 0;
			c.gridy = 0;
			c.anchor = GridBagConstraints.WEST;
			sourcefile_panel.add(sourcefile_label, c);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.weightx = 1;
			sourcefile_panel.add(sourcefile_textfield, c);
			c.fill = GridBagConstraints.NONE;
			c.gridx = 2;
			c.weightx = 0;
			sourcefile_panel.add(sourcefileselect_button, c);
			c.gridx = 3;
			c.weightx = 0;
			sourcefile_panel.add(sourcefileload_button, c);
			c.fill = GridBagConstraints.NONE;
			c.gridx = 4;
			c.insets = new Insets(0, 0, 0, 0);
			c.fill = GridBagConstraints.NONE;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.fill = GridBagConstraints.NONE;
			c.gridx = 4;
			c.insets = new Insets(0, 0, 0, 0);
			sourcefile_panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

			JPanel option_panel = new JPanel();
			option_panel.setLayout(new GridBagLayout());
			c.fill = GridBagConstraints.NONE;
			c.insets = new Insets(0, 0, 0, 0);
			c.gridx = 0;
			c.gridy = 0;
			option_panel.add(localpathsensitivity_checkbox, c);
			c.gridx = 1;
			option_panel.add(contextsensitivity_checkbox, c);
			c.gridx = 2;
			option_panel.add(recency_checkbox, c);
			c.gridx = 3;
			option_panel.add(modified_checkbox, c);
			c.gridx = 0;
			c.gridy = 1;
			option_panel.add(exceptions_checkbox, c);
			c.gridx = 1;
			option_panel.add(gc_checkbox, c);
			c.gridx = 2;
			option_panel.add(lazy_checkbox, c);
			c.gridx = 3;
			option_panel.add(copyonwrite_checkbox, c);
			c.gridx = 0;
			c.gridy = 2;
			option_panel.add(unsound_checkbox, c);
			c.gridx = 1;
			option_panel.add(debug_checkbox, c);
			c.gridx = 2;
			option_panel.add(newflow_checkbox, c);
			c.gridx = 3;
			option_panel.add(states_checkbox, c);
			c.gridx = 0;
			c.gridy = 3;
			option_panel.add(timing_checkbox, c);
			c.gridx = 1;
			option_panel.add(dom_checkbox, c);
			c.gridx = 2;
			option_panel.add(mozilla_checkbox, c);
			option_panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

			JPanel command_panel = new JPanel();
			command_panel.setLayout(new GridBagLayout());
			c.anchor = GridBagConstraints.CENTER;
			c.gridx = 0;
			c.insets = new Insets(0, 5, 0, 0);
			command_panel.add(run_button, c);
			c.gridx = 1;
			command_panel.add(step_button, c);
			c.gridx = 2;
			command_panel.add(pause_button, c);
			c.gridx = 3;
			command_panel.add(stop_button, c);
			c.gridx = 4;
			c.insets = new Insets(0, 50, 0, 0);
			command_panel.add(about_button, c);
			c.gridx = 5;
			c.insets = new Insets(0, 5, 0, 0);
			command_panel.add(reset_button, c);
			c.gridx = 6;
			command_panel.add(exit_button, c);
			command_panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
			pause_button.setEnabled(false);
			stop_button.setEnabled(false);

			JPanel source_panel = new JPanel();
			source_panel.setLayout(new BoxLayout(source_panel, BoxLayout.Y_AXIS));
			source_textarea = new LineNumberedTextArea();
			JScrollPane source_scrollpane = new JScrollPane(source_textarea, 
					JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			source_panel.add(sourcefile_panel);
			source_panel.add(source_scrollpane);
			sourcefile_panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, sourcefile_panel.getHeight()));

			flowgraph_panel = new JPanel();
			callgraph_panel = new JPanel();
			stategraph_panel = new JPanel();

			tab_pane = new JTabbedPane();
			tab_pane.addTab("Source", source_panel);
			tab_pane.addTab("Flow graph", flowgraph_panel); // TODO: flow graph
			tab_pane.addTab("Call graph", callgraph_panel); // TODO: call graph
			tab_pane.addTab("State graph", stategraph_panel); // TODO: state graph
			tab_pane.setPreferredSize(new Dimension(400, 300));

			JScrollPane output_pane = new JScrollPane(output_textarea, 
					JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			output_pane.setPreferredSize(new Dimension(400, 200));
			output_pane.setBorder(
					BorderFactory.createCompoundBorder(
							BorderFactory.createCompoundBorder(
									BorderFactory.createTitledBorder("Output"),
									BorderFactory.createEmptyBorder(5,5,5,5)),
									output_pane.getBorder()));
			out = new PrintWriter(new TextAreaWriter(output_textarea)); 
			System.setOut(new PrintStream(new WriterOutputStream(out)));
			
			JSplitPane split_pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tab_pane, output_pane);
			tab_pane.setMinimumSize(new Dimension(500, 45));
			output_pane.setMinimumSize(new Dimension(500, 80));
			JPanel split_panel = new JPanel(new BorderLayout());
			split_panel.add(split_pane);

			setTitle("TAJS - Type Analyzer for JavaScript");
			setMinimumSize(new Dimension(700, 450));
			getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
			add(logo_panel);
			add(option_panel);
			add(command_panel);
			add(split_panel);
			logo_panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, logo_panel.getHeight()));
			option_panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, option_panel.getHeight()));
			command_panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, command_panel.getHeight()));

			pack();

			final Component frame = this;
			setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					exit();
				}
			});
			about_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(frame, 
							"TAJS - Type Analyzer for JavaScript\n" +
							"Copyright (C) 2009 Anders M\u00F8ller, Simon Holm Jensen, Peter Thiemann\n\n" +
							"Please see http://www.brics.dk/TAJS/ for information about this software.", 
							"TAJS", JOptionPane.INFORMATION_MESSAGE);
				}
			});
			exit_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					exit();
				}
			});
			reset_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (JOptionPane.showConfirmDialog(Window.this, "Reset everything?", 
							"TAJS", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						localpathsensitivity_checkbox.setSelected(true); 
						contextsensitivity_checkbox.setSelected(true); 
						recency_checkbox.setSelected(true); 
						modified_checkbox.setSelected(true); 
						exceptions_checkbox.setSelected(true); 
						gc_checkbox.setSelected(true); 
						lazy_checkbox.setSelected(true); 
						copyonwrite_checkbox.setSelected(true); 
						unsound_checkbox.setSelected(false);
						debug_checkbox.setSelected(false);
						newflow_checkbox.setSelected(false);
						states_checkbox.setSelected(false);
						timing_checkbox.setSelected(false);
						dom_checkbox.setSelected(false);
						mozilla_checkbox.setSelected(false);
						tab_pane.setSelectedIndex(0);
						if (sync.isActive())
							stopWorker();
						source_textarea.setText("");
						sourcefile_textfield.setText("");
						output_textarea.setText("");
					}
				}
			});
			sourcefileselect_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (sourcefile_chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
						String src = sourcefile_chooser.getSelectedFile().getPath();
						sourcefile_textfield.setText(src);
						load();
					}
				}    	
			});
			sourcefileload_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					load();
				}
			});
			run_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					action("run");					
				}
			});
			step_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					action("step");
				}
			});
			source_textarea.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void changedUpdate(DocumentEvent e) {
					modified();
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					modified();
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					modified();
				}
				
				private void modified() {
					source_modified = true;
					if (!loading && sourcefile_textfield.getText().length() > 0) 
						sourcefile_textfield.setText("");
				}
			});
			output_textarea.addMouseListener(new MouseInputAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					int offset = output_textarea.viewToModel(e.getPoint());
					while (offset<output_textarea.getText().length()) {
						char ch = output_textarea.getText().charAt(offset);
						if (ch == '\n')
							return;
						if (ch >= '0' && ch < '9')
							break;
						offset++;
					}
					int begin = offset;
					while (begin>0) {
						char ch = output_textarea.getText().charAt(begin);
						if (ch < '0' || ch > '9')
							break;
						begin--;
					}
					if (begin+1 < output_textarea.getText().length() && output_textarea.getText().charAt(begin) == ':') {
						begin++;
						int end = begin;
						while (end<output_textarea.getText().length()) {
							char ch = output_textarea.getText().charAt(end);
							if (ch < '0' || ch > '9')
								break;
							end++;
						}
						if (end > begin) {
							int line = Integer.parseInt(output_textarea.getText().substring(begin, end));
							try {
								source_textarea.setCaretPosition(source_textarea.getLineEndOffset(line-1));
								source_textarea.moveCaretPosition(source_textarea.getLineStartOffset(line-1));
								source_textarea.requestFocus();
							} catch (BadLocationException e1) {
								// ignore
							}
						}
					}
				}
			});
			output_textarea.setToolTipText("click on a source location to jump to that location");

			sourcefile_textfield.setToolTipText("location of the source files (JavaScript or HTML/XHTML)");
			sourcefile_label.setToolTipText(sourcefile_textfield.getToolTipText());
			sourcefileselect_button.setToolTipText("select a source file");
			localpathsensitivity_checkbox.setToolTipText("if checked, enable local path sensitivity");
			contextsensitivity_checkbox.setToolTipText("if checked, enable context sensitivity");
			recency_checkbox.setToolTipText("if checked, enable recency abstraction");
			modified_checkbox.setToolTipText("if checked, enable modified flags");
			exceptions_checkbox.setToolTipText("if checked, enable implicit exception flow");
			gc_checkbox.setToolTipText("if checked, enable abstract garbage collection");
			lazy_checkbox.setToolTipText("if checked, enable lazy propagation");
			copyonwrite_checkbox.setToolTipText("if checked, enable copy-on-write");
			unsound_checkbox.setToolTipText("if checked, enable unsound assumptions");
			debug_checkbox.setToolTipText("if checked, output debug information");
			newflow_checkbox.setToolTipText("if checked, output new function entry flow");
			states_checkbox.setToolTipText("if checked, output intermediate states");
			timing_checkbox.setToolTipText("if checked, output analysis time");
			dom_checkbox.setToolTipText("if checked, enable Mozilla DOM browser model");
			mozilla_checkbox.setToolTipText("if checked, enable Mozilla deviations");
			run_button.setToolTipText("run the analysis");
			step_button.setToolTipText("single step");
			pause_button.setToolTipText("pause analysis");
			stop_button.setToolTipText("stop analysis");
			about_button.setToolTipText("information about this tool");
			reset_button.setToolTipText("reset options, input, and output");
		}

		@Override
		public void setVisible(boolean b) {
			if (b) {
				Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
				setLocation((screensize.width-getWidth())/2, (screensize.height-getHeight())/2);
			}
			super.setVisible(b);
		}

		private synchronized void action(final String action) {
			sync.setSingleStep(action.equals("step"));
			if (sync.isActive()) {
				run_button.setEnabled(false);
				step_button.setEnabled(false);
				pause_button.setEnabled(action.equals("run"));
				stop_button.setEnabled(true);
				sync.notifyRunOrSingleStep();
			} else {
				sync.setActive(true);
				new SwingWorker<Void,Void>() {

					@Override
					protected Void doInBackground() throws Exception {
						worker = Thread.currentThread();
						pause_button.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								sync.setSingleStep(true);
								pause_button.setEnabled(false);
							}
						});
						stop_button.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								stopWorker();			
							}
						});
						run_button.setEnabled(false);
						step_button.setEnabled(false);
						try {
							String source = source_textarea.getText();
							if (source.trim().length() == 0) {
								fail("No input file!");
								return null;
							}
							pause_button.setEnabled(action.equals("run"));
							stop_button.setEnabled(true);
							analyze(source);
						} catch (AbortException ex) {
							out.println("- execution aborted!");
						} catch (IllegalArgumentException ex) {
							fail(ex.getMessage());
						} catch (Exception ex) {
							fail(ex.toString());
						} finally {
							pause_button.setEnabled(false);
							stop_button.setEnabled(false);
							run_button.setEnabled(true);
							step_button.setEnabled(true);
							sync.setActive(false);
						}
						return null;
					}
				}.execute();
			}
		}

		private void fail(String msg) {
			JOptionPane.showMessageDialog(this, "Error: " + msg, 
					"TAJS", JOptionPane.ERROR_MESSAGE);
		}

		private void exit() {
			if (JOptionPane.showConfirmDialog(this, "You really want to quit?", 
					"TAJS", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				System.exit(0);
		}
		
		private void load() {
			if (sourcefile_textfield.getText().trim().length() == 0)
				fail("No file specified!");
			else 
				try {
					loading = true;
					source_textarea.setText(Loader.getString(sourcefile_textfield.getText(), null)); // TODO: support multiple source files
					source_modified = false;
				} catch (IOException ex) {
					fail("Unable to load file: " + ex.getMessage());
				} finally {
					loading = false;
				}
		}
		
		@SuppressWarnings("cast")
		private void analyze(String source) {
			output_textarea.setText("");
			Options.reset();
			if (!localpathsensitivity_checkbox.isSelected())
				Options.set("-no-local-path-sensitivity");
			if (!contextsensitivity_checkbox.isSelected())
				Options.set("-no-context-sensitivity");
			if (!recency_checkbox.isSelected())
				Options.set("-no-recency");
			if (!modified_checkbox.isSelected()) 
				Options.set("-no-modified");
			if (!exceptions_checkbox.isSelected()) 
				Options.set("-no-exceptions");
			if (!gc_checkbox.isSelected()) 
				Options.set("-no-gc");
			if (!lazy_checkbox.isSelected()) 
				Options.set("-no-lazy");
			if (!copyonwrite_checkbox.isSelected()) 
				Options.set("-no-copy-on-write");
			if (unsound_checkbox.isSelected()) 
				Options.set("-unsound");
			if (debug_checkbox.isSelected()) 
				Options.set("-debug");
			if (newflow_checkbox.isSelected()) 
				Options.set("-newflow");
			if (states_checkbox.isSelected()) 
				Options.set("-states");
			if (timing_checkbox.isSelected()) 
				Options.set("-timing");
			if (dom_checkbox.isSelected()) 
				Options.set("-dom");
			if (mozilla_checkbox.isSelected()) 
				Options.set("-mozilla");
			List<String> args = newList();
			InputStream old_in = System.in;
			try {
				if (source_modified) {
					System.setIn(new ByteArrayInputStream(source_textarea.getText().getBytes()));
					args.add("-");
				} else
					args.add(sourcefile_textfield.getText());
				Main.setSynchronizer(sync);
				Main.main((String[]) args.toArray(new String[args.size()]));
			} finally {
				System.setIn(old_in);
			}
		}

		@SuppressWarnings("deprecation")
		private void stopWorker() {
			worker.stop(new AbortException());
		}
	}
	
	private static class WriterOutputStream extends OutputStream {  
		   
	    private final Writer writer;  
	   
	    public WriterOutputStream(Writer writer) {
	        this.writer = writer;  
	    }  
	   
	    @Override
		public void write(int b) throws IOException {  
	        write(new byte[] {(byte) b}, 0, 1);  
	    }  
	   
	    @Override
		public void write(byte b[], int off, int len) throws IOException {  
	        writer.write(new String(b, off, len));  
	    }  
	   
	    @Override
		public void flush() throws IOException {  
	        writer.flush();  
	    }  
	   
	    @Override
		public void close() throws IOException {  
	        writer.close();  
	    }  
	}  
}
