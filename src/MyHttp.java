import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by JackPeng(pengjunkun@gmail.com) on 2021/1/2.
 */
public class MyHttp
{
	private  HttpClient client;
	private CompletableFuture<HttpResponse<Path>> response;

	public MyHttp()
	{
		this.client = HttpClient.newBuilder()
			.version(HttpClient.Version.HTTP_1_1)
			.followRedirects(HttpClient.Redirect.NORMAL)
			.connectTimeout(Duration.ofSeconds(20))
			.build();
	}

	//
	public  CompletableFuture<HttpResponse<Path>> get(String uri) {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(uri))
				.header("Content-Type", "video/mp4")
				.build();
		//use bodyhanlder to convert receiving bytes into a file
			CompletableFuture<HttpResponse<Path>> response=client.sendAsync(request,
					HttpResponse.BodyHandlers.ofFile(Paths.get("/Users/pengjunkun/Desktop/packetProject/ClientRequest/downloads/a")));
		return response;
	}

}
