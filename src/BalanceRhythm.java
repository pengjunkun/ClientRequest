import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class BalanceRhythm extends RequestRhythm
{

	// the parameter is treated as the requesting times per second
	float timesPerSecond;

	public BalanceRhythm(float rhythm_param)
	{
		super();
		timesPerSecond = rhythm_param;
	}

	@Override public Timer makeTimer()
	{
		//set the threads of timer to be daemon threads, which will help theses requesting threads stop when duration arrives
		Timer timer = new Timer(true);
		//get the period(interval)
		long period = (long) (1000.0 / timesPerSecond);
		if (period <= 0)
		{
			System.out.println(
					"ERROR: please make sure the times per second is no more than 1000");
			System.exit(1);
		}

		//this variable is used as a callback, first is response, second is requested url
		BiConsumer<HttpResponse<Path>, Long> action=
				//the two params are from two completedstages
			(pathHttpResponse, startTimeStamp) -> {
				if (pathHttpResponse == null)
				{
					System.out.println(
							"There is one request timeout!!!");
					//timeout is 5s
					latencySum+=5000;
					return;
				}

				responseCount+=1;
				HttpHeaders headers = pathHttpResponse.headers();

				int size = Integer.parseInt(
						headers.firstValue("Content-Length")
								.orElse(null));
				sizeSum += size;

				long latency = System.currentTimeMillis() - startTimeStamp;
				latencySum += latency;

				System.out.println(
						String.format("fileSize:%s", size));
				System.out.println("latency:" + latency);
			};
		timer.schedule(getRequestTask(action),0,period);
		return timer;
	}

}