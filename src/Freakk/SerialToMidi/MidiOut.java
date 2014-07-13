package Freakk.SerialToMidi;

import javax.sound.midi.*;
public class MidiOut {

	private static Receiver rcvr;

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

	private MidiMessage CreateNoteOnMessage(int note) throws InvalidMidiDataException {
		return GetMessage(ShortMessage.NOTE_ON, note);
	}

	private MidiMessage CreateNoteOffMessage(int note) throws InvalidMidiDataException {
		return GetMessage(ShortMessage.NOTE_OFF, note);
	}

	private MidiMessage GetMessage(int cmd, int note) throws InvalidMidiDataException {
		ShortMessage msg = new ShortMessage();
		msg.setMessage(cmd, 0, note, 60);
		return (MidiMessage) msg;
	}

	public void SendNoteOn(int[] msg) throws InvalidMidiDataException {
		ShortMessage out = new ShortMessage();
		out.setMessage(out.NOTE_ON, msg[0], msg[1], msg[2]);
		long timeStamp = -1;
		rcvr.send(out, timeStamp);
	}

	public void SendNoteOff(int[] msg) throws InvalidMidiDataException {
		ShortMessage out = new ShortMessage();
		out.setMessage(out.NOTE_OFF, msg[0], msg[1], msg[2]);
		long timeStamp = -1;
		rcvr.send(out, timeStamp);
	}
	
	public void SendShortMessage( ShortMessage smsg){
		long timeStamp = -1;
		rcvr.send(smsg, timeStamp);
	}
}