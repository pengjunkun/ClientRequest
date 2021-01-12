import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;

public class PoissonRhythm extends RequestRhythm
{

	private int period_param;
	private long baseTime;

	//in this class, this param means the expected period(millisecond) of a poisson process
	public PoissonRhythm(int rhythm_param)
	{
		super();
		period_param = rhythm_param;
		baseTime = System.currentTimeMillis();
	}

	BiConsumer<HttpResponse<Path>, Long> action;

	@Override public Timer makeTimer()
	{
		Timer timer = new Timer(true);
		if (period_param <= 0)
		{
			MyLog.logger.severe(
					"ERROR: please make sure the parameter of possison rhythm");
			System.exit(1);
		}

		//this variable is used as a callback, first is response, second is requested url
		action = (pathHttpResponse, startTimeStamp) -> {
			if (pathHttpResponse == null)
			{
				MyLog.logger.severe("There is one request timeout!!!");
				//timeout is 5s
				latencySum += 5000;
				return;
			}

			responseCount+=1;
			HttpHeaders headers = pathHttpResponse.headers();
			int size = Integer.parseInt(
					headers.firstValue("Content-Length").orElse(null));
			sizeSum += size;

			long latency = System.currentTimeMillis() - startTimeStamp;
			latencySum += latency;

			MyLog.logger.fine(String.format("fileSize:%s", size));
			MyLog.logger.fine("latency:" + latency);

			//add more tasks
			baseTime += getNextPeriod(period_param);
			timer.schedule(getRequestTask(action), new Date(baseTime));
		};

		//initial 100 tasks into Timer's taskQueen
		for (int i = 0; i < 50; i++)
		{
			baseTime += getNextPeriod(period_param);
			timer.schedule(getRequestTask(action), new Date(baseTime));
		}
		//		timer.schedule(getRequestTask(),0,period);
		return timer;
	}

	private static Random rand = new Random();

	private static long getNextPeriod(int lambda)
	{
		return (long) (-lambda * Math.log(rand.nextDouble()));
	}
}