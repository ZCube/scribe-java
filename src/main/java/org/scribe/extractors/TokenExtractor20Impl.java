package org.scribe.extractors;

import java.util.regex.*;

import org.scribe.exceptions.*;
import org.scribe.model.*;
import org.scribe.utils.*;

/**
 * Default implementation of {@AccessTokenExtractor}. Conforms to OAuth 2.0
 * 
 */
public class TokenExtractor20Impl implements AccessTokenExtractor
{
  private static final String ACCESS_TOKEN_REGEX = "access_token=([^&]+)";
  private static final String REFRESH_TOKEN_REGEX = "access_token=([^&]+)";
  private static final String EMPTY_SECRET = "";

  /**
   * {@inheritDoc}
   */
  public Token extract(String response)
  {
    Preconditions.checkEmptyString(response, "Response body is incorrect. Can't extract a token from an empty string");
    String accessToken = null;
    String refreshToken = null;

    Matcher matcher = Pattern.compile(ACCESS_TOKEN_REGEX).matcher(response);
    if (matcher.find())
    {
      accessToken = OAuthEncoder.decode(matcher.group(1));
    }

    matcher = Pattern.compile(REFRESH_TOKEN_REGEX).matcher(response);
    if (matcher.find())
    {
      refreshToken = OAuthEncoder.decode(matcher.group(1));
    }
    
    if (accessToken == null)
    {
      throw new OAuthException("Cannot extract an acces token. Response was: " + response);
    }

    return new Token(matcher.group(1), "", refreshToken, response);
  }
}
