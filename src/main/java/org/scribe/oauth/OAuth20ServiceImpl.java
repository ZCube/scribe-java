package org.scribe.oauth;

import org.scribe.builder.api.*;
import org.scribe.model.*;

public class OAuth20ServiceImpl implements OAuthService
{
  private static final String VERSION = "2.0";
  
  private final DefaultApi20 api;
  private final OAuthConfig config;
  
  /**
   * Default constructor
   * 
   * @param api OAuth2.0 api information
   * @param config OAuth 2.0 configuration param object
   */
  public OAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config)
  {
    this.api = api;
    this.config = config;
  }

  /**
   * {@inheritDoc}
   */
  public Token getAccessToken(Token requestToken, Verifier verifier)
  {
    OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(),
        api.getAccessTokenEndpoint());

    Verb verb = api.getAccessTokenVerb();
    request.addParameter(verb, OAuthConstants.CLIENT_ID, config.getApiKey());
    request.addParameter(verb, OAuthConstants.CLIENT_SECRET, config.getApiSecret());

    if (requestToken != null && requestToken.getRefreshToken() != null)
    {
      request.addParameter(verb, OAuthConstants.GRANT_TYPE, "refresh_token");
      request.addParameter(verb, OAuthConstants.REFRESH_TOKEN, requestToken.getRefreshToken());
    }
    else
    {
      request.addParameter(verb, OAuthConstants.GRANT_TYPE, "authorization_code");
      request.addParameter(verb, OAuthConstants.CODE, verifier.getValue());
      request.addParameter(verb, OAuthConstants.REDIRECT_URI, config.getCallback());
      if (config.hasScope())
        request.addParameter(verb, OAuthConstants.SCOPE, config.getScope());
    }

    Response response = request.send();
    Token token = api.getAccessTokenExtractor().extract(response.getBody());
    if (requestToken != null && requestToken.getRefreshToken() != null)
    {
      return new Token(token.getToken(), token.getSecret(), requestToken.getRefreshToken(), token.getRawResponse());
    }
    return token;
  }

  /**
   * {@inheritDoc}
   */
  public Token getRequestToken()
  {
    throw new UnsupportedOperationException("Unsupported operation, please use 'getAuthorizationUrl' and redirect your users there");
  }

  /**
   * {@inheritDoc}
   */
  public String getVersion()
  {
    return VERSION;
  }

  /**
   * {@inheritDoc}
   */
  public void signRequest(Token accessToken, OAuthRequest request)
  {
    request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN, accessToken.getToken());
  }

  /**
   * {@inheritDoc}
   */
  public String getAuthorizationUrl(Token requestToken)
  {
    return api.getAuthorizationUrl(config);
  }

}
