/**
 *	this class is used to create a new user who have some requests to send
 */
public class Client
{
	private WorkAgent agent;


	//describe the requesting rhythm which also can be seen as the requesting interval time
	public enum RHYTHM
	{
		BALANCE, POSSION,TRACE
	}

	//describe the requesting contents(urls) portion
	public enum POPULARITY
	{
		EVEN, ZIPF
	}

	//init a agent to do the detail execution.
	public Client(int duration, RHYTHM rhythm, String rhythm_param,
			POPULARITY popularity, String pop_param,int granularity)
	{
		agent=new WorkAgent();
		agent.setDuration(duration);
		agent.setRequestRhythm(rhythm,rhythm_param);
		agent.setPopularity(popularity,pop_param);
		agent.setGranularity(granularity);
	}

	//this constructor is designed for trace model use
	public Client(int duration, RHYTHM rhythm, String rhythm_param,int granularity)
	{
		if (rhythm!=RHYTHM.TRACE)
		{
			System.out.println("This client constructor is only for Trace model!");
			System.exit(1);
		}
		agent=new WorkAgent();
		agent.setDuration(duration);
		agent.setRequestRhythm(RHYTHM.TRACE,rhythm_param);
		agent.setGranularity(granularity);
		//in this model, the popularity of content is not needed!
	}
	//start
	public void run(){
		agent.doRequest();
	}

}