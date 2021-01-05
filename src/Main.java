import java.time.Duration;
import java.util.Random;

/**
 * Created by JackPeng(pengjunkun@gmail.com) on 2021/1/1.
 */
public class Main
{
	public static void main(String[] args)
	{
		Client client = new Client(0.3, Client.RHYTHM.POSSION, 200, Client.POPULARITY.EVEN,
				0);
		client.run();
	}
}
