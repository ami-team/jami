package net.hep.ami.jami;

import java.security.cert.*;

import javax.net.ssl.*;

public class PermissiveSocketFactory
{
	/*----------------------------------------------------------------------------------------------------------------*/

	private static final String SSL_PROTOCOL = "SSLv3";
	private static final String TLS_PROTOCOL = "TLSv1";

	/*----------------------------------------------------------------------------------------------------------------*/

	private static final class PermissiveX509TrustManager implements X509TrustManager
	{
		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
		{
			/* IGNORE */
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
		{
			/* IGNORE */
		}

		@Override
		public X509Certificate[] getAcceptedIssuers()
		{
			return new X509Certificate[] {};
		}
	}

	/*----------------------------------------------------------------------------------------------------------------*/

	private final SSLSocketFactory m_sslSocketFactory;
	private final SSLSocketFactory m_tlsSocketFactory;

	/*----------------------------------------------------------------------------------------------------------------*/

	protected PermissiveSocketFactory(KeyManager[] keyManagers)
	{
		SSLSocketFactory tmp;

		/*------------------------------------------------------------------------------------------------------------*/
		/* SSL                                                                                                        */
		/*------------------------------------------------------------------------------------------------------------*/

		try
		{
			/*--------------------------------------------------------------------------------------------------------*/
			/* CREATE CONTEXT                                                                                         */
			/*--------------------------------------------------------------------------------------------------------*/

			SSLContext sslContext = SSLContext.getInstance(SSL_PROTOCOL);

			sslContext.init(keyManagers, new TrustManager[] {
					new PermissiveX509TrustManager()
				}, new java.security.SecureRandom()
			);

			/*--------------------------------------------------------------------------------------------------------*/
			/* CREATE FACTORY                                                                                         */
			/*--------------------------------------------------------------------------------------------------------*/

			tmp = sslContext.getSocketFactory();

			/*--------------------------------------------------------------------------------------------------------*/
		}
		catch(Exception e)
		{
			tmp = null;
		}

		m_sslSocketFactory = tmp;

		/*------------------------------------------------------------------------------------------------------------*/
		/* TLS                                                                                                        */
		/*------------------------------------------------------------------------------------------------------------*/

		try
		{
			/*--------------------------------------------------------------------------------------------------------*/
			/* CREATE CONTEXT                                                                                         */
			/*--------------------------------------------------------------------------------------------------------*/

			SSLContext tlsContext = SSLContext.getInstance(TLS_PROTOCOL);

			tlsContext.init(keyManagers, new TrustManager[] {
					new PermissiveX509TrustManager()
				}, new java.security.SecureRandom()
			);

			/*--------------------------------------------------------------------------------------------------------*/
			/* CREATE FACTORY                                                                                         */
			/*--------------------------------------------------------------------------------------------------------*/

			tmp = tlsContext.getSocketFactory();

			/*--------------------------------------------------------------------------------------------------------*/
		}
		catch(Exception e)
		{
			tmp = null;
		}

		m_tlsSocketFactory = tmp;

		/*------------------------------------------------------------------------------------------------------------*/
	}

	/*----------------------------------------------------------------------------------------------------------------*/

	public SSLSocketFactory getSSLSocketFactory()
	{
		return m_sslSocketFactory;
	}

	/*----------------------------------------------------------------------------------------------------------------*/

	public SSLSocketFactory getTLSSocketFactory()
	{
		return m_tlsSocketFactory;
	}

	/*----------------------------------------------------------------------------------------------------------------*/
}
