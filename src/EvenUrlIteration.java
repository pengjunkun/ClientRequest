import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class EvenUrlIteration extends URLIteration {
	@Override public Iterator<String> iterator()
	{
		return new MyIterator();
	}

	class MyIterator implements Iterator<String>{
		private String[] urls={
				"http://gcp/2/seg-1.m4s",
				"http://gcp/2/seg-2.m4s",
				"http://gcp/2/seg-3.m4s",
				"http://gcp/2/seg-4.m4s",
				"http://gcp/2/seg-5.m4s",
				"http://gcp/2/seg-6.m4s",
				"http://gcp/2/seg-7.m4s",
				"http://gcp/2/seg-8.m4s",
				"http://gcp/2/seg-9.m4s",
				"http://gcp/2/seg-10.m4s",
				"http://gcp/2/seg-11.m4s",
				"http://gcp/2/seg-12.m4s",
				"http://gcp/2/seg-13.m4s",
				"http://gcp/2/seg-14.m4s",
				"http://gcp/2/seg-15.m4s",
				"http://gcp/2/seg-16.m4s",
				"http://gcp/2/seg-17.m4s",
				"http://gcp/2/seg-18.m4s",
				"http://gcp/2/seg-19.m4s",
				"http://gcp/2/seg-20.m4s",
				"http://gcp/2/seg-21.m4s",
				"http://gcp/2/seg-22.m4s",
				"http://gcp/2/seg-23.m4s",
				"http://gcp/2/seg-24.m4s",
				"http://gcp/2/seg-25.m4s",
				"http://gcp/2/seg-26.m4s",
				"http://gcp/2/seg-27.m4s",
				"http://gcp/2/seg-28.m4s",
				"http://gcp/2/seg-29.m4s"
		};
		private int index =0;
		@Override
		public boolean hasNext() {
			//as we use a circle
			return true;
		}

		@Override
		public String next() {
			if (index==urls.length)
				index=0;
			String tmp=urls[index++];
			return tmp;
		}
	}

}