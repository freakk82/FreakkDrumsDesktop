package Freakk.SerialToMidi;

import java.util.List;
import javax.sound.midi.*;
public class MidiOut {

	private static Receiver rcvr;
	private static Transmitter trns;
	private static MidiDevice device;
	
	public MidiOut() {

	}

	public synchronized boolean SetDevice(MidiDevice dev)
			throws MidiUnavailableException {
		try {
			if(!dev.isOpen()) dev.open();
			rcvr = dev.getReceiver();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
			// System.exit(1);
			return false;
		}
		return true;
	}

	private MidiMessage getNoteOnMessage(int note) {
		return getMessage(ShortMessage.NOTE_ON, note);
	}

	private MidiMessage getNoteOffMessage(int note) {
		return getMessage(ShortMessage.NOTE_OFF, note);
	}

	private MidiMessage getMessage(int cmd, int note) {
		try {
			ShortMessage msg = new ShortMessage();
			msg.setMessage(cmd, 0, note, 60);

			return (MidiMessage) msg;
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	public void SendNoteOn(int[] msg) throws InvalidMidiDataException {

		ShortMessage out = new ShortMessage();
		//System.out.println("Playing " + msg[0] + " " + msg[1] + " " + msg[2]);
		out.setMessage(out.NOTE_ON, msg[0], msg[1], msg[2]);
		long timeStamp = -1;
		rcvr.send(out, timeStamp);
	}

	public static final byte[] intToByteArray(int value) {
		return new byte[] {
				// (byte)(value >>> 24),
				(byte) (value >>> 16), (byte) (value >>> 8), (byte) value };
	}

}