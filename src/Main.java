import org.apache.commons.math3.distribution.ZipfDistribution;

import java.time.Duration;
import java.util.Random;

/**
 * Created by JackPeng(pengjunkun@gmail.com) on 2021/1/1.
 */
public class Main
{
	public static void main(String[] args)
	{
		//when use the rhythm of balance, the parameter is treated as the requesting count per second
		//when use the rhythm of possion, the parameter is treated as the expected period(millisecond) of a poisson process
		//granularity: in second, the period of one average data
//		Client client = new Client(0.3, Client.RHYTHM.POSSION, "1000",
//				Client.POPULARITY.ZIPF, "1.07");
		Client client=new Client(30, Client.RHYTHM.TRACE,"iqiyiTest.csv",2);


		client.run();

	}
	//how to send reuqests
	//test the maximum requests
	//compress the duration time
	//different size payload to test the bandwidth/latency
	//how to compress 14days running,如何切割
	//调整流行度，检测threshold
}
