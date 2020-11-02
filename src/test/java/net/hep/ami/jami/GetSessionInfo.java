package net.hep.ami.jami;

import java.util.*;

public class GetSessionInfo
{
	/*----------------------------------------------------------------------------------------------------------------*/

	public static void main(String[] args)
	{
		try
		{
			Client client = new Client("ami.in2p3.fr", "/AMI/servlet/net.hep.atlas.Database.Bookkeeping.AMI.Servlet.FrontEnd", 443);

			System.out.println(client.execute("GetSessionInfo", new HashMap<String, String>()));

			System.out.println("Cookie: " + client.getCookie());
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}

		System.exit(0);
	}

	/*----------------------------------------------------------------------------------------------------------------*/
}
