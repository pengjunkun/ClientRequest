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
		Client client = new Client(0.3, Client.RHYTHM.TRACE, "THU.txt",
				Client.POPULARITY.ZIPF, 1.07);
		client.run();

	}
	//����zipf�ֲ�
	//���ô洢
	//���trace content
	//how to send reuqests
	//test the maximum requests
	//compress the duration time
	//different size payload to test the bandwidth/latency
	//how to compress 14days running,����и�
	//�������жȣ����threshold
}
