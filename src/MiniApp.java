import javax.sound.midi.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;


public class MiniApp {
	
	JPanel mainPanel;
	ArrayList<JCheckBox> checkboxList;
	Sequencer sequencer;
	Sequence sequence;
	Track track;
	JFrame theFrame;
	
	String[] instrumentNames= {"Bass Drum", "closed hi-hat", "open hi-hat", "Acoustic snare", "crash cymbal", "hand clap", 
			"high tom", "hi bongo", "maracas", "whistle", "low conga", "cow bell", "vibraslap", "low-mid tom", "high agogo", "open hi conga"};
	
	int[] instruments= {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};
	
	public static void main(String[] args) {
		
		MiniApp app= new MiniApp();
		app.buildGUI();

	}
	
	
	public void buildGUI()
	{
		//outer frame
		
		theFrame= new JFrame();
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BorderLayout layout= new BorderLayout();
		JPanel background= new JPanel(layout);
		background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		// checkboxlist
		
		checkboxList= new ArrayList<JCheckBox>();
		
		// buttonbox
		Box buttonBox= new Box(BoxLayout.Y_AXIS);
		
		//start button
		
		JButton start= new JButton("start");
		start.addActionListener(new MyStartListener());
		buttonBox.add(start);
		
		// stop button
		
		JButton stop= new JButton("stop");
		start.addActionListener(new MyStopListener());
		buttonBox.add(stop);
		
		
		// uptempo button
		
		JButton upTempo= new JButton("upTempo");
		start.addActionListener(new MyUpTempoListener());
		buttonBox.add(upTempo);
		
		// downtempo button
		
		JButton downTempo= new JButton("downTempo");
		start.addActionListener(new MyDownTempoListener());
		buttonBox.add(downTempo);
		
		
		// add those names as labels
		
		Box nameBox= new Box(BoxLayout.Y_AXIS);
		for(int i=0; i<16; i++)
			nameBox.add(new JLabel(instrumentNames[i]));
		
		// add two boxes to background panel
		
		background.add(BorderLayout.EAST, buttonBox);
		background.add(BorderLayout.WEST, nameBox);
		
		
		// add the panel to the frame
		
		theFrame.getContentPane().add(background);
		
		// create the checkbox panel called mainpanel.
		
		GridLayout grid= new GridLayout(16,16);
		grid.setVgap(1);
		grid.setHgap(2);
		mainPanel= new JPanel(grid);
		background.add(BorderLayout.CENTER, mainPanel);
		
			for(int i=0; i<256; i++)
			{
				JCheckBox c= new JCheckBox();
				c.setSelected(false);
				checkboxList.add(c);
				mainPanel.add(c);
			}
		
		
		setUpMidi();
		
		theFrame.setBounds(50, 50, 300, 300);
		theFrame.pack();
		theFrame.setVisible(true);
		
				
		
	}


	public void setUpMidi() {
		
		try{
				sequencer= MidiSystem.getSequencer();
				sequencer.open();
				sequence= new Sequence(Sequence.PPQ, 4);
				track= sequence.createTrack();
				sequencer.setTempoInBPM(120);
		} catch(Exception e)
		{
			e.printStackTrace(); 
		}
		
	}
	
	
	public void buildTrackAndStart()
	{
		int[] trackList= null;
		
		sequence.deleteTrack(track);
		track= sequence.createTrack();
		
			for(int i=0; i<16; i++)
			{
				trackList= new int[16];
			
				int key= instruments[i];
				
				for(int j=0; j<16; j++)
				{
					JCheckBox jc= (JCheckBox) checkboxList.get(j+ (16*i));
					if(jc.isSelected())
					{
						trackList[j] = key;
					}else
					{
						trackList[j]=0;
					}
				}
				
				makeTracks(trackList);
				track.add(makeEvent(176, 1, 127, 0, 16));
			
			}
			
			track.add(makeEvent(192,9,1,0,16));
			
			try{
				sequencer.setSequence(sequence);
				sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);
				sequencer.start();
				sequencer.setTempoInBPM(120);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
	}
	
	public class MyStartListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			buildTrackAndStart();
			
		}
		
	}
	
	
	public class MyStopListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			sequencer.stop();
			
		}
		
	}
	
	
	public class MyUpTempoListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			float tempoFactor= sequencer.getTempoFactor();
			sequencer.setTempoFactor((float) (tempoFactor * 1.03));
			
		}
		
	}
	
	public class MyDownTempoListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			float tempoFactor= sequencer.getTempoFactor();
			sequencer.setTempoFactor((float) (tempoFactor * .97));
			
		}
		
	}


	private void makeTracks(int[] list) {
		
		for(int i=0; i<16; i++)
		{
			int key= list[i];
			
			if(key!= 0)
			{
				track.add(makeEvent(144, 9, key, 100, i));
				track.add(makeEvent(128, 9, key, 100, i+1));
			}
		}
		
	}


	private MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
		
			MidiEvent event= null;
			try{
				
				ShortMessage a= new ShortMessage();
				a.setMessage(comd, chan, one, two);
				event= new MidiEvent(a, tick);
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
