import java.util.ArrayList;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;


public class SoundTest {

	public static void main(String[] args){
		
		ArrayList<Mixer.Info> stuff;
		stuff = new ArrayList<Mixer.Info>(Arrays.asList(AudioSystem.getMixerInfo()));
		Mixer.Info def = null;
		
		for(Mixer.Info m : stuff){
			
			System.out.println(m.getName());
			if(m.getName().contains("default"))
				def = m;
		} //for
		
		System.out.println(def.toString());

		 float sampleRate = 8000;
		 int sampleSizeInBits = 8;
		 int channels = 1;
		 boolean signed = true;
		 boolean bigEndian = true;
		 AudioFormat format =  new AudioFormat(sampleRate, 
				 sampleSizeInBits, channels, signed, bigEndian);
		 
		 TargetDataLine tdl = null;
		 try {
			tdl = AudioSystem.getTargetDataLine(format, def);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		
		 
		 
	
	} //main
}
