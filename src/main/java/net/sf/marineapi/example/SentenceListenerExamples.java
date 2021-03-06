package net.sf.marineapi.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sf.marineapi.nmea.event.AbstractSentenceListener;
import net.sf.marineapi.nmea.event.SentenceEvent;
import net.sf.marineapi.nmea.event.SentenceListener;
import net.sf.marineapi.nmea.io.SentenceReader;
import net.sf.marineapi.nmea.sentence.GGASentence;
import net.sf.marineapi.nmea.sentence.GLLSentence;
import net.sf.marineapi.nmea.sentence.GSASentence;
import net.sf.marineapi.nmea.sentence.GSVSentence;
import net.sf.marineapi.nmea.sentence.Sentence;
import net.sf.marineapi.nmea.sentence.SentenceId;

public class SentenceListenerExamples {

	/**
	 * @param args File to read
	 */
	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.out.println("Example usage:\njava FileExample nmea.log");
			System.exit(1);
		}
		
		try {
			new SentenceListenerExamples(new File(args[0]));
			System.out.println("Running, press CTRL-C to stop..");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Constructor
	 */
	public SentenceListenerExamples(File file) throws IOException {
		
		InputStream stream = new FileInputStream(file);
		SentenceReader reader = new SentenceReader(stream);

		reader.addSentenceListener(new GSAListener());
		reader.addSentenceListener(new MultiSentenceListener());
		reader.addSentenceListener(new SingleSentenceListener(), SentenceId.GSV);
		
		reader.start();
	}
	
	
	public class MultiSentenceListener implements SentenceListener {
		@Override
		public void readingPaused() {
		}
		@Override
		public void readingStarted() {
		}
		@Override
		public void readingStopped() {
		}
		@Override
		public void sentenceRead(SentenceEvent event) {
			Sentence s = event.getSentence();
			if("GLL".equals(s.getSentenceId())) {
				GLLSentence gll = (GLLSentence) s;
				System.out.println("GLL position: " + gll.getPosition());
			} else if ("GGA".equals(s.getSentenceId())) {
				GGASentence gga = (GGASentence) s;
				System.out.println("GGA position: " + gga.getPosition());
			}
		}
	}
	
	public class SingleSentenceListener implements SentenceListener {
		@Override
		public void readingPaused() {
		}
		@Override
		public void readingStarted() {
		}
		@Override
		public void readingStopped() {
		}
		@Override
		public void sentenceRead(SentenceEvent event) {
			GSVSentence gsv = (GSVSentence) event.getSentence();
			System.out.println("GSV satellites in view: " + gsv.getSatelliteCount());
		}		
	}
	
	public class GSAListener extends AbstractSentenceListener<GSASentence> {
		@Override
		public void sentenceRead(GSASentence gsa) {
			System.out.println("GSA position DOP: " + gsa.getPositionDOP());
		}
	}
}
