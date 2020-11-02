package net.hep.ami.jami;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.net.ssl.*;

/**
 * A JAVA AMI client.
 *
 * @author Jérôme Odier
 * {@inheritDoc}
 */

public class Client
{
	/*----------------------------------------------------------------------------------------------------------------*/

	private SSLSocketFactory m_socketFactory;

	/*----------------------------------------------------------------------------------------------------------------*/

	private final String m_tcfp   ;
	private final String m_host   ;
	private final String m_path   ;
	private final int    m_port   ;
	private final int    m_timeout;

	/*----------------------------------------------------------------------------------------------------------------*/

	private String m_cookie = "";

	/*----------------------------------------------------------------------------------------------------------------*/

	/**
	 * Initializes a new AMI client.
	 * @param host The AMI service host.
	 * @param path The AMI service path.
	 * @param port The AMI service port.
	 * @throws Exception If unable to initialize the client.
	 */

	public Client(String host, String path, int port) throws Exception
	{
		this(host, path, port, null, null, 1500);
	}

	/*----------------------------------------------------------------------------------------------------------------*/

	/**
	 * Initializes a new AMI client.
	 * @param host The AMI service host.
	 * @param path The AMI service path.
	 * @param port The AMI service port.
	 * @param sessionName An arbitrary unique session name or null.
	 * @param keyManagers The sources of authentication keys or null.
	 * @param timeout The socket timeout, in milliseconds.
	 * @throws Exception If unable to initialize the client.
	 */

	public Client(String host, String path, int port, String sessionName, KeyManager[] keyManagers, int timeout) throws Exception
	{
		/*------------------------------------------------------------------------------------------------------------*/

		m_tcfp = System.getProperty("user.home") + File.separator + ".ami.cookie." + (sessionName != null ? sessionName : "global");

		/*------------------------------------------------------------------------------------------------------------*/

		m_host    = host   ;
		m_path    = path   ;
		m_port    = port   ;
		m_timeout = timeout;

		/*------------------------------------------------------------------------------------------------------------*/

		PermissiveSocketFactory permissiveSocketFactory = new PermissiveSocketFactory(keyManagers);

		/*------------------------------------------------------------------------------------------------------------*/

		m_socketFactory = permissiveSocketFactory.getTLSSocketFactory();

		if(m_socketFactory == null)
		{
			m_socketFactory = permissiveSocketFactory.getSSLSocketFactory();

			if(m_socketFactory == null)
			{
				throw new Exception("Could not initialize client");
			}
		}

		/*------------------------------------------------------------------------------------------------------------*/
	}

	/*----------------------------------------------------------------------------------------------------------------*/

	private String cookie(String value)
	{
		if(value == null)
		{
			try
			{
				try(BufferedReader bufferedReader = new BufferedReader(new FileReader(m_tcfp)))
				{
					value = bufferedReader.readLine();
				}
			}
			catch(Exception e)
			{
				value = "";
			}
		}
		else
		{
			try
			{
				try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(m_tcfp)))
				{
					bufferedWriter.write(value);
				}
			}
			catch(Exception e)
			{
				value = "";
			}
		}

		m_cookie = value;

		return value;
	}

	/*----------------------------------------------------------------------------------------------------------------*/

	/**
	 * Executes a command.
	 * @param command The command name.
	 * @param arguments The map of arguments.
	 * @return A JSON string containing the command result.
	 * @throws Exception If unable to execute the command.
	 */

	public String execute(String command, Map<String, String> arguments) throws Exception
	{
		StringBuilder result = new StringBuilder();

		/*------------------------------------------------------------------------------------------------------------*/
		/* BUILD POST DATA                                                                                            */
		/*------------------------------------------------------------------------------------------------------------*/

		StringBuilder argumentString = new StringBuilder();

		for(Map.Entry<String, String> entry: arguments.entrySet())
		{
			argumentString.append(" -").append(entry.getKey()).append("=\"").append(entry.getValue().replace("\"", "\\\"")).append("\"");
		}

		/*------------------------------------------------------------------------------------------------------------*/

		String data = "Command=" + command + URLEncoder.encode(argumentString.toString(), "UTF-8") + "&Converter=AMIXmlToJson.xsl";

		/*------------------------------------------------------------------------------------------------------------*/
		/* BUILD HTTPS URL                                                                                            */
		/*------------------------------------------------------------------------------------------------------------*/

		URL url = new URL("https://" + m_host + ":" + m_port + m_path);

		/*------------------------------------------------------------------------------------------------------------*/
		/* CONNECTION                                                                                                 */
		/*------------------------------------------------------------------------------------------------------------*/

		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

		try
		{
			/*--------------------------------------------------------------------------------------------------------*/

			connection.setRequestMethod("POST");

			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF");
			connection.setRequestProperty("Content-Length", String.valueOf(data.length()));

			connection.setRequestProperty("Cookie", cookie(null));
			connection.setRequestProperty("Connection", "Close");
			connection.setRequestProperty("User-Agent", "jami");

			/*--------------------------------------------------------------------------------------------------------*/

			connection.setSSLSocketFactory(m_socketFactory);
			connection.setConnectTimeout(m_timeout);
			connection.setReadTimeout(m_timeout);
			connection.setDoOutput(true);
			connection.setDoInput(true);

			/*--------------------------------------------------------------------------------------------------------*/

			try(BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())))
			{
				bufferedWriter.write(data);
			}

			/*--------------------------------------------------------------------------------------------------------*/

			try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream())))
			{
				String line;

				while((line = bufferedReader.readLine()) != null)
				{
					result.append(line)
					      .append('\n')
					;
				}
			}

			/*--------------------------------------------------------------------------------------------------------*/

			cookie(connection.getHeaderField("Set-Cookie"));

			/*--------------------------------------------------------------------------------------------------------*/
		}
		finally
		{
			connection.disconnect();
		}

		/*------------------------------------------------------------------------------------------------------------*/

		return result.toString();
	}

	/*----------------------------------------------------------------------------------------------------------------*/

	public String getCookie()
	{
		return m_cookie;
	}

	/*----------------------------------------------------------------------------------------------------------------*/
}
