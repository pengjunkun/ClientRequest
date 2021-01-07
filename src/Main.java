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
		//The instructions to create new client:
		//1st param: the testing duration(in second), after this duration the whole process will be ended.
		//2nd param: the rhythm of sending requests: Balance/Poisson/Trace
		//3rd param: the rhythm param,
		//           1.when use the rhythm of balance, the parameter is treated as the requesting count per second
		//           2.when use the rhythm of possion, the parameter is treated as the expected period(millisecond) of a poisson process
		//           3.when use the rhythm of trace, the parameter is treated as the file in trace directory
		//4th param: the popularity(content) that will be sent: Even/Zipf/Trace
		//5th param: the popularity param,
		//           1.when use Even, this is set to  be null;
		//           2.when use Zipf, this is set to  be Zipf param;
		//           3.when use Trace, this is not needed
		//6th param: the granularity to report(in second)

		Client client = new Client(30, Client.RHYTHM.TRACE, "iqiyiTest.csv", 2);

		client.run();

	}
	//todo:
	//how to send reuqests
	//test the maximum requests
	//compress the duration time
	//different size payload to test the bandwidth/latency
	//how to compress 14days running,如何切割
	//调整流行度，检测threshold
}
