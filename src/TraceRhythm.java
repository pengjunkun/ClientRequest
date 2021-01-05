import java.io.*;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.Date;
import java.util.Timer;
import java.util.function.BiConsumer;

/**
 * Created by JackPeng(pengjunkun@gmail.com) on 2021/1/5.
 */
public class TraceRhythm extends RequestRhythm
{
	BufferedReader reader;
	private long baseTime;
	private long traceBase;

	public TraceRhythm(String traceFileName)
	{
		baseTime = System.currentTimeMillis();
		try
		{
			reader = new BufferedReader(
					new FileReader("./trace/" + traceFileName));
			traceBase = Long.parseLong(reader.readLine());
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("please check the traceFile!");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	BiConsumer<HttpResponse<Path>, Long> action;

	@Override public Timer makeTimer()
	{
		Timer timer = new Timer(true);

		//this variable is used as a callback, first is response, second is requested url
		action = (pathHttpResponse, startTimeStamp) -> {
			if (pathHttpResponse == null)
			{
				System.out.println("There is one request timeout!!!");
				//timeout is 5s
				latencySum += 5000;
				return;
			}

			responseCount += 1;
			HttpHeaders headers = pathHttpResponse.headers();
			int size = Integer.parseInt(
					headers.firstValue("Content-Length").orElse(null));
			sizeSum += size;

			long latency = System.currentTimeMillis() - startTimeStamp;
			latencySum += latency;

			System.out.println(String.format("fileSize:%s", size));
			System.out.println("latency:" + latency);

			//add more tasks
			try
			{
				String tmpLine = reader.readLine();
				if (tmpLine == null)
				{
					System.out.println("----------------------------use all traces!");
					return;
				}
				baseTime += (Long.parseLong(tmpLine) - traceBase);
				timer.schedule(getRequestTask(action), new Date(baseTime));
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		};

		//initial 100 tasks into Timer's taskQueen
		try
		{
			String line;
			for (int i = 0; i < 50; i++)
			{
				line = reader.readLine();
				if (line == null)
				{
					System.out.println("----------------------------use all traces!");
					break;
				}
				baseTime += (Long.parseLong(line) - traceBase);
				timer.schedule(getRequestTask(action), new Date(baseTime));
			}
			//		timer.schedule(getRequestTask(),0,period);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return timer;
	}
}
