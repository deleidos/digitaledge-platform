package ${package};

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.deleidos.rtws.core.framework.Description;
import com.deleidos.rtws.core.framework.UserConfigured;
import com.deleidos.rtws.transport.AbstractTransportService;
import com.deleidos.rtws.transport.TransportService;

@Description("Simple example of an DigitalEdge TransportService that reads files from a directory, reads the contents of the files," +
		" and pushes JMS messages into DigitalEdge")
public class SimpleLocalFileTransportService extends AbstractTransportService {

	private String watchedDirectory;
	private DirWatcherRunner runner;
	private int messagesSent = 0; 
	
	public int getMessagesSent() {
		return messagesSent;
	}

	@UserConfigured(value = "/usr/local/data", description = "The local directory to poll for data to "
			+ "transmit into the system.", flexValidator = { "StringValidator minLength=3 maxLength=63" })
	public void setWatchedDirectory(String val) {
		watchedDirectory = val;
	}

	
	public String getWatchedDirectory() {
		return watchedDirectory;
	}

	/**
	 * Start this Transport service.
	 * @see TransportService#execute()
	 */
	@Override
	public void execute() {
		runner = new DirWatcherRunner(watchedDirectory);
		Thread fileWatcher = new Thread(runner);
		try {
			fileWatcher.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Close any open resources and stop the transport.
	 * @see TransportService#terminate()
	 */
	@Override
	public void terminate() {
		try {
			runner.setStop(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *  Runnable to read the file stream, split into lines, and send as a JMS message.
	 */
	private class DirWatcherRunner implements Runnable {

		private String watchedDirectory;
		private boolean stop = false;

		public void setStop(boolean stop) {
			this.stop = stop;
		}

		public DirWatcherRunner(String watchedDirectory) {
			this.watchedDirectory = watchedDirectory;
		}

		public void run() {
			try {
				while (!stop) {
					File watchDir = new File(watchedDirectory);
					if (watchDir.isDirectory()) {
						for (File f: watchDir.listFiles()) {
							if (f.isFile()) {
								FileInputStream stream = new FileInputStream(f);
								BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
								String record = reader.readLine();
								while (record != null) {
									// SendJMSMessage is the key method to push JMS messages into the DigitalEdge system
									SendJMSMessage(record);
									messagesSent++;
									record = reader.readLine();
								}
								stream.close();
								//f.delete();
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
