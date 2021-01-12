import java.io.IOException;
import java.util.logging.*;

/**
 * Created by JackPeng(pengjunkun@gmail.com) on 2021/1/12.
 */
public class MyLog
{
	public static Logger logger;
	static {
		logger = Logger.getLogger("clientLog");
		FileHandler fh = null;
		try
		{
			fh = new FileHandler("client.log");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		fh.setLevel(Level.INFO);
		fh.setFormatter(new SimpleFormatter());
		ConsoleHandler ch=new ConsoleHandler();
		ch.setLevel(Level.ALL);
		ch.setFormatter(new SimpleFormatter());
		logger.addHandler(fh);
		logger.addHandler(ch);
	}

}
