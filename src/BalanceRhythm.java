import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class BalanceRhythm extends RequestRhythm
{

	// the parameter is treated as the requesting times per second
	float timesPerSecond;

	public BalanceRhythm(float rhythm_param)
	{
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
		timer.schedule(getRequestTask(),0,period);
		return timer;
	}

}