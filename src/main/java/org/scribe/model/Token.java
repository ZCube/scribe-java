package org.scribe.model;

import java.io.Serializable;

/**
 * Represents an OAuth token (either request or access token) and its secret
 * 
 * @author Pablo Fernandez
 */
public class Token implements Serializable
{
  private static final long serialVersionUID = 715000866082812683L;
	
  private final String token;
  private final String secret;
  private final String refreshToken;
  private final String rawResponse;

  /**
   * Default constructor
   * 
   * @param token token value
   * @param secret token secret
   */
  public Token(String token, String secret)
  {
    this(token, secret, null, null);
  }

  public Token(String token, String secret, String refreshToken, String rawResponse)
  {
    this.token = token;
    this.secret = secret;
    this.refreshToken = refreshToken;
    this.rawResponse = rawResponse;
  }

  public String getToken()
  {
    return token;
  }

  public String getSecret()
  {
    return secret;
  }

  public String getRefreshToken()
  {
    return refreshToken;
  }

  public String getRawResponse()
  {
    if (rawResponse == null)
    {
      throw new IllegalStateException("This token object was not constructed by scribe and does not have a rawResponse");
    }
    return rawResponse;
  }

  @Override
  public String toString()
  {
    return String.format("Token[%s , %s, %s]", token, secret, refreshToken);
  }
}
