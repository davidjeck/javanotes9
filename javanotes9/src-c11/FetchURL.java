import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * This short program demonstrates the URL and URLConnection classes by
 * attempting to open a connection to a URL and read text from it.  The url 
 * can be specified on the command line.  If no command line argument is
 * provided, the user is prompted for input.  It can be a complete url, including 
 * the "protocol" at the beginning ("http://", "ftp://", or "file://").   If 
 * it does not start with one of these protocols, "http://" is added to the
 * beginning of the input line.  If an error occurs while trying to fetch the data, 
 * a message is output. Otherwise, the text from the URL is copied to the screen.
 */

public class FetchURL {

	public static void main(String[] args) {
		String url;    // The url from the command line or from user input.
		String urlLC;  // The url transformed to lower case.
		if (args.length == 0) {
			Scanner stdin = new Scanner(System.in);
			System.out.print("Enter a url: ");
			url = stdin.nextLine();
		}
		else {
			url = args[0];
		}
		urlLC = url.toLowerCase();
		if ( ! (urlLC.startsWith("http://") || urlLC.startsWith("ftp://") || 
				urlLC.startsWith("file://") || urlLC.startsWith("https://") )) {
			url = "http://" + url;
			System.out.println("Using: " + url);
		}
		System.out.println();
		try {
			readTextFromURL(url);
		}
		catch (IOException e) {
			System.out.println("\n*** Sorry, an error has occurred ***\n");
			System.out.println(e);
			System.out.println();
		}  
	}

	/**
	 * This subroutine attempts to copy text from the specified URL onto the screen.  
	 * Any error must be handled by the caller of this subroutine.
	 * @param urlString contains the URL in text form
	 */
	static void readTextFromURL( String urlString ) throws IOException {

		/* Open a connection to the URL, and get an input stream
           for reading data from the URL. */

		URL url = new URL(urlString);
		URLConnection connection = url.openConnection();
		InputStream urlData = connection.getInputStream();

		/* Check that the content is some type of text.  Note: 
		   connection.getContentType() should be called after
		   connction.getInputStream(). */

		String contentType = connection.getContentType();
		System.out.println("Stream opened with content type: " + contentType);
		System.out.println();
		if (contentType == null || contentType.startsWith("text") == false)
			throw new IOException("URL does not seem to refer to a text file.");
		System.out.println("Fetching content from " + urlString + " ...");
		System.out.println();

		/* Copy lines of text from the input stream to the screen, until
           end-of-file is encountered  (or an error occurs). */

		BufferedReader in;  // For reading from the connection's input stream.
		in = new BufferedReader( new InputStreamReader(urlData) );

		while (true) {
			String line = in.readLine();
			if (line == null)
				break;
			System.out.println(line);
		}
		in.close();

	} // end readTextFromURL()

} // end class FetchURL

