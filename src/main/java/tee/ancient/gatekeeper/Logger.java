package tee.ancient.gatekeeper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

	public static void log(Object _class, String message) {
		write(_class.getClass().getName() + " - message");
	}
	
	private static void write(String value) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File("/tmp/gatekeeper.log"), true))) {
			writer.write(value + "\n");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
