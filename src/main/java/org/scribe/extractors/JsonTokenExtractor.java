package org.scribe.extractors;

import java.util.regex.*;

import org.scribe.exceptions.*;
import org.scribe.model.*;
import org.scribe.utils.*;

public class JsonTokenExtractor implements AccessTokenExtractor
{
  private Pattern accessTokenPattern = Pattern.compile("\"access_token\":\\s*\"(\\S*?)\"");

  private Pattern refreshTokenPattern = Pattern.compile("\"refresh_token\":\\s*\"(\\S*?)\"");

  public Token extract(String response)
  {
    Preconditions.checkEmptyString(response, "Cannot extract a token from a null or empty String");
    String accessToken = null;
    String refreshToken = null;

    Matcher matcher = accessTokenPattern.matcher(response);
    if (matcher.find())
    {
      accessToken = matcher.group(1);
    }

    matcher = refreshTokenPattern.matcher(response);
    if (matcher.find())
    {
      refreshToken = matcher.group(1);
    }

    if (accessToken == null)
    {
      throw new OAuthException("Cannot extract an acces token. Response was: " + response);
    }

    return new Token(accessToken, "", refreshToken, response);
  }

}