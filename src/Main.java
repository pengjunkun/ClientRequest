import org.apache.commons.math3.distribution.ZipfDistribution;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Random;

/**
 * Created by JackPeng(pengjunkun@gmail.com) on 2021/1/1.
 */
public class Main
{

	private static BufferedReader reader;

	static
	{
	}

	public static void main(String[] args)
	{
		//when use the rhythm of balance, the parameter is treated as the requesting count per second
		//when use the rhythm of possion, the parameter is treated as the expected period(millisecond) of a poisson process
		//granularity: in second, the period of one average data
		//		Client client = new Client(0.3, Client.RHYTHM.POSSION, "1000",
		//				Client.POPULARITY.ZIPF, "1.07");
		//The instructions to create new client:
		//1st param: the testing duration(in second), after this duration the whole process will be ended.
		//2nd param: the rhythm of sending requests: Balance/Poisson/Trace
		//3rd param: the rhythm param,
		//           1.when use the rhythm of balance, the parameter is treated as the requesting count per second
		//           2.when use the rhythm of possion, the parameter is treated as the expected period(millisecond) of a poisson process
		//           3.when use the rhythm of trace, the parameter is treated as the fileName in conf directory
		//4th param: the popularity(content) that will be sent: Even/Zipf/Trace
		//5th param: the popularity param,
		//           1.when use Even, this is set to  be null;
		//           2.when use Zipf, this is set to  be Zipf param;
		//           3.when use Trace, this is set to  be null;
		//6th param: the granularity to report(in second)
		Client client = null;

		try
		{
			reader = new BufferedReader(new FileReader("./conf/client.conf"));
			String tmp = reader.readLine();
			String[] params = new String[6];
			int index = 0;
			while (tmp != null)
			{
				String oneLine = tmp;
				tmp = reader.readLine();
				if (oneLine.stripLeading().startsWith("#"))
					continue;
				params[index++] = oneLine;
			}
			client = new Client(Integer.parseInt(params[0]),
					getRhythm(params[1]), params[2], getPopularity(params[3]),
					params[4], Integer.parseInt(params[5]));
			client.run();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		//			Client client = new Client(30, Client.RHYTHM.TRACE, "iqiyiTest.csv", 2);



	}

	private static Client.RHYTHM getRhythm(String rhythm)
	{
		if (rhythm.equalsIgnoreCase("Balance"))
			return Client.RHYTHM.BALANCE;
		else if (rhythm.equalsIgnoreCase("Possion"))
			return Client.RHYTHM.POSSION;
		else if (rhythm.equalsIgnoreCase("Trace"))
			return Client.RHYTHM.TRACE;
		return null;
	}

	private static Client.POPULARITY getPopularity(String popularity)
	{
		if (popularity.equalsIgnoreCase("Even"))
			return Client.POPULARITY.EVEN;
		else if (popularity.equalsIgnoreCase("Zipf"))
			return Client.POPULARITY.ZIPF;
		else if (popularity.equalsIgnoreCase("Trace"))
			return Client.POPULARITY.TRACE;
		return null;
	}
	//todo:
	//how to send reuqests
	//test the maximum requests
	//compress the duration time
	//different size payload to test the bandwidth/latency
	//how to compress 14days running,如何切割
	//调整流行度，检测threshold
}
